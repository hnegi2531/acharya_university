package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
import com.au.dto.ScholarshipApprovalStatusStudentIdUpdateDto;
import com.au.dto.ScholarshipCancelDto;
import com.au.dto.ScholarshipDto;

import com.au.model.ScholarshipApprovalStatus;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.ScholarshipApprovalStatusService;
import com.au.service.ScholarshipService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class ScholarshipApprovalStatusController {

	Logger log = LoggerFactory.getLogger(ScholarshipApprovalStatusController.class);
	
	@Autowired
	private ScholarshipApprovalStatusService s_service;

	@Autowired
	private ScholarshipService s_s;
	
	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/scholarshipapprovalstatus")
	public ResponseEntity<Object> saveScholarshipapprovalstatus(@RequestBody @Valid ScholarshipApprovalStatus l,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			l.setCreated_by(jwtDetails.getUserId());
			l.setCreated_username(jwtDetails.getUserName());
			ScholarshipApprovalStatus sch_approv = s_service.save_ScholarshipApprovalStatus(l);
			ResponseEntity<Object> sch_approv_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, sch_approv);
			return sch_approv_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/scholarshipapprovalstatus")
	public ResponseEntity<Object> listAll(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> sch_approv_sorted = s_service.listAll1(pageable, keyword);//,column,value);
						return sch_approv_sorted;
				}else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> sch_approv_pageable = s_service.listAll2(pageable1);
						return sch_approv_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		 //return s_service.listAll();
	}

	
	@GetMapping("/scholarshipapprovalstatus/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
	    	
						ScholarshipApprovalStatus product = s_service.get(id);
						ResponseEntity<Object> program_type_response_by_id= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
						return program_type_response_by_id;
	        
				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@PutMapping("/scholarshipapprovalstatus/{id}")
	public ResponseEntity<Object> update(@RequestBody ScholarshipApprovalStatus l, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    	try {
	    			ScholarshipApprovalStatus existProduct = s_service.get(id);
	    			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    			l.setModified_by(jwtDetails.getUserId());
	    			l.setModified_username(jwtDetails.getUserName());
	    			s_service.save_ScholarshipApprovalStatus(l);
	    			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
	    			return response;
	    	} catch (NoSuchElementException e) {
	    			ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
	    			return response1;
	    	}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	    	
	}
	
	
	@DeleteMapping("/scholarshipapprovalstatus/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				s_service.delete(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@DeleteMapping("/deactivateScholarshipapprovalstatus/{id}")
	public ResponseEntity<Object> deactivatescholarshipapprovalstatus(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				s_service.deactivatescholarshipapprovalstatus(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@DeleteMapping("/activateScholarshipapprovalstatus/{id}")
	public ResponseEntity<Object> activateScholarshipapprovalstatus(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				s_service.activateScholarshipapprovalstatus(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/updateScholarshipStatus/{scholarshipid}")
	public ResponseEntity<Object> update1(@RequestBody @Valid ScholarshipDto sdto,
			@PathVariable Integer scholarshipid,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	//    		ScholarshipApprovalStatus existProduct = s_service.get(id);
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				sdto.getSas().setModified_by(jwtDetails.getUserId());
				sdto.getSas().setModified_username(jwtDetails.getUserName());
				sdto.getSas().setApproved_by(jwtDetails.getUserId());
				ScholarshipApprovalStatus  update_sch_approv = s_service.update(sdto);
				ResponseEntity<Object> update_sch_approv_by_id= ResponseHandler.generateResponse(true, HttpStatus.OK, update_sch_approv);
				return update_sch_approv_by_id;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		

	}


	@PutMapping("/updateScholarshipStatus1/{scholarshipid}")
	public ResponseEntity<Object> update2(@RequestBody @Valid ScholarshipCancelDto sdto1,
			@PathVariable Integer scholarshipid) {
		if(RateLimitController.bucket.tryConsume(1)) {
				ScholarshipApprovalStatus sch_approve_satus_update = s_service.update2(sdto1);
				ResponseEntity<Object> sch_approve_satus_update_by_id= ResponseHandler.generateResponse(true, HttpStatus.OK, sch_approve_satus_update);
				return sch_approve_satus_update_by_id;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	
	@PutMapping("/editteScholarshipStatusStuid/{candidate_id}")
	public ResponseEntity<Object> update2(@RequestBody @Valid ScholarshipApprovalStatusStudentIdUpdateDto s,
			@PathVariable Integer candidate_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				ScholarshipApprovalStatus sch_approval_upadate_student_id = s_service.update1(s);
				ResponseEntity<Object> sch_approval_upadate_student_id_response= ResponseHandler.generateResponse(true, HttpStatus.OK, sch_approval_upadate_student_id);
				return sch_approval_upadate_student_id_response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/getDataForTestimonials/{student_id}")
	public ResponseEntity<Object> getDataForTestimonials(@PathVariable Integer student_id)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				HashMap<String,Object> dataForTestimonials = s_service.getDataForTestimonials(student_id);
				ResponseEntity<Object> dataForTestimonials_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, dataForTestimonials);
				return dataForTestimonials_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/getIsVerifiedDataForIndex")
	public ResponseEntity<Object> getIsVerifiedDataForIndex()
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String,Object>> is_verified_data = s_service.getIsVerifiedDataForIndex();
				ResponseEntity<Object> is_verified_data_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, is_verified_data);
				return is_verified_data_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/getIsApprovedDataForIndex/{ac_year_id}")
	public ResponseEntity<Object> getIsApprovedDataForIndex(@PathVariable Integer ac_year_id)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String,Object>> is_approved_data = s_service.getIsApprovedDataForIndex(ac_year_id);
				ResponseEntity<Object> is_approved_data_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, is_approved_data);
				return is_approved_data_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/getYearWiseDataByStudentId/{student_id}")
	public ResponseEntity<Object> getYearWiseDataByStudentId(@PathVariable Integer student_id)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<ScholarshipApprovalStatus> is_approved_data = s_service.getYearWiseDataByStudentId(student_id);
				ResponseEntity<Object> is_approved_data_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, is_approved_data);
				return is_approved_data_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
}


