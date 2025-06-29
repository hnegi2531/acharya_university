package com.au.controller;

import java.util.HashMap;
import java.util.List;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.model.CandidateResults;
import com.au.response.ResponseHandler;
import com.au.service.CandidateResultsService;
import com.au.service.JwtTokenService;

@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class CandidateResultsController {
	

	@Autowired
	private CandidateResultsService csr_ser;
	
	@Autowired
	private JwtTokenService jwt_service;

	Logger log = LoggerFactory.getLogger(LessonPlanController.class);
	
	@PostMapping("/getCandidateResults")
	public ResponseEntity<Object> getCandidateResults(@ModelAttribute CandidateResults cr,
			@RequestHeader("Authorization") String jwtToken)throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			csr_ser.getDataFromFile(cr.getFile(), cr,jwtToken);
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllCandidateResults")
	public ResponseEntity<Object> fetchAllCandidateResults(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> candidateResult_filtered =  csr_ser.getAllDataFilteredByKeyword(pageable, keyword);
		    return candidateResult_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> candidateResult_sorted = csr_ser.getAllSortedData(pageable1);
			return candidateResult_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getCountOfResult")
	 public ResponseEntity<Object> getCountOfResult() {
		if(RateLimitController.bucket.tryConsume(1)) {
			HashMap<String, Object> count= csr_ser.getCountOfResult();
				ResponseEntity<Object> response= ResponseHandler.generateResponse(true, HttpStatus.OK, count);
				return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	 }

	@GetMapping("/getAllDataOfCandidateResult")
	 public ResponseEntity<Object> getAllDataOfCandidateResult() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> count= csr_ser.getAllDataOfCandidateResult();
				ResponseEntity<Object> response= ResponseHandler.generateResponse(true, HttpStatus.OK, count);
				return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	 }
	

}
