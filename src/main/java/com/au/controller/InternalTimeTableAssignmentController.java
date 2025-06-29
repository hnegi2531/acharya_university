
package com.au.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
import com.au.model.InternalTimeTableAssignment;

import com.au.response.ResponseHandler;
import com.au.service.InternalTimeTableAssignmentService;
import com.au.service.JwtTokenService;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import com.au.controller.RateLimitController;

@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class InternalTimeTableAssignmentController {

	Logger log = LoggerFactory.getLogger(InternalTimeTableAssignmentController.class);
	

	@Autowired
	private InternalTimeTableAssignmentService internalTimeTableAssignmentService;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	

//	@PostMapping("/internalTimeTableAssignment")
//	public ResponseEntity<Object> saveInternalTimeTableAssignment(@RequestBody @Valid InternalTimeTableAssignment itta,
//		@RequestHeader("Authorization") String jwtToken)throws Exception,JsonParseException,JsonMappingException,IOException{
//		if(RateLimitController.bucket.tryConsume(1)) {
//				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
//				itta.setCreated_by(jwtDetails.getUserId());
//				itta.setCreated_username(jwtDetails.getUserName());
//				InternalTimeTableAssignment ittas = internalTimeTableAssignmentService.saveInternalTimeTableAssignments(itta);
//				ResponseEntity<Object> itta_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, ittas);
//				return itta_response;
//		}else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}		
//	}
//	
//	
//	@GetMapping("/internalTimeTableAssignment")
//	public ResponseEntity<Object> listAll() {
//		if(RateLimitController.bucket.tryConsume(1)) {
//			List<InternalTimeTableAssignment> list_itta = internalTimeTableAssignmentService.listAll();
//			ResponseEntity<Object> list_itta_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_itta);
//			return list_itta_response;
//		}else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}
//	}
//	
//	
//	@GetMapping("/fetchAllInternalTimeTableAssignmentDetail")
//	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
//			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
//		if(RateLimitController.bucket.tryConsume(1)) {
//					Sort sorted = Sort.by(Direction.DESC, sort );
//					if(keyword != null) {	
//							Pageable pageable = PageRequest.of(page, page_size,sorted);
//							System.out.println("page, page_size, sorted, keyword");
//							ResponseEntity<Object> itta_sorted =  internalTimeTableAssignmentService.listAll1(pageable, keyword);
//							return itta_sorted;
//					}else {
//							Pageable pageable1 = PageRequest.of(page, page_size,sorted);
//							System.out.println("page, page_size, sorted");
//							ResponseEntity<Object> itta_pageable = internalTimeTableAssignmentService.listAll2(pageable1);
//							return itta_pageable;
//					}
//		}else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}			
//		
//	}
//
//	
//	@GetMapping("/internalTimeTableAssignment/{id}")
//	public ResponseEntity<Object> get(@PathVariable Integer id) {
//		if(RateLimitController.bucket.tryConsume(1)) {
//				try {
//	    	
//					InternalTimeTableAssignment ittas = internalTimeTableAssignmentService.get(id);
//						ResponseEntity<Object> ittas_by_id_response= ResponseHandler.generateResponse(true, HttpStatus.OK, ittas);
//						return ittas_by_id_response;
//	        
//				} catch (NoSuchElementException e) {
//						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
//						return response1;
//				}
//		}else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}		
//	}
//	
//	
//	
//	@PutMapping("/internalTimeTableAssignment/{id}")
//	public ResponseEntity<Object> update(@RequestBody InternalTimeTableAssignment ittaz, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
//			throws JsonParseException, JsonMappingException, IOException {
//		if(RateLimitController.bucket.tryConsume(1)) {
//				try {
//					//ProgramSpecilization existProduct = pr_service.get(id);
//					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
//					ittaz.setModified_by(jwtDetails.getUserId());
//					ittaz.setModified_username(jwtDetails.getUserName());
//					internalTimeTableAssignmentService.saveInternalTimeTableAssignments1(ittaz);
//
//					ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
//					return response;
//				} catch (NoSuchElementException e) {
//					//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//					ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
//					return response1;
//				}
//		}else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}	
//	}
//	
//	
//	@DeleteMapping("/internalTimeTableAssignment/{id}")
//	public ResponseEntity<Object> delete(@PathVariable Integer id) {
//		if(RateLimitController.bucket.tryConsume(1)) {
//			internalTimeTableAssignmentService.delete(id);
//				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
//				return response;
//		}else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}		
//	}
//	
//	@DeleteMapping("/activateInternalTimeTableAssignment/{id}")
//	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
//		if(RateLimitController.bucket.tryConsume(1)) {
//			internalTimeTableAssignmentService.delete1(id);
//				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
//				return response;
//		}else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}
//	}
//	
//
//	@GetMapping("/ittaEmpBasedOnTimeAndDate/{time_slots_id}/{selected_date}/{course_id}")
//	public ResponseEntity<Object> listIttaEmpIdBasedOnTimeAndDate1(@PathVariable Integer time_slots_id,@PathVariable String selected_date,@PathVariable Integer course_id) throws ParseException {
//		if(RateLimitController.bucket.tryConsume(1)) {
//			DateFormat f= new SimpleDateFormat("yyyy-MM-dd");
//			Date fromDate=f.parse(selected_date);
//			List<Map<String, Object>> itta = internalTimeTableAssignmentService.listAllIttaEmpBasedOnTimeAndDate1(time_slots_id,selected_date,course_id);
//			ResponseEntity<Object> itta_response= ResponseHandler.generateResponse(true, HttpStatus.OK, itta);
//			return itta_response;
//		} else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}
//	}
//	
//	@GetMapping("/ittaRoomidBasedOnTimeAndDate/{program_specialization_id}/{course_id}/{date}")
//	public ResponseEntity<Object> listIttaRoomIdBasedOnTimeAndDate(@PathVariable Integer program_specialization_id,
//			@PathVariable Integer course_id,@PathVariable String date) throws ParseException {
//		if(RateLimitController.bucket.tryConsume(1)) {
//			DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
//			Date selected_date=df.parse(date);
//			List<Map<String, Object>> itta = internalTimeTableAssignmentService.listAllIttaRoomBasedOnTimeAndDate1(program_specialization_id,course_id,selected_date);
//			ResponseEntity<Object> itta_response= ResponseHandler.generateResponse(true, HttpStatus.OK, itta);
//			return itta_response;
//		} else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}
//	}
//	
//	@GetMapping("/ittaCourseBasedOnDate/{internal_id}/{date_of_exam}")
//	public ResponseEntity<Object> listIttaCourseBasedOnDate(@PathVariable Integer internal_id,@PathVariable String date_of_exam) throws ParseException {
//		if(RateLimitController.bucket.tryConsume(1)) {
//			DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
//			Date fromDate=df.parse(date_of_exam);
//			List<HashMap<String, Object>> itta = internalTimeTableAssignmentService.listIttaCourseBasedOnDate1(internal_id,fromDate);
//			ResponseEntity<Object> itta_response= ResponseHandler.generateResponse(true, HttpStatus.OK, itta);
//			return itta_response;
//		} else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}
//	}
//	
//	
//	@GetMapping("/IttaStudentDetails/{internal_id}/{course_id}/{school_id}/{program_specialization_id}/{ac_year_id}/{year_sem}")
//	public ResponseEntity<Object> listOfStudentDetails(@PathVariable Integer internal_id,@PathVariable Integer course_id,@PathVariable Integer school_id,
//			@PathVariable Integer program_specialization_id,@PathVariable Integer ac_year_id,@PathVariable Integer year_sem) {
//		if(RateLimitController.bucket.tryConsume(1)) {
//			List<HashMap<String, Object>> itts1 = internalTimeTableAssignmentService.listOfStudentDetails1(internal_id,course_id,school_id,program_specialization_id,ac_year_id,year_sem);
//			ResponseEntity<Object> internalTimeTable_response= ResponseHandler.generateResponse(true, HttpStatus.OK, itts1);
//			return internalTimeTable_response;
//		} else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}
//	}
//	
//	@GetMapping("/internalTimeTableAssignmentDetailsByUserId/{userId}")
//	public ResponseEntity<Object> internalTimeTableAssignmentDetailsByUserId(@PathVariable Integer userId) {
//		if(RateLimitController.bucket.tryConsume(1)) {
//			List<Map<String, Object>> listItta = internalTimeTableAssignmentService.internalTimeTableAssignmentDetailsByUserId(userId);
//			return ResponseHandler.generateResponse(true, HttpStatus.OK, listItta);
//		}else {
//
//			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//		}
//	}
//	
	
}
