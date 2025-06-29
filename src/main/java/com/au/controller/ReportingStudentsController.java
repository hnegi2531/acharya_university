package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.JwtDetails;
import com.au.model.ReportingStudents;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.ReportingStudentsService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class ReportingStudentsController {

	Logger log = org.slf4j.LoggerFactory.getLogger(ReportingStudentsController.class);
	
	@Autowired
	private ReportingStudentsService reportingStudentsService;
	
	@Autowired
	private JwtTokenService jwtTokenService;
	
	@PostMapping("/ReportingStudents")
	public ResponseEntity<Object> saveReportingStudents(@RequestBody @Valid ReportingStudents reportingStudents,@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
			reportingStudents.setCreated_by(jwtDetails.getUserId());
			reportingStudents.setCreated_username(jwtDetails.getUserName());
			ReportingStudents savedReportingStudent = reportingStudentsService.saveReportingStudents(reportingStudents); 
			return ResponseHandler.generateResponse(true, HttpStatus.CREATED, savedReportingStudent);
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/ReportingStudents")
	public ResponseEntity<Object> listAll(){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<ReportingStudents> reportingStudentList = reportingStudentsService.listAll();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, reportingStudentList);
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	
	@GetMapping("/ReportingStudents/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {

				ReportingStudents product = reportingStudentsService.get(id);
				return ResponseHandler.generateResponse(true,HttpStatus.OK, product);
			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
			}
		} else {
			return  ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
		}
	}

	@PutMapping("/ReportingStudents/{ids}")
	public ResponseEntity<Object> update(@RequestBody @Valid List<ReportingStudents> reportingStudents, @PathVariable List<Integer> ids,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
				reportingStudents.stream().forEach(rs -> {
					rs.setModified_by(jwtDetails.getUserId());
					rs.setModified_username(jwtDetails.getUserName());
				});
				reportingStudentsService.saveReportingStudents(reportingStudents);
				return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			}
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	
	@DeleteMapping("/ReportingStudents/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			reportingStudentsService.delete(id);
			return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getAllStudentDetailsWithNoStatus")
	public ResponseEntity<Object> fetchAllStudentDetailsWithNoStatus(@RequestParam(value ="school_id") Integer school_id, @RequestParam(value ="program_id") Integer program_id,
			@RequestParam(value ="ac_year_id") Integer ac_year_id,@RequestParam(value ="program_specialization_id") Integer program_specialization_id,@RequestParam(value ="current_sem",required = false) Integer current_sem,
			@RequestParam(value ="current_year",required = false) Integer current_year) {
		if (RateLimitController.bucket.tryConsume(1)) {
			if(current_sem != null) {
			List<Map<String, Object>> studentDetailsBySem = reportingStudentsService.fetchAllStudentDetailsToReportOnSem( school_id,program_id, ac_year_id,program_specialization_id, current_sem);
			return ResponseHandler.generateResponse(true, HttpStatus.OK,
					studentDetailsBySem);
			}else{
				List<Map<String, Object>> studentDetailsByYear = reportingStudentsService.fetchAllStudentDetailsToReportOnYear(school_id, program_id, ac_year_id,program_specialization_id, current_year);
				return ResponseHandler.generateResponse(true, HttpStatus.OK,
						studentDetailsByYear);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}
	
	@GetMapping("/getAllStudentDetailsWithNoStatusAndNotEligibleStatus")
	public ResponseEntity<Object> fetchAllStudentDetailssWithNoStatusAndNotEligibleStatus(@RequestParam(value ="school_id") Integer school_id, @RequestParam(value ="program_id") Integer program_id,
			@RequestParam(value ="current_sem",required = false) Integer current_sem,
			@RequestParam(value ="current_year",required = false) Integer current_year) {
		if (RateLimitController.bucket.tryConsume(1)) {
			if(current_sem != null) {
			List<Map<String, Object>> studentDetailsBySem = reportingStudentsService.fetchAllStudentsWithNoStatusAndNotEligibleOnSem( school_id,program_id, current_sem);
			return ResponseHandler.generateResponse(true, HttpStatus.OK,
					studentDetailsBySem);
			}else{
				List<Map<String, Object>> studentDetailsByYear = reportingStudentsService.fetchAllStudentsWithNoStatusAndNotEligibleOnYear(school_id, program_id, current_year);
				return ResponseHandler.generateResponse(true, HttpStatus.OK,
						studentDetailsByYear);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}
	
	@GetMapping("/getAllStudentDetailsWithEligibleStatus")
	public ResponseEntity<Object> fetchAllStudentDetailssWithEligibleStatus(@RequestParam(value ="school_id") Integer school_id, @RequestParam(value ="program_id") Integer program_id,
																			@RequestParam(value ="program_specialization_id") Integer program_specialization_id,@RequestParam(value ="current_sem",required = false) Integer current_sem,
			@RequestParam(value ="current_year",required = false) Integer current_year,@RequestParam(value ="eligible_reported_status",required = false) Integer eligible_reported_status) {
		if (RateLimitController.bucket.tryConsume(1)) {
			if(current_sem != null) {
			List<Map<String, Object>> stuDetailsBySem = reportingStudentsService.fetchAllStudentDetailssWithEligibleStatusOnSem( school_id,program_id,program_specialization_id, current_sem,eligible_reported_status);
			return ResponseHandler.generateResponse(true, HttpStatus.OK,
					stuDetailsBySem);
			}else{
				List<Map<String, Object>> studentDetailsByYear = reportingStudentsService.fetchAllStudentDetailssWithEligibleStatusOnYear(school_id, program_id,program_specialization_id, current_year,eligible_reported_status);
				return ResponseHandler.generateResponse(true, HttpStatus.OK,
						studentDetailsByYear);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}
	
	@PutMapping("/updationOfStudentReportingYearAndUsn/{auid}")
	public ResponseEntity<Object> updationOfStudentReportingYearAndUsn(@PathVariable String auid)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				reportingStudentsService.updationOfStudentReportingYearAndUsn(auid);
				return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponse(false, HttpStatus.NOT_FOUND,e.getMessage());
			}
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	
	}
	
	@GetMapping("/reportingStudentByStudentId/{studentId}")
	public ResponseEntity<Object> reportingStudentByStudentId(@PathVariable Integer studentId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {

				ReportingStudents reportingStudent = reportingStudentsService.reportingStudentByStudentId(studentId);
				return ResponseHandler.generateResponse(true,HttpStatus.OK, reportingStudent);
			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
		}
	}
	
	@GetMapping("/allNotReportedStudentDetails")
	public ResponseEntity<Object> allNotReportedStudentDetails(@RequestParam(value ="school_id") Integer school_id, @RequestParam(value ="program_id") Integer program_id,
			@RequestParam(value ="program_specialization_id") Integer program_specialization_id,
			@RequestParam(value ="current_sem",required = false) Integer current_sem,
			@RequestParam(value ="current_year",required = false) Integer current_year) {
		if (RateLimitController.bucket.tryConsume(1)) {
			if(current_sem != null) {
			List<Map<String, Object>> stuDetailsBySem = reportingStudentsService.allNotReportedStudentDetailsBySem( school_id,program_id,program_specialization_id, current_sem);
			return ResponseHandler.generateResponse(true, HttpStatus.OK,
					stuDetailsBySem);
			}else{
				List<Map<String, Object>> studentDetailsByYear = reportingStudentsService.allNotReportedStudentDetailsByYear(school_id, program_id,program_specialization_id, current_year);
				return ResponseHandler.generateResponse(true, HttpStatus.OK,
						studentDetailsByYear);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}
	
}
