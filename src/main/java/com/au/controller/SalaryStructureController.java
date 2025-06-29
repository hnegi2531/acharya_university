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
import com.au.model.Offer;
import com.au.model.SalaryStructure;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.SalaryStructureService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
//@RequestMapping("/api")
public class SalaryStructureController {
	
	Logger log = LoggerFactory.getLogger(SalaryStructureController.class);

	@Autowired
	private SalaryStructureService ssr_service;
	
	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/SalaryStructure")
	public ResponseEntity<Object> saveSalaryStructure(@RequestBody @Valid SalaryStructure bs,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails=jwt_service.callJwtToken(jwtToken);
				bs.setCreated_by(jwtDetails.getUserId());
				bs.setCreated_username(jwtDetails.getUserName());
				SalaryStructure salary_structure = ssr_service.saveSalaryStructure(bs);
				ResponseEntity<Object> salary_structure_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, salary_structure);
				return salary_structure_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@GetMapping("/SalaryStructure")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
		       List<SalaryStructure> ss_list = ssr_service.listAll();
		       ResponseEntity<Object> ss_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, ss_list);
		       return ss_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}       
	}
	
	@GetMapping("/fetchAllSalaryStructureDetail")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> salary_structure_sorted = ssr_service.listAll1(pageable, keyword);//,column,value);
						return salary_structure_sorted;
				}else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> salary_structure_pageable = ssr_service.listAll2(pageable1);
						return salary_structure_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		//return ssr_service.listAll1();
	}

	@GetMapping("/SalaryStructure/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {

						SalaryStructure product = ssr_service.get(id);
						ResponseEntity<Object> salary_structure_response_by_id = ResponseHandler.generateResponse(true, HttpStatus.OK, product);
						return salary_structure_response_by_id;

				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/SalaryStructure/{id}")
	public ResponseEntity<Object> update(@RequestBody SalaryStructure bs, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken) 
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
						JwtDetails jwtDetails=jwt_service.callJwtToken(jwtToken);
						bs.setModified_by(jwtDetails.getUserId());
						bs.setModified_username(jwtDetails.getUserName());
						ssr_service.updateSalaryStructure(bs);
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

	@DeleteMapping("/SalaryStructure/{salary_structure_id}")
	public ResponseEntity<Object> delete(@PathVariable Integer salary_structure_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		       ssr_service.delete(salary_structure_id);
		       ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@DeleteMapping("/activateSalaryStructure/{salary_structure_id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer salary_structure_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		       ssr_service.delete1(salary_structure_id);
		       ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@GetMapping("/checkSalaryStructureIdInOffer/{salary_structure_id}")
	public ResponseEntity<Object> checkSalaryStructureIdInOffer(@PathVariable Integer salary_structure_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {

						Object o = ssr_service.checkSalaryStructureIdInOffer(salary_structure_id);
						ResponseEntity<Object> offer_by_salary_structure_id = ResponseHandler.generateResponse(true, HttpStatus.FOUND, o);
						return offer_by_salary_structure_id;

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