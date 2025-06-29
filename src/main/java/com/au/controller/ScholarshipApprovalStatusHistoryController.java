package com.au.controller;

import java.io.IOException;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.JwtDetails;
import com.au.model.ScholarshipApprovalStatus;
import com.au.model.ScholarshipApprovalStatusHistory;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.ScholarshipApprovalStatusHistoryService;
import com.au.service.ScholarshipApprovalStatusService;
import com.au.service.ScholarshipService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/${secretkey1}")
public class ScholarshipApprovalStatusHistoryController {
	
Logger log = LoggerFactory.getLogger(ScholarshipApprovalStatusController.class);
	
	@Autowired
	private ScholarshipApprovalStatusHistoryService s_service;

	@Autowired
	private ScholarshipService s_s;
	
	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/scholarshipApprovalStatusHistory")
	public ResponseEntity<Object> saveScholarshipapprovalstatus(@RequestBody @Valid ScholarshipApprovalStatusHistory l,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			l.setModified_by(jwtDetails.getUserId());
			l.setModified_username(jwtDetails.getUserName());
			ScholarshipApprovalStatusHistory sch_approv = s_service.save_ScholarshipApprovalStatusHistory(l);
			ResponseEntity<Object> sch_approv_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, sch_approv);
			return sch_approv_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllScholarshipApprovalStatusHistoryData")
	public ResponseEntity<Object> listAll(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> sch_approv_sorted = s_service.listAll1(pageable, keyword);//,column,value);
						return sch_approv_sorted;
				}else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> sch_approv_pageable = s_service.listAll2(pageable1);
						return sch_approv_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		 //return s_service.listAll();
	}

	@GetMapping("/getScholarshipApprovalStatusHistoryData/{scholarship_id}")
	public ResponseEntity<Object> getScholarshipApprovalStatusHistoryData(@PathVariable Integer scholarship_id)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<ScholarshipApprovalStatusHistory> is_approved_data = s_service.getScholarshipApprovalStatusHistoryData(scholarship_id);
				ResponseEntity<Object> is_approved_data_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, is_approved_data);
				return is_approved_data_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
}
