package com.au.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.response.ResponseHandler;
import com.au.service.CourtCasesHistoryService;
import com.au.service.LegalDepartmentUsersService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class CourtCasesHistoryController {
	
	
	@Autowired
	private CourtCasesHistoryService court_cases_his_ser;
	
	@Autowired
	private LegalDepartmentUsersService legal_users_ser;
	
	
	@GetMapping("/courtCaseDetailsHistoryOnCaseNo")
	public ResponseEntity<Object> courtCaseDetailsOnCaseNo(@RequestParam(value="court_cases_id") Integer court_cases_id,@RequestHeader("LegalAuthorization") String legal_validation_token){
		if(RateLimitController.bucket.tryConsume(1)) {
			legal_users_ser.getLegalDepartmentUsersByToken(legal_validation_token);
			List<HashMap<String, Object>> court_cases_history_details = court_cases_his_ser.courtCaseDetailsOnCaseNo(court_cases_id);
			ResponseEntity<Object> court_cases_history_details_response = ResponseHandler.generateResponse(true, HttpStatus.OK, court_cases_history_details);
			return court_cases_history_details_response;
	}else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
		
	}
	
	@GetMapping("/courtCaseDetailsHistoryOnId/{court_cases_history_id}")
	public ResponseEntity<Object> courtCaseDetailsHistoryOnId(@PathVariable Integer court_cases_history_id,@RequestHeader("LegalAuthorization") String legal_validation_token){
		if(RateLimitController.bucket.tryConsume(1)) {
			legal_users_ser.getLegalDepartmentUsersByToken(legal_validation_token);
			List<HashMap<String, Object>> court_cases_history_details = court_cases_his_ser.courtCaseDetailsHistoryOnId(court_cases_history_id);
			ResponseEntity<Object> court_cases_history_details_response = ResponseHandler.generateResponse(true, HttpStatus.OK, court_cases_history_details);
			return court_cases_history_details_response;
	}else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
		
	}

}
