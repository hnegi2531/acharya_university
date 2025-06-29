package com.au.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.JwtDetails;
import com.au.model.EventBlockedRooms;
import com.au.response.ResponseHandler;
import com.au.service.EventBlockedRoomsService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey4}")
@CrossOrigin
public class EventBlockedRoomsController {
	

	Logger log = LoggerFactory.getLogger(EventBlockedRoomsController.class);

	@Autowired
	private EventBlockedRoomsService event_blocked_rooms_service;

	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/eventBlockedRooms")
	public ResponseEntity<Object> saveEventBlockedRooms(@RequestBody @Valid List<EventBlockedRooms> ebr,@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			ebr.stream().forEach(ebr1 -> {
				ebr1.setCreated_by(jwtDetails.getUserId());
				ebr1.setCreated_username(jwtDetails.getUserName());
			});
			List<EventBlockedRooms> event_blocked_rooms = event_blocked_rooms_service.saveEventBlockedRooms(ebr);
			ResponseEntity<Object> event_blocked_rooms_response = ResponseHandler.generateResponse(true,
					HttpStatus.CREATED, event_blocked_rooms);
			return event_blocked_rooms_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/eventBlockedRooms")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<EventBlockedRooms> event_creation_list = event_blocked_rooms_service.listAll();
				ResponseEntity<Object> event_creation_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK,event_creation_list);
				return event_creation_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/fetchAllEventBlockedRooms")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> event_blocked_rooms_sorted = event_blocked_rooms_service.listAll1(pageable, keyword);//,column,value);
					return event_blocked_rooms_sorted;
				}else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> event_blocked_rooms_pageable =event_blocked_rooms_service.listAll2(pageable1);
					return event_blocked_rooms_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/eventBlockedRooms/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				EventBlockedRooms product = event_blocked_rooms_service.get(id);
				ResponseEntity<Object> event_creation_response_by_id = ResponseHandler.generateResponse(true,HttpStatus.OK, product);
				return event_creation_response_by_id;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}		
	}
	
	@PutMapping("/eventBlockedRooms/{id}")
	public ResponseEntity<Object> update(@RequestBody EventBlockedRooms ebr, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
					event_blocked_rooms_service.get(id);
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					ebr.setModified_by(jwtDetails.getUserId());
					ebr.setModified_username(jwtDetails.getUserName());
					event_blocked_rooms_service.updateEventBlockedRooms(ebr);
					ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
					return response;
				} catch (NoSuchElementException e) {
					ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
					return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@DeleteMapping("/eventBlockedRooms/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			event_blocked_rooms_service.delete(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@DeleteMapping("/activateEventBlockedRooms/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			event_blocked_rooms_service.delete1(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	

	@GetMapping("/getAvailableBlockAndRooms")
	 public ResponseEntity<Object> getAvailableBlockAndRooms(
	            @RequestParam(value = "event_start_time") String eventStartTime,
	            @RequestParam(value = "event_end_time") String eventEndTime) throws ParseException {

	        // Normalize date formats if necessary (use previous normalization method if needed)
	        eventStartTime = normalizeDateFormat(eventStartTime);
	        eventEndTime = normalizeDateFormat(eventEndTime);

	        // Call the service to get available rooms
	        List<Map<String, Object>> availableRooms = event_blocked_rooms_service.getAvailableBlockAndRooms(eventStartTime, eventEndTime);
	        return new ResponseEntity<>(availableRooms, HttpStatus.OK);
	    }

	    private String normalizeDateFormat(String date) {
	        // Check if the date string contains a space, replace it with 'T'
	        if (date.contains(" ")) {
	            date = date.replace(" ", "T");
	        }
	        return date;
	    }
	    
	    
//		@GetMapping("/checkingAvailableBlockAndRooms")
//		 public ResponseEntity<Object> checkingAvailableBlockAndRooms( @RequestParam(value = "room_id") Integer room_id,
//		            @RequestParam(value = "event_start_time") String eventStartTime,
//		            @RequestParam(value = "event_end_time") String eventEndTime) throws ParseException {
//
//		        // Normalize date formats if necessary (use previous normalization method if needed)
//		        eventStartTime = normalizeDateFormat(eventStartTime);
//		        eventEndTime = normalizeDateFormat(eventEndTime);
//
//		        // Call the service to get available rooms
//		        String checkingAvailableBlockAndRooms = event_blocked_rooms_service.checkingAvailableBlockAndRooms(room_id,eventStartTime, eventEndTime);
//		        return new ResponseEntity<>(checkingAvailableBlockAndRooms, HttpStatus.OK);
//		    }

		 
	    
	    @GetMapping("/checkingAvailableBlockAndRooms")
	    public ResponseEntity<Object> checkingAvailableBlockAndRooms(
	            @RequestParam(value = "room_id") Integer roomId,
	            @RequestParam(value = "event_start_time") String eventStartTime,
	            @RequestParam(value = "event_end_time") String eventEndTime) throws ParseException {

	    	 // Call the service to check availability (no need to normalize date times anymore)
	        String availabilityStatus = event_blocked_rooms_service.checkingAvailableBlockAndRooms(roomId, eventStartTime, eventEndTime);

	        // Return the appropriate response
	        if ("eligible to create".equals(availabilityStatus)) {
	            return new ResponseEntity<>(availabilityStatus, HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(availabilityStatus, HttpStatus.BAD_REQUEST); // 400 - Not eligible
	        }
	    }
	    
}
