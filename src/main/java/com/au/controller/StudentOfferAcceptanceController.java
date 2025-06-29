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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.ApplicationDto;
import com.au.dto.JwtDetails;
import com.au.dto.StudentOfferAcceptanceRequest;
import com.au.model.StudentOfferAcceptance;
import com.au.model.Student_Details;
import com.au.response.ResponseHandler;
import com.au.service.StudentDetailsService;
import com.au.service.StudentOfferAcceptanceService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class StudentOfferAcceptanceController {
	
	
	Logger log = LoggerFactory.getLogger(StudentOfferAcceptanceController.class);
	
	
	@Autowired
	private StudentOfferAcceptanceService studentOfferAcceptanceService;
	
	
	@PostMapping("/studentOfferAcceptance")
	public ResponseEntity<Object> saveStudentOfferAcceptance(@RequestBody @Valid StudentOfferAcceptanceRequest soar)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			StudentOfferAcceptance soa=studentOfferAcceptanceService.saveStudentOfferAcceptance(soar); 
			ResponseEntity<Object> studentOfferAcceptanceResponse = ResponseHandler.generateResponse(true, HttpStatus.CREATED, soa);
			return studentOfferAcceptanceResponse;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/activeStudentOfferAcceptance")
	public ResponseEntity<Object> activeStudentOfferAcceptanceDetails() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<StudentOfferAcceptance> soa=studentOfferAcceptanceService.activeStudentOfferAcceptanceDetails(); 
			ResponseEntity<Object> studentOfferAcceptanceDetailsResponse = ResponseHandler.generateResponse(true, HttpStatus.CREATED, soa);
			return studentOfferAcceptanceDetailsResponse ;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	

}
