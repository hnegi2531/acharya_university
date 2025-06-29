package com.au.controller;

import java.io.IOException;
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
import com.au.model.SyllabusSemester;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.SyllabusSemesterService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
//@RequestMapping("/api")
public class SyllabusSemesterController {

	Logger log = LoggerFactory.getLogger(SyllabusSemesterController.class);

	@Autowired
	private SyllabusSemesterService sy_service;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/SyllabusSemester")
	public ResponseEntity<Object> saveSyllabusSemester(@RequestBody @Valid SyllabusSemester syllabus,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			syllabus.setCreated_by(jwtDetails.getUserId());
			syllabus.setCreated_username(jwtDetails.getUserName());
			SyllabusSemester  syllabus_semster = sy_service.save_Syllabus_Semester(syllabus);
			ResponseEntity<Object> syllabus_semster_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, syllabus_semster);
			return syllabus_semster_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/SyllabusSemester")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<SyllabusSemester>  syllabus_semster_list = sy_service.listAll();
			ResponseEntity<Object> syllabus_semster_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, syllabus_semster_list);
			return syllabus_semster_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchAllSyllabusSemesterDetails")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> syllabus_semster_sorted = sy_service.listAll1(pageable, keyword);//,column,value);
				return syllabus_semster_sorted;
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> syllabus_semster_pageable = sy_service.listAll2(pageable1);
				return syllabus_semster_pageable;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}

	}

	@GetMapping("/SyllabusSemester/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {

				SyllabusSemester product = sy_service.get(id);
				ResponseEntity<Object> syllabus_semster_response_by_id = ResponseHandler.generateResponse(true,HttpStatus.OK, product);
				return syllabus_semster_response_by_id;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/SyllabusSemester/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid SyllabusSemester syllabus,
			@PathVariable Integer id, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				// SyllabusSemester existProduct = sy_service.get(id);
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				syllabus.setModified_by(jwtDetails.getUserId());
				syllabus.setModified_username(jwtDetails.getUserName());
				sy_service.save_Syllabus_Semester(syllabus);
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

	@DeleteMapping("/SyllabusSemester/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			sy_service.delete(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateSyllabusSemester/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			sy_service.delete1(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
}
