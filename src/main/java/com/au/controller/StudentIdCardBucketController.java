package com.au.controller;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.StudentIdCardBucketDto;
import com.au.response.ResponseHandler;
import com.au.service.StudentIdCardBucketService;

@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class StudentIdCardBucketController {
	
Logger log = LoggerFactory.getLogger(StudentIdCardBucketController.class);
	
	@Autowired
	private StudentIdCardBucketService studentIdCardBucketService;
	
	
	@PostMapping("/studentIdCardBucket")
	public ResponseEntity<Object> studentIdCardBucket(@RequestBody @Valid List<StudentIdCardBucketDto> studentIdCardBucketDto,@RequestHeader("Authorization") String jwtToken){
		
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				studentIdCardBucketService.studentIdCardBucket(studentIdCardBucketDto,jwtToken);
				return	ResponseHandler.generateResponse(true, HttpStatus.CREATED,"Created Successfully");
			}catch(Exception e) {
				return ResponseHandler.generateResponse(false,HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
			}
		} else {
			return	ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
		}		
		
	}
	
	@GetMapping("/studentIdCardBucketDetails")
	public ResponseEntity<Object> studentIdCardBucketDetails() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>>  studentIdCardBucketDetails = studentIdCardBucketService.studentIdCardBucketDetails();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, studentIdCardBucketDetails);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@DeleteMapping("/removeStudentDetailsFromBucket/{StudentIdCardBucketIds}")
	public ResponseEntity<Object> removeStudentDetailsFromBucket(@PathVariable List<Integer> StudentIdCardBucketIds) {
		if(RateLimitController.bucket.tryConsume(1)) {
			studentIdCardBucketService.removeStudentDetailsFromBucket(StudentIdCardBucketIds);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "Student Removed Successfully From Bucket");
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

}
