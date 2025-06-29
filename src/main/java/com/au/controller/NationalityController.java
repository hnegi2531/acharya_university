package com.au.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.JwtDetails;
import com.au.model.Conferences;
import com.au.model.Nationality;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.NationalityService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class NationalityController {
	
	@Autowired
	private NationalityService nation_service;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	
	@PostMapping("/nationality")
	public ResponseEntity<Object> saveNationality(@RequestBody @Valid Nationality nation, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			nation.setCreated_by(jwtDetails.getUserId());
			nation.setCreated_username(jwtDetails.getUserName());
			Nationality nationality = nation_service.saveNationality(nation);
			ResponseEntity<Object> nationality_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, nationality);
			return nationality_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	
	}
	
	@GetMapping("/nationality")
	public List<Nationality> allActiveDetailsList(){
		List<Nationality> nation_list = nation_service.allActiveDetailsList();
		return nation_list;
	}
	
	@GetMapping("/getAllActiveNationality")
	public ResponseEntity<Object> getAllActiveNationality() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> con_details  = nation_service.getAllActiveNationality();
		ResponseEntity<Object>national_response= ResponseHandler.generateResponse(true, HttpStatus.OK, con_details);
		return national_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}	

}
