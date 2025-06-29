package com.au.controller;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.response.ResponseHandler;
import com.au.service.Graduation_Service;
import com.au.service.SendingSmsForUZBService;

@RestController
@RequestMapping("/api/${secretkey8}")
@CrossOrigin
public class SendingSmsForUZBController {
	
	Logger log = LoggerFactory.getLogger(SendingSmsForUZBController.class);
	
	
	@Autowired
	private SendingSmsForUZBService sending_sms_for_uzb_ser;
	
	@PostMapping("/sendingSms")
	public ResponseEntity<Object> sendSms(@RequestParam(value="mobile_number") String mobile_number,@RequestParam(value="message_text") String message_text){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String,Object>> sms_response= sending_sms_for_uzb_ser.sendSms(mobile_number,message_text);
			ResponseEntity<Object> sms_final_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, sms_response);
			return sms_final_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

}
