package com.au.controller;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.JwtDetails;
import com.au.model.PGApplicable;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.PGApplicableService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class PGApplicableController {
	
	Logger log = LoggerFactory.getLogger(PGApplicableController.class);
	
	@Autowired
	private PGApplicableService pg_service;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/PGApplicable")
	public ResponseEntity<Object> savePGApplicable(@RequestBody @Valid PGApplicable pg,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			pg.setCreated_by(jwtDetails.getUserId());
			pg.setCreated_username(jwtDetails.getUserName());
			PGApplicable pga = pg_service.save_PGApplicable(pg);
			ResponseEntity<Object> pga_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, pga);
			return pga_response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
	}
	
	@GetMapping("/PGApplicable")
	public ResponseEntity<Object> listAll(){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<PGApplicable> list_pga = pg_service.listAll();
			ResponseEntity<Object> pga_response= ResponseHandler.generateResponse(true, HttpStatus.OK, list_pga);
			return pga_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	
	@GetMapping("/PGApplicable/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	PGApplicable pga = pg_service.get(id);
	    	ResponseEntity<Object> pga_response= ResponseHandler.generateResponse(true, HttpStatus.OK, pga);
			return pga_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/PGApplicable/{id}")
	public ResponseEntity<Object> update(@RequestBody PGApplicable pg, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	PGApplicable existProduct = pg_service.get(id);
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			pg.setModified_by(jwtDetails.getUserId());
			pg.setModified_username(jwtDetails.getUserName());
	    	pg_service.save_PGApplicable(pg);
	    	ResponseEntity<Object> pga_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return pga_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}  
	}
	
	
	@DeleteMapping("/PGApplicable/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		pg_service.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	
}
