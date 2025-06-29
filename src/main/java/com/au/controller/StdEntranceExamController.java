package com.au.controller;

import java.io.IOException;
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
import com.au.model.StdEntranceExam;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.StdEntranceExamService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class StdEntranceExamController {

	
	Logger log = LoggerFactory.getLogger(StdEntranceExamController.class);
	
	@Autowired
	private StdEntranceExamService sc_service;

	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/StdEntranceExam")
	public StdEntranceExam saveStdEntranceExam(@RequestBody @Valid StdEntranceExam s,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		s.setCreated_by(jwtDetails.getUserId());
		s.setCreated_username(jwtDetails.getUserName());
		return sc_service.saveStdEntranceExam(s);
	}
	
	@GetMapping("/StdEntranceExam")
	public List<StdEntranceExam> listAll(){
		return sc_service.listAll();
	}
	
	@GetMapping("/StdEntranceExam/{id}")
	public ResponseEntity<StdEntranceExam> get(@PathVariable Integer id) {
	    try {
	    	
	    	StdEntranceExam s = sc_service.get(id);
	        return new ResponseEntity<StdEntranceExam>(s, HttpStatus.OK);
	    } catch (NoSuchElementException e) {
	        return new ResponseEntity<StdEntranceExam>(HttpStatus.NOT_FOUND);
	    }      
	}

	@PutMapping("/StdEntranceExam/{id}")
	public ResponseEntity<StdEntranceExam> update(@RequestBody @Valid StdEntranceExam s, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
	    try {
	    	StdEntranceExam existSchool = sc_service.get(id);
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			s.setModified_by(jwtDetails.getUserId());
			s.setModified_username(jwtDetails.getUserName());
	    	sc_service.saveStdEntranceExam(s);
	        return new ResponseEntity<StdEntranceExam>(HttpStatus.OK);
	    } catch (NoSuchElementException e) {
	        return new ResponseEntity<StdEntranceExam>(HttpStatus.NOT_FOUND);
	    }      
	}
	
	
	@DeleteMapping("/StdEntranceExam/{id}")
	public void delete(@PathVariable Integer id) {
		sc_service.delete(id);
	}
	
	@GetMapping("/getEntranceDetails/{studentId}")
	public ResponseEntity<Object> getEntranceDetails(@PathVariable Integer studentId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<StdEntranceExam> studentDetails = sc_service.getEntranceDetails(studentId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK,
					studentDetails);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}
	
}
