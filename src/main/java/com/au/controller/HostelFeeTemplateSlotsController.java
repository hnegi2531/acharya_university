package com.au.controller;

import java.io.IOException;
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
import com.au.model.AcademicProgram;
import com.au.model.HostelFeeTemplateSlots;
import com.au.model.HostelHeadWiseAmt;
import com.au.response.ResponseHandler;
import com.au.service.HostelFeeTemplateSlotsService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
//@RequestMapping("/api")
public class HostelFeeTemplateSlotsController {

	@Autowired
	private HostelFeeTemplateSlotsService hostel_fee_template_slot_service;
	
	Logger log = LoggerFactory.getLogger(HostelFeeTemplateSlotsController.class);
	
	@Autowired
	private JwtTokenService jwt_service;
	/*
	@PostMapping("/HostelFeeTemplateSlots")
	public ResponseEntity<HostelFeeTemplateSlots> saveAcademicYear(@RequestBody @Valid HostelFeeTemplateSlots h,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		h.setCreatedBy(jwtDetails.getUserId());
		h.setCreatedUsername(jwtDetails.getUserName());
		HostelFeeTemplateSlots bs1 = hostel_fee_template_slot_service.saveHostelFeeTemplateSlots(h);
		return new ResponseEntity<HostelFeeTemplateSlots>(h, HttpStatus.CREATED);
	}
*/
	@PostMapping("/HostelFeeTemplateSlots")
	public ResponseEntity<Object> saveAcademicYear(@RequestBody @Valid HostelFeeTemplateSlots h,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			h.setCreatedBy(jwtDetails.getUserId());
			h.setCreatedUsername(jwtDetails.getUserName());
			HostelFeeTemplateSlots hfts = hostel_fee_template_slot_service.saveHostelFeeTemplateSlots(h);
			ResponseEntity<Object> hostel_fee_template_slot_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, hfts);
			return hostel_fee_template_slot_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/HostelFeeTemplateSlots")
	public ResponseEntity<Object> fetchAllHostelHeadWiseAmtDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> hostel_fee_template_slot_sorted = hostel_fee_template_slot_service.listAll1(pageable, keyword);//,column,value);
			return hostel_fee_template_slot_sorted;
		}else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> hostel_fee_template_slot_pageable = hostel_fee_template_slot_service.listAll2(pageable1);
			return hostel_fee_template_slot_pageable;
		}
	}
	
	@GetMapping("/HostelFeeTemplateSlotsOnFeeTemplateId/{hostel_fee_template_id}")
	public ResponseEntity<Object> getHostelFeeTemplateSlots(@PathVariable Integer hostel_fee_template_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HostelFeeTemplateSlots> hfts_by_id = hostel_fee_template_slot_service.getHostelFeeTemplateSlots(hostel_fee_template_id);
			ResponseEntity<Object> hfts_by_id_response = ResponseHandler.generateResponse(true, HttpStatus.OK, hfts_by_id);
			return hfts_by_id_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
}
