package com.au.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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
import org.springframework.web.bind.annotation.RestController;
import com.au.dto.JwtDetails;
import com.au.model.CourseObjective;
import com.au.model.Language;
import com.au.service.LanguageService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import com.au.service.JwtTokenService;
import com.au.response.ResponseHandler;

@RestController
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class LanguageController {

Logger log = LoggerFactory.getLogger(LanguageController.class);
	
	@Autowired
	private LanguageService lang_service;

	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/saveLanguage")
	public ResponseEntity<Object> saveLanguage(@RequestBody @Valid List<Language> lang,
			@RequestHeader("Authorization") String jwtToken)
			  throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			lang.stream().forEach(f -> {

				f.setCreated_by(jwtDetails.getUserId());
				f.setCreated_username(jwtDetails.getUserName());
				});
	List<Language> lan = lang_service.saveLanguage(lang);
	ResponseEntity<Object> language_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED,lan);
			return language_response;
	} else {
	ResponseEntity<Object> rsh=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		   return rsh;
		}
	}
	
	
	@GetMapping("/getAllActiveLanguage")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Language> lang = lang_service.listAll();
		ResponseEntity<Object> lang_response= ResponseHandler.generateResponse(true, HttpStatus.OK, lang);
		return lang_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}	
		
	
	@GetMapping("/getLanguage/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	Language lang = lang_service.get(id);
	    	ResponseEntity<Object> lang_response= ResponseHandler.generateResponse(true, HttpStatus.OK, lang);
			return lang_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
		
	
	@PutMapping("/updateLanguage/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid List<Language> lang, @PathVariable List<Integer> id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    	lang.stream().forEach(f -> {

				f.setCreated_by(jwtDetails.getUserId());
				f.setCreated_username(jwtDetails.getUserName());
				});
	    	lang_service.updateLanguage(lang);
	    	ResponseEntity<Object> co_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return co_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	
	@GetMapping("/getLanguageBasedOnEmpId/{emp_id}")
	public ResponseEntity<Object> getLanguageBasedOnEmpId(@PathVariable Integer emp_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Language> lang = lang_service.getLanguageBasedOnEmpId(emp_id);
		ResponseEntity<Object> lang_response= ResponseHandler.generateResponse(true, HttpStatus.OK, lang);
		return lang_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}	
}
