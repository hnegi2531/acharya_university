package com.au.controller;



import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.au.response.ResponseHandler;
import com.au.service.PaidBoardDueService;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
public class PaidBoardDueController {

	Logger log = LoggerFactory.getLogger(PaidBoardDueController.class);
	
	@Autowired
	private PaidBoardDueService paidBoardDueService;
	
	
	@GetMapping("/paidBoardReportBasedOnBoard")
	public ResponseEntity<Object> paidBoardReportBasedOnFeeAdmissionCategory() {
		if(RateLimitController.bucket.tryConsume(1)) {
			return paidBoardDueService.paidBoardReportBasedOnFeeAdmissionCategory();
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}		
	}
	
	@GetMapping("/paidBoardReportBasedOnSchoolByBoard/{boardUniqueId}")
	public ResponseEntity<Object> paidBoardReportBasedOnSchoolByBoard(@PathVariable Integer boardUniqueId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			return paidBoardDueService.paidBoardReportBasedOnSchoolByBoard(boardUniqueId);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}		
	}
	
	
	@GetMapping("/paidBoardReportBasedOnAcademicYearByBoardAndSchool/{boardUniqueId}/{schoolId}")
	public ResponseEntity<Object> paidBoardReportBasedOnAcademicYearByBoardAndSchool(@PathVariable Integer boardUniqueId,@PathVariable Integer schoolId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			return paidBoardDueService.paidBoardReportBasedOnAcademicYearByBoardAndSchool(boardUniqueId,schoolId);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}		
	}
	
	@GetMapping("/studentDetailsByBoardSchoolAcademicYear/{boardUniqueId}/{schoolId}/{academicYearId}")
	public ResponseEntity<Object> studentDetailsByBoardSchoolAcademicYear(@PathVariable Integer boardUniqueId,
			@PathVariable Integer schoolId, @PathVariable Integer academicYearId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>>  PaidBoardDuereport= paidBoardDueService.studentDetailsByBoardSchoolAcademicYear(boardUniqueId,schoolId,academicYearId);
			 return ResponseHandler.generateResponse(true, HttpStatus.OK, PaidBoardDuereport);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}		
	}
	
	@GetMapping("/feeTemplateDetailsByAcademicYearAndYearSem/{acYearId}/{yearAndSem}")
	public ResponseEntity<Object> feeTemplateDetailsByAcademicYearAndYearSem(@PathVariable Integer acYearId,
			@PathVariable Integer yearAndSem) {
		if(RateLimitController.bucket.tryConsume(1)) {
			return paidBoardDueService.feeTemplateDetailsByAcademicYearAndYearSem(acYearId,yearAndSem);
			
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}		
	}
	
}
