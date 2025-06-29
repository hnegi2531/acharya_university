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
import com.au.model.RTGSFeeHistory;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.RTGSFeeHistoryService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
public class RTGSFeeHistoryController {

Logger log = LoggerFactory.getLogger(RTGSFeeHistoryController.class);
	
	@Autowired
	private RTGSFeeHistoryService rtgs_fee_history_service;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/rtgsFeeHistory")
	public ResponseEntity<Object> saveRTGSFeeHistory(@RequestBody @Valid RTGSFeeHistory rtfh, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException{
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				rtfh.setCreated_by(jwtDetails.getUserId());
				rtfh.setCreated_username(jwtDetails.getUserName());
				RTGSFeeHistory rtgs_fee_history = rtgs_fee_history_service.saveRTGSFeeHistory(rtfh);
				ResponseEntity<Object> rtgs_fee_history_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, rtgs_fee_history);
				return rtgs_fee_history_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/activeRTGSFeeHistory")
	public ResponseEntity<Object> getActiveRTGSFeeHistory(){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<RTGSFeeHistory> rtgs_fee_history_list = rtgs_fee_history_service.getActiveRTGSFeeHistory();
			ResponseEntity<Object> rtgs_fee_history_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, rtgs_fee_history_list);
			return rtgs_fee_history_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
	@GetMapping("/fetchAllDetailsRTGSFeeHistory")
	public ResponseEntity<Object> getAllFeeReceipt(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> rtgs_fee_history_sorted = rtgs_fee_history_service.getAllRTGSFeeHistory1(pageable, keyword);//,column,value);
						return rtgs_fee_history_sorted;
				}else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> rtgs_fee_history_pageable =rtgs_fee_history_service.getAllRTGSFeeHistory2(pageable1);
						return rtgs_fee_history_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/rtgsFeeHistory/{rtgs_fee_history_id}")
	public ResponseEntity<Object> getRTGSFeeHistoryById(@PathVariable Integer rtgs_fee_history_id){
		if(RateLimitController.bucket.tryConsume(1)) {
				try {

						RTGSFeeHistory  product =rtgs_fee_history_service.getRTGSFeeHistoryById(rtgs_fee_history_id);
						ResponseEntity<Object> rtgs_fee_history_response_by_id= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
						return rtgs_fee_history_response_by_id;

				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@PutMapping("/rtgsFeeHistory/{rtgs_fee_history_id}")
	public ResponseEntity<Object> updateRTGSFeeHistory(@RequestBody @Valid RTGSFeeHistory rtgsfh, @PathVariable Integer rtgs_fee_history_id ,
			@RequestHeader("Authorization") String jwtToken)throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
						JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
						rtgsfh.setModified_by(jwtDetails.getUserId());
						rtgsfh.setModified_username(jwtDetails.getUserName());
						rtgs_fee_history_service.updateRTGSFeeHistory(rtgsfh);
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
	
	@DeleteMapping("/rtgsFeeHistory/{rtgs_fee_history_id}")
	public ResponseEntity<Object> delete(@PathVariable Integer rtgs_fee_history_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				rtgs_fee_history_service.delete(rtgs_fee_history_id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@DeleteMapping("/activateRTGSFeeHistory/{rtgs_fee_history_id}")
	public ResponseEntity<Object> activateRTGSFeeHistory(@PathVariable Integer rtgs_fee_history_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				rtgs_fee_history_service.activateRTGSFeeHistory(rtgs_fee_history_id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@GetMapping("/allRTGSFeeHistoryDetails/{bank_transaction_history_id}")
	public ResponseEntity<Object> allRTGSFeeHistoryDetails(@PathVariable Integer bank_transaction_history_id){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String,Object>> rtgs_fee_history_list = rtgs_fee_history_service.allRTGSFeeHistoryDetails(bank_transaction_history_id);
			ResponseEntity<Object> rtgs_fee_history_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, rtgs_fee_history_list);
			return rtgs_fee_history_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

	@GetMapping("/rtgsAmountForPaidAtBoardTag/{fcYearId}/{receiptNo}")
	public ResponseEntity<Object> rtgsAmountForPaidAtBoardTag(@PathVariable Integer fcYearId, @PathVariable String receiptNo){
		if(RateLimitController.bucket.tryConsume(1)) {
			return rtgs_fee_history_service.rtgsAmountForPaidAtBoardTag(fcYearId,receiptNo);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
}
