package com.au.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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

import com.au.model.EmployeeExitFormalityQuestions;
import com.au.response.ResponseHandler;

import com.au.service.EmployeeExitFormalityQuestionsService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class EmployeeExitFormalityQuestionsController {

	Logger log = LoggerFactory.getLogger(EmployeeExitFormalityQuestionsController.class);
	
	@Autowired
	private EmployeeExitFormalityQuestionsService eefqService;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	
	@PostMapping("/employeeExitFormalityQuestions")
	public ResponseEntity<Object> saveDept(@RequestBody @Valid EmployeeExitFormalityQuestions eefq,@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		eefq.setCreated_by(jwtDetails.getUserId());
		eefq.setCreated_username(jwtDetails.getUserName());
		EmployeeExitFormalityQuestions eEFQ = eefqService.save_EmployeeExitFormalityQuestions(eefq);
		ResponseEntity<Object> employeeExitFormalityQuestions_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, eEFQ);
		return employeeExitFormalityQuestions_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}
	}
	
	@GetMapping("/fetchAllemployeeExitFormalityQuestionsDetail")
	public ResponseEntity<Object> getAllDept(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> employeeExitFormalityQuestions_filtered =  eefqService.getAllDataFilteredByKeyword(pageable, keyword);
			return employeeExitFormalityQuestions_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> employeeExitFormalityQuestions_sorted = eefqService.getAllSortedData(pageable1);
			return employeeExitFormalityQuestions_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/employeeExitFormalityQuestionsActive")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<EmployeeExitFormalityQuestions> eEFQ = eefqService.listAll1();
		ResponseEntity<Object> employeeExitFormalityQuestions_response= ResponseHandler.generateResponse(true, HttpStatus.OK, eEFQ);
		return employeeExitFormalityQuestions_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/employeeExitFormalityQuestions/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	EmployeeExitFormalityQuestions  eEFQ = eefqService.get(id);
	    	ResponseEntity<Object> employeeExitFormalityQuestions_response= ResponseHandler.generateResponse(true, HttpStatus.OK, eEFQ);
			return employeeExitFormalityQuestions_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/employeeExitFormalityQuestions/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid EmployeeExitFormalityQuestions eefq, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    	eefq.setModified_by(jwtDetails.getUserId());
	    	eefq.setModified_username(jwtDetails.getUserName());
	    	eefqService.saveEmployeeExitFormalityQuestions(eefq);
	    	ResponseEntity<Object> employeeExitFormalityQuestions_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return employeeExitFormalityQuestions_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	@DeleteMapping("/employeeExitFormalityQuestions/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			eefqService.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateEmployeeExitFormalityQuestions/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			eefqService.delete1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	

	@GetMapping("/employeeExitFormalityQuestionsAllData")
	public ResponseEntity<Object> listAllData() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String,Object>> eEFQ = eefqService.listAllData();
		ResponseEntity<Object> employeeExitFormalityQuestions_response= ResponseHandler.generateResponse(true, HttpStatus.OK, eEFQ);
		return employeeExitFormalityQuestions_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
}

