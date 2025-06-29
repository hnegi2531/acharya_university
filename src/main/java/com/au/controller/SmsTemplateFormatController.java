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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import com.au.controller.RateLimitController;
import com.au.controller.SmsTemplateFormatController;
import com.au.dto.JwtDetails;
import com.au.model.SmsTemplateFormat;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.SmsTemplateFormatService;

@RestController
@RequestMapping("/api/${secretkey8}")
@CrossOrigin
public class SmsTemplateFormatController {
	
	Logger log = LoggerFactory.getLogger(SmsTemplateFormatController.class);

	@Autowired
	private SmsTemplateFormatService sms_template_format_service;

	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/saveSmsTemplateFormat")
	public ResponseEntity<Object> saveSmsTemplateFormat(@RequestBody @Valid SmsTemplateFormat stf,@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				stf.setCreated_by(jwtDetails.getUserId());
				stf.setCreated_username(jwtDetails.getUserName());
				SmsTemplateFormat sms_template_format = sms_template_format_service.saveSmsTemplateFormat(stf);
				ResponseEntity<Object> sms_template_format_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, sms_template_format);
				return sms_template_format_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@GetMapping("/smsTemplateFormat")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<SmsTemplateFormat> sms_template_format_list = sms_template_format_service.listAll();
				ResponseEntity<Object> sms_template_format_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, sms_template_format_list);
				return sms_template_format_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/fetchAllSmsTemplateFormatDetails")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> board_sorted = sms_template_format_service.listAll1(pageable, keyword);//,column,value);
						return board_sorted;
				}else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> board_pageable = sms_template_format_service.listAll2(pageable1);
						return board_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		//return bo_service.listAll1();
	}

	@GetMapping("/smsTemplateFormatById/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {

						SmsTemplateFormat product = sms_template_format_service.get(id);
						log.debug("Request {}", id);
						ResponseEntity<Object> board_by_id_response= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
						return board_by_id_response;

				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@PutMapping("/updateSmsTemplateFormat/{id}")
	public ResponseEntity<Object> updateSmsTemplateFormat(@RequestBody @Valid SmsTemplateFormat stf, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
						//Board existProduct = fts_service.get(id);
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					stf.setModified_by(jwtDetails.getUserId());
					stf.setModified_username(jwtDetails.getUserName());
					sms_template_format_service.updateSmsTemplateFormat(stf);
					log.debug("Request {}", stf);
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

	@DeleteMapping("/smsTemplateFormat/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				sms_template_format_service.delete(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@DeleteMapping("/activateSmsTemplateFormat/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				sms_template_format_service.delete1(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

}