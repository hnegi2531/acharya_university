package com.au.controller;

import java.io.IOException;
import java.text.ParseException;
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

import com.au.dto.AdvancePayScaleDeductionDto;
import com.au.dto.JwtDetails;
import com.au.model.AdvancePayScaleDeduction;
import com.au.response.ResponseHandler;
import com.au.service.AdvancePayScaleDeductionService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
public class AdvancePayScaleDeductionController {

Logger log = LoggerFactory.getLogger(AdvancePayScaleDeductionController.class);
	
	@Autowired
	private AdvancePayScaleDeductionService PSDeduction_service;

	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/advancePayScaleDeduction")
	public ResponseEntity<Object> advancePayScaleDeduction(@RequestBody @Valid AdvancePayScaleDeductionDto apsd,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			apsd.setCreated_by(jwtDetails.getUserId());
			apsd.setCreated_username(jwtDetails.getUserName());
			List<AdvancePayScaleDeduction> payScale = PSDeduction_service.advancePayScaleDeduction(apsd);
			ResponseEntity<Object> payScale_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, payScale);
		return payScale_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	

	@GetMapping("/fetchAllAdvancePayScaleDeduction")
	public ResponseEntity<Object> getAllDept(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> oc_filtered =  PSDeduction_service.getAllDataFilteredByKeyword(pageable, keyword);
			return oc_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> oc_sorted = PSDeduction_service.getAllSortedData(pageable1);
			return oc_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	
	@DeleteMapping("/deactivateAdvancePayScaleDeduction/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			PSDeduction_service.deactivate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@DeleteMapping("/activateAdvancePayScaleDeduction/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			PSDeduction_service.activate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/updateAdvancePayScaleDeduction/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid AdvancePayScaleDeduction apsd, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    	apsd.setModified_by(jwtDetails.getUserId());
	    	apsd.setModified_username(jwtDetails.getUserName());
	    	PSDeduction_service.updateAdvancePayScaleDeduction(apsd);
	    	ResponseEntity<Object> apsd_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return apsd_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	@GetMapping("/fetchMonthlyEmiData/{advance_id}")
	public ResponseEntity<Object> fetchTimeTableDetailsByEmployeeId(@PathVariable Integer advance_id) throws ParseException {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> fetchMonthlyEmiData = PSDeduction_service.fetchTimeTableDetailsByEmployeeId(advance_id);
			ResponseEntity<Object> fetchMonthlyEmiDataResponse = ResponseHandler.generateResponse(true, HttpStatus.OK, fetchMonthlyEmiData);
			return fetchMonthlyEmiDataResponse;
			
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
		
	}	
	
	@PutMapping("/updateEmi/{emi_id}/{advance_id}/{emi_amount}")
	public ResponseEntity<Object> updateEmi(@PathVariable Integer emi_id, @PathVariable Integer advance_id, @PathVariable Double emi_amount) throws ParseException {
		if (RateLimitController.bucket.tryConsume(1)) {
			PSDeduction_service.updateEmi(emi_id,advance_id,emi_amount);
			ResponseEntity<Object> apsd_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return apsd_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
		
	}
}
