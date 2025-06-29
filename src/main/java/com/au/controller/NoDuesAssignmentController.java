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
import com.au.dto.NoDuesAssignmentDto;
import com.au.dto.NoDuesAssignmentRequest;
import com.au.dto.NoDuesAssignmentResponse;
import com.au.model.NoDuesAssignment;
import com.au.model.Resignation;
import com.au.repository.DepartmentRepository;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.NoDuesAssignmentService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class NoDuesAssignmentController {
	
Logger log = LoggerFactory.getLogger(NoDuesAssignmentController.class);
	
	@Autowired
	private NoDuesAssignmentService noDuesAssignmentService;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@Autowired
	private DepartmentRepository deptrepo;
	
	
	@PostMapping("/noDuesAssignment")
	public ResponseEntity<Object> saveNoDuesAssignment(@RequestBody @Valid List<NoDuesAssignmentRequest> noDuesAssignmentRequest,@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<NoDuesAssignmentResponse> noDuesAssignments = noDuesAssignmentService.saveNoDuesAssignment(noDuesAssignmentRequest,jwtToken);
		ResponseEntity<Object> noDuesAssignmentsResponse= ResponseHandler.generateResponse(true, HttpStatus.CREATED, noDuesAssignments);
		return noDuesAssignmentsResponse;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}
	}
	
	
	@GetMapping("/fetchAllNoDuesDetailsBasedOnUserId")
	public ResponseEntity<Object> fetchAllResignationDetailsBasedOnUserId(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="UserId") Integer UserId,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
			Integer count = deptrepo.countBasedOnHodId(UserId);
				if(keyword != null) {	
					if(count >= 0) {
						System.out.println("!!!!!!!!!!!!!!! hod_id");
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						ResponseEntity<Object> resignation_details_sorted = noDuesAssignmentService.listAllWithKeyword(pageable, keyword );//,column,value);
						return resignation_details_sorted;
						}else {
							System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ leaveApprover");
							Pageable pageable = PageRequest.of(page, page_size,sorted);
							ResponseEntity<Object> resignation_details_sorted = noDuesAssignmentService.listAllWithLeaveApprKeyword(pageable, keyword ,UserId);//,column,value);
							return resignation_details_sorted;
						}
				}else {
					if(count >=0) {
						System.out.println("!!!!!!!!!!!!!!! hod_id");
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						ResponseEntity<Object> resignation_details_pageable = noDuesAssignmentService.listAllWithOutKeyword(pageable1 );
						return resignation_details_pageable;
						}else {
							System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ leaveApprover");
							Pageable pageable1 = PageRequest.of(page, page_size,sorted);
							ResponseEntity<Object> resignation_details_pageable = noDuesAssignmentService.listAllLeaveApprWithOutKeyword(pageable1,UserId );
							return resignation_details_pageable;
					} 
				}
		}
				else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
	
	@GetMapping("/getNoDueAssignmentData/{resignation_id}")
	public ResponseEntity<Object> getNoDueAssignmentData(@PathVariable Integer resignation_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> bit = noDuesAssignmentService.getNoDueAssignmentData(resignation_id);
			ResponseEntity<Object> bit_response= ResponseHandler.generateResponse(true, HttpStatus.OK, bit);

			return bit_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/updateNoDuesAssignment/{ids}")
	public ResponseEntity<Object> update(@RequestBody List<NoDuesAssignmentDto> res, @PathVariable List<Integer> ids,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
					noDuesAssignmentService.updateNoDuesAssignment(res,jwtToken);
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
	
	@GetMapping("/getAllNoDueAssignmentData/{resignation_id}")
	public ResponseEntity<Object> getAllNoDueAssignmentData(@PathVariable Integer resignation_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> bit = noDuesAssignmentService.getAllNoDueAssignmentData(resignation_id);
			ResponseEntity<Object> bit_response= ResponseHandler.generateResponse(true, HttpStatus.OK, bit);

			return bit_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
}
