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
import com.au.model.IntakeAssignmentHistory;
import com.au.response.ResponseHandler;
import com.au.service.IntakeAssignmentHistoryService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class IntakeAssignmentHistoryController {

	Logger log = LoggerFactory.getLogger(IntakeAssignmentHistoryController.class);
	
	@Autowired
	private IntakeAssignmentHistoryService intake_assignment_history_service;

	@Autowired
	private JwtTokenService jwt_service;
	
	
	@PostMapping("/intakeAssignmentHistory")
	public ResponseEntity<Object> saveIntakeAssignmentHistory(@RequestBody @Valid List<IntakeAssignmentHistory> iah,@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		iah.stream().forEach(iah1 ->{
		iah1.setCreated_by(jwtDetails.getUserId());
		iah1.setCreated_username(jwtDetails.getUserName());
		});
		List<IntakeAssignmentHistory> intakeassignmenthistory = intake_assignment_history_service.saveIntakeAssignmentHistory(iah);
		ResponseEntity<Object> intake_assignment_history_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, intakeassignmenthistory);
		return intake_assignment_history_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}
	}
	
	@GetMapping("/intakeAssignmentHistoryDetails/{intake_id}")
	public ResponseEntity<Object> intakeAssignmentHistoryDetails(@PathVariable Integer intake_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<HashMap<String,Object>> intake_assign_history = intake_assignment_history_service.intakeAssignmentHistoryDetails(intake_id);
				ResponseEntity<Object> intake_assign_history_response = ResponseHandler.generateResponse(true,HttpStatus.OK, intake_assign_history);
				return intake_assign_history_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}		
	}
	
}
