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
import com.au.model.SalaryStructureCategory;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.SalaryStructureCategoryService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
//@RequestMapping("/api")
public class SalaryStructureCategoryController {

	Logger log = LoggerFactory.getLogger(SalaryStructureCategoryController.class);

	@Autowired
	private SalaryStructureCategoryService sscs_service;
	
	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/SalaryStructureCategory")
	public ResponseEntity<Object> saveSala(@RequestBody @Valid SalaryStructureCategory bs,@RequestHeader("Authorization") String jwtToken) 
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails=jwt_service.callJwtToken(jwtToken);
				bs.setCreated_by(jwtDetails.getUserId());
				bs.setCreated_username(jwtDetails.getUserName());
				SalaryStructureCategory salary_structure_category = sscs_service.saveSalaryStructureCategory(bs);
				ResponseEntity<Object> salary_structure_category_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, salary_structure_category);
				return salary_structure_category_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@GetMapping("/SalaryStructureCategory")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<SalaryStructureCategory> list_ssc = sscs_service.listAll();
				ResponseEntity<Object> list_ssc_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_ssc);
				return list_ssc_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/fetchAllSalaryStructureCategoryDetails")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> salary_structure_category_sorted = sscs_service.listAll1(pageable, keyword);//,column,value);
						return salary_structure_category_sorted;
				}else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> salary_structure_category_pageable = sscs_service.listAll2(pageable1);
						return salary_structure_category_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		//return sscs_service.listAll1();
	}

	@GetMapping("/SalaryStructureCategory/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {

						SalaryStructureCategory product = sscs_service.get(id);
						ResponseEntity<Object> ssc_response_by_id = ResponseHandler.generateResponse(true, HttpStatus.OK, product);
						return ssc_response_by_id;

				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@PutMapping("/SalaryStructureCategory/{id}")
	public ResponseEntity<Object> update(@RequestBody SalaryStructureCategory bs,@PathVariable Integer id,@RequestHeader("Authorization") String jwtToken) 
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
						//SalaryStructureCategory existProduct = sscs_service.get(id);
					JwtDetails jwtDetails=jwt_service.callJwtToken(jwtToken);
					bs.setModified_by(jwtDetails.getUserId());
					bs.setModified_username(jwtDetails.getUserName());
					sscs_service.saveSalaryStructureCategory(bs);
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

	@DeleteMapping("/SalaryStructureCategory/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				sscs_service.delete(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@DeleteMapping("/activateSalaryStructureCategory/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		       sscs_service.delete1(id);
		       ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}

}