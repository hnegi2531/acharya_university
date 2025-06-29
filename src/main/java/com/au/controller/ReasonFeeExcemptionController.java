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
import com.au.model.ProgramType;
import com.au.model.ReasonFeeExcemption;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.ReasonFeeExcemptionService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
//@RequestMapping("/api")
public class ReasonFeeExcemptionController {

	Logger log = LoggerFactory.getLogger(ReasonFeeExcemptionController.class);
	
	@Autowired
	private ReasonFeeExcemptionService r_repo;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/ReasonFeeExcemption")
	public ResponseEntity<Object> saveCourse(@RequestBody @Valid ReasonFeeExcemption r,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException,Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			r.setCreated_by(jwtDetails.getUserId());
			r.setCreated_username(jwtDetails.getUserName());
			ReasonFeeExcemption rea_exc = r_repo.save_ReasonFeeExcemption(r);
			ResponseEntity<Object> rea_exc_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, rea_exc);
			return rea_exc_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
	@GetMapping("/activeReasonFeeExcemption")
	public ResponseEntity<Object> listAllActiveDetails(){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<ReasonFeeExcemption> list_reason_fee_excemption = r_repo.listAllActiveDetails();
			ResponseEntity<Object> list_reason_fee_excemption_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_reason_fee_excemption);
			return list_reason_fee_excemption_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		//return r_repo.listAll();
	}
	
	@GetMapping("/ReasonFeeExcemption")
	public ResponseEntity<Object> listAll(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> rea_exc_sorted = r_repo.listAll1(pageable, keyword);//,column,value);
					return rea_exc_sorted;
				}else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> rea_exc_pageable = r_repo.listAll2(pageable1);
					return rea_exc_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		//return r_repo.listAll();
	}

	
	@GetMapping("/ReasonFeeExcemption/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
	    	
					ReasonFeeExcemption product = r_repo.get(id);
					ResponseEntity<Object> rea_exc_response_by_id = ResponseHandler.generateResponse(true, HttpStatus.OK, product);
					return rea_exc_response_by_id;
			} catch (NoSuchElementException e) {
					ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
					return response1;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

	@PutMapping("/ReasonFeeExcemption/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid ReasonFeeExcemption r, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException,Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
						ReasonFeeExcemption existProduct = r_repo.get(id);
						JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

						r.setModified_by(jwtDetails.getUserId());
						r.setModified_username(jwtDetails.getUserName());
			
						r_repo.updateReasonFeeExcemption(r);
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
	
	
	@DeleteMapping("/ReasonFeeExcemption/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				r_repo.delete(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}

	@DeleteMapping("/activateReasonFeeExcemption/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		     r_repo.delete1(id);
		     ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}


}
