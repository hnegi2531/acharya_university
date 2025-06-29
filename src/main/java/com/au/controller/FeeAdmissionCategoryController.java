package com.au.controller;

import java.io.IOException;
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
import com.au.model.FeeAdmissionCategory;
import com.au.response.ResponseHandler;
import com.au.service.FeeAdmissionCategoryService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

//@RequestMapping("/api")
@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class FeeAdmissionCategoryController {

	Logger log = LoggerFactory.getLogger(FeeAdmissionCategoryController.class);
		
	@Autowired
	private FeeAdmissionCategoryService fee_service;
	
	@Autowired
	private JwtTokenService jwt_service;

	
	@PostMapping("/FeeAdmissionCategory")
	public ResponseEntity<Object> saveFeeAdmissionCategory(@RequestBody @Valid FeeAdmissionCategory fee,@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				fee.setCreated_by(jwtDetails.getUserId());
				fee.setCreated_username(jwtDetails.getUserName());
				FeeAdmissionCategory fac = fee_service.saveFeeAdmissionCategory(fee);
				ResponseEntity<Object> fee_admission_category_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, fac);
				return fee_admission_category_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/FeeAdmissionCategory")
	public ResponseEntity<Object> listAll(){
		if(RateLimitController.bucket.tryConsume(1)) {
				List<FeeAdmissionCategory> list_admission_category = fee_service.listAll();
				ResponseEntity<Object> fee_admission_category_response= ResponseHandler.generateResponse(true, HttpStatus.OK, list_admission_category);
				return fee_admission_category_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	  
	 @GetMapping("/fetchAllFeeAdmissionCategoryDetail")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
		 if(RateLimitController.bucket.tryConsume(1)) {
			 	Sort sorted = Sort.by(Direction.DESC, sort );
			 	if(keyword != null) {	
			 			Pageable pageable = PageRequest.of(page, page_size,sorted);
			 			System.out.println("page, page_size, sorted, keyword");
			 			ResponseEntity<Object> fee_admission_category_sorted =  fee_service.listAll1(pageable, keyword);//,column,value);
			 			return fee_admission_category_sorted;
			 	}else {
			 			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			 			System.out.println("page, page_size, sorted");
			 			ResponseEntity<Object> fee_admission_category_pageable = fee_service.listAll2(pageable1);
			 			return fee_admission_category_pageable;
			 	}
		 }else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}	 	
		//return fee_service.listAll1();
	}  

	
	@GetMapping("/FeeAdmissionCategory/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
	    	
						FeeAdmissionCategory fee = fee_service.get(id);
						ResponseEntity<Object> fee_admission_category_by_id_response= ResponseHandler.generateResponse(true, HttpStatus.OK, fee);
						return fee_admission_category_by_id_response;
	        
				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@PutMapping("/FeeAdmissionCategory/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid FeeAdmissionCategory fee, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
						//FeeAdmissionCategory existProduct = fee_service.get(id);
						JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
						fee.setModified_by(jwtDetails.getUserId());
						fee.setModified_username(jwtDetails.getUserName());
						fee_service.saveFeeAdmissionCategory1(fee);
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
	
	
	@DeleteMapping("/FeeAdmissionCategory/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				fee_service.delete(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@DeleteMapping("/ActivatefeeAdmissionCategory/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				fee_service.delete1(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
}
