package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
import com.au.model.ScholarshipVoucherHeadWiseAmountDetailsHistory;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.ScholarshipVoucherHeadWiseAmountDetailsHistoryService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RequestMapping("/api/${secretkey1}")
@CrossOrigin
@RestController
public class ScholarshipVoucherHeadWiseAmountDetailsHistoryController {
	

	Logger log = LoggerFactory.getLogger(ScholarshipVoucherHeadWiseAmountDetailsHistoryController.class);
	
	@Autowired
	private ScholarshipVoucherHeadWiseAmountDetailsHistoryService svhwad_service;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/saveScholarshipVoucherHeadWiseAmountDetailsHistory")
	public ResponseEntity<Object> saveScholarshipVocherHeadWiseAmountDetails(@RequestBody @Valid List<ScholarshipVoucherHeadWiseAmountDetailsHistory> svhwad,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				svhwad.stream().forEach(svhw ->{
					svhw.setCreated_by(jwtDetails.getUserId());
					svhw.setCreated_username(jwtDetails.getUserName());
				});
				List<ScholarshipVoucherHeadWiseAmountDetailsHistory> scholarship_head_wise_amount_details = svhwad_service.saveScholarshipVoucherHeadWiseAmountDetailsHistory(svhwad);
				ResponseEntity<Object> scholarship_head_wise_amount_details_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, scholarship_head_wise_amount_details);
				return scholarship_head_wise_amount_details_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/scholarshipVoucherHeadWiseAmountDetailsHistoryOnScholarshipId/{scholarship_id}")
	public ResponseEntity<Object> scholarshipHeadWiseAmountDetailsOnScholarshipId(@PathVariable Integer scholarship_id)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<HashMap<String,Object>> scholarship_head_wise_amount_details_list = svhwad_service.scholarshipHeadWiseAmountDetailsOnScholarshipId(scholarship_id);
				ResponseEntity<Object> scholarship_head_wise_amount_details_list_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, scholarship_head_wise_amount_details_list);
				return scholarship_head_wise_amount_details_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/scholarshipVoucherHeadWiseAmountDetailsHistoryOnScholarshipIdAndVocherHeadNewId/{scholarship_id}/{voucher_head_new_id}")
	public ResponseEntity<Object> scholarshipHeadWiseAmountDetailsOnVocherHeadNewId(@PathVariable Integer scholarship_id,@PathVariable Integer voucher_head_new_id)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<HashMap<String,Object>> scholarship_head_wise_amount_details_list = svhwad_service.scholarshipHeadWiseAmountDetailsOnVocherHeadNewId(scholarship_id,voucher_head_new_id);
				ResponseEntity<Object> scholarship_head_wise_amount_details_list_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, scholarship_head_wise_amount_details_list);
				return scholarship_head_wise_amount_details_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@DeleteMapping("/deactivateScholarshipVoucherHeadWiseAmountDetailsHistory/{id}")
	public ResponseEntity<Object> deactivateScholarshipHeadWiseAmountDetails(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			svhwad_service.deactivateScholarshipHeadWiseAmountDetails(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@DeleteMapping("/activateScholarshipVoucherHeadWiseAmountDetailsHistory/{id}")
	public ResponseEntity<Object> activateScholarshipHeadWiseAmountDetails(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			svhwad_service.activateScholarshipHeadWiseAmountDetails(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@PutMapping("/upadteScholarshipVoucherHeadWiseAmountDetailsHistory/{ids}")
	public ResponseEntity<Object> update(@RequestBody List<ScholarshipVoucherHeadWiseAmountDetailsHistory> p, @PathVariable List<Integer> ids,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
						JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					p.stream().forEach(p1 -> {
						
					
						p1.setModified_by(jwtDetails.getUserId());
						p1.setModified_username(jwtDetails.getUserName());
					});
						svhwad_service.saveScholarshipVoucherHeadWiseAmountDetailsHistory1(p);
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
	
	@GetMapping("/fetchAllScholarshipVoucherHeadWiseAmountDetailsHistory")
	public ResponseEntity<Object> fetchAllCancelAdmissionsDetail(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> cors_stu_assign_sorted = svhwad_service.listAll1(pageable, keyword);//,column,value);
					return cors_stu_assign_sorted;
				}else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> cors_stu_assign_pageable = svhwad_service.listAll2(pageable1);
					return cors_stu_assign_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	
	

}
