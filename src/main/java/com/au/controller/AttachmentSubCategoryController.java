package com.au.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.model.AttachmentCategory;
import com.au.model.AttachmentSubCategory;
import com.au.response.ResponseHandler;
import com.au.service.AttachmentSubCategoryService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class AttachmentSubCategoryController {

	@Autowired
	private AttachmentSubCategoryService a_service;	
	
	Logger log = LoggerFactory.getLogger(AttachmentSubCategoryController.class);
	
	@PostMapping("/AttachmentSubCategory")
	public ResponseEntity<Object> saveAttachmentSubCategory(@RequestBody @Valid AttachmentSubCategory a) {
		if(RateLimitController.bucket.tryConsume(1)) {
				AttachmentSubCategory attch_sub_cate = a_service.saveAttachmentSubCategory(a);
				ResponseEntity<Object> attch_sub_cate_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, attch_sub_cate);
				return attch_sub_cate_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
//	@GetMapping("/AttachmentSubCategory")
//	public List<AttachmentSubCategory> listAll(){
//		return a_service.listAll();
//	}
	
	@GetMapping("/AttachmentSubCategory")
	public ResponseEntity<Object> listAll(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> attch_sub_cate_sorted = a_service.listAll1(pageable, keyword);//,column,value);
				return attch_sub_cate_sorted;
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> attch_sub_cate_pageable = a_service.listAll2(pageable1);
				return attch_sub_cate_pageable;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
		
	}
	
	@GetMapping("/AttachmentSubCategory/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
	    	
						AttachmentSubCategory product = a_service.get(id);
						ResponseEntity<Object> attch_sub_cate_response_by_id= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
						return attch_sub_cate_response_by_id;
	        
				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}

	@PutMapping("/AttachmentSubCategory/{id}")
	public ResponseEntity<Object> update(@RequestBody AttachmentSubCategory a, @PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
						AttachmentSubCategory existProduct = a_service.get(id);
						a_service.saveAttachmentSubCategory(a);
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
	
	@DeleteMapping("/AttachmentSubCategory/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				a_service.delete(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@GetMapping("/AttachmentSubCategory1/{as_name}")
	 public ResponseEntity<Object> getAttachCategory(@PathVariable String as_name) {
		if(RateLimitController.bucket.tryConsume(1)) {
				AttachmentSubCategory attch_sub_cate = a_service.getAttachCategory(as_name);
				ResponseEntity<Object> attch_sub_cate_response_by_as_name= ResponseHandler.generateResponse(true, HttpStatus.OK, attch_sub_cate);
				return attch_sub_cate_response_by_as_name;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	  }
	
}
