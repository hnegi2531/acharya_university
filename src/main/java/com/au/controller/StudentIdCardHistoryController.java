package com.au.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.au.dto.EmployeeIdCardHistoryDetailsDto;
import com.au.dto.JwtDetails;
import com.au.dto.StudentIdCardHistoryDto;
import com.au.model.Student_Details;
import com.au.response.ResponseHandler;
import com.au.service.StudentIdCardHistoryService;

@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class StudentIdCardHistoryController {
	
	Logger log = LoggerFactory.getLogger(StudentIdCardHistoryController.class);
	
	@Autowired
	private StudentIdCardHistoryService studentIdCardHistoryService;
	
	
	@PostMapping("/studentIdCardCreationWithHistory")
	public ResponseEntity<Object> studentIdCardCreationWithHistory(@RequestBody @Valid List<StudentIdCardHistoryDto> studentIdCardHistoryDto,@RequestHeader("Authorization") String jwtToken){
		
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				studentIdCardHistoryService.studentIdCardCreationWithHistory(studentIdCardHistoryDto,jwtToken);
				return	ResponseHandler.generateResponse(true, HttpStatus.CREATED,"Created Successfully");
			}catch(Exception e) {
				return ResponseHandler.generateResponse(false,HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
			}
		} else {
			return	ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
		}		
		
	}
	
	@PostMapping("/studentDuplicateIdCardCreationWithHistory")
	public ResponseEntity<Object> studentDuplicateIdCardCreationWithHistory(@RequestBody @Valid List<StudentIdCardHistoryDto> studentIdCardHistoryDto,@RequestHeader("Authorization") String jwtToken){
		
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				studentIdCardHistoryService.studentDuplicateIdCardCreationWithHistory(studentIdCardHistoryDto,jwtToken);
				return	ResponseHandler.generateResponse(true, HttpStatus.CREATED,"Created Successfully");
			}catch(Exception e) {
				return ResponseHandler.generateResponse(false,HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
			}
		} else {
			return	ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
		}		
		
	}
	
	@GetMapping("/studentIdCardHistoryDetails")
	public ResponseEntity<Object> studentIdCardHistoryDetails() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>>  emp = studentIdCardHistoryService.studentIdCardHistoryDetails();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, emp);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	

}
