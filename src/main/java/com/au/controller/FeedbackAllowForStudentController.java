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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.JwtDetails;
import com.au.model.MealBill;
import com.au.response.ResponseHandler;
import com.au.service.FeedbackAllowForStudentService;
import com.au.service.JwtTokenService;
import com.au.service.MealBillService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.au.model.AcademicsProgramVision;
import com.au.model.FeedbackAllowForStudent;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class FeedbackAllowForStudentController {

	Logger log = LoggerFactory.getLogger(FeedbackAllowForStudentController.class);

	@Autowired
	private JwtTokenService jwt_service;

	@Autowired
	private FeedbackAllowForStudentService feedbackAllowForStudentService;
	
	
	@PostMapping("/createFeedbackAllowForStudent")
	public ResponseEntity<Object> createFeedbackAllowForStudent(@RequestBody @Valid List<FeedbackAllowForStudent> apv,@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			apv.stream().forEach(a -> {

			a.setCreated_by(jwtDetails.getUserId());
			a.setCreated_username(jwtDetails.getUserName());
			});
			List<FeedbackAllowForStudent> apvs = feedbackAllowForStudentService.createFeedbackAllowForStudent(apv);
			ResponseEntity<Object> co_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, apvs);
		return co_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getAllActiveFeedbackAllowForStudent")
	public ResponseEntity<Object> getAllActiveFeedbackAllowForStudent() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<FeedbackAllowForStudent> meal_list = feedbackAllowForStudentService.getAllActiveFeedbackAllowForStudent();
			ResponseEntity<Object> meal_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					meal_list);
			return meal_list_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllFeedbackAllowForStudentDetails")
	public ResponseEntity<Object> fetchAllFeedbackAllowForStudentDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> list_sorted = feedbackAllowForStudentService.fetchAllFeedbackAllowForStudentDetails(pageable, keyword);//,column,value);
				return list_sorted;
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> list_pageable = feedbackAllowForStudentService.fetchAllFeedbackAllowForStudentDetails1(pageable1);
				return list_pageable;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
}
