package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
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
import com.au.model.SalaryStructureDetails;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.SalaryStructureDetailsService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
//@RequestMapping("/api")
public class SalaryStructureDetailsController {

	Logger log = LoggerFactory.getLogger(SalaryStructureDetailsController.class);

	@Autowired
	private SalaryStructureDetailsService ssds_service;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/SalaryStructureDetails")
	public ResponseEntity<Object> saveSalaryStructureDetail(@RequestBody @Valid SalaryStructureDetails bs,
			@RequestHeader("Authorization") String jwtToken)throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				bs.setCreated_by(jwtDetails.getUserId());
				bs.setCreated_username(jwtDetails.getUserName());
				SalaryStructureDetails salary_structure_detail = ssds_service.saveSalaryStructureDetails(bs);
				ResponseEntity<Object> salary_structure_detail_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, salary_structure_detail);
				return salary_structure_detail_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@GetMapping("/SalaryStructureDetails")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
		      List<SalaryStructureDetails> ssd_list = ssds_service.listAll();
		      ResponseEntity<Object> ssd_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, ssd_list);
		      return ssd_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}      
	}

	@GetMapping("/fetchAllSalaryStructureDetails")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
		    Sort sorted = Sort.by(Direction.DESC, sort );
		    if(keyword != null) {	
			    Pageable pageable = PageRequest.of(page, page_size,sorted);
			    System.out.println("page, page_size, sorted, keyword");
			    ResponseEntity<Object> salary_structure_detail_sorted = ssds_service.listAll1(pageable, keyword);//,column,value);
			    return salary_structure_detail_sorted;
		   }else {
			    Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			    System.out.println("page, page_size, sorted");
			    ResponseEntity<Object> salary_structure_detail_pageable = ssds_service.listAll2(pageable1);
			    return salary_structure_detail_pageable;
		   }
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}    
		    
		//return ssds_service.listAll1();
	}

	@GetMapping("/SalaryStructureDetails/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {

					SalaryStructureDetails product = ssds_service.get(id);
					ResponseEntity<Object> salary_structure_detail_response_by_id = ResponseHandler.generateResponse(true, HttpStatus.OK, product);
					return salary_structure_detail_response_by_id;

			} catch (NoSuchElementException e) {
					ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
					return response1;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

	@PutMapping("/SalaryStructureDetails/{id}")
	public ResponseEntity<Object> update(@RequestBody SalaryStructureDetails bs,
			@PathVariable Integer id, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		      try {
			        JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			        bs.setModified_by(jwtDetails.getUserId());
			        bs.setModified_username(jwtDetails.getUserName());
			        ssds_service.saveSalaryStructureDetail(bs);
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

	@DeleteMapping("/SalaryStructureDetails/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		     ssds_service.delete(id);
		     ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}

	@DeleteMapping("/activateSalaryStructureDetails/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		       ssds_service.delete1(id);
		       ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}

	@GetMapping("/getFormulaDetails/{salary_structure_id}")
	public ResponseEntity<Object> getAllDetails(@PathVariable Integer salary_structure_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<HashMap<String, Object>> formula_details = ssds_service.getAllDetails(salary_structure_id);
				ResponseEntity<Object> formula_details_response = ResponseHandler.generateResponse(true, HttpStatus.OK, formula_details);
				return formula_details_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/getDetailsssssssssssssssss")
	public ResponseEntity<Object> getAllDetailsssssss(@RequestBody SalaryStructureDetails tp ) {
		if(RateLimitController.bucket.tryConsume(1)) {
		      Object obj = ssds_service.getAllDetailsssssss(tp.getSalary_structure_id(),tp.getSalary_structure_head_id());
		      ResponseEntity<Object> obj_response = ResponseHandler.generateResponse(true, HttpStatus.OK, obj);
		      return obj_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}      
	}
}