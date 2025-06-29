package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
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
import com.au.model.SalaryStructureHead;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.SalaryStructureHeadService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
//@RequestMapping("/api")
public class SalaryStructureHeadController {

	Logger log = LoggerFactory.getLogger(SalaryStructureHeadController.class);

	@Autowired
	private SalaryStructureHeadService sshs_service;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/SalaryStructureHead")
	public ResponseEntity<Object> saveSalaryStructureHead(@RequestBody @Valid SalaryStructureHead bs,
			@RequestHeader("Authorization") String jwtToken)throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				bs.setCreated_by(jwtDetails.getUserId());
				bs.setCreated_username(jwtDetails.getUserName());
				SalaryStructureHead salary_structure_head = sshs_service.saveSalaryStructureHead(bs);
				ResponseEntity<Object> salary_structure_head_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, salary_structure_head);
				return salary_structure_head_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@GetMapping("/SalaryStructureHead")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<HashMap<String, Object>> ssh_list = sshs_service.listAll();
				ResponseEntity<Object> ssh_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, ssh_list);
				return ssh_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/fetchAllSalaryStructureHeadDetails")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> salary_structure_head_sorted = sshs_service.listAll1(pageable, keyword);//,column,value);
						return salary_structure_head_sorted;
				}else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> salary_structure_head_pageable = sshs_service.listAll2(pageable1);
						return salary_structure_head_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		//return sshs_service.listAll1();
	}

	@GetMapping("/SalaryStructureHead/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {

					SalaryStructureHead product = sshs_service.get(id);
					ResponseEntity<Object> salary_structure_head_response_by_id = ResponseHandler.generateResponse(true, HttpStatus.OK, product);
					return salary_structure_head_response_by_id;

			} catch (NoSuchElementException e) {
					ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
					return response1;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

	@PutMapping("/SalaryStructureHead/{id}")
	public ResponseEntity<Object> update(@RequestBody SalaryStructureHead bs, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
						// SalaryStructureHead existProduct = sshs_service.get(id);
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					bs.setModified_by(jwtDetails.getUserId());
					bs.setModified_username(jwtDetails.getUserName());
					sshs_service.saveSalaryStructureHead1(bs);
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

	@DeleteMapping("/SalaryStructureHead/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			sshs_service.delete(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@DeleteMapping("/activateSalaryStructureHead/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				sshs_service.delete1(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@GetMapping("/getPrintName/{salary_structure_head_id}")
	public ResponseEntity<Object> getPrintName(@PathVariable Integer salary_structure_head_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<HashMap<String, Object>> print_name = sshs_service.getPrintName(salary_structure_head_id);
				ResponseEntity<Object> print_name_response = ResponseHandler.generateResponse(true, HttpStatus.OK, print_name);
				return print_name_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		
	}
	
	@GetMapping("/categoryNameType/{voucher_head_new_id}")
	public ResponseEntity<Object> categoryNameType(@PathVariable Integer voucher_head_new_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<HashMap<String, Object>> category_name_type = sshs_service.categoryNameType(voucher_head_new_id);
				ResponseEntity<Object> category_name_type_response = ResponseHandler.generateResponse(true, HttpStatus.OK, category_name_type);
				return category_name_type_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		
	}
	
	@GetMapping("/SalaryStructureHead1")
	public ResponseEntity<Object> allSalaryStructureHeadAndVocherHeadDetails() {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<HashMap<String, Object>> ssh_list = sshs_service.allSalaryStructureHeadAndVocherHeadDetails();
				ResponseEntity<Object> ssh_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, ssh_list);
				return ssh_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/SalaryStructureHead2/{salary_structure_id}")
	public ResponseEntity<Object> listAll4(@PathVariable Integer salary_structure_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<HashMap<String, Object>> ssh_list = sshs_service.listAll4(salary_structure_id);
				ResponseEntity<Object> ssh_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, ssh_list);
				return ssh_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
}