package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.JwtDetails;
import com.au.model.HostelHeadWiseAmt;
import com.au.model.VoucherHeadNew;
import com.au.response.ResponseHandler;
import com.au.service.HostelHeadWiseAmtService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
//@RequestMapping("/api")
public class HostelHeadWiseAmtController {

	@Autowired
	private HostelHeadWiseAmtService hostel_head_wise_amt_service;
	
	Logger log = LoggerFactory.getLogger(HostelHeadWiseAmtController.class);

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/HostelHeadWiseAmt")
	public ResponseEntity<Object> saveHostelHeadWiseAmt(@RequestBody @Valid HostelHeadWiseAmt h,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			h.setCreatedBy(jwtDetails.getUserId());
			h.setCreatedUsername(jwtDetails.getUserName());
			HostelHeadWiseAmt hhwa = hostel_head_wise_amt_service.saveHostelHeadWiseAmt(h);
			ResponseEntity<Object> hostel_head_wise_amount_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, hhwa);
			return hostel_head_wise_amount_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllHostelHeadWiseAmtDetails")
	public ResponseEntity<Object> fetchAllHostelHeadWiseAmtDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> hostel_head_wise_amount_sorted = hostel_head_wise_amt_service.listAll1(pageable, keyword);//,column,value);
				return hostel_head_wise_amount_sorted;
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> hostel_head_wise_amount_pageable = hostel_head_wise_amt_service.listAll2(pageable1);
				return hostel_head_wise_amount_pageable;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/voucherHeadNewOnHostelStatus")
	public ResponseEntity<Object> voucherHeadNewOnHostelStatus() {
		if(RateLimitController.bucket.tryConsume(1)) {
					List<VoucherHeadNew> list_voucher_head_new = hostel_head_wise_amt_service.voucherHeadNewOnHostelStatus();
					ResponseEntity<Object> list_voucher_head_new_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_voucher_head_new);
					return list_voucher_head_new_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}			
	}
	
	@GetMapping("/hostelHeadWiseAmtOnFeeTemplateId/{hostel_fee_template_id}")
	public ResponseEntity<Object> hostelHeadWiseAmtOnFeeTemplateId(@PathVariable Integer hostel_fee_template_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
					List<HashMap<String, Object>> hhwa = hostel_head_wise_amt_service.hostelHeadWiseAmtOnFeeTemplateId(hostel_fee_template_id);
					ResponseEntity<Object> hhwa_response = ResponseHandler.generateResponse(true, HttpStatus.OK, hhwa);
					return hhwa_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}			
	}
	
}
