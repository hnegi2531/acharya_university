package com.au.controller;

import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import com.au.response.ResponseHandler;
import com.au.service.PaymePaymentGatewayService;

@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class PaymePaymentGatewayController {
	
	
	@Autowired
	private PaymePaymentGatewayService payme_payment_gateway_service;
	
	@Autowired
	private ResponseHandler response_handler;
	

	Logger log = LoggerFactory.getLogger(PaymePaymentGatewayController.class);
	

	@PostMapping("/paymePayment")
	public ResponseEntity<Object> saveResponseOFPayme(@RequestBody HashMap<String,Object> payme,@RequestHeader(value ="authorization",required = false) String token)
			throws JsonParseException, JsonMappingException, Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			if(token != null) {
				System.out.println("ffffffffffff "+token);
				HashMap<String, Object> authorization_result = response_handler.checkAuthorizationToken(token);
				HashMap<String, Object> authorization_details=(HashMap<String, Object>) authorization_result.get("error");
				if((int)authorization_details.get("code")== -32504) {
					return new ResponseEntity<Object>(authorization_result,HttpStatus.OK);
				}else {
					HashMap<String,Object> check_perform_transaction_response=payme_payment_gateway_service.saveResponseOFPayme(payme);
					return new ResponseEntity<Object>(check_perform_transaction_response,HttpStatus.OK);
				}
			} else {
				HashMap<String, Object> transaction_response = new HashMap<>();
				HashMap<String, Object> result = new HashMap<String, Object>();
				result.put("code", -32504);
				result.put("message", "The RPC request is missing required fields");
				transaction_response.put("error", result);
				return new ResponseEntity<Object>(transaction_response,HttpStatus.OK);
			} 

		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}

	}
	
	

	@PostMapping("/startingOfPayment")
	public ResponseEntity<Object> startingOfPayment(@RequestBody HashMap<String,Object> candidate_details) throws JsonProcessingException{
		HashMap<String,Object> Payme_transactions_details = payme_payment_gateway_service.startingOfPayment(candidate_details);
		return new ResponseEntity<Object>(Payme_transactions_details,HttpStatus.OK);
	}
	
	
	
	@GetMapping("/getPaymentStatus")
	public ResponseEntity<Object> getPaymentStatus(@RequestParam(value ="order_id") String order_id) {
		HashMap<String,Object> payment_status_details = payme_payment_gateway_service.getPaymentStatus(order_id);
		return new ResponseEntity<Object>(payment_status_details,HttpStatus.OK);
	}

}
