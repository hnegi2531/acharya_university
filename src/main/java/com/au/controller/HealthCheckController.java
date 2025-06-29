package com.au.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.au.response.ResponseHandler;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class HealthCheckController {
	
	Logger log = LoggerFactory.getLogger(HealthCheckController.class);
	
	@GetMapping("/health")
	public ResponseEntity<Object> health() {
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "Status is UP");
	}

}
