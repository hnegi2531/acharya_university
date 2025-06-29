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
import com.au.model.Designation;
import com.au.response.ResponseHandler;
import com.au.service.DesignationService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class DesignationController {
	
	Logger log = LoggerFactory.getLogger(DesignationController.class);

	@Autowired
	private DesignationService ds_service;
	
	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/Designation")
	public ResponseEntity<Object> saveDesignation(@RequestBody @Valid Designation bs,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails=jwt_service.callJwtToken(jwtToken);
			bs.setCreated_by(jwtDetails.getUserId());
			bs.setCreated_username(jwtDetails.getUserName());
			Designation des = ds_service.saveDesignation(bs);
			ResponseEntity<Object> des_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, des);
			return des_response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
	}

	@GetMapping("/Designation")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Designation> des = ds_service.listAll();
			ResponseEntity<Object> des_response= ResponseHandler.generateResponse(true, HttpStatus.OK, des);
			return des_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllDesignationDetail")
	public ResponseEntity<Object> getAllDesignationDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> des_filtered =  ds_service.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
			return des_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> des_sorted = ds_service.getAllSortedData(pageable1);
			return des_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/Designation/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {

			Designation des = ds_service.get(id);
			ResponseEntity<Object> des_response= ResponseHandler.generateResponse(true, HttpStatus.OK, des);
			return des_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/Designation/{id}")
	public ResponseEntity<Object> update(@RequestBody Designation bs, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken) throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails=jwt_service.callJwtToken(jwtToken);
				bs.setModified_by(jwtDetails.getUserId());
				bs.setModified_username(jwtDetails.getUserName());
				ds_service.saveDesignation1(bs);
				ResponseEntity<Object> des_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return des_response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/Designation/{designation_id}")
	public ResponseEntity<Object> delete(@PathVariable Integer designation_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		ds_service.delete(designation_id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@DeleteMapping("/activateDesignation/{designation_id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer designation_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		ds_service.delete1(designation_id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
}
