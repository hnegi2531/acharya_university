package com.au.controller;

import java.io.IOException;
import java.util.List;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.BatchAssignmentRequestDto;
import com.au.dto.JwtDetails;
import com.au.response.ResponseHandler;
import com.au.service.BatchProgramAssignmentService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;



@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class BatchProgramAssignmentController {

	
Logger log = LoggerFactory.getLogger(BatchProgramAssignmentController.class);
	
	@Autowired
	private BatchProgramAssignmentService batchProgramAssignmentService;
	
	@Autowired
	private JwtTokenService jwt_service;

	
	@GetMapping("/assignedProgramSpecilizationByBatchAssignmentId/{batch_assignment_id}")
	public ResponseEntity<Object> assignedProgramSpecilizationByBatchAssignmentId(@PathVariable Integer batch_assignment_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Integer> assignedSpecilizationId = batchProgramAssignmentService.assignedProgramSpecilizationByBatchAssignmentId(batch_assignment_id);
			ResponseEntity<Object> assignedSpecilizationIdResponse = ResponseHandler.generateResponse(true, HttpStatus.OK, assignedSpecilizationId);
			return assignedSpecilizationIdResponse;
			
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/assignedProgramAssignmentByBatchAssignmentId/{batch_assignment_id}")
	public ResponseEntity<Object> assignedProgramAssignmentByBatchAssignmentId(@PathVariable Integer batch_assignment_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Integer> assignedSpecilizationId = batchProgramAssignmentService.assignedProgramAssignmentByBatchAssignmentId(batch_assignment_id);
			ResponseEntity<Object> assignedSpecilizationIdResponse = ResponseHandler.generateResponse(true, HttpStatus.OK, assignedSpecilizationId);
			return assignedSpecilizationIdResponse;
			
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/assignedProgramByBatchAssignmentId/{batch_assignment_id}")
	public ResponseEntity<Object> assignedProgramByBatchAssignmentId(@PathVariable Integer batch_assignment_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Integer> assignedSpecilizationId = batchProgramAssignmentService.assignedProgramByBatchAssignmentId(batch_assignment_id);
			ResponseEntity<Object> assignedSpecilizationIdResponse = ResponseHandler.generateResponse(true, HttpStatus.OK, assignedSpecilizationId);
			return assignedSpecilizationIdResponse;
			
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	

}
