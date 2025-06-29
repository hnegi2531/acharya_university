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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.JwtDetails;
import com.au.model.CandidateCounsellorSwapping;
import com.au.response.ResponseHandler;
import com.au.service.CandidateCounsellorSwappingService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class CandidateCounsellorSwappingController {
	
	Logger log = LoggerFactory.getLogger(CandidateCounsellorSwappingController.class);
	
	@Autowired
	private CandidateCounsellorSwappingService ccs_service;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/saveCandidateCounsellorSwapping")
	public ResponseEntity<Object> saveCandidateCounsellorSwapping(@RequestBody @Valid CandidateCounsellorSwapping CandidateCounsellorSwapping,@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				CandidateCounsellorSwapping.setCreated_by(jwtDetails.getUserId());
				CandidateCounsellorSwapping.setCreated_username(jwtDetails.getUserName());
				CandidateCounsellorSwapping bod = ccs_service.saveCandidateCounsellorSwapping(CandidateCounsellorSwapping);
				ResponseEntity<Object> CandidateCounsellorSwapping_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, bod);
				return CandidateCounsellorSwapping_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@GetMapping("/CandidateCounsellorSwapping")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<CandidateCounsellorSwapping> list_CandidateCounsellorSwapping = ccs_service.listAll();
				ResponseEntity<Object> list_CandidateCounsellorSwapping_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_CandidateCounsellorSwapping);
				return list_CandidateCounsellorSwapping_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/fetchAllCandidateCounsellorSwappingDetail")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> CandidateCounsellorSwapping_sorted = ccs_service.listAll1(pageable, keyword);//,column,value);
						return CandidateCounsellorSwapping_sorted;
				}else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> CandidateCounsellorSwapping_pageable = ccs_service.listAll2(pageable1);
						return CandidateCounsellorSwapping_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		//return ccs_service.listAll1();
	}

	@GetMapping("/CandidateCounsellorSwapping/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {

						CandidateCounsellorSwapping product = ccs_service.get(id);
						log.debug("Request {}", id);
						ResponseEntity<Object> CandidateCounsellorSwapping_by_id_response= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
						return CandidateCounsellorSwapping_by_id_response;

				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/CandidateCounsellorSwappingByCandidateId/{candidateId}")
	public ResponseEntity<Object> listAll(@PathVariable Integer candidateId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> list_CandidateCounsellorSwapping = ccs_service.findAllByCandidateId(candidateId);
				ResponseEntity<Object> list_CandidateCounsellorSwapping_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_CandidateCounsellorSwapping);
				return list_CandidateCounsellorSwapping_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

}
