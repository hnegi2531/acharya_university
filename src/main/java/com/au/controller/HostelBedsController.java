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
import com.au.model.HostelBeds;
import com.au.model.HostelBlocks;
import com.au.response.ResponseHandler;
import com.au.service.HostelBedsService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey6}")
@CrossOrigin
public class HostelBedsController {

	@Autowired
	private HostelBedsService s_service;
	
	Logger log = LoggerFactory.getLogger(HostelBedsController.class);
	
	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/HostelBeds")
	public ResponseEntity<Object> saveSubjectType(@RequestBody @Valid HostelBeds r,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			r.setCreatedBy(jwtDetails.getUserId());
			r.setCreatedUsername(jwtDetails.getUserName());
			HostelBeds hostel_beds = s_service.saveHostelBeds(r);
			ResponseEntity<Object> hostel_beds_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, hostel_beds);
			return hostel_beds_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	

	
	@GetMapping("/HostelBeds")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HostelBeds> hostel_beds_list = s_service.listAll();
			ResponseEntity<Object> hostel_beds_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, hostel_beds_list);
			return hostel_beds_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

	@GetMapping("/fetchAllHostelBedsDetails")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> hostel_beds_sorted = s_service.listAll1(pageable, keyword);//,column,value);
				return hostel_beds_sorted;
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> hostel_beds_pageable = s_service.listAll2(pageable1);
				return hostel_beds_pageable;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/HostelBeds/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				HostelBeds product = s_service.get(id);
				ResponseEntity<Object> program_type_response_by_id = ResponseHandler.generateResponse(true,HttpStatus.OK, product);
				return program_type_response_by_id;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/HostelBeds/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid HostelBeds r, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				s_service.get(id);
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

				r.setModifiedBy(jwtDetails.getUserId());
				r.setModifiedUsername(jwtDetails.getUserName());

				s_service.saveHostelBeds(r);
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

	@DeleteMapping("/HostelBeds/{id}")
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

	@DeleteMapping("/activateHostelBeds/{id}")
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

	@GetMapping("/fetchHostelBedDetails")
	public ResponseEntity<Object> getHosteBedDetails(){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> bed_room_name_Details = s_service.getHosteBedDetails();
			ResponseEntity<Object> bed_room_name_Details_response = ResponseHandler.generateResponse(true,HttpStatus.OK, bed_room_name_Details);
			return bed_room_name_Details_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getCountOfFloorsRoomsBedsForGridView")
	public ResponseEntity<Object> getCountOfFloorsRoomsBeds() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> bed_details_by_block_id = s_service.getCountOfFloorsRoomsBeds();
			ResponseEntity<Object> bed_response_by_block_id = ResponseHandler.generateResponse(true, HttpStatus.OK, bed_details_by_block_id);
			return bed_response_by_block_id;
	
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	

	@GetMapping("/getAllBedsDetails/{hostelsBlockId}")
	public ResponseEntity<Object> getAllBedsDetails(@PathVariable Integer hostelsBlockId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			HashMap<Integer, Object> bed_details_by_block_id = s_service.getAllBedsDetailsByHostelsBlockId(hostelsBlockId);
			ResponseEntity<Object> bed_response_by_block_id = ResponseHandler.generateResponse(true, HttpStatus.OK, bed_details_by_block_id);
			return bed_response_by_block_id;
	
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/hostelBedsByHostelBlockAndFloor")
	public ResponseEntity<Object> hostelBedsByHostelBlockAndFloor(
			@RequestParam(name = "hostelBlockId") Integer hostelsBlockId,
			@RequestParam(name = "hostelFloorId", required = false) Integer hostelsFloorId,
			@RequestParam(name = "roomTypeId") Integer roomTypeId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Map<Object, List<HashMap<String, Object>>> hostelBedlist = s_service.hostelBedsByHostelBlockAndFloor(hostelsBlockId,hostelsFloorId,roomTypeId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, hostelBedlist);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@GetMapping("/unassignedBedDetailsForBedChange")
	public ResponseEntity<Object> unassignedBedDetails() {
		if(RateLimitController.bucket.tryConsume(1)) {
			Map<Object, List<HashMap<String, Object>>> hostelBedlist = s_service.unassignedBedDetails();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, hostelBedlist);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@GetMapping("/hostelBedsByHostelBlockId")
	public ResponseEntity<Object> hostelBedsByHostelBlockId(
			@RequestParam(name = "hostelBlockId") Integer hostelsBlockId,@RequestParam(name = "roomTypeId",required=false) Integer roomTypeId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Map<Object, Map<Object, List<HashMap<String, Object>>>> hostelBedlist = s_service.hostelBedsByHostelBlockId(hostelsBlockId,roomTypeId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, hostelBedlist);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
}
