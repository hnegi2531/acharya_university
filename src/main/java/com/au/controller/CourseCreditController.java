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
import com.au.dto.ProgramSpecilizationDto;
import com.au.model.CourseCredit;
import com.au.response.ResponseHandler;
import com.au.service.CourseCreditService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
//@RequestMapping("/api")
public class CourseCreditController {

	Logger log = LoggerFactory.getLogger(CourseCreditController.class);

	@Autowired
	private CourseCreditService ccs_service;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/CourseCredit")
	public ResponseEntity<Object> saveCourseCredit(@RequestBody @Valid ProgramSpecilizationDto psd,
			@RequestHeader("Authorization") String jwtToken)throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
					List<CourseCredit> course_credit = ccs_service.saveCourseCredits(psd,jwtToken);
					ResponseEntity<Object> course_credit_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, course_credit);
					return course_credit_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}			
	}

	@GetMapping("/CourseCredit")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<CourseCredit> list_course_credit = ccs_service.listAll();
				ResponseEntity<Object> list_course_credit_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_course_credit);
				return list_course_credit_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
//	  @GetMapping("/fetchAllCourseCreditDetails") 
//	  public List<HashMap<String,Object>> listAll1(){
//		  return ccs_service.listAll1(); 
//	}
	
	  @GetMapping("/fetchAllCourseCreditDetails") 
	  public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
				@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
		  if(RateLimitController.bucket.tryConsume(1)) {
			  		Sort sorted = Sort.by(Direction.DESC, sort );
			  		if(keyword != null) {	
			  					Pageable pageable = PageRequest.of(page, page_size,sorted);
			  					System.out.println("page, page_size, sorted, keyword");
			  					ResponseEntity<Object> course_credit_sorted = ccs_service.listAll1(pageable, keyword);//,column,value);
			  					return course_credit_sorted;
			  		}else {
			  					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			  					System.out.println("page, page_size, sorted");
			  					ResponseEntity<Object> course_credit_pageable = ccs_service.listAll2(pageable1);
			  					return course_credit_pageable;
			  		}
		  }else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			} 		
		  //return ccs_service.listAll1(); 
	}
	 

	@GetMapping("/CourseCredit/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
						CourseCredit product = ccs_service.get(id);
						ResponseEntity<Object> course_credit_response_by_id= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
						return course_credit_response_by_id;
				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@PutMapping("/CourseCredit/{id}")
	public ResponseEntity<Object> update(@RequestBody CourseCredit cc, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
						// ProgramSpecilization existProduct = ccs_service.get(id);
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					cc.setModified_by(jwtDetails.getUserId());
					cc.setModified_username(jwtDetails.getUserName());
					ccs_service.saveCourseCredit(cc);
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

	@DeleteMapping("/CourseCredit/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				ccs_service.delete(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}

	@DeleteMapping("/activateCourseCredit/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				ccs_service.delete1(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}

}
