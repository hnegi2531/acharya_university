package com.au.controller;

import java.io.IOException;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.JwtDetails;
import com.au.dto.OfferHistoryResponse;
import com.au.model.OfferHistory;
import com.au.model.OfferHistoryRequest;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.OfferHistoryService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class OfferHistoryController {
	
	Logger log = LoggerFactory.getLogger(OfferHistoryController.class);

	@Autowired
	private OfferHistoryService offerHistoryService;

	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/offerHistory")
	public ResponseEntity<Object> saveOfferHistory(@RequestBody @Valid OfferHistory offerHistoryRequest,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			offerHistoryRequest.setCreated_by(jwtDetails.getUserId());
			offerHistoryRequest.setCreatedUsername(jwtDetails.getUserName());
			OfferHistory offerHistoryResponse = offerHistoryService.saveOfferHistory(offerHistoryRequest);
			ResponseEntity<Object> offerHistory= ResponseHandler.generateResponse(true, HttpStatus.CREATED, offerHistoryResponse );
			return offerHistory;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
	
	@GetMapping("/offerHistoryByJobId/{job_id}")
	public ResponseEntity<Object> offerHistoryByJobId(@PathVariable Integer job_id){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<OfferHistory> offerHistoryDetails=offerHistoryService.offerHistoryByJobId(job_id);
			ResponseEntity<Object> offerHistoryDetailsResponse= ResponseHandler.generateResponse(true, HttpStatus.CREATED, offerHistoryDetails );
			return offerHistoryDetailsResponse;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/offerHistoryByEmployeeId/{employeeId}")
	public ResponseEntity<Object> offerHistoryByEmployeeId(@PathVariable Integer employeeId){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<OfferHistory> offerHistoryDetails=offerHistoryService.offerHistoryByEmployeeId(employeeId);
			ResponseEntity<Object> offerHistoryDetailsResponse= ResponseHandler.generateResponse(true, HttpStatus.CREATED, offerHistoryDetails );
			return offerHistoryDetailsResponse;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

}
