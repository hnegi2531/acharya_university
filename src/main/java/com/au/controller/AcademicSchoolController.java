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
import com.au.dto.AcademicSchoolRequest;
import com.au.dto.JwtDetails;
import com.au.model.academic_school;
import com.au.response.ResponseHandler;
import com.au.service.AcademicSchoolService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class AcademicSchoolController {

	@Autowired
	private AcademicSchoolService as_service;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	Logger log = LoggerFactory.getLogger(AcademicSchoolController.class);
	
	@PostMapping("/academicschool")
	public ResponseEntity<Object> saveacademic_school(@RequestBody @Valid AcademicSchoolRequest bs,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				bs.setCreated_by(jwtDetails.getUserId());
				bs.setCreated_username(jwtDetails.getUserName());
				List<academic_school> list_academic_school = as_service.getAcaSchool(bs);
				ResponseEntity<Object> list_academic_school_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, list_academic_school);
				return list_academic_school_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@GetMapping("/academicschool")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<academic_school> list_academic_school1 = as_service.listAll();
				ResponseEntity<Object> list_academic_school_response1= ResponseHandler.generateResponse(true, HttpStatus.OK, list_academic_school1);
				return list_academic_school_response1;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/fetchAcademicschoolDetail")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> academic_school_details = as_service.listAll1(pageable, keyword);//,column,value);
						return academic_school_details;
				}else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> academic_program_detail1 = as_service.listAll2(pageable1);
						return academic_program_detail1;
			}
			
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

	@GetMapping("/academicschool/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
					try {

							academic_school product = as_service.get(id);
							ResponseEntity<Object> academic_school_response= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
							return academic_school_response;

					} catch (NoSuchElementException e) {
							ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
							return response1;
					}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@PutMapping("/academicschool/{id}")
	public ResponseEntity<Object> update(@RequestBody academic_school bs, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {

				try {
						academic_school existProduct = as_service.get(id);
						JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
						bs.setModified_by(jwtDetails.getUserId());
						bs.setModified_username(jwtDetails.getUserName());
						as_service.save_academic_school(bs);
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

	@DeleteMapping("/academicschool/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				return as_service.delete(id);
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@DeleteMapping("/activateAcademicschool/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				return as_service.delete1(id);
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
}
