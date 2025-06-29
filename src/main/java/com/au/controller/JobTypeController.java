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
import com.au.model.JobType;
import com.au.response.ResponseHandler;
import com.au.service.JobTypeService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class JobTypeController {

	Logger log = LoggerFactory.getLogger(JobTypeController.class);
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@Autowired
	private JobTypeService j_service;
	
	@PostMapping("/JobType")
	public ResponseEntity<Object> saveMenu(@Valid @RequestBody JobType j,@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				j.setCreated_by(jwtDetails.getUserId());
				j.setCreated_username(jwtDetails.getUserName());
				JobType jt = j_service.saveJobType(j);
				ResponseEntity<Object> job_type_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, jt);
				return job_type_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@GetMapping("/JobType")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<JobType> list_job_type = j_service.listAll();
				ResponseEntity<Object> list_job_type_type_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_job_type);
				return list_job_type_type_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/fetchAllJobTypeDetail")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
							Pageable pageable = PageRequest.of(page, page_size,sorted);
							System.out.println("page, page_size, sorted, keyword");
							ResponseEntity<Object> job_type_sorted = j_service.listAll1(pageable, keyword);//,column,value);
							return job_type_sorted;
				}else {
							Pageable pageable1 = PageRequest.of(page, page_size,sorted);
							System.out.println("page, page_size, sorted");
							ResponseEntity<Object> job_type_pageable = j_service.listAll2(pageable1);
							return job_type_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
			//return j_service.listAll1();
	}

	@GetMapping("/JobType/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
						JobType product = j_service.get(id);
						ResponseEntity<Object> job_type_response_by_id= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
						return job_type_response_by_id;
				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@PutMapping("/JobType/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid JobType j, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
						JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
						j.setModified_by(jwtDetails.getUserId());
						j.setModified_username(jwtDetails.getUserName());
						j_service.saveJobType1(j);
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

	@DeleteMapping("/JobType/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				j_service.delete(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}

	@DeleteMapping("/activateJobType/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				j_service.delete1(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@GetMapping("/jobValidation")
	public ResponseEntity<Object> jobValidation(@RequestParam(value = "job_type",required = false) String job_type,
			@RequestParam(value = "job_short_name",required = false) String job_short_name){
		if (RateLimitController.bucket.tryConsume(1)) {
				HashMap<String,Boolean> valiadtion = j_service.jobValidation(job_type,job_short_name);
				ResponseEntity<Object> schools_response = ResponseHandler.generateResponse(true, HttpStatus.OK, valiadtion);
				return schools_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}		
	}


}
