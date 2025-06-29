package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.au.model.CategoryTypeCreation;
import com.au.response.ResponseHandler;
import com.au.service.CategoryTypeCreationService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class CategoryTypeCreationController {
	
	Logger log = LoggerFactory.getLogger(CategoryTypeCreationController.class);
	
	
	@Autowired
	private CategoryTypeCreationService category_type_creation_service;
	
	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/categoryTypeCreation")
	public ResponseEntity<Object> savecategoryTypeCreation(@RequestBody @Valid CategoryTypeCreation ctc,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			ctc.setCreated_by(jwtDetails.getUserId());
			ctc.setCreated_username(jwtDetails.getUserName());
			CategoryTypeCreation category_type_creation = category_type_creation_service.savecategoryTypeCreation(ctc);
			ResponseEntity<Object> calender_year_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, category_type_creation);
			return calender_year_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

	@GetMapping("/categoryTypeCreation")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<CategoryTypeCreation> category_type_creation_list = category_type_creation_service.listAll();
			ResponseEntity<Object> category_type_creation_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, category_type_creation_list);
			return category_type_creation_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

	@GetMapping("/fetchAllCategoryTypeCreation")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> calender_year_sorted = category_type_creation_service.listAll1(pageable, keyword);//,column,value);
				return calender_year_sorted;
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> calender_year_pageable = category_type_creation_service.listAll2(pageable1);
				return calender_year_pageable;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
		
	}
	
	@GetMapping("/categoryTypeCreation/{id}")
	public ResponseEntity<Object> getCategoryTypeCreation(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {

					CategoryTypeCreation product = category_type_creation_service.get(id);
					ResponseEntity<Object> calender_year_response_by_id = ResponseHandler.generateResponse(true, HttpStatus.OK, product);
					return calender_year_response_by_id;
			} catch (NoSuchElementException e) {
					ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
					return response1;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

	@PutMapping("/categoryTypeCreation/{id}")
	public ResponseEntity<Object> updateCategoryTypeCreation(@RequestBody @Valid CategoryTypeCreation ctc, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
					category_type_creation_service.get(id);
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

					ctc.setModified_by(jwtDetails.getUserId());
					ctc.setModified_username(jwtDetails.getUserName());
					category_type_creation_service.updateCategoryTypeCreation(ctc);
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

	@DeleteMapping("/deactivateCategoryTypeCreation/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				category_type_creation_service.delete(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}

	@DeleteMapping("/activateCategoryTypeCreation/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				category_type_creation_service.delete1(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@GetMapping("/getCategoriesForFrro")
	public ResponseEntity<Object> getCategoriesForFrro(){
		return category_type_creation_service.getCategoriesForFrro();
	}
	
	
	@GetMapping("/getCategoriesForPaymentType")
	public ResponseEntity<Object> getCategoriesForPaymentType() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> category_type_creation_list = category_type_creation_service.getCategoriesForPaymentType();
			ResponseEntity<Object> category_type_creation_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, category_type_creation_list);
			return category_type_creation_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
		

}


