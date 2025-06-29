package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.au.dto.JwtDetails;
import com.au.model.Scholarship;
import com.au.model.ScholarshipVocherHeadWiseAmountDetails;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.ScholarshipVocherHeadWiseAmountDetailsService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class ScholarshipVocherHeadWiseAmountDetailsController {
	
	Logger log = LoggerFactory.getLogger(ScholarshipVocherHeadWiseAmountDetailsController.class);
	
	@Autowired
	private ScholarshipVocherHeadWiseAmountDetailsService svhwad_service;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/scholarshipHeadWiseAmountDetails")
	public ResponseEntity<Object> saveScholarshipVocherHeadWiseAmountDetails(@RequestBody @Valid List<ScholarshipVocherHeadWiseAmountDetails> svhwad,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				svhwad.stream().forEach(svhw ->{
					svhw.setCreated_by(jwtDetails.getUserId());
					svhw.setCreated_username(jwtDetails.getUserName());
				});
				List<ScholarshipVocherHeadWiseAmountDetails> scholarship_head_wise_amount_details = svhwad_service.saveScholarshipVocherHeadWiseAmountDetails(svhwad);
				ResponseEntity<Object> scholarship_head_wise_amount_details_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, scholarship_head_wise_amount_details);
				return scholarship_head_wise_amount_details_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/scholarshipHeadWiseAmountDetailsOnScholarshipId/{scholarship_id}")
	public ResponseEntity<Object> scholarshipHeadWiseAmountDetailsOnScholarshipId(@PathVariable Integer scholarship_id)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<HashMap<String,Object>> scholarship_head_wise_amount_details_list = svhwad_service.scholarshipHeadWiseAmountDetailsOnScholarshipId(scholarship_id);
				ResponseEntity<Object> scholarship_head_wise_amount_details_list_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, scholarship_head_wise_amount_details_list);
				return scholarship_head_wise_amount_details_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/scholarshipHeadWiseAmountDetailsOnStudentId/{student_id}")
	public ResponseEntity<Object> scholarshipHeadWiseAmountDetailsOnStudentId(@PathVariable Integer student_id)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<HashMap<String,Object>> scholarship_head_wise_amount_details_list = svhwad_service.scholarshipHeadWiseAmountDetailsOnStudentId(student_id);
				ResponseEntity<Object> scholarship_head_wise_amount_details_list_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, scholarship_head_wise_amount_details_list);
				return scholarship_head_wise_amount_details_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/scholarshipHeadWiseAmountDetailsOnScholarshipIdAndVocherHeadNewId/{scholarship_id}/{voucher_head_new_id}")
	public ResponseEntity<Object> scholarshipHeadWiseAmountDetailsOnVocherHeadNewId(@PathVariable Integer scholarship_id,@PathVariable Integer voucher_head_new_id)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<HashMap<String,Object>> scholarship_head_wise_amount_details_list = svhwad_service.scholarshipHeadWiseAmountDetailsOnVocherHeadNewId(scholarship_id,voucher_head_new_id);
				ResponseEntity<Object> scholarship_head_wise_amount_details_list_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, scholarship_head_wise_amount_details_list);
				return scholarship_head_wise_amount_details_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@DeleteMapping("/deactivateScholarshipHeadWiseAmountDetails/{id}")
	public ResponseEntity<Object> deactivateScholarshipHeadWiseAmountDetails(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			svhwad_service.deactivateScholarshipHeadWiseAmountDetails(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@DeleteMapping("/activateScholarshipHeadWiseAmountDetails/{id}")
	public ResponseEntity<Object> activateScholarshipHeadWiseAmountDetails(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			svhwad_service.activateScholarshipHeadWiseAmountDetails(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@PutMapping("/scholarshipHeadWiseAmountDetails/{ids}")
	public ResponseEntity<Object> update(@RequestBody List<ScholarshipVocherHeadWiseAmountDetails> p, @PathVariable List<Integer> ids,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
						JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					p.stream().forEach(p1 -> {
						
					
						p1.setModified_by(jwtDetails.getUserId());
						p1.setModified_username(jwtDetails.getUserName());
					});
						svhwad_service.saveScholarshipVocherHeadWiseAmountDetails1(p);
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

}
