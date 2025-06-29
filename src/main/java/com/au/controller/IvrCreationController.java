package com.au.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.IvrCreationDto;
import com.au.dto.UserAuthenticationDto;
import com.au.model.IvrCreation;
import com.au.response.ResponseHandler;
import com.au.service.IvrCreationService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
@RequestMapping("/api")
@CrossOrigin
public class IvrCreationController {

	
	Logger log = LoggerFactory.getLogger(IvrCreationController.class);
	
	
	@Autowired
	private IvrCreationService ivrCreationService;

	@Autowired
	private JwtTokenService jwt_service;
	
    @PostMapping("/callOutbound")
    public ResponseEntity<Object> callOutbound(@RequestBody @Valid IvrCreation tm) {
     
        ResponseEntity<Object> ivrCreation = ivrCreationService.callOutboundApi(tm);
        if (ivrCreation != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("body", "success");  
            return ResponseHandler.newGenerateResponse(true, HttpStatus.CREATED, data);
        } else {
            Map<String, Object> data = new HashMap<>();
            data.put("body", "failed");
            return ResponseHandler.newGenerateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, data);
        }
    }  

    
    @GetMapping("/getCallOutbound")
	public ResponseEntity<Object> getCallOutbound(@RequestParam(value = "custnumber") String custnumber , @RequestParam(value = "userId") Integer userId) {
		if (RateLimitController.bucket.tryConsume(1)) {

			Map<String, Object> status = ivrCreationService.getCallOutbound(custnumber ,userId );

			ResponseEntity<Object> response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					Collections.singletonMap("status", status));
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
    
    @GetMapping("/getIvrCreationData/{student_id}")
	public ResponseEntity<Object> getIvrCreationData(@PathVariable Integer student_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> proctorStudentAssignment = ivrCreationService.getIvrCreationData(student_id);
			ResponseEntity<Object> proctorStudentAssignment_response= ResponseHandler.generateResponse(true, HttpStatus.OK, proctorStudentAssignment);
			return proctorStudentAssignment_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
    
    
    @PutMapping("/updateIvrCreation/{ivr_creation_id}")
	public ResponseEntity<Object> updateIvrCreation(@RequestBody IvrCreationDto dto,
			@PathVariable Integer ivr_creation_id, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {

			ivrCreationService.updateIvrCreation(dto, jwtToken);
			ResponseEntity<Object> emp_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return emp_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
    
}
