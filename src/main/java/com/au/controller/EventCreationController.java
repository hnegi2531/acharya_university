package com.au.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
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

import com.au.dto.EmployeeMedicalHistoryDto;
import com.au.dto.EventCreationDto;
import com.au.dto.JwtDetails;
import com.au.model.EventCreation;
import com.au.response.ResponseHandler;
import com.au.service.EventCreationService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey4}")
@CrossOrigin
public class EventCreationController {
	
	
	Logger log = LoggerFactory.getLogger(EventCreationController.class);

	@Autowired
	private EventCreationService event_creation_service;

	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/eventCreation")
	public ResponseEntity<Object> saveEventCreation(@RequestBody @Valid EventCreation ec, @RequestHeader("Authorization") String jwtToken)
	        throws Exception, JsonParseException, JsonMappingException, IOException {
	    if (RateLimitController.bucket.tryConsume(1)) {
	        JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	        ec.setCreated_by(jwtDetails.getUserId());
	        ec.setCreated_username(jwtDetails.getUserName());

	        // Convert event_start_time and event_end_time from String to Date
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX"); // ISO 8601 format
	        Date eventStartTime = dateFormat.parse(ec.getEvent_start_time());
	        Date eventEndTime = dateFormat.parse(ec.getEvent_end_time());

	        // Pass the EventCreation object and parsed Date objects to the service
	        EventCreation eventCreation = event_creation_service.saveEventCreation(ec, eventStartTime, eventEndTime);

	        ResponseEntity<Object> eventCreationResponse = ResponseHandler.generateResponse(true, HttpStatus.CREATED, eventCreation);
	        return eventCreationResponse;
	    } else {
	        ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
	        return rs;
	    }
	}
	
	@GetMapping("/eventCreation")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<EventCreation> event_creation_list = event_creation_service.listAll();
				ResponseEntity<Object> event_creation_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK,event_creation_list);
				return event_creation_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/fetchAllEventCreation")
	public ResponseEntity<Object> fetchAllEventCreationDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value= "school_id",required = false) Integer school_id,
			@RequestParam(value= "date",required = false) String date,
			@RequestParam(value= "room_id",required = false) Integer room_id,
			@RequestParam(value= "facility_type_id",required = false) Integer facility_type_id,
			@RequestParam(value="sort") String sort,@RequestParam(value="created_by",required = false) Integer created_by,
			@RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> event_creation_sorted = event_creation_service.fetchAllEventCreationDetails(pageable, keyword,school_id,date,room_id,facility_type_id, created_by);//,column,value);
					return event_creation_sorted;
				}else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> event_creation_pageable = event_creation_service.fetchAllEventCreationDetailsWOKeyword(pageable1,school_id,date,room_id,facility_type_id, created_by);
					return event_creation_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllEventCreationForApprover")
	public ResponseEntity<Object> fetchAllEventCreationForApprover(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort,@RequestParam(value="userId") Integer userId, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> event_creation_sorted = event_creation_service.fetchAllEventCreationForApproverlistAll1(pageable,userId, keyword);//,column,value);
					return event_creation_sorted;
				}else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> event_creation_pageable = event_creation_service.fetchAllEventCreationForApproverlistAll2(pageable1,userId);
					return event_creation_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/eventCreation/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				EventCreation product = event_creation_service.get(id);
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
	
	@PutMapping("/eventCreation/{id}")
	public ResponseEntity<Object> update(@RequestBody EventCreation ec, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
					event_creation_service.get(id);
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					ec.setModified_by(jwtDetails.getUserId());
					ec.setModified_username(jwtDetails.getUserName());
					event_creation_service.updateEventCreation(ec);
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
	
	@DeleteMapping("/eventCreation/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			event_creation_service.delete(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@DeleteMapping("/activateEventCreation/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			event_creation_service.delete1(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/fetchEventDetailsOnSchoolId/{school_id}")
	public ResponseEntity<Object> fetchEventDetailsOnSchoolId(@PathVariable Integer school_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> event_details = event_creation_service.fetchEventDetailsOnSchoolId(school_id);
			ResponseEntity<Object> event_details_response = ResponseHandler.generateResponse(true, HttpStatus.OK, event_details);
			return event_details_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
	@GetMapping("/getEventReportDetails")
	public ResponseEntity<Object> getEventReportDetails(@RequestParam(value="room_id") Integer room_id,@RequestParam(value="Date") String date) {
		if(RateLimitController.bucket.tryConsume(1)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
			LocalDate localDate = LocalDate.parse(date, formatter);
			List<Map<String, Object>> event_details = event_creation_service.getEventReportDetails(room_id,localDate);
			ResponseEntity<Object> event_details_response = ResponseHandler.generateResponse(true, HttpStatus.OK, event_details);
			return event_details_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
	
	@PutMapping("/updateEventCreation/{id}")
	public ResponseEntity<Object> updateEventCreation(@RequestBody EventCreationDto dto,
			@PathVariable Integer id, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {

			event_creation_service.updateEventCreation(dto, jwtToken);
			ResponseEntity<Object> emp_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return emp_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
}
