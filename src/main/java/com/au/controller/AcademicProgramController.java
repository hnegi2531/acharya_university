package com.au.controller;

import java.io.IOException;
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
import com.au.model.AcademicProgram;
import com.au.response.ResponseHandler;
import com.au.service.AcademicProgramService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
//@RequestMapping("/api")
//@Api(tags = {"Academic"})
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class AcademicProgramController {

	Logger log = LoggerFactory.getLogger(AcademicProgramController.class);

	private AcademicProgramService ap_service;

	@Autowired
	private JwtTokenService jwt_service;
	
	
	public AcademicProgramController(AcademicProgramService ap_service) {
		super();
		this.ap_service = ap_service;
	}

	@PostMapping("/AcademicProgram")
	public ResponseEntity<Object> saveAcademicYear(@RequestBody @Valid AcademicProgram bs,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			bs.setCreated_by(jwtDetails.getUserId());
			bs.setCreated_username(jwtDetails.getUserName());
			AcademicProgram bs1 = ap_service.save_AcademicProgram(bs);
			ResponseEntity<Object> academic_program_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, bs1);
			return academic_program_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
	@GetMapping(value ="/AcademicProgram")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			System.out.println("hello"); 
			System.out.println("hi"); 
			List<AcademicProgram> academic_program_list= ap_service.listAll();
			ResponseEntity<Object> academic_program_list_response= ResponseHandler.generateResponse(true, HttpStatus.OK, academic_program_list);
			return academic_program_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
	/*@GetMapping("/fetchAcademicProgramDetail")
	public List<HashMap<String, Object>> listAll1() {
		return ap_service.listAll1();
		
	}*/
	
	@GetMapping("/fetchAcademicProgramDetail")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> academic_program_sorted =  ap_service.listAll1(pageable, keyword);//,column,value);
						return academic_program_sorted;
				}else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> academic_program_pageable = ap_service.listAll2(pageable1);
						return academic_program_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
			
	}

	@GetMapping("/AcademicProgram/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {

					AcademicProgram product = ap_service.get(id);
					ResponseEntity<Object> academic_program_by_id_response= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
					return academic_program_by_id_response;

				} catch (NoSuchElementException e) {
					ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
					return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

	@PutMapping("/AcademicProgram/{id}")
	public ResponseEntity<Object> update(@RequestBody AcademicProgram bs, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
					ap_service.get(id);
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					bs.setModified_by(jwtDetails.getUserId());
					bs.setModified_username(jwtDetails.getUserName());
					ap_service.saveAcademicProgram(bs);
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

	@DeleteMapping("/AcademicProgram/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			return ap_service.delete(id);
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
	@DeleteMapping("/activateAcademicProgram/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			return ap_service.delete1(id);
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
/*
	@GetMapping("/FetchAcademicProgram/{ac_year_id}/{program_id}")
	public List<AcademicProgram> getNumOfSemAndYearByProgram_IdAndAcYear_Id(@PathVariable Integer ac_year_id,@PathVariable Integer program_id)
	{
		return ap_service.getNumOfSemAndYearByProgram_IdAndAcYear_Id(ac_year_id, program_id);
	}
*/	
	@GetMapping("/fetchAcademicProgram1/{ac_year_id}/{program_id}")
	 public ResponseEntity<Object> getProgram(@PathVariable Integer ac_year_id,@PathVariable Integer  program_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Integer count= ap_service.getProgram(ac_year_id, program_id);
				ResponseEntity<Object> fetchAcademicProgram1_count_response= ResponseHandler.generateResponse(true, HttpStatus.OK, count);
				return fetchAcademicProgram1_count_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	 }
}
