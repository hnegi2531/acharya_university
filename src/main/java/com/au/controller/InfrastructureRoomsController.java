package com.au.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.au.dto.JwtDetails;
import com.au.model.InfrastructureRooms;
import com.au.repository.InfrastructureRoomsRepository;
import com.au.response.ResponseHandler;
import com.au.service.InfrastructureRoomsService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class InfrastructureRoomsController {
	
	Logger log = LoggerFactory.getLogger(InfrastructureRoomsController.class);

	@Autowired
	private JwtTokenService jwt_service;
	
	@Autowired
	private InfrastructureRoomsService rooms_ser;
	
	@Autowired
	public InfrastructureRoomsRepository rooms_repo;
	
	@PostMapping("/rooms")
	public ResponseEntity<Object> saveRooms(@Valid @RequestBody InfrastructureRooms rooms,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			
			rooms_ser.checkForExistingData(rooms);
			
			List<InfrastructureRooms> rms = rooms_ser.saveRooms(jwtDetails,rooms);
			ResponseEntity<Object> rooms_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, rms);
			return rooms_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/rooms")
	public ResponseEntity<Object> listAll() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<InfrastructureRooms> list_rooms = rooms_ser.listAll();
			ResponseEntity<Object> list_rooms_response = ResponseHandler.generateResponse(true, HttpStatus.OK,list_rooms);
			return list_rooms_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllRoomsDetail")
	public ResponseEntity<Object> listAll1(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword) {
		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> rooms_sorted = rooms_ser.getAllDataFilteredByKeyword(pageable,
						keyword);
				return rooms_sorted;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> rooms_pageable = rooms_ser.getAllSortedData(pageable1);
				return rooms_pageable;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/rooms/{room_id}")
	public ResponseEntity<Object> get(@PathVariable Integer room_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				InfrastructureRooms room = rooms_ser.get(room_id);
				ResponseEntity<Object> room_response = ResponseHandler.generateResponse(true, HttpStatus.OK,room);
				return room_response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
						HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/rooms/{room_id}")
	public ResponseEntity<Object> update(@RequestBody @Valid InfrastructureRooms ir, @PathVariable Integer room_id,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				ir.setModified_by(jwtDetails.getUserId());
				ir.setModified_username(jwtDetails.getUserName());
				rooms_ser.saveUpdateRoom(ir);
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,HttpStatus.OK);
				return response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}
	}
	
	@DeleteMapping("/deactivateRoom/{room_id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer room_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			rooms_ser.deactivate(room_id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateRoom/{room_id}")
	public ResponseEntity<Object> activate(@PathVariable Integer room_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			rooms_ser.activate(room_id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getAllActiveRoomsForTimeTable")
	public ResponseEntity<Object> getAllActiveRoomsForTimeTable() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<InfrastructureRooms> sections = rooms_ser.getAllActiveRoomsForTimeTable();
			ResponseEntity<Object> sections_response = ResponseHandler.generateResponse(true, HttpStatus.OK, sections);
			return sections_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
//	@PutMapping("/updateRoomsIfExist/{ob_ids}")
//	public ResponseEntity<Object> update(@RequestBody List<VendorOpeningBalance> vob,@PathVariable List<Integer> ob_ids,@RequestHeader("Authorization") String jwtToken)
//			throws JsonParseException, JsonMappingException, IOException {
//		if(RateLimitController.bucket.tryConsume(1)) {
//			try {
//				//VoucherHead existProduct = vs.get(id);
//				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
//				vobs_ser.update_VendorOpeningBalance(vob,jwtToken);
//				ResponseEntity<Object> ven_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
//				return ven_response;
//			} catch (NoSuchElementException e) {
//				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
//				return response;
//			}
//		} else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}   
//	}
	
	
	@GetMapping("/getAllActiveRoomsForTimeTableBsn/{time_slot_id}/{from_date}/{to_date}/{day}")
	public ResponseEntity<Object> getAllActiveRoomsForTimeTableSn(@PathVariable Integer time_slot_id,@PathVariable String from_date,
			@PathVariable String to_date, @PathVariable String day) throws ParseException {
		if(RateLimitController.bucket.tryConsume(1)) {
			DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
			Date date1=df.parse(from_date);
			Date date2=df.parse(to_date);
			List<Map<String,Object>> sections = rooms_ser.getAllActiveRoomsForTimeTableBsn(time_slot_id,date1,date2,day);
			ResponseEntity<Object> sections_response = ResponseHandler.generateResponse(true, HttpStatus.OK, sections);
			return sections_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getAllActiveRoomsForTimeTableBsn/{time_slot_id}/{from_date}/{to_date}")
	public ResponseEntity<Object> getAllActiveRoomsForTimeTableSn(@PathVariable Integer time_slot_id,@PathVariable String from_date,@PathVariable String to_date) throws ParseException {
		if(RateLimitController.bucket.tryConsume(1)) {
			DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
			Date date1=df.parse(from_date);
			Date date2=df.parse(to_date);
			List<Map<String,Object>> sections = rooms_ser.getAllActiveRoomsForTimeTableBsn(time_slot_id,date1,date2);
			ResponseEntity<Object> sections_response = ResponseHandler.generateResponse(true, HttpStatus.OK, sections);
			return sections_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/roomsForTimeTableRoomSwapping/{time_slot_id}/{date}")
	public ResponseEntity<Object> roomsForTimeTableRoomSwapping(@PathVariable Integer time_slot_id,@PathVariable String date) throws ParseException {
		if(RateLimitController.bucket.tryConsume(1)) {
			DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
			Date date1=df.parse(date);
			List<Map<String,Object>> sections = rooms_ser.roomsForTimeTableRoomSwapping(time_slot_id,date1);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, sections);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@GetMapping("/getEventRoomAvailabilityForTimeTable")
	public ResponseEntity<Object> getEventRoomAvailabilityForTimeTable(@RequestParam(value = "block_id", required = false) Integer block_id,
			@RequestParam(value = "floor_id", required = false) Integer floor_id,
            @RequestParam("month") Integer month,
            @RequestParam("year") Integer year) throws ParseException {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String,Object>> sections = rooms_ser.getEventRoomAvailabilityForTimeTable(block_id,floor_id,month,year);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, sections);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}	

}
