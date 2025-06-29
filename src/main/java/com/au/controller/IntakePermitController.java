package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Map.Entry;

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
import com.au.model.IntakePermit;
import com.au.response.ResponseHandler;
import com.au.service.IntakePermitService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class IntakePermitController {
	
	Logger log = LoggerFactory.getLogger(IntakePermitController.class);
	
	@Autowired
	private IntakePermitService intake_permit_service;

	@Autowired
	private JwtTokenService jwt_service;
	
	
	@PostMapping("/copiedIntakePermitDetails")
	public ResponseEntity<Object> copiedIntakePermitDetails(@RequestBody @Valid List<IntakePermit> ipl, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				ipl.stream().forEach(ip ->{
					ip.setCreated_by(jwtDetails.getUserId());
					ip.setCreated_username(jwtDetails.getUserName());
				});
				List<IntakePermit> copied_intake_permit_list =intake_permit_service.copiedIntakePermitDetails(ipl);
				ResponseEntity<Object> copied_intake_permit_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK,copied_intake_permit_list);
				return copied_intake_permit_list_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/intakePermit")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<IntakePermit> intake_permit_list = intake_permit_service.listAll();
				ResponseEntity<Object> intake_permit_response = ResponseHandler.generateResponse(true, HttpStatus.OK,intake_permit_list);
				return intake_permit_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/fetchAlllIntakePermitDetails")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> intake_permit_sorted = intake_permit_service.listAll1(pageable, keyword);//,column,value);
					return intake_permit_sorted;
				}else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> intake_permit_pageable = intake_permit_service.listAll2(pageable1);
					return intake_permit_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getIntakePermitByIntakeId/{intake_id}")
	public ResponseEntity<Object> getIntakePermitByIntakeId(@PathVariable Integer intake_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> intake_permit_list = intake_permit_service.getIntakePermitByIntakeId(intake_id);
				ResponseEntity<Object> intake_permit_response = ResponseHandler.generateResponse(true, HttpStatus.OK,intake_permit_list);
				return intake_permit_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	

	@PutMapping("/updateIntake/{intake_id}")
	public ResponseEntity<Object> update(@RequestBody @Valid IntakePermit ip,
			@PathVariable Integer intake_id, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			ip.setModified_by(jwtDetails.getUserId());
			ip.setModified_username(jwtDetails.getUserName());
			intake_permit_service.saveIntake(ip);
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
		
	@PutMapping("/intakePermit/{ids}")
	public ResponseEntity<Object> update(@RequestBody List<IntakePermit> ip, @PathVariable List<Integer> ids,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					ip.stream().forEach(ip1 -> {
						ip1.setModified_by(jwtDetails.getUserId());
						ip1.setModified_username(jwtDetails.getUserName());
					});
					intake_permit_service.updateIntakePermit(ip);
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
	
	@GetMapping("/getIntakePermitDetailsForGridView/{intake_ids}")
	public ResponseEntity<Object> getIntakePermitDetailsForGridView(@PathVariable List<Integer> intake_ids) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Map<Object, List<HashMap<String, Object>>> intake_permit_list = intake_permit_service.getIntakePermitDetailsForGridView(intake_ids);
				ResponseEntity<Object> intake_permit_response = ResponseHandler.generateResponse(true, HttpStatus.OK,intake_permit_list);
				return intake_permit_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

}
