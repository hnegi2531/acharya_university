package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
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
import com.au.model.TallyReceipt;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.TallyReceiptService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
//@RequestMapping("/api")
public class TallyReceiptController {
	
		Logger log=LoggerFactory.getLogger(TallyReceiptController.class);
		
		@Autowired
		private TallyReceiptService tally_rec_ser;
		
		@Autowired
		private JwtTokenService jwt_service;
		
		
		@PostMapping("/tallyReceipt")
		public ResponseEntity<Object> saveTallyReceipt(@RequestBody @Valid TallyReceipt tal_rec, @RequestHeader("Authorization") String jwtToken)
				throws JsonParseException, JsonMappingException, IOException{
			if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				tal_rec.setCreated_by(jwtDetails.getUserId());
				tal_rec.setCreated_username(jwtDetails.getUserName());
				TallyReceipt tally_receipt = tally_rec_ser.saveTallyReceipt(tal_rec);
				ResponseEntity<Object> tally_receipt_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, tally_receipt);
				return tally_receipt_response;
			}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		@GetMapping("/activeTallyReceipt")
		public ResponseEntity<Object> getActiveTallyReceipt(){
			if(RateLimitController.bucket.tryConsume(1)) {
				List<TallyReceipt> list_tally_receipt = tally_rec_ser.getActiveTallyReceipt();
				ResponseEntity<Object> list_tally_receipt_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_tally_receipt);
				return list_tally_receipt_response;
			}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		@GetMapping("/fetchAllTallyReceipt")
		public ResponseEntity<Object> getAllTallyReceipt(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
				@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
			if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> tally_receipt_sorted = tally_rec_ser.getAllTallyReceipt1(pageable, keyword);//,column,value);
					return tally_receipt_sorted;
				}else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> tally_receipt_pageable = tally_rec_ser.getAllTallyReceipt2(pageable1);
					return tally_receipt_pageable;
				}
			}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		@GetMapping("/tallyReceipt/{tally_receipt_id}")
		public ResponseEntity<Object> getTallyReceiptById(@PathVariable Integer tally_receipt_id){
			if (RateLimitController.bucket.tryConsume(1)) {
				try {
					TallyReceipt product=tally_rec_ser.getTallyReceiptById(tally_receipt_id);
					ResponseEntity<Object> tally_receipt_response_by_id = ResponseHandler.generateResponse(true,HttpStatus.OK, product);
					return tally_receipt_response_by_id;
				}catch(NoSuchElementException e) {
					ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
					return response1;
				}
			} else {
				ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
				return rs;
			}
		}
		
		@PutMapping("/tallyReceipt/{tally_receipt_id}")
		public ResponseEntity<Object> updateTallyReceipt(@RequestBody @Valid TallyReceipt tal_rec , 
				@PathVariable Integer tally_receipt_id, @RequestHeader("Authorization") String jwtToken)throws 
				JsonParseException, JsonMappingException, IOException {
			if(RateLimitController.bucket.tryConsume(1)) {
				try {
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					tal_rec.setModified_by(jwtDetails.getUserId());
					tal_rec.setModified_username(jwtDetails.getUserName());
					TallyReceipt tal=tally_rec_ser.updateTallyReceipt(tal_rec);
					ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
					return response;
				}catch (NoSuchElementException e) {
					ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
					return response1;
				}
			}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		@DeleteMapping("/tallyReceipt/{tally_receipt_id}")
		public ResponseEntity<Object> delete1(@PathVariable Integer tally_receipt_id) {
			if(RateLimitController.bucket.tryConsume(1)) {
				tally_rec_ser.delete1(tally_receipt_id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
			}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		@DeleteMapping("/activateTallyReceipt/{tally_receipt_id}")
		public ResponseEntity<Object> delete2(@PathVariable Integer tally_receipt_id) {
			if(RateLimitController.bucket.tryConsume(1)) {
				tally_rec_ser.delete2(tally_receipt_id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
			}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}


}
