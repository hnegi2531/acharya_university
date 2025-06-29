package com.au.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.response.ResponseHandler;
import com.au.service.CancelledTallyReceiptService;
import com.au.service.JwtTokenService;


@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
public class CancelledTallyReceiptController {
	
Logger log=LoggerFactory.getLogger(TallyReceiptController.class);
	
	@Autowired
	private CancelledTallyReceiptService cancel_tally_rec_ser;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	
	@GetMapping("/fetchAllCancelledTallyReceipt")
	public ResponseEntity<Object> CancelledTallyReceipt(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> tally_receipt_sorted = cancel_tally_rec_ser.getAllCancelledTallyReceipt(pageable, keyword);//,column,value);
				return tally_receipt_sorted;
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> tally_receipt_pageable = cancel_tally_rec_ser.getAllCancelledTallyReceipt2(pageable1);
				return tally_receipt_pageable;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}


}
