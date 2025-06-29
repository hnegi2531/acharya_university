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
import com.au.model.ProgramSpecilization;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.ProgramSpecilizationService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
//@RequestMapping("/api")
public class ProgramSpecilizationController {

	Logger log = LoggerFactory.getLogger(ProgramSpecilizationController.class);
	
	@Autowired
	private ProgramSpecilizationService pr_service;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/ProgramSpecilization")
	public ResponseEntity<Object> saveProgramSpecilization(@RequestBody @Valid ProgramSpecilization ps,
		@RequestHeader("Authorization") String jwtToken)throws Exception,JsonParseException,JsonMappingException,IOException{
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				ps.setCreated_by(jwtDetails.getUserId());
				ps.setCreated_username(jwtDetails.getUserName());
				ProgramSpecilization psp = pr_service.save_ProgramSpecilization(ps);
				ResponseEntity<Object> ps_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, psp);
				return ps_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/ProgramSpecilization")
	public ResponseEntity<Object> listAll(){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String,Object>> list_psp = pr_service.listAll();
					ResponseEntity<Object> list_psp_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_psp);
					return list_psp_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}			
	}
	
	@GetMapping("/fetchAllProgramSpecilizationDetail")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
		if(RateLimitController.bucket.tryConsume(1)) {
					Sort sorted = Sort.by(Direction.DESC, sort );
					if(keyword != null) {	
							Pageable pageable = PageRequest.of(page, page_size,sorted);
							System.out.println("page, page_size, sorted, keyword");
							ResponseEntity<Object> psp_sorted =  pr_service.listAll1(pageable, keyword);//,column,value);
							return psp_sorted;
					}else {
							Pageable pageable1 = PageRequest.of(page, page_size,sorted);
							System.out.println("page, page_size, sorted");
							ResponseEntity<Object> fee_admission_sub_category_pageable = pr_service.listAll2(pageable1);
							return fee_admission_sub_category_pageable;
					}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}			
		//return pr_service.listAll1();
	}

	
	@GetMapping("/ProgramSpecilization/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
	    	
						ProgramSpecilization psp = pr_service.get(id);
						ResponseEntity<Object> psp_by_id_response= ResponseHandler.generateResponse(true, HttpStatus.OK, psp);
						return psp_by_id_response;
	        
				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@PutMapping("/ProgramSpecilization/{id}")
	public ResponseEntity<Object> update(@RequestBody ProgramSpecilization p, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
					//ProgramSpecilization existProduct = pr_service.get(id);
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					p.setModified_by(jwtDetails.getUserId());
					p.setModified_username(jwtDetails.getUserName());
					pr_service.save_ProgramSpecilization1(p);
					//return new ResponseEntity<>(HttpStatus.OK);
					ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
					return response;
				} catch (NoSuchElementException e) {
					//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
					ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
					return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
	@DeleteMapping("/ProgramSpecilization/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				pr_service.delete(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@DeleteMapping("/activateProgramSpecilization/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				pr_service.delete1(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/FetchProgramSpecialization/{school_id}/{program_id}")      //(Behalf of schoolid and programid)
	public ResponseEntity<Object> fetch(@PathVariable Integer school_id, @PathVariable Integer program_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
					List<ProgramSpecilization> school = (List<ProgramSpecilization>) pr_service.findById(school_id,program_id);
					ResponseEntity<Object> list_psp_on_school_id_and_program_id_response = ResponseHandler.generateResponse(true, HttpStatus.OK, school);
					return list_psp_on_school_id_and_program_id_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}			
	}

	@GetMapping("/ProgramSpecializationCount/{school_id}")      //no of count behalf of school_id
	public ResponseEntity<Object> countRecords(@PathVariable("school_id") Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {	
				Integer count = pr_service.countRecords(id);
				ResponseEntity<Object> psp_count_response = ResponseHandler.generateResponse(true, HttpStatus.OK, count);
				return psp_count_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/ProgramSpecializationCount1/{school_id}/{dept_id}/{program_id}/{auid_format}")
	public ResponseEntity<Object> getProgramSpecilization(@PathVariable Integer school_id,@PathVariable Integer dept_id,
			@PathVariable Integer program_id,@PathVariable String auid_format) {
		if(RateLimitController.bucket.tryConsume(1)) {		
				Integer  count_psp = pr_service.getProgramSpecilization1(school_id, dept_id, program_id, auid_format);
				ResponseEntity<Object> psp_count_response1 = ResponseHandler.generateResponse(true, HttpStatus.OK, count_psp);
				return psp_count_response1;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/ps/{ps_id}")
	public ResponseEntity<Object> getProgramAuid(@PathVariable Integer ps_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				String auid = pr_service.getProgramAuid(ps_id);
				ResponseEntity<Object> psp_auid_response = ResponseHandler.generateResponse(true, HttpStatus.OK, auid);
				return psp_auid_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@GetMapping("/FetchAllProgramSpecializationCourseDetail/{school_id}/{program_id}/{dept_id}")
	public ResponseEntity<Object> fetchAllCouseDetails(@PathVariable Integer school_id,
			@PathVariable Integer program_id, @PathVariable Integer dept_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<ProgramSpecilization> list_psp_on_id = pr_service.getAllCourseDetail(school_id, program_id, dept_id);
				ResponseEntity<Object> list_psp_on_id_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_psp_on_id);
				return list_psp_on_id_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
}
