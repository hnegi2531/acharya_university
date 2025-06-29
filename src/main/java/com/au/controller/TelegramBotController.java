package com.au.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.au.response.ResponseHandler;
import com.au.service.TelegramBotService;

@RestController
@RequestMapping("/api/${secretkey8}")
@CrossOrigin
public class TelegramBotController {
	
Logger log = LoggerFactory.getLogger(TelegramBotController.class);
	
	@Autowired
	private TelegramBotService telegram_bot_service;
	
	
//	@GetMapping("/verificationOfTelegramIdAndUsername")
//	public ResponseEntity<Object> verificationOfTelegramIdAndUsername(@RequestParam(value ="application_no_npf") String application_no_npf)
//			throws JsonParseException, JsonMappingException, IOException {
//		if(RateLimitController.bucket.tryConsume(1)) {
//				HashMap<String,Object> verification= telegram_bot_service.verificationOfTelegramIdAndUsername(application_no_npf);
//				ResponseEntity<Object> verification_response= ResponseHandler.generateResponse(true, HttpStatus.OK, verification);
//				return verification_response ;
//		}else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}		
//	}
//	
//	@GetMapping("/verificationProcessOfTelegramIdAndUsername")
//	public ResponseEntity<Object> verificationProcessOfTelegramIdAndUsername(@RequestParam(value ="application_no_npf") String application_no_npf,@RequestParam(value ="url") String url)
//			throws JsonParseException, JsonMappingException, IOException {
//		if(RateLimitController.bucket.tryConsume(1)) {
//				HashMap<String,Object> verification= telegram_bot_service.verificationProcessOfTelegramIdAndUsername(application_no_npf,url);
//				ResponseEntity<Object> verification_response= ResponseHandler.generateResponse(true, HttpStatus.OK, verification);
//				return verification_response ;
//		}else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}		
//	}
//	
//	@PostMapping("/verificationProcessOfTelegramIdAndUsernameByWebhooks")
//	public ResponseEntity<Object> verificationProcessOfTelegramIdAndUsernameByWebhooks(@RequestBody HashMap<String, Object> getUpdatesDetailsFromTelegramBot)
//			throws JsonParseException, JsonMappingException, IOException {
//		if(RateLimitController.bucket.tryConsume(1)) {
//				telegram_bot_service.verificationProcessOfTelegramIdAndUsernameByWebhooks(getUpdatesDetailsFromTelegramBot);
//				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
//				return response;
//		}else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}		
//	}

}
