package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
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
import com.au.model.HostelRooms;
import com.au.response.ResponseHandler;
import com.au.service.HostelRoomsService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey6}")
@CrossOrigin
public class HostelRoomsController {

	@Autowired
	private HostelRoomsService s_service;
	
	Logger log = LoggerFactory.getLogger(HostelRoomsController.class);
	
	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/HostelRooms")
	public ResponseEntity<Object> saveHostelRooms(@RequestBody @Valid HostelRooms r,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			r.setCreatedBy(jwtDetails.getUserId());
			r.setCreatedUsername(jwtDetails.getUserName());
			List<HostelRooms> hostel_rooms = s_service.saveHostelRooms(r);
			ResponseEntity<Object> hostel_rooms_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, hostel_rooms);
			return hostel_rooms_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/HostelRooms")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HostelRooms> hostel_rooms_list = s_service.listAll();
			ResponseEntity<Object> hostel_rooms_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, hostel_rooms_list);
			return hostel_rooms_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchAllHostelRoomsDetails")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> hostel_rooms_sorted = s_service.listAll1(pageable, keyword);//,column,value);
				return hostel_rooms_sorted;
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> hostel_rooms_pageable = s_service.listAll2(pageable1);
				return hostel_rooms_pageable;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	

	@GetMapping("/HostelRooms/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				HostelRooms product = s_service.get(id);
				ResponseEntity<Object> hostel_rooms_response_by_id = ResponseHandler.generateResponse(true,HttpStatus.OK, product);
				return hostel_rooms_response_by_id;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/HostelRooms/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid HostelRooms r, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				HostelRooms existProduct = s_service.get(id);
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

				r.setModifiedBy(jwtDetails.getUserId());
				r.setModifiedUsername(jwtDetails.getUserName());

				return s_service.updateHostelRooms(r);
				
			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			}
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@DeleteMapping("/HostelRooms/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			s_service.delete(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateHostelRooms/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			s_service.delete1(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllRoomDetails")
	public ResponseEntity<Object> fetchAllDetails(){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> hostel_room_details = s_service.getAllRoomDetails();
			ResponseEntity<Object> hostel_room_details_response = ResponseHandler.generateResponse(true,HttpStatus.OK,hostel_room_details);
			return hostel_room_details_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/allHOstelsRoomsONBlockAndFloor/{hostelsBlockId}/{hostelsFloorId}")
	public ResponseEntity<Object> allHostelOnBlockAndFloor(@PathVariable Integer hostelsBlockId,@PathVariable Integer hostelsFloorId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> hostel_rooms_list = s_service.allHostelOnBlockAndFloor(hostelsBlockId,hostelsFloorId);
			ResponseEntity<Object> hostel_rooms_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, hostel_rooms_list);
			return hostel_rooms_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/getAllHostelBedDetails/{hostelRoomId}")
	public ResponseEntity<Object> getAllHostelBedDetails(@PathVariable Integer hostelRoomId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> hostel_rooms_list = s_service.getAllHostelBedDetails(hostelRoomId);
			ResponseEntity<Object> hostel_rooms_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, hostel_rooms_list);
			return hostel_rooms_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllUnassignedRoom")
	public ResponseEntity<Object> fetchAllUnassignedRoom(){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> roomDetails = s_service.fetchAllUnassignedRoom();
			return  ResponseHandler.generateResponse(true,HttpStatus.OK,roomDetails);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
}
