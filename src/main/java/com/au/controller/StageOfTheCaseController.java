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

import com.au.model.LegalDepartmentUsers;
import com.au.model.StageOfTheCase;
import com.au.response.ResponseHandler;
import com.au.service.LegalDepartmentUsersService;
import com.au.service.StageOfTheCaseService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class StageOfTheCaseController {
	
	Logger log = LoggerFactory.getLogger(StageOfTheCaseController.class);
	
	@Autowired
	private StageOfTheCaseService stage_case_service;
	
	@Autowired
	private LegalDepartmentUsersService legal_users_ser;
	
	
	@PostMapping("/stageOfTheCase")
	public ResponseEntity<Object> saveStageOfTheCase(@RequestBody @Valid StageOfTheCase stage_case,@RequestHeader("LegalAuthorization") String legal_validation_token) 
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			LegalDepartmentUsers legal_department_user=legal_users_ser.getLegalDepartmentUsersByToken(legal_validation_token);
			stage_case.setCreated_by(legal_department_user.getLegal_department_user_id());
			stage_case.setCreated_username(legal_department_user.getLegal_department_user_name());
			StageOfTheCase stage_of_the_case = stage_case_service.saveStageOfTheCase(stage_case);
				ResponseEntity<Object> stage_of_the_case_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, stage_of_the_case);
				return stage_of_the_case_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/stageOfTheCase")
	public ResponseEntity<Object> listAll(@RequestHeader("LegalAuthorization") String legal_validation_token) {
		if(RateLimitController.bucket.tryConsume(1)) {
			legal_users_ser.getLegalDepartmentUsersByToken(legal_validation_token);
			List<StageOfTheCase> list_stage_of_the_case = stage_case_service.listAll();
			ResponseEntity<Object> list_stage_of_the_case_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_stage_of_the_case);
			return list_stage_of_the_case_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/fetchAllStageOfTheCaseDetail")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword,@RequestHeader("LegalAuthorization") String legal_validation_token) {
		if(RateLimitController.bucket.tryConsume(1)) {
				legal_users_ser.getLegalDepartmentUsersByToken(legal_validation_token);
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> stage_of_the_case_sorted = stage_case_service.listAll1(pageable, keyword);//,column,value);
					return stage_of_the_case_sorted;
				}else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> stage_of_the_case_pageable = stage_case_service.listAll2(pageable1);
					return stage_of_the_case_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/stageOfTheCase/{stage_of_the_case_id}")
	public ResponseEntity<Object> get(@PathVariable Integer stage_of_the_case_id,@RequestHeader("LegalAuthorization") String legal_validation_token) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				legal_users_ser.getLegalDepartmentUsersByToken(legal_validation_token);
				StageOfTheCase product = stage_case_service.get(stage_of_the_case_id);
				ResponseEntity<Object> program_type_response_by_id = ResponseHandler.generateResponse(true,HttpStatus.OK, product);
				return program_type_response_by_id;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}		
	}
	
	@PutMapping("/stageOfTheCase/{stage_of_the_case_id}")
	public ResponseEntity<Object> update(@RequestBody StageOfTheCase stage_case, @PathVariable Integer stage_of_the_case_id,@RequestHeader("LegalAuthorization") String legal_validation_token) throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
					legal_users_ser.getLegalDepartmentUsersByToken(legal_validation_token);
					stage_case_service.get(stage_of_the_case_id);
					stage_case_service.updatestageOfTheCase(stage_case);
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
	
	@DeleteMapping("/stageOfTheCase/{stage_of_the_case_id}")
	public ResponseEntity<Object> delete(@PathVariable Integer stage_of_the_case_id,@RequestHeader("LegalAuthorization") String legal_validation_token) {
		if(RateLimitController.bucket.tryConsume(1)) {
			legal_users_ser.getLegalDepartmentUsersByToken(legal_validation_token);
			stage_case_service.delete(stage_of_the_case_id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@DeleteMapping("/activateStageOfTheCase/{stage_of_the_case_id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer stage_of_the_case_id,@RequestHeader("LegalAuthorization") String legal_validation_token) {
		if(RateLimitController.bucket.tryConsume(1)) {
			legal_users_ser.getLegalDepartmentUsersByToken(legal_validation_token);
			stage_case_service.delete1(stage_of_the_case_id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

}
