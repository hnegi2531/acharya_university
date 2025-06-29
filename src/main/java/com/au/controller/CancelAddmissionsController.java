package com.au.controller;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.au.dto.CancelAdmissionsDto;
import com.au.dto.JwtDetails;
import com.au.model.CancelAddmissions;
import com.au.response.ResponseHandler;
import com.au.service.CancelAdmissionsService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class CancelAddmissionsController {

Logger log = LoggerFactory.getLogger(CancelAddmissionsController.class);
	
	@Autowired
	private CancelAdmissionsService service;
	
	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping(value = "/cancelAdmissions")
	public ResponseEntity<Object> cancelAdmissions(@RequestBody CancelAdmissionsDto request,
			@RequestHeader("Authorization") String jwtToken){
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				return service.cancelAdmissions(request, jwtToken);
				
			}catch(Exception e) {
				throw new RuntimeException(""+ e.getMessage());
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@PostMapping(value = "/cancelAdmissionsUploadFile")
	public ResponseEntity<Object> uploadFile(@RequestParam MultipartFile multipartFile,
			@RequestParam Integer cancel_id) throws IOException {
		service.uploadFile(multipartFile, cancel_id);
		ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}
	
	@GetMapping(path = "/cancelAdmissionsFileviews")
	public ResponseEntity<ByteArrayResource> viewFiles(@RequestParam("fileName") final String fileName) {
		try {
			final byte[] data = service.viewFiles(fileName);
			final ByteArrayResource resource = new ByteArrayResource(data);
			return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/pdf")
					.header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
					.header("Cache-Control", "no-cache").body(resource);
		} catch (NoSuchFileException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().contentLength(0).body(null);
		}
	}

	
	
	@PutMapping("/updateCancelAdmissions/{cancel_id}")
	public ResponseEntity<Object> confirmCancelAdmission(@RequestBody @Valid CancelAddmissions ca,@PathVariable Integer cancel_id,
			@RequestHeader("Authorization") String jwtToken){
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		    	ca.setModified_by(jwtDetails.getUserId());
		    	ca.setModified_username(jwtDetails.getUserName());
				CancelAddmissions response = service.confirmCancelAdmissions(ca);
				ResponseEntity<Object> sd_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, response);
				return sd_response;
			}catch(Exception e) {
				throw new RuntimeException(""+ e.getMessage());
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/updateApprovedByAndDate/{cancel_id}")
	public ResponseEntity<?> update(@RequestBody @Valid CancelAddmissions ca, @PathVariable Integer cancel_id,@RequestHeader("Authorization")  String jwtToken)
	      throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
					//Bank existProduct = bank_service.get(id);
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					ca.setModified_by(jwtDetails.getUserId());
					ca.setModified_username(jwtDetails.getUserName());
					service.updateApprovedByAndDate(ca);
					System.out.println("dddddddddddddddddddddddd ");
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
	@GetMapping("/fetchAllCancelAdmissionsDetail")
	public ResponseEntity<Object> fetchAllCancelAdmissionsDetail(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> cors_stu_assign_sorted = service.listAll1(pageable, keyword);//,column,value);
					return cors_stu_assign_sorted;
				}else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> cors_stu_assign_pageable = service.listAll2(pageable1);
					return cors_stu_assign_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getCancelAdmission/{cancel_id}")
	public ResponseEntity<Object> get(@PathVariable Integer cancel_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
	    	
						CancelAddmissions product = service.get(cancel_id);
						ResponseEntity<Object> bank_list_response_by_id = ResponseHandler.generateResponse(true, HttpStatus.OK, product);
						return bank_list_response_by_id;
				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	//For Report
	@GetMapping("/fetchAllCancelAdmissionsReport")
	public ResponseEntity<Object> fetchAllCancelAdmissionsReport(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> cors_stu_assign_sorted = service.listAllReport(pageable, keyword);//,column,value);
					return cors_stu_assign_sorted;
				}else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> cors_stu_assign_pageable = service.listAllReport1(pageable1);
					return cors_stu_assign_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	
	@GetMapping("/studentDetailsInactiveIndex")
	public ResponseEntity<Object> getStudentIndex(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort,@RequestParam(value="ac_year_id") Integer ac_year_id,
			@RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> abc =  service.getStudentIndex2(pageable, keyword, ac_year_id);
					return abc;
			}
			else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> xyz = service.getStudentIndex(pageable1, ac_year_id );
					return xyz;
			}
	}else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return (ResponseEntity<Object>) rs;
	}		
}	
	
	
	@GetMapping("/Student_DetailsWithCancelAdmissionDetailsData/{student_id}")
	public ResponseEntity<Object> Student_DetailsWithCancelAdmissionDetailsData(@PathVariable Integer student_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> stu_details = service.Student_DetailsWithCancelAdmissionDetailsData(student_id);
			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					stu_details);
			return student_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
}
