package com.au.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.au.response.ResponseHandler;
import com.au.service.ClickPaymentService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import com.au.controller.ClickPaymentController;
import com.au.controller.RateLimitController;

@RestController
@RequestMapping("/api/${secretkey1}")
public class ClickPaymentController {
	
	
	@Autowired
	private ClickPaymentService click_payment_service;
	
	@Autowired
	private ResponseHandler response_handler;
	
	Logger log = LoggerFactory.getLogger(ClickPaymentController.class);
	
	
	@PostMapping("/startingOfClickPayment")
	public ResponseEntity<Object> startingOfPayment(@RequestBody HashMap<String,Object> candidate_details) throws ParseException{
		HashMap<String,Object> Payme_transactions_details = click_payment_service.startingOfPayment(candidate_details);
		return new ResponseEntity<Object>(Payme_transactions_details,HttpStatus.OK);
	}
	
	@PostMapping("/prepareClickPayment")
	public ResponseEntity<Object> saveResponseOfPrepareClickPayment(@RequestParam Map<String,Object> click_pay)
			throws JsonParseException, JsonMappingException, Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			HashMap<String,Object> click_payment_response= click_payment_service.saveResponseOfPrepareClickPayment(click_pay);
			return new ResponseEntity<Object>(click_payment_response,HttpStatus.OK);

		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}

	}
	
	@PostMapping("/completeClickPayment")
	public ResponseEntity<Object> saveResponseOfCompleteClickPayment(@RequestParam Map<String,Object> click_pay)
			throws JsonParseException, JsonMappingException, Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			HashMap<String,Object> click_payment_response= click_payment_service.saveResponseOfCompleteClickPayment(click_pay);
			return new ResponseEntity<Object>(click_payment_response,HttpStatus.OK);

		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}

	}
	
	
	@GetMapping("/getClickPaymentStatus")
	public ResponseEntity<Object> getClickPaymentStatus(@RequestParam(value ="merchant_trans_id") String merchant_trans_id) {
		HashMap<String,Object> payment_status_details = click_payment_service.getClickPaymentStatus(merchant_trans_id);
		return new ResponseEntity<Object>(payment_status_details,HttpStatus.OK);
	}
	

}
