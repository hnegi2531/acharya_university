package com.au.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.au.model.LegalDepartmentUsers;
import com.au.response.ResponseHandler;
import com.au.service.LegalDepartmentUsersService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class LegalDepartmentUsersController {
	
	Logger log = LoggerFactory.getLogger(LegalDepartmentUsersController.class);
	
	@Autowired
	private LegalDepartmentUsersService legal_users_ser;
	
	
	@PostMapping("/legalDepartmentUsers")
	public ResponseEntity<Object> saveLegalDepartmentUsers(@RequestBody @Valid LegalDepartmentUsers legal_users) throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			LegalDepartmentUsers legal_department_users = legal_users_ser.saveLegalDepartmentUsers(legal_users);
				ResponseEntity<Object> legal_department_users_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, legal_department_users);
				return legal_department_users_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/legalDepartmentUsers/{legal_department_user_name}")
	public ResponseEntity<Object> getLegalDepartmentUsers(@PathVariable String legal_department_user_name,@RequestHeader("LegalAuthorization") String legal_validation_token) {
		if(RateLimitController.bucket.tryConsume(1)) {
			legal_users_ser.getLegalDepartmentUsersByToken(legal_validation_token);
			LegalDepartmentUsers legal_department_users = legal_users_ser.getLegalDepartmentUsers(legal_department_user_name);
			ResponseEntity<Object> legal_department_users_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, legal_department_users);
			return legal_department_users_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@PutMapping("/updatelegalDepartmentUsers")
	public ResponseEntity<Object> updateLegalDepartmentUsers(@RequestParam("legal_department_user_name") String legal_department_user_name,@RequestParam("legal_department_user_password") String legal_department_user_password,
			@RequestHeader("LegalAuthorization") String legal_validation_token) throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			legal_users_ser.getLegalDepartmentUsersByToken(legal_validation_token);
			legal_users_ser.updateLegalDepartmentUsers(legal_department_user_name,legal_department_user_password);
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/regenrationOfToken")
	public ResponseEntity<Object> regenrationOfToken(@RequestParam("legal_department_user_name") String legal_department_user_name,@RequestParam("legal_department_user_password") String legal_department_user_password) {
		if(RateLimitController.bucket.tryConsume(1)) {
				LegalDepartmentUsers detail_with_new_token = legal_users_ser.regenrationOfToken(legal_department_user_name,legal_department_user_password);
				ResponseEntity<Object> detail_with_new_token_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, detail_with_new_token);
				return detail_with_new_token_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

}
