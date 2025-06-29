package com.au.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.validation.Valid;

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
import com.au.model.CandidateFollowUp;
import com.au.response.ResponseHandler;
import com.au.service.CandidateFollowUpService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;



@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class CandidateFollowUpController {


	@Autowired
	private CandidateFollowUpService can_ser;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/saveCandidateFollowUp")
	public ResponseEntity<Object> saveCandidateFollowUp(@RequestBody @Valid CandidateFollowUp cfu,@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				cfu.setCreated_by(jwtDetails.getUserId());
				cfu.setCreated_username(jwtDetails.getUserName());
				CandidateFollowUp data = can_ser.saveCandidateFollowUp(cfu);
				ResponseEntity<Object> followup_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, data);
				return followup_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/getActiveCandidateFollowUp")
	public ResponseEntity<Object>  listAll(){
		if(RateLimitController.bucket.tryConsume(1)) {
				List<CandidateFollowUp> followup_list = can_ser.listAll();
				ResponseEntity<Object>  followup_response = ResponseHandler.generateResponse(true, HttpStatus.OK, followup_list);
				return followup_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/fetchAllCandidateFollowUp")
	public ResponseEntity<Object> fetchAllCandidateFollowUp(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> followup_sorted = can_ser.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
				return followup_sorted;
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> followup_pageable = can_ser.getAllSortedData(pageable1);
				return followup_pageable;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/getActiveCandidateFollowUp/{candidate_followup_id}")
	public ResponseEntity<Object> get(@PathVariable Integer candidate_followup_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
	    	
					CandidateFollowUp product = can_ser.get(candidate_followup_id);
						ResponseEntity<Object> followup_response_by_id = ResponseHandler.generateResponse(true, HttpStatus.OK, product);
						return followup_response_by_id;
				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@PutMapping("/updateCandidateFollowUp/{candidate_followup_id}")
	public ResponseEntity<?> update(@RequestBody @Valid CandidateFollowUp cfu, @PathVariable Integer candidate_followup_id,@RequestHeader("Authorization")  String jwtToken)
	      throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
					//Bank existProduct = bank_service.get(id);
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					cfu.setModified_by(jwtDetails.getUserId());
					cfu.setModified_username(jwtDetails.getUserName());
					can_ser.saveCandidateFollowUp(cfu);
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
	
	@DeleteMapping("/deactivateCandidateFollowUp/{candidate_followup_id}")
	public ResponseEntity<Object> delete(@PathVariable Integer candidate_followup_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				can_ser.delete(candidate_followup_id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}

	@DeleteMapping("/activateCandidateFollowUp/{candidate_followup_id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer candidate_followup_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				can_ser.delete1(candidate_followup_id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@GetMapping("/getCandidateFollowUpByCandidateId/{candidate_id}")
	public ResponseEntity<Object>  getData(@PathVariable Integer candidate_id){
		if(RateLimitController.bucket.tryConsume(1)) {
				List<CandidateFollowUp> followup_list = can_ser.getData(candidate_id);
				ResponseEntity<Object>  followup_response = ResponseHandler.generateResponse(true, HttpStatus.OK, followup_list);
				return followup_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
//	@GetMapping("/getCandidateFollowUpByFollowUpDate/{follow_up_date}")
//	public ResponseEntity<Object>  getDataByFollowUpDate(@PathVariable String follow_up_date) throws ParseException{
//		if(RateLimitController.bucket.tryConsume(1)) {
//			DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
//			Date d= df.parse(follow_up_date);
//				List<CandidateFollowUp> followup_list = can_ser.getDataByFollowUpDate(follow_up_date);
//				ResponseEntity<Object>  followup_response = ResponseHandler.generateResponse(true, HttpStatus.OK, followup_list);
//				return followup_response;
//		}else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}		
//	}
//	@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
//	@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword,
	
	@GetMapping("/fetchCandidateFollowUpData")
	public ResponseEntity<Object> fetchCandidateFollowUpData(
			@RequestParam(value="created_by",required = false) Integer created_by,
			@RequestParam(value="from_date") String from_date, @RequestParam(value="to_date") String to_date) throws ParseException {
		if(RateLimitController.bucket.tryConsume(1)) {
			DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
			Date date1=df.parse(from_date);
			Date date2=df.parse(to_date);

			List<Map<String, Object>> list_candidate_walkin = can_ser.fetchCandidateFollowUpData(created_by,date1,date2);
			ResponseEntity<Object> list_candidate_walkin_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_candidate_walkin);
			return list_candidate_walkin_response;
			
		
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

}
