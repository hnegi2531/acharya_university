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

import com.au.model.CourtCases;
import com.au.model.LegalDepartmentUsers;
import com.au.response.ResponseHandler;
import com.au.service.CourtCasesService;
import com.au.service.LegalDepartmentUsersService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class CourtCasesController {
	
	Logger log = LoggerFactory.getLogger(CourtCasesController.class);
	
	@Autowired
	private CourtCasesService court_cases_ser;
	
	@Autowired
	private LegalDepartmentUsersService legal_users_ser;
	
	@PostMapping("/courtCases")
	public ResponseEntity<Object> saveCourtCases(@RequestBody @Valid CourtCases court_cases,@RequestHeader("LegalAuthorization") String legal_validation_token) 
			throws Exception, JsonParseException, JsonMappingException, IOException{
		if(RateLimitController.bucket.tryConsume(1)) {
			LegalDepartmentUsers legal_department_user=legal_users_ser.getLegalDepartmentUsersByToken(legal_validation_token);
			court_cases.setCreated_by(legal_department_user.getLegal_department_user_id());
			court_cases.setCreated_username(legal_department_user.getLegal_department_user_name());
			CourtCases court_cases_data = court_cases_ser.saveCourtCases(court_cases);
			ResponseEntity<Object> court_cases_data_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, court_cases_data);
			return court_cases_data_response;
			
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}

	}
	
	@GetMapping("/courtCases")
	public ResponseEntity<Object> getActiveDetails(@RequestHeader("LegalAuthorization") String legal_validation_token){
		if(RateLimitController.bucket.tryConsume(1)) {
			legal_users_ser.getLegalDepartmentUsersByToken(legal_validation_token);
			List<CourtCases> court_cases_list = court_cases_ser.getActiveDetails();
			ResponseEntity<Object> court_cases_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, court_cases_list);
			return court_cases_list_response;

		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
	@GetMapping("/fetchAllCourtCasesDetails")
	public ResponseEntity<Object> listAll(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword,@RequestHeader("LegalAuthorization") String legal_validation_token){
		if(RateLimitController.bucket.tryConsume(1)) {
			legal_users_ser.getLegalDepartmentUsersByToken(legal_validation_token);
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				ResponseEntity<Object> court_cases_sorted = court_cases_ser.listAll1(pageable, keyword);//,column,value);
				return court_cases_sorted;
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				ResponseEntity<Object> court_cases_pageable = court_cases_ser.listAll2(pageable1);
				return court_cases_pageable;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
		
	}
	
	@GetMapping("/courtCases/{court_cases_id}")
	public ResponseEntity<Object> getDetailById(@PathVariable Integer court_cases_id,@RequestHeader("LegalAuthorization") String legal_validation_token){
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				legal_users_ser.getLegalDepartmentUsersByToken(legal_validation_token);
				CourtCases product = court_cases_ser.getDetailById(court_cases_id);
				ResponseEntity<Object> court_cases_response_by_id = ResponseHandler.generateResponse(true,HttpStatus.OK, product);
				return court_cases_response_by_id;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}	
	}
	
	@PutMapping("/courtCasesUpdateOnly/{court_cases_id}")
	public ResponseEntity<Object> updateDetailCourtCasesUpdateOnlyById(@RequestBody  @Valid CourtCases court_cases, @PathVariable Integer court_cases_id,@RequestHeader("LegalAuthorization") String legal_validation_token)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				LegalDepartmentUsers legal_department_user = legal_users_ser.getLegalDepartmentUsersByToken(legal_validation_token);
				court_cases.setModified_by(legal_department_user.getLegal_department_user_id());
				court_cases.setModified_username(legal_department_user.getLegal_department_user_name());
				court_cases_ser.updateDetailCourtCasesUpdateOnlyById(court_cases);
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
	
	@PutMapping("/courtCases/{court_cases_id}")
	public ResponseEntity<Object> updateDetailById(@RequestBody  @Valid CourtCases court_cases, @PathVariable Integer court_cases_id,@RequestHeader("LegalAuthorization") String legal_validation_token)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				LegalDepartmentUsers legal_department_user = legal_users_ser.getLegalDepartmentUsersByToken(legal_validation_token);
				court_cases.setModified_by(legal_department_user.getLegal_department_user_id());
				court_cases.setModified_username(legal_department_user.getLegal_department_user_name());
				court_cases_ser.updateDetailById(court_cases);
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
	
	@DeleteMapping("/courtCases/{court_cases_id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer court_cases_id,@RequestHeader("LegalAuthorization") String legal_validation_token){
		if(RateLimitController.bucket.tryConsume(1)) {
			legal_users_ser.getLegalDepartmentUsersByToken(legal_validation_token);
			court_cases_ser.deactivate(court_cases_id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@DeleteMapping("/activateCourtCases/{court_cases_id}")
	public ResponseEntity<Object> activate(@PathVariable Integer court_cases_id,@RequestHeader("LegalAuthorization") String legal_validation_token){
		if(RateLimitController.bucket.tryConsume(1)) {
			legal_users_ser.getLegalDepartmentUsersByToken(legal_validation_token);
			court_cases_ser.activate(court_cases_id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}



}
