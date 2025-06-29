package com.au.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.HostelRoomAssignmentDto;
import com.au.response.ResponseHandler;
import com.au.service.HostelRoomAssignmentService;

@RestController
@RequestMapping("/api/${secretkey6}")
@CrossOrigin
public class HostelRoomAssignmentController {
	
	Logger log=LoggerFactory.getLogger(HostelRoomAssignmentController.class);
	
	@Autowired
	private HostelRoomAssignmentService hostelRoomAssignmentService;
	
	@PostMapping("/hostelRoomAssignment")
	public ResponseEntity<Object> hostelRoomAssignment(
			@RequestBody @Valid HostelRoomAssignmentDto hostelRoomAssignmentDto,
			@RequestHeader("Authorization") String jwtToken) {

		if (RateLimitController.bucket.tryConsume(1)) {

			return hostelRoomAssignmentService.hostelRoomAssignment(hostelRoomAssignmentDto, jwtToken);

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}

}
