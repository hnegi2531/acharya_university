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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.JwtDetails;
import com.au.model.ApplicantPaidDetails;
import com.au.response.ResponseHandler;
import com.au.service.ApplicantPaidDetailsService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class ApplicantPaidDetailsController {

	Logger log = LoggerFactory.getLogger(ApplicantPaidDetailsController.class);
	
	
	@Autowired
	private ApplicantPaidDetailsService applicantPaidDetailsService;

	@Autowired
	private JwtTokenService jwt_service;
	
	
	@PostMapping("/saveApplicantPaidDetails")
	public ResponseEntity<Object> saveApplicantPaidDetails(@RequestBody @Valid ApplicantPaidDetails tm,@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			tm.setCreated_by(jwtDetails.getUserId());
			tm.setCreated_username(jwtDetails.getUserName());

			ApplicantPaidDetails tms = applicantPaidDetailsService.saveApplicantPaidDetails(tm);
			ResponseEntity<Object> tm_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, tms);
		return tm_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getAllActiveApplicantPaidDetails")
	public ResponseEntity<Object> getAllActiveApplicantPaidDetails() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<ApplicantPaidDetails> tms = applicantPaidDetailsService.getAllActiveApplicantPaidDetails();
		ResponseEntity<Object> tm_response= ResponseHandler.generateResponse(true, HttpStatus.OK, tms);
		return tm_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	
	@GetMapping("/getApplicantPaidDetails/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	ApplicantPaidDetails dps = applicantPaidDetailsService.get(id);
	    	ResponseEntity<Object> tm_response= ResponseHandler.generateResponse(true, HttpStatus.OK, dps);
			return tm_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	
	@PutMapping("/updateApplicantPaidDetails/{id}")
	public ResponseEntity<Object> updateApplicantPaidDetails(@RequestBody @Valid ApplicantPaidDetails dp, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	//Department existProduct = deptService.get(id);
	    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    	dp.setModified_by(jwtDetails.getUserId());
	    	dp.setModified_username(jwtDetails.getUserName());
	    	applicantPaidDetailsService.updateApplicantPaidDetails(dp);
	    	ResponseEntity<Object> tm_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return tm_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	@GetMapping("/getApplicantPaidDetailsBasedOnId")
	public ResponseEntity<Object> getApplicantPaidDetailsBasedOnId(@RequestParam(value="lead_id") String lead_id,@RequestParam(value="opportunity_id") String opportunity_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> daily_planner = applicantPaidDetailsService.getApplicantPaidDetailsBasedOnId(lead_id,opportunity_id);
			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK,daily_planner);
			return student_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
}
