package com.au.controller;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.JwtDetails;
import com.au.dto.TutionFeeWaiverDto;
import com.au.model.TutionFeeWaiverSubAmount;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.TutionFeeWaiverSubAmountService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class TutionFeeWaiverSubAmountController {
	
	@Autowired
	private TutionFeeWaiverSubAmountService tfwsas_service;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PutMapping("/updateTutionFeeWaiverSubAmount")
	public ResponseEntity<Object> updateTutionFeeWaiverSubAmount(@RequestBody @Valid TutionFeeWaiverDto tfw, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    	tfwsas_service.updateTutionFeeWaiverSubAmount(tfw,jwtDetails);
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

}
