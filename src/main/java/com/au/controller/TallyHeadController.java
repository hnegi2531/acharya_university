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
import com.au.model.TallyHead;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.TallyHeadService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
//@RequestMapping("/api")
public class TallyHeadController {

	@Autowired
	private TallyHeadService tally_service;

	Logger log = LoggerFactory.getLogger(TallyHeadController.class);
	
	@Autowired
	private JwtTokenService jwt_service;

	
	@PostMapping("/TallyHead")
	public ResponseEntity<Object> saveTally(@RequestBody @Valid TallyHead tally,	@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				tally.setCreated_by(jwtDetails.getUserId());
				tally.setCreated_username(jwtDetails.getUserName());
				TallyHead TH = tally_service.save_TallyHead(tally);
				ResponseEntity<Object> tally_head_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, TH);
				return tally_head_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/TallyHead")
	public ResponseEntity<Object> listAll(){
		if(RateLimitController.bucket.tryConsume(1)) {
				List<TallyHead> list_tally_head = tally_service.listAll();
				ResponseEntity<Object> list_tally_head_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_tally_head);
				return list_tally_head_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	
	  @GetMapping("/FetchAllTallyHeadDetail")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
		  if(RateLimitController.bucket.tryConsume(1)) {
			  		Sort sorted = Sort.by(Direction.DESC, sort );
			  		if(keyword != null) {	
			  				Pageable pageable = PageRequest.of(page, page_size,sorted);
			  				System.out.println("page, page_size, sorted, keyword");
			  				ResponseEntity<Object> tally_head_sorted = tally_service.listAll1(pageable, keyword);//,column,value);
			  				return tally_head_sorted;
			  		}else {
			  				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			  				System.out.println("page, page_size, sorted");
			  				ResponseEntity<Object> tally_head_pageable = tally_service.listAll2(pageable1);
			  				return tally_head_pageable;
			  		}
		  }else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}	  		
		//return tally_service.listAll1();
	}
	 

	
	@GetMapping("/TallyHead/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
	    	
						TallyHead product = tally_service.get(id);
						ResponseEntity<Object> tally_head_response_by_id= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
						return tally_head_response_by_id;
	        
				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@PutMapping("/TallyHead/{id}")
	public ResponseEntity<Object> update(@RequestBody TallyHead tally, @PathVariable Integer id,	@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
						//TallyHead existProduct = tally_service.get(id);
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					tally.setModified_by(jwtDetails.getUserId());
					tally.setModified_username(jwtDetails.getUserName());
					tally_service.save_TallyHead1(tally);
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
	
	
	@DeleteMapping("/TallyHead/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				tally_service.delete(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}		
	}
	
	@DeleteMapping("/ActivateTallyHead/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				tally_service.delete1(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}		
	}
}
