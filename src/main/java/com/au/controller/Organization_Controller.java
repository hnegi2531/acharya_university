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
import com.au.model.Organization;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.Org_service;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey4}")
@CrossOrigin
public class Organization_Controller {

	Logger log = LoggerFactory.getLogger(Organization_Controller.class); 
	
	@Autowired
	private Org_service org_service;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	/*	@ApiOperation(value = "Create User",notes = "Create New User",tags = {"User Management"})
	@ApiResponses(value = {
			@ApiResponse(code = 200,message = " Academic Year created Successfully"),
			@ApiResponse(code = 404,message = "Invalid Data"),
			@ApiResponse(code = 500,message = "INTERNAL SERVER ERROR")				
	})
*/
	@PostMapping("/org")
	public ResponseEntity<Object> saveOrganization(@RequestBody @Valid Organization org,@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				org.setCreated_by(jwtDetails.getUserId());
				org.setCreated_username(jwtDetails.getUserName());
				Organization org1 = org_service.save_Org(org);
				ResponseEntity<Object> org1_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, org1);
				return org1_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/org")
	public ResponseEntity<Object> listAll(){
		if(RateLimitController.bucket.tryConsume(1)) {
				List<Organization> list_org = org_service.listAll();
				ResponseEntity<Object> list_org_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_org);
				return list_org_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/fetchAllOrgDetail")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
		if(RateLimitController.bucket.tryConsume(1)) {
					Sort sorted = Sort.by(Direction.DESC, sort );
					if(keyword != null) {	
							Pageable pageable = PageRequest.of(page, page_size,sorted);
							System.out.println("page, page_size, sorted, keyword");
							ResponseEntity<Object> org_sorted = org_service.listAll1(pageable, keyword);//,column,value);
							return org_sorted;
					}else {
							Pageable pageable1 = PageRequest.of(page, page_size,sorted);
							System.out.println("page, page_size, sorted");
							ResponseEntity<Object> org_pageable = org_service.listAll2(pageable1);
							return org_pageable;
					}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}			
		//return org_service.listAll1();
	}

	
	@GetMapping("/org/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
	    	
						Organization org = org_service.get(id);
						ResponseEntity<Object> org_by_id_response= ResponseHandler.generateResponse(true, HttpStatus.OK, org);
						return org_by_id_response;
	        
				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@PutMapping("/org/{id}")
	public ResponseEntity<Object> update(@RequestBody Organization org, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
					//Organization existProduct = org_service.get(id);
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					org.setModified_by(jwtDetails.getUserId());
					org.setModified_username(jwtDetails.getUserName());
					org_service.save_Org1(org);
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
	
	
	@DeleteMapping("/org/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
					org_service.delete(id);
					ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
					return response;
		}else {	
					ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
					return rs;
		}
	}
	
	@DeleteMapping("/activateOrg/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				org_service.delete1(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/orgValidation")
	public ResponseEntity<Object> schoolValidation(@RequestParam(value = "org_name",required = false) String org_name,
			@RequestParam(value = "org_type",required = false) String org_type){
		if (RateLimitController.bucket.tryConsume(1)) {
				HashMap<String,Boolean> valiadtion = org_service.orgValidation(org_name,org_type);
				ResponseEntity<Object> schools_response = ResponseHandler.generateResponse(true, HttpStatus.OK, valiadtion);
				return schools_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}		
	}
	
}
