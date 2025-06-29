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

import com.au.dto.HostelFeeTemplateDto;
import com.au.dto.JwtDetails;
import com.au.model.HostelFeeTemplate;
import com.au.response.ResponseHandler;
import com.au.service.HostelFeeTemplateService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
//@RequestMapping("/api")
public class HostelFeeTemplateController {

	@Autowired
	private HostelFeeTemplateService hostelFeeTemplateService;
	
	Logger log = LoggerFactory.getLogger(HostelFeeTemplateController.class);
	
	@Autowired
	private JwtTokenService jwt_service; 
	
	/*
	@PostMapping("/HostelFeeTemplate")
	public List<HostelFeeTemplate> saveSubjectType(@RequestBody @Valid HostelFeeTemplateRequest r,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		r.setCreatedBy(jwtDetails.getUserId());
		r.setCreatedUsername(jwtDetails.getUserName());
		return hostelFeeTemplateService.saveHostelFeeTemplate(r);
	}
	*/
	
	@PostMapping("/HostelFeeTemplate")
	public ResponseEntity<Object> saveSubjectType(@RequestBody @Valid HostelFeeTemplateDto hostel,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			hostel.getHft().forEach(feeTemplate -> {

				feeTemplate.setCreatedBy(jwtDetails.getUserId());		
				feeTemplate.setCreatedUsername(jwtDetails.getUserName());
			});
			List<HostelFeeTemplate> hostel_fee_template = hostelFeeTemplateService.saveHostelFeeTemplate2(hostel);
			return ResponseHandler.generateResponse(true, HttpStatus.CREATED,hostel_fee_template);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@GetMapping("/HostelFeeTemplate")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HostelFeeTemplate> hostel_fee_template_list = hostelFeeTemplateService.listAll();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, hostel_fee_template_list);
		}else {
			return  ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@GetMapping("/fetchAllHostelFeeTemplateDetails")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> hostel_fee_template_sorted = hostelFeeTemplateService.listAll1(pageable, keyword);//,column,value);
			return hostel_fee_template_sorted;
		}else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> hostel_fee_template_pageable = hostelFeeTemplateService.listAll2(pageable1);
			return hostel_fee_template_pageable;
		}
	}
	
	@GetMapping("/HostelFeeTemplate/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				List<HostelFeeTemplate> product = hostelFeeTemplateService.get(id);
				ResponseEntity<Object> hostel_fee_template_response_by_id = ResponseHandler.generateResponse(true,HttpStatus.OK, product);
				return hostel_fee_template_response_by_id;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/HostelFeeTemplate/{hostelFeeTemplateIds}")
	public ResponseEntity<Object> update(@RequestBody @Valid List<HostelFeeTemplate> hostelFeeTemplates, @PathVariable List<Integer> hostelFeeTemplateIds,
			@RequestHeader("Authorization") String jwtToken)
					throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				hostelFeeTemplates.stream().forEach(hft -> {
					if(hft.getHostel_fee_template_id() != null) {
						hft.setModifiedBy(jwtDetails.getUserId());
						hft.setModifiedUsername(jwtDetails.getUserName());
					}else {
						hft.setCreatedBy(jwtDetails.getUserId());
						hft.setCreatedUsername(jwtDetails.getUserName());
					}
				});
				hostelFeeTemplateService.saveHostelFeeTemplate1(hostelFeeTemplates);
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

	@DeleteMapping("/HostelFeeTemplate/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			hostelFeeTemplateService.delete(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateHostelFeeTemplate/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			hostelFeeTemplateService.delete1(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/HostelFeeTemplateIndex")
	public ResponseEntity<Object> hostemFeeTemplateIndex(){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String , Object>> hostel_fee_template = hostelFeeTemplateService.hostelFeeTemplateIndex();
			ResponseEntity<Object> hostel_fee_template_response = ResponseHandler.generateResponse(true,HttpStatus.OK, hostel_fee_template);
			return hostel_fee_template_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/HostelFeeTemplateDetails/{hostel_fee_template_id}")
	public ResponseEntity<Object> getHostelFeeTemplateDetails(@PathVariable Integer hostel_fee_template_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				List<HashMap<String , Object>> details = hostelFeeTemplateService.getHostelFeeTemplateDetails(hostel_fee_template_id);
				ResponseEntity<Object> hostel_fee_template_response_by_id = ResponseHandler.generateResponse(true,HttpStatus.OK, details);
				return hostel_fee_template_response_by_id;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/hostelFeeTemplateByAcademicYearAndSchool/{academicYearId}/{schoolId}")
	public ResponseEntity<Object> hostelFeeTemplateByAcademicYearAndSchool(@PathVariable Integer academicYearId,@PathVariable Integer  schoolId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				List<HostelFeeTemplate> hostelTemplateDetails = hostelFeeTemplateService.hostelFeeTemplateByAcademicYearAndSchool(academicYearId,schoolId);
				return ResponseHandler.generateResponse(true,HttpStatus.OK, hostelTemplateDetails);
			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
		}
	}
	
	@GetMapping("/countOfStudentBasedOnHostelFeeTemplateId/{hostel_fee_template_id}")
	public ResponseEntity<Object> countOfStudentBasedOnHostelFeeTemplateId(@PathVariable Integer hostel_fee_template_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				HashMap<String , Object> hostelTemplateDetails = hostelFeeTemplateService.countOfStudentBasedOnHostelFeeTemplateId(hostel_fee_template_id);
				return ResponseHandler.generateResponse(true,HttpStatus.OK, hostelTemplateDetails);
			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
		}
	}

	@GetMapping("/hostelFeeTemplateByAcademicYearSchoolTemplateId/{academicYearId}/{schoolId}/{hostelFeeTemplateId}")
	public ResponseEntity<Object> hostelFeeTemplateByAcademicYearSchoolTemplateId(@PathVariable Integer academicYearId,@PathVariable Integer  schoolId,@PathVariable Integer hostelFeeTemplateId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				List<HostelFeeTemplate> hostelTemplateDetails = hostelFeeTemplateService.hostelFeeTemplateByAcademicYearSchoolTemplateId(academicYearId,schoolId,hostelFeeTemplateId);
				return ResponseHandler.generateResponse(true,HttpStatus.OK, hostelTemplateDetails);
			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
		}
	}
}

