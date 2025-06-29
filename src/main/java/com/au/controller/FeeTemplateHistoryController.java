package com.au.controller;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.au.model.FeeTemplateHistory;
import com.au.response.ResponseHandler;
import com.au.service.FeeTemplateHistoryService;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
//@RequestMapping("/api")
public class FeeTemplateHistoryController {
	
	@Autowired
	private FeeTemplateHistoryService fthr_service;
	
	@GetMapping("/FeeTemplateHistory")
	public ResponseEntity<Object> listAll(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> fthr_sorted = fthr_service.listAll1(pageable, keyword);//,column,value);
						return fthr_sorted;
				}else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> fthr_pageable = fthr_service.listAll2(pageable1);
						return fthr_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		//return fthr_service.listAll();

	}
	
	@GetMapping("/FeeTemplateHistory/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
	    	
				FeeTemplateHistory product = fthr_service.get(id);
	    		ResponseEntity<Object> fth_response_by_id= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
	    		return fth_response_by_id;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response1;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
			
			
	}
	
	@GetMapping("/FetchFeeTemplateHistoryDetail/{fee_template_id}") // (Behalf of fee_template_id)
	public ResponseEntity<Object> fetch1(@RequestBody @PathVariable Integer fee_template_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> fth_list = fthr_service.findByFeeTemplate(fee_template_id);
				ResponseEntity<Object> fth_list_response= ResponseHandler.generateResponse(true, HttpStatus.OK, fth_list);
				return fth_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

}
