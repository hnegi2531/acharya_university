package com.au.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.JwtDetails;
import com.au.model.HostelRooms;
import com.au.response.ResponseHandler;
import com.au.service.HostelRoomsHistoryService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey6}")
@CrossOrigin
public class HostelRoomsHistoryController {
	
	Logger log=LoggerFactory.getLogger(HostelRoomsHistoryController.class);
	
	@Autowired
	private HostelRoomsHistoryService hostelRoomsHistoryService;
	
	@PostMapping("/hostelRoomsHistory")
	public ResponseEntity<Object> saveHostelRoomsHistory(@RequestBody @Valid HostelRooms hostelRooms,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			return hostelRoomsHistoryService.saveHostelRoomsHistory(hostelRooms,jwtToken);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	

}
