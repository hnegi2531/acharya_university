package com.au.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
import com.au.model.Bank;
import com.au.response.ResponseHandler;
import com.au.service.BankService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
//@RequestMapping("/api")
public class BankController {
	
Logger log = LoggerFactory.getLogger(BankController.class);
	
	@Autowired
	private BankService bank_service;
	
	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/Bank")
	public ResponseEntity<Object> saveBank(@RequestBody @Valid Bank bank,@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
//		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				bank.setCreated_by(jwtDetails.getUserId());
				bank.setCreated_username(jwtDetails.getUserName());
				Bank bk = bank_service.saveBank(bank);
				ResponseEntity<Object> bank_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, bk);
				return bank_response;
//		}else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}		
	}
	
	@GetMapping("/Bank")
	public ResponseEntity<Object>  listAll(){
		if(RateLimitController.bucket.tryConsume(1)) {
				List<Bank> bank_list = bank_service.listAll();
				ResponseEntity<Object>  bank_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, bank_list);
				return bank_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}


	@GetMapping("/fetchAllBanknDetails")
	public ResponseEntity<Object> fetchAllBanknDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> program_type_sorted = bank_service.fetchAllBanknDetails1(pageable, keyword);//,column,value);
				return program_type_sorted;
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> program_type_pageable = bank_service.fetchAllBanknDetails2(pageable1);
				return program_type_pageable;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/Bank/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
	    	
						Bank product = bank_service.get(id);
						ResponseEntity<Object> bank_list_response_by_id = ResponseHandler.generateResponse(true, HttpStatus.OK, product);
						return bank_list_response_by_id;
				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@PutMapping("/Bank/{id}")
	public ResponseEntity<?> update(@RequestBody @Valid Bank bank, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				bank_service.updateBank(bank, jwtDetails);
                return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			} catch (NoSuchElementException e) {
                return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			}
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	
	@DeleteMapping("/Bank/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				bank_service.delete(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}

	@DeleteMapping("/activateBank/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				bank_service.delete1(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}

	@GetMapping("/getTotalAmountBankWise/{financial_year_id}")
	public ResponseEntity<Object>  getTotalAmountBankWise(@PathVariable Integer financial_year_id){	
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> bank_list = bank_service.getTotalAmountBankWise(financial_year_id);
			ResponseEntity<Object>  bank_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, bank_list);
			return bank_list_response;
	}else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}		
 }
	
	
	@GetMapping("/getCreditDebitTotalAmountMonthly/{school_id}/{financial_year_id}/{bank_id}")
	public ResponseEntity<Object>  getCreditDebitTotalAmountMonthly(@PathVariable Integer school_id, @PathVariable Integer financial_year_id, 
			@PathVariable Integer bank_id){	
		if(RateLimitController.bucket.tryConsume(1)) {
			
			List<HashMap<String, Object>> bank_list = bank_service.getCreditDebitTotalAmountMonthly(school_id, financial_year_id, bank_id);
			ResponseEntity<Object>  bank_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, bank_list);
			return bank_list_response;
	}else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}		
  }	
		

	@GetMapping("/getCreditDebitTotalAmountDayWise/{school_id}/{financial_year_id}/{bank_id}/{from_date}/{to_date}")
	public ResponseEntity<Object>  getCreditDebitTotalAmountDayWise(@PathVariable Integer school_id, @PathVariable Integer financial_year_id, 
			@PathVariable Integer bank_id, @PathVariable String from_date, @PathVariable String to_date) throws ParseException{
		
		if(RateLimitController.bucket.tryConsume(1)) {
			DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
			Date fDate=df.parse(from_date);
			Date TDate=df.parse(to_date);
				List<HashMap<String, Object>> bank_list = bank_service.getCreditDebitTotalAmountDayWise(school_id, financial_year_id, bank_id, fDate, TDate);
				ResponseEntity<Object>  bank_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, bank_list);
				return bank_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}


	@GetMapping("/getCreditDebitTotalAmountMonthlyVoucherHeadWise/{school_id}/{financial_year_id}/{voucher_head_new_id}")
	public ResponseEntity<Object>  getCreditDebitTotalAmountMonthlyVoucherHeadWise( @PathVariable Integer school_id,@PathVariable Integer financial_year_id, 
			@PathVariable Integer voucher_head_new_id){	
		if(RateLimitController.bucket.tryConsume(1)) {
			
			List<HashMap<String, Object>> bank_list = bank_service.getCreditDebitTotalAmountMonthlyVoucherHeadWise(school_id, financial_year_id, voucher_head_new_id);
			ResponseEntity<Object>  bank_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, bank_list);
			return bank_list_response;
	}else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}		
}
	
	@GetMapping("/getCreditDebitTotalAmountDayWisebyVoucherHeadWise/{school_id}/{financial_year_id}/{voucher_head_new_id}/{from_date}/{to_date}")
	public ResponseEntity<Object>  getCreditDebitTotalAmountDayWisebyVoucherHeadWise(@PathVariable Integer school_id,@PathVariable Integer financial_year_id, 
			@PathVariable Integer voucher_head_new_id, @PathVariable String from_date, @PathVariable String to_date) throws ParseException{
		if(RateLimitController.bucket.tryConsume(1)) {	
			DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
			Date fDate=df.parse(from_date);
			Date TDate=df.parse(to_date);
				List<HashMap<String, Object>> bank_list = bank_service.getCreditDebitTotalAmountDayWisebyVoucherHeadWise( school_id,financial_year_id, voucher_head_new_id, fDate, TDate);
				ResponseEntity<Object>  bank_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, bank_list);
				return bank_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}


	@GetMapping("/bankDetailsBasedOnSchoolId/{school_id}")
	public ResponseEntity<Object>  bankDetailsBasedOnSchoolId(@PathVariable Integer school_id){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> bank_list = bank_service.bankDetailsBasedOnSchoolId(school_id);
				ResponseEntity<Object>  bank_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, bank_list);
				return bank_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}	
	
	
	@GetMapping("/getAllbankDetailsData")
	public ResponseEntity<Object>  getAllbankDetailsData(){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> bank_list = bank_service.getAllbankDetailsData();
				ResponseEntity<Object>  bank_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, bank_list);
				return bank_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}	
	
}

