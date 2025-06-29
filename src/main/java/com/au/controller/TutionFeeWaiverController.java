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

import com.au.model.TutionFeeWaiver;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.TutionFeeWaiverService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class TutionFeeWaiverController {
	
		Logger log = LoggerFactory.getLogger(TutionFeeWaiverController.class);
		
		@Autowired
		private TutionFeeWaiverService tut_waive_ser;
		
		@Autowired
		private JwtTokenService jwt_service;
		
		@PostMapping("/tutionFeeWaiver")
		public ResponseEntity<Object> saveTutionFeeWaiver(@RequestBody @Valid TutionFeeWaiver tut, @RequestHeader("Authorization") String jwtToken)
				throws JsonParseException, JsonMappingException, IOException{
			if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails=jwt_service.callJwtToken(jwtToken);
				tut.setCreated_by(jwtDetails.getUserId());
				tut.setCreated_username(jwtDetails.getUserName());

				TutionFeeWaiver tution_fee_waiver = tut_waive_ser.saveTutionFeeWaiver(tut);
				ResponseEntity<Object> tution_fee_waiver_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, tution_fee_waiver);
				return tution_fee_waiver_response;
			}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		@GetMapping("/tutionFeeWaiver")
		public ResponseEntity<Object> getTutionFeeActiveDetails(){
			if(RateLimitController.bucket.tryConsume(1)) {
				List<TutionFeeWaiver> list_tution_fee_waiver = tut_waive_ser.getActiveDetails();
				ResponseEntity<Object> list_tution_fee_waiver_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_tution_fee_waiver);
				return list_tution_fee_waiver_response;
			}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		@GetMapping("/fetchAlltutionFeeWaiverDetails")
		public ResponseEntity<Object> getTutionFeeAllDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
				@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
			if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> tution_fee_waiver_sorted = tut_waive_ser.getAllDetails1(pageable, keyword);//,column,value);
					return tution_fee_waiver_sorted;
				}else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> tution_fee_waiver_pageable = tut_waive_ser.getAllDetails2(pageable1);
					return tution_fee_waiver_pageable;
				}
			}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		@GetMapping("/tutionFeeWaiverDetailsById/{tution_fee_waiver_id}")
		public ResponseEntity<Object> getTutionFeeDetailsById(@PathVariable Integer tution_fee_waiver_id) {
			if(RateLimitController.bucket.tryConsume(1)) {
				try {
					Map<String ,Object> tut_Fee = tut_waive_ser.getTutionFeeDetailsById(tution_fee_waiver_id);
					ResponseEntity<Object> tut_Fee_response_by_id = ResponseHandler.generateResponse(true,HttpStatus.OK, tut_Fee);
					return tut_Fee_response_by_id;

				} catch (NoSuchElementException e) {
					ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
					return response1;
				}
			} else {
				ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
				return rs;
			}
		}
		
		@PutMapping("/tutionFeeWaiver/{tution_fee_waiver_id}")
		public ResponseEntity<Object> updateTutionFeeWaiver(@RequestBody TutionFeeWaiver tut, @PathVariable Integer tution_fee_waiver_id,
				@RequestHeader("Authorization") String jwtToken) throws JsonParseException, JsonMappingException, IOException {
			if(RateLimitController.bucket.tryConsume(1)) {	
				try {
					
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					tut.setModified_by(jwtDetails.getUserId());
					tut.setModified_username(jwtDetails.getUserName());
					tut_waive_ser.updateTutionFeeWaiver(tut);
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
		
		@DeleteMapping("/activateTutionFeeWaiver/{tution_fee_waiver_id}")
		public ResponseEntity<Object> delete1(@PathVariable Integer tution_fee_waiver_id) {
			if(RateLimitController.bucket.tryConsume(1)) {
				tut_waive_ser.delete1(tution_fee_waiver_id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
			}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		@DeleteMapping("/deactivateTutionFeeWaiver/{tution_fee_waiver_id}")
		public ResponseEntity<Object> delete2(@PathVariable Integer tution_fee_waiver_id) {
			if(RateLimitController.bucket.tryConsume(1)) {
				tut_waive_ser.delete2(tution_fee_waiver_id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
			}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		@GetMapping("/tutionFeeWaiverDetailsByStudentId/{student_id}")
		public ResponseEntity<Object> getTutionFeeDetailsByStudentId(@PathVariable Integer student_id) {
			if(RateLimitController.bucket.tryConsume(1)) {
				try {

					TutionFeeWaiver tut_Fee = tut_waive_ser.getTutionFeeDetailsByStudentId(student_id);
					ResponseEntity<Object> tut_Fee_response = ResponseHandler.generateResponse(true,HttpStatus.OK, tut_Fee);
					return tut_Fee_response;

				} catch (NoSuchElementException e) {
					ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
					return response1;
				}
			} else {
				ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
				return rs;
			}
		}
		
		@GetMapping("/fetchTutionFeeWaiverForEdit/{tution_fee_waiver_id}") 
		public ResponseEntity<Object> fetchTutionFeeWaiverForEdit(@RequestBody @PathVariable Integer tution_fee_waiver_id) {
			if(RateLimitController.bucket.tryConsume(1)) {
					List<Map<String, Object>> data = tut_waive_ser.fetchTutionFeeWaiverForEdit(tution_fee_waiver_id);
					ResponseEntity<Object> list_of_fee_template_detail_response= ResponseHandler.generateResponse(true, HttpStatus.OK, data);
					return list_of_fee_template_detail_response;
			}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}		
		}	

}
