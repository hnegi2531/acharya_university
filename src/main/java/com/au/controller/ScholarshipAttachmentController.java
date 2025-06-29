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
import com.au.dto.ScholarshipAttachmentStudentidUpdateDto;
import com.au.model.ScholarshipApprovalStatus;
import com.au.model.ScholarshipAttachment;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.ScholarshipAttachmentService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class ScholarshipAttachmentController {

	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ScholarshipAttachmentService s_service;
	
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@GetMapping("/ScholarshipAttachment")
	public ResponseEntity<Object> getAllDept(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> scholarship_attch_sorted = s_service.listAll1(pageable, keyword);//,column,value);
						return scholarship_attch_sorted;
				}else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> scholarship_attch_pageable = s_service.listAll2(pageable1);
						return scholarship_attch_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		//return s_service.listAll();
	}
	
	
	@PostMapping("/ScholarshipAttachment")
	public ResponseEntity<Object> saveCourseAssignment(@RequestBody @Valid ScholarshipAttachment s,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				s.setCreated_by(jwtDetails.getUserId());
				s.setCreated_username(jwtDetails.getUserName());
				ScholarshipAttachment scholarship_attch = s_service.saveScholarshipAttachment(s);
				ResponseEntity<Object> scholarship_attch_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, scholarship_attch);
				return scholarship_attch_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	
	@GetMapping("/ScholarshipAttachment/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
	    	
						ScholarshipAttachment product = s_service.get(id);
						ResponseEntity<Object> scholarship_attch_response_by_id= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
						return scholarship_attch_response_by_id;
				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@PutMapping("/ScholarshipAttachment/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid ScholarshipAttachment s, @PathVariable Integer id,@RequestHeader("Authorization") 
	String jwtToken) throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
						ScholarshipAttachment existProduct = s_service.get(id);
						JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
						s.setModified_by(jwtDetails.getUserId());
						s.setModified_username(jwtDetails.getUserName());
	    	
						s_service.saveScholarshipAttachment(s);
						//return new ResponseEntity<ScholarshipAttachment>(HttpStatus.OK);
						ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
						return response;
				} catch (NoSuchElementException e) {
						//return new ResponseEntity<ScholarshipAttachment>(HttpStatus.NOT_FOUND);
					ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
					return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	
	@DeleteMapping("/ScholarshipAttachment/{id}")
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
	
	@DeleteMapping("/deactivateScholarshipAttachment/{id}")
	public ResponseEntity<Object> deactivateScholarshipAttachment(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				s_service.deactivateScholarshipAttachment(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@DeleteMapping("/activateScholarshipAttachment/{id}")
	public ResponseEntity<Object> activateScholarshipAttachment(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				s_service.activateScholarshipAttachment(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}


	@PutMapping("/editteScholarshipAttachmentStuid/{candidate_id}")
	public ResponseEntity<Object> update2(@RequestBody @Valid ScholarshipAttachmentStudentidUpdateDto s,
			@PathVariable Integer candidate_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				ScholarshipAttachment scholarship_attch = s_service.update1(s);
				ResponseEntity<Object> updated_student_id_scholarship_attch_response = ResponseHandler.generateResponse(true, HttpStatus.OK, scholarship_attch);
				return updated_student_id_scholarship_attch_response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	//For getting path to download file
	@GetMapping("/ScholarshipAttachment/{candidate_id}")
	public ResponseEntity<Object> getDetailByCandidateId(@PathVariable Integer candidate_id){
			if(RateLimitController.bucket.tryConsume(1)) {
					ScholarshipAttachment scholarship_attch_by_cid = s_service.getDetailByCandidateId(candidate_id);
					ResponseEntity<Object> scholarship_attch_by_cid_response = ResponseHandler.generateResponse(true, HttpStatus.OK, scholarship_attch_by_cid);
					return scholarship_attch_by_cid_response;
			}else {	
					ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
					return rs;
			}	
		}
}
	

