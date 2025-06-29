package com.au.controller;

import java.io.IOException;
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
import com.au.model.CategoryTypeDetails;
import com.au.response.ResponseHandler;
import com.au.service.CategoryTypeDetailsService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;



@RestController
@RequestMapping("/api")
@CrossOrigin
public class CategoryTypeDetailsController {
	
Logger log = LoggerFactory.getLogger(CategoryTypeCreationController.class);
	
	
	@Autowired
	private CategoryTypeDetailsService ctd_service;
	
	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/categoryTypeDetails")
	public ResponseEntity<Object> savecategoryTypeDetails(@RequestBody @Valid CategoryTypeDetails ctd,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			ctd.setCreated_by(jwtDetails.getUserId());
			ctd.setCreated_username(jwtDetails.getUserName());
			CategoryTypeDetails category_type_details = ctd_service.saveCategoryTypeDetails(ctd);
			ResponseEntity<Object> category_type_details_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, category_type_details);
			return category_type_details_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

	@GetMapping("/categoryTypeDetails")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<CategoryTypeDetails> category_type_details_list = ctd_service.listAll();
			ResponseEntity<Object> category_type_details_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, category_type_details_list);
			return category_type_details_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
	@GetMapping("/categoryTypeDetailsOnBonafide")
	public ResponseEntity<Object> categoryTypeDetailsOnBonafide() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String,Object>> category_type_details_list = ctd_service.categoryTypeDetailsOnBonafide();
			ResponseEntity<Object> category_type_details_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, category_type_details_list);
			return category_type_details_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

	@GetMapping("/fetchAllcategoryTypeDetails")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> category_type_details_sorted = ctd_service.listAll1(pageable, keyword);//,column,value);
				return category_type_details_sorted;
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> category_type_details_pageable = ctd_service.listAll2(pageable1);
				return category_type_details_pageable;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
		
	}
	
	@GetMapping("/categoryTypeDetails/{id}")
	public ResponseEntity<Object> getCategoryTypeDetails(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {

					CategoryTypeDetails product = ctd_service.get(id);
					ResponseEntity<Object>category_type_details_by_id = ResponseHandler.generateResponse(true, HttpStatus.OK, product);
					return category_type_details_by_id;
			} catch (NoSuchElementException e) {
					ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
					return response1;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

	@PutMapping("/categoryTypeDetails/{id}")
	public ResponseEntity<Object> updateCategoryTypeDetails(@RequestBody @Valid CategoryTypeDetails ctd, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
					ctd_service.get(id);
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

					ctd.setModified_by(jwtDetails.getUserId());
					ctd.setModified_username(jwtDetails.getUserName());
					ctd_service.updateCategoryTypeDetails(ctd);
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

	@DeleteMapping("/deactivateCategoryTypeDetails/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				ctd_service.delete(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}

	@DeleteMapping("/activateCategoryTypeDetails/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				ctd_service.delete1(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@GetMapping("/categoryTypeDetailsForReasonFeeExcemption")
	public ResponseEntity<Object> categoryTypeDetailsForReasonFeeExcemption() {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {

					List<CategoryTypeDetails> category_type_details = ctd_service.categoryTypeDetailsForReasonFeeExcemption();
					ResponseEntity<Object> category_type_details_response = ResponseHandler.generateResponse(true, HttpStatus.OK, category_type_details);
					return category_type_details_response;
			} catch (NoSuchElementException e) {
					ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
					return response1;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

	@GetMapping("/categoryTypeDetailsDataByShortName/{category_name_sort}")
	public ResponseEntity<Object> categoryTypeCreationDataByShortName(@PathVariable String category_name_sort) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<String> category_type_creation_list = ctd_service.categoryTypeCreationDataByShortName(category_name_sort);
			ResponseEntity<Object> category_type_creation_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, category_type_creation_list);
			return category_type_creation_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
}
