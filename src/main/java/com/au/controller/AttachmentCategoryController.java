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

import com.au.model.Academic_year;
import com.au.model.AttachmentCategory;
import com.au.response.ResponseHandler;
import com.au.service.AttachmentCategoryService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class AttachmentCategoryController {

	@Autowired
	private AttachmentCategoryService a_service;
	
	
	Logger log = LoggerFactory.getLogger(AttachmentCategoryController.class);
	
	@PostMapping("/AttachmentCategory")
	public ResponseEntity<Object> saveAttachmentCategory(@RequestBody @Valid AttachmentCategory a) {
		if(RateLimitController.bucket.tryConsume(1)) {
				AttachmentCategory attc = a_service.saveAttachmentCategory(a);
				ResponseEntity<Object> attachment_category_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, attc);
				return attachment_category_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/AttachmentCategory")
	public ResponseEntity<Object> listAll(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
							Pageable pageable = PageRequest.of(page, page_size,sorted);
							System.out.println("page, page_size, sorted, keyword");
							ResponseEntity<Object> applicant_details_reponse = a_service.listAll1(pageable, keyword);//,column,value);
							return applicant_details_reponse;
				}else {
							Pageable pageable1 = PageRequest.of(page, page_size,sorted);
							System.out.println("page, page_size, sorted");
							ResponseEntity<Object> applicant_details_reponse1 = a_service.listAll2(pageable1);
							return applicant_details_reponse1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		//return a_service.listAll();
	}

	
	@GetMapping("/AttachmentCategory/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
	    	
						AttachmentCategory product = a_service.get(id);
						ResponseEntity<Object> attachment_category_by_id_response= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
						return attachment_category_by_id_response;
	        
				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@PutMapping("/AttachmentCategory/{id}")
	public ResponseEntity<Object> update(@RequestBody AttachmentCategory a, @PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
						AttachmentCategory existProduct = a_service.get(id);
						a_service.saveAttachmentCategory(a);
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
	
	
	@DeleteMapping("/AttachmentCategory/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				return a_service.delete(id);
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
			
	}
}
