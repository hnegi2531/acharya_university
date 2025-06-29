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
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.au.dto.PreadmissionDto;
import com.au.dto.ScholarshipStudentidUpdate;
import com.au.model.PreAdmissionProcess;
import com.au.model.Scholarship;
import com.au.model.ScholarshipApprovalStatus;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.ScholarshipService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class ScholarshipController {

	@Autowired
	private ScholarshipService s_service;

	Logger log = LoggerFactory.getLogger(ScholarshipController.class);
	
	@Autowired
	private JwtTokenService jwt_service;
	
	//@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/Scholarship")
	public ResponseEntity<Object> saveAcademicYear(@RequestBody @Valid Scholarship p,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				p.setCreated_by(jwtDetails.getUserId());
				p.setCreated_username(jwtDetails.getUserName());
				Scholarship scholarship = s_service.saveScholarship(p);
				ResponseEntity<Object> scholarship_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, scholarship);
				return scholarship_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/Scholarship")
	public ResponseEntity<Object> listAll(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> scholarship_sorted = s_service.listAll1(pageable, keyword);//,column,value);
						return scholarship_sorted;
				}else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> scholarship_pageable = s_service.listAll2(pageable1);
						return  scholarship_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		//return s_service.listAll();
	}

	
	@GetMapping("/Scholarship/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
	    	
					Scholarship product = s_service.get(id);
					ResponseEntity<Object> program_type_response_by_id= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
					return program_type_response_by_id;
	        
			} catch (NoSuchElementException e) {
					ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
					return response1;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

	@PutMapping("/Scholarship/{id}")
	public ResponseEntity<Object> update(@RequestBody Scholarship p, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
						Scholarship existProduct = s_service.get(id);
						JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

						p.setModified_by(jwtDetails.getUserId());
						p.setModified_username(jwtDetails.getUserName());
			
						s_service.saveScholarship(p);
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
	
	
	@DeleteMapping("/Scholarship/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				s_service.delete(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@DeleteMapping("/deactivateScholarship/{id}")
	public ResponseEntity<Object> deactivateScholarship(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				s_service.deactivateScholarship(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@DeleteMapping("/activateScholarship/{id}")
	public ResponseEntity<Object> activateScholarship(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				s_service.activateScholarship(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}

	//verify scholarship api
	@GetMapping("/fetchScholarship2/{sid}")
	public ResponseEntity<Object> get1(@PathVariable Integer sid) {
		if(RateLimitController.bucket.tryConsume(1)) {
//	    		Scholarship product = s_service.get1(cid);
				List<HashMap<String, Object>> scholarship_by_candidate_id =  s_service.get1(sid);
				ResponseEntity<Object> scholarship_by_candidate_id_response= ResponseHandler.generateResponse(true, HttpStatus.OK, scholarship_by_candidate_id);
				return scholarship_by_candidate_id_response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	//scholarship index api
	@GetMapping("/fetchScholarship1")
	public ResponseEntity<Object> get2() {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<HashMap<String, Object>>  scholarship_approval_status_data = s_service.fetchScholarship();
				ResponseEntity<Object> scholarship_approval_status_data_response= ResponseHandler.generateResponse(true, HttpStatus.OK, scholarship_approval_status_data);
				return scholarship_approval_status_data_response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	//scholarhip approval screen api
//	@GetMapping("/fetchScholarship3")
//	public ResponseEntity<Object> get3( ) {
//		if(RateLimitController.bucket.tryConsume(1)) {
//				List<HashMap<String, Object>> scholarship_status_data = s_service.get2();
//				ResponseEntity<Object> scholarship_status_data_response= ResponseHandler.generateResponse(true, HttpStatus.OK, scholarship_status_data );
//				return scholarship_status_data_response;
//		}else {	
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}		
//	}
	
	@GetMapping("/fetchScholarship3")
	public ResponseEntity<Object> indexForVerification(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> scholarship_sorted = s_service.listAllApprovedData(pageable, keyword);//,column,value);
						return scholarship_sorted;
				}else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> scholarship_pageable = s_service.listAllApprovedData1(pageable1);
						return  scholarship_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		//return s_service.listAll();
	}
	
	@GetMapping("/fetchScholarship4")
	public ResponseEntity<Object> indexAfterVerification(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> scholarship_sorted = s_service.listAllApprovedData4(pageable, keyword);//,column,value);
						return scholarship_sorted;
				}else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> scholarship_pageable = s_service.listAllApprovedData14(pageable1);
						return  scholarship_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		//return s_service.listAll();
	}

	@PutMapping("/updateScholarshipStatus3/{candidate_id}")
	public ResponseEntity<Object> update2(@RequestBody @Valid ScholarshipStudentidUpdate s1,
			@PathVariable Integer candidate_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Scholarship scholarship_update = s_service.update1(s1);
				ResponseEntity<Object> scholarship_update_response= ResponseHandler.generateResponse(true, HttpStatus.OK, scholarship_update );
				return scholarship_update_response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@PostMapping("/saveDirectScholarship")
	public ResponseEntity<Object> saveDirectScholarship(@RequestBody @Valid PreadmissionDto p,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				//p.setCreated_by(jwtDetails.getUserId());
				Scholarship s = p.getS();
				s.setCreated_by(jwtDetails.getUserId());
				s.setCreated_username(jwtDetails.getUserName());
				p.getS();
				ScholarshipApprovalStatus scholarship = s_service.saveDirectScholarship(p,jwtToken);
				ResponseEntity<Object> scholarship_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, scholarship);
				return scholarship_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/checkForScholarshipAlreadyPresentOrNot/{student_id}")
	public ResponseEntity<Object> checkForScholarshipAlreadyPresentOrNot(@PathVariable Integer student_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				HttpStatus  scholarship_approval_status_data = s_service.checkForScholarshipAlreadyPresentOrNot(student_id);
				ResponseEntity<Object> scholarship_approval_status_data_response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true,scholarship_approval_status_data);
				return scholarship_approval_status_data_response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	
	@GetMapping("/fetchScholarshipDetailsForVerified")
	public ResponseEntity<Object> fetchScholarshipDetailsForVerified(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword, @RequestParam(value="ac_year_id") Integer ac_year_id){
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> scholarship_sorted = s_service.listAllApprovedData1(pageable, keyword, ac_year_id);//,column,value);
						return scholarship_sorted;
				}else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> scholarship_pageable = s_service.listAllApprovedData11(pageable1, ac_year_id);
						return  scholarship_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		//return s_service.listAll();
	}	
}
