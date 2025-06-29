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
import com.au.model.MealVendorAssignment;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.MealVendorAssignmentService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class MealVendorAssignmentController {
	
Logger Log = LoggerFactory.getLogger(MealVendorAssignmentController.class);
	
	@Autowired
	private MealVendorAssignmentService mva_ser;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/saveMealVendorAssignment")
	public ResponseEntity<Object> saveMealVendorAssignment(@RequestBody @Valid MealVendorAssignment mva, @RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException{
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				mva.setCreated_by(jwtDetails.getUserId());
				mva.setCreated_username(jwtDetails.getUserName());
				MealVendorAssignment ac1 = mva_ser.saveMealVendorAssignment(mva);
				ResponseEntity<Object> mvaresponse = ResponseHandler.generateResponse(true, HttpStatus.CREATED, ac1);
				return mvaresponse;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	
	
	@GetMapping("/getActiveMealVendorAssignment")
	public ResponseEntity<Object>  listAll1(){
		if(RateLimitController.bucket.tryConsume(1)) {
				List<MealVendorAssignment> list = mva_ser.listAll1();
				ResponseEntity<Object>  mvaresponse = ResponseHandler.generateResponse(true, HttpStatus.OK, list);
				return mvaresponse;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
   	
   	
   	@GetMapping("/fetchAllMealVendorAssignmentDetails")
	public ResponseEntity<Object> fetchAllMealVendorAssignmentDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> approver_creation_list_sorted = mva_ser.fetchAllMealVendorAssignmentDetails(pageable, keyword);//,column,value);
				return approver_creation_list_sorted;
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> approver_creation_list_pageable = mva_ser.fetchAllMealVendorAssignmentDetails1(pageable1);
				return approver_creation_list_pageable;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
   	
   	@GetMapping("/getMealVendorAssignmentById/{id}")
	public ResponseEntity<Object> getMealVendorAssignmentById(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
	    	
					MealVendorAssignment product = mva_ser.get(id);
						ResponseEntity<Object> approver_creation_list = ResponseHandler.generateResponse(true, HttpStatus.OK, product);
						return approver_creation_list;
				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
   	
   	@PutMapping("/updateMealVendorAssignment/{id}")
	public ResponseEntity<?> update(@RequestBody @Valid MealVendorAssignment mva, @PathVariable Integer id,@RequestHeader("Authorization")  String jwtToken)
	      throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
					//Bank existProduct = bank_service.get(id);
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					mva.setModified_by(jwtDetails.getUserId());
					mva.setModified_username(jwtDetails.getUserName());
					mva_ser.saveUpdateVendorAssignment(mva);
					System.out.println("dddddddddddddddddddddddd ");
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
	
	
	@DeleteMapping("/deactivateMealVendorAssignment/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				mva_ser.delete(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}

	@DeleteMapping("/activateMealVendorAssignment/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				mva_ser.delete1(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	

}
