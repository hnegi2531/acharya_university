package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
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
import com.au.model.IntakePermitHistory;
import com.au.response.ResponseHandler;
import com.au.service.IntakePermitHistoryService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class IntakePermitHistoryController {

	Logger log = LoggerFactory.getLogger(IntakePermitHistoryController.class);
	
	@Autowired
	private IntakePermitHistoryService intake_permit_history_service;

	@Autowired
	private JwtTokenService jwt_service;
	
	
	@PostMapping("/intakePermitHistory")
	public ResponseEntity<Object> saveIntakepermitHistory(@RequestBody @Valid List<IntakePermitHistory> iph,@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		iph.stream().forEach(iph1 ->{
		iph1.setCreated_by(jwtDetails.getUserId());
		iph1.setCreated_username(jwtDetails.getUserName());
		});
		List<IntakePermitHistory> intakepermithistory = intake_permit_history_service.saveIntakePermitHistory(iph);
		ResponseEntity<Object> intake_permit_history_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, intakepermithistory);
		return intake_permit_history_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}
	}
	
	@GetMapping("/getIntakePermitHistoryDetails/{intake_history_ids}")
	public ResponseEntity<Object> getIntakePermitHistoryDetails(@PathVariable List<Integer> intake_history_ids) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> intake_permit_history = intake_permit_history_service.getIntakePermitHistoryDetails(intake_history_ids);
				ResponseEntity<Object> intake_permit_history_response = ResponseHandler.generateResponse(true, HttpStatus.OK,intake_permit_history);
				return intake_permit_history_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
}
