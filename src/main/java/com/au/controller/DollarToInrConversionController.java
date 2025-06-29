package com.au.controller;

import java.io.IOException;
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
import com.au.model.DollarToInrConversion;
import com.au.response.ResponseHandler;
import com.au.service.DollarToInrConversionService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
public class DollarToInrConversionController {

	Logger log = LoggerFactory.getLogger(DollarToInrConversionController.class);

	@Autowired
	private DollarToInrConversionService dollar_to_inr_conv_service;

	@Autowired
	private JwtTokenService jwt_service;


	@PostMapping("/dollarToInrConversion")
	public ResponseEntity<Object> dollarToInrConversion(@RequestBody @Valid DollarToInrConversion dtucc,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				dtucc.setCreated_by(jwtDetails.getUserId());
				dtucc.setCreated_username(jwtDetails.getUserName());
				DollarToInrConversion dollar_to_inr_currency_conv = dollar_to_inr_conv_service.dollarToInrConversion(dtucc);
				ResponseEntity<Object> dollar_to_inr_currency_conv_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, dollar_to_inr_currency_conv);
				return dollar_to_inr_currency_conv_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@GetMapping("/allActiveDollarToInrConversion")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<DollarToInrConversion> dollar_to_inr_currency_conv_list = dollar_to_inr_conv_service.listAll();
				ResponseEntity<Object> dollar_to_inr_currency_conv_list_response= ResponseHandler.generateResponse(true, HttpStatus.OK, dollar_to_inr_currency_conv_list);
				return dollar_to_inr_currency_conv_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	
	@GetMapping("/fetchAllDollarToInrConversionDetails")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> dollar_to_uzb_currency_conv_details =  dollar_to_inr_conv_service.listAll1(pageable, keyword);//,column,value);
						return dollar_to_uzb_currency_conv_details;
				}else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> dollar_to_uzb_currency_conv_details = dollar_to_inr_conv_service.listAll2(pageable1);
						return dollar_to_uzb_currency_conv_details;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		//return ac_service.listAll1();
	}

	@GetMapping("/dollarToInrConversion/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {

					DollarToInrConversion product = dollar_to_inr_conv_service.get(id);
						ResponseEntity<Object> dollar_to_uzb_currency_conv_response= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
						return dollar_to_uzb_currency_conv_response;

				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@PutMapping("/dollarToInrConversion/{id}")
	public ResponseEntity<Object> update(@RequestBody DollarToInrConversion dtucc, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					dtucc.setModified_by(jwtDetails.getUserId());
					dtucc.setModified_username(jwtDetails.getUserName());
					dollar_to_inr_conv_service.dollarToInrConversion(dtucc);
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

	@DeleteMapping("/deActivateDollarToInrConversion/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			dollar_to_inr_conv_service.deActivate(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@DeleteMapping("/activateDollarToInrConversion/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			dollar_to_inr_conv_service.activate(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
}
