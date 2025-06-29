package com.au.controller;

import java.io.IOException;
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
import com.au.dto.WardenRequest;
import com.au.model.HostelFloor;
import com.au.model.HostelRoomType;
import com.au.response.ResponseHandler;
import com.au.service.HostelFloorService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey6}")
@CrossOrigin
public class HostelFloorController {
	
	@Autowired
	private HostelFloorService s_service;
	
	Logger log = LoggerFactory.getLogger(HostelFloorController.class);
	
	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/HostelFloor")
	public ResponseEntity<Object> saveSubjectType(@RequestBody @Valid HostelFloor r,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			r.setCreatedBy(jwtDetails.getUserId());
			r.setCreatedUsername(jwtDetails.getUserName());
			HostelFloor hf = s_service.saveHostelFloor(r);
			ResponseEntity<Object> hostel_floor_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, hf);
			return hostel_floor_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	

	
	@GetMapping("/HostelFloor")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HostelFloor> hostel_floor_list = s_service.listAll();
			ResponseEntity<Object> hostel_floor_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, hostel_floor_list);
			return hostel_floor_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}


	@GetMapping("/fetchAllHostelFloorDetails")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> hostel_floor_sorted = s_service.listAll1(pageable, keyword);//,column,value);
				return hostel_floor_sorted;
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> hostel_floor_pageable = s_service.listAll2(pageable1);
				return hostel_floor_pageable;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/HostelFloor/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				HostelFloor product = s_service.get(id);
				ResponseEntity<Object> hostel_floor_response_by_id = ResponseHandler.generateResponse(true,HttpStatus.OK, product);
				return hostel_floor_response_by_id;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/HostelFloor/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid HostelFloor r, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				HostelFloor existProduct = s_service.get(id);
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

				r.setModifiedBy(jwtDetails.getUserId());
				r.setModifiedUsername(jwtDetails.getUserName());

				s_service.saveHostelFloor1(r);
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

	@DeleteMapping("/HostelFloor/{id}")
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

	@DeleteMapping("/activateHostelFloor/{id}")
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
	
	@GetMapping("/fetchFloorDetailsByBlockid/{block_id}")
	public ResponseEntity<Object> getFloorByBlockid(@PathVariable Integer block_id){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HostelFloor> hostel_floors_by_block_id = s_service.getFloorByBlockid(block_id);
			ResponseEntity<Object> hostel_floors_by_block_id_response = ResponseHandler.generateResponse(true,HttpStatus.OK, hostel_floors_by_block_id);
			return hostel_floors_by_block_id_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	/*
	@PutMapping("/updateWardenId/{wardens_id}/{hostel_floor_id}")
	public void updateWardenId(@PathVariable Integer wardens_id,@PathVariable Integer hostel_floor_id) {
		s_service.updateWardenId(wardens_id,hostel_floor_id);
	}
	*/
	@PutMapping("/updateWardenId1/{hostel_floor_id}")
	public ResponseEntity<Object> updateWardenId1(@RequestBody WardenRequest wid,@PathVariable Integer hostel_floor_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			s_service.updateWardenId(wid,hostel_floor_id);
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchhostelFloorIndex")
	public ResponseEntity<Object> fetchhostelFloorIndex(){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String,Object>> hostel_floor_details = s_service.fetchhostelFloorIndex();
			ResponseEntity<Object> hostel_floor_details_response = ResponseHandler.generateResponse(true,HttpStatus.OK, hostel_floor_details);
			return hostel_floor_details_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

}
