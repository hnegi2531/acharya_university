package com.au.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.model.HostelDue;
import com.au.response.ResponseHandler;
import com.au.service.HostelDueService;


@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
public class HostelDueController {

	Logger log = LoggerFactory.getLogger(HostelDueController.class);

	@Autowired
	private HostelDueService hostelDueService;
	
	@GetMapping("/getAllHostelDue")
	public ResponseEntity<Object> getAllHostelDue() {
		if(RateLimitController.bucket.tryConsume(1)) {
			return hostelDueService.getAllHostelDue();
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);

		}
	}
	
	@GetMapping("/getHostelDueReportByAcademicYearGroupedByBlock")
	public ResponseEntity<Object> getHostelDueReportByAcademicYearGroupedByBlock(@RequestParam(value = "acYearId",required = false) Integer acYearId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			return hostelDueService.getHostelDueReportByAcademicYearGroupedByBlock(acYearId);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
		}
	}
	
	@GetMapping("/studentHostelDue/{studentId}")
	public ResponseEntity<Object> studentHostelDue(@PathVariable Integer studentId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			return hostelDueService.studentHostelDue(studentId);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);

		}
	}
}
