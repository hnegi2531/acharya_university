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
import com.au.model.Court;
import com.au.model.LegalDepartmentUsers;
import com.au.response.ResponseHandler;
import com.au.service.CourtService;
import com.au.service.JwtTokenService;
import com.au.service.LegalDepartmentUsersService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class CourtController {
	
	
	Logger log = LoggerFactory.getLogger(CourtController.class);
	
	@Autowired
	private CourtService court_ser;
	
	@Autowired
	private LegalDepartmentUsersService legal_users_ser;
	
	@PostMapping("/court")
	public ResponseEntity<Object> saveCourt(@RequestBody @Valid Court court,@RequestHeader("LegalAuthorization") String legal_validation_token) 
			throws Exception, JsonParseException, JsonMappingException, IOException{
		if(RateLimitController.bucket.tryConsume(1)) {
			LegalDepartmentUsers legal_department_user=legal_users_ser.getLegalDepartmentUsersByToken(legal_validation_token);
			court.setCreated_by(legal_department_user.getLegal_department_user_id());
			court.setCreated_username(legal_department_user.getLegal_department_user_name());
			Court court_data=court_ser.saveCourt(court);
			ResponseEntity<Object> court_data_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, court_data);
			return court_data_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}

	}
	
	@GetMapping("/court")
	public ResponseEntity<Object> getActiveDetails(@RequestHeader("LegalAuthorization") String legal_validation_token){
		if(RateLimitController.bucket.tryConsume(1)) {
			legal_users_ser.getLegalDepartmentUsersByToken(legal_validation_token);
			List<Court> court_list = court_ser.getActiveDetails();
			ResponseEntity<Object> court_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, court_list);
			return court_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllCourtDetails")
	public ResponseEntity<Object> listAll(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword,@RequestHeader("LegalAuthorization") String legal_validation_token){
		if(RateLimitController.bucket.tryConsume(1)) {
			legal_users_ser.getLegalDepartmentUsersByToken(legal_validation_token);
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				ResponseEntity<Object> court_sorted = court_ser.listAll1(pageable, keyword);//,column,value);
				return court_sorted;
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				ResponseEntity<Object> court_pageable = court_ser.listAll2(pageable1);
				return court_pageable;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
		
	}
	
	@GetMapping("/court/{court_id}")
	public ResponseEntity<Object> getDetailById(@PathVariable Integer court_id,@RequestHeader("LegalAuthorization") String legal_validation_token){
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				legal_users_ser.getLegalDepartmentUsersByToken(legal_validation_token);
				Court product = court_ser.getDetailById(court_id);
				ResponseEntity<Object> court_response_by_id = ResponseHandler.generateResponse(true,HttpStatus.OK, product);
				return court_response_by_id;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}	
	}
	
	@PutMapping("/court/{court_id}")
	public ResponseEntity<Object> updateDetailById(@RequestBody  @Valid Court court, @PathVariable Integer court_id ,@RequestHeader("LegalAuthorization") String legal_validation_token)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				legal_users_ser.getLegalDepartmentUsersByToken(legal_validation_token);
				court_ser.updateDetailById(court);
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
	
	@DeleteMapping("/court/{court_id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer court_id ,@RequestHeader("LegalAuthorization") String legal_validation_token){
		if(RateLimitController.bucket.tryConsume(1)) {
			legal_users_ser.getLegalDepartmentUsersByToken(legal_validation_token);
			court_ser.deactivate(court_id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@DeleteMapping("/activateCourt/{court_id}")
	public ResponseEntity<Object> activate(@PathVariable Integer court_id ,@RequestHeader("LegalAuthorization") String legal_validation_token){
		if(RateLimitController.bucket.tryConsume(1)) {
			legal_users_ser.getLegalDepartmentUsersByToken(legal_validation_token);
			court_ser.activate(court_id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

}
