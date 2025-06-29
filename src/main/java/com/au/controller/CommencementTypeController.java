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
import com.au.model.CommencementType;
import com.au.model.Department;
import com.au.response.ResponseHandler;
import com.au.service.CommencementTypeService;
import com.au.service.DepartmentService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class CommencementTypeController {

	Logger log = LoggerFactory.getLogger(CommencementTypeController.class);

	@Autowired
	private CommencementTypeService ct_Service;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	

	@PostMapping("/commencementType")
	public ResponseEntity<Object> saveCt(@RequestBody @Valid CommencementType ct,@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		ct.setCreated_by(jwtDetails.getUserId());
		ct.setCreated_username(jwtDetails.getUserName());
		CommencementType cts = ct_Service.saveCommencementType(ct);
		ResponseEntity<Object> ct_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, cts);
		return ct_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}
	}


	@GetMapping("/fetchAllCommencementTypeDetail")
	public ResponseEntity<Object> getAllCt(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> ct_filtered =  ct_Service.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
			return ct_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> ct_sorted = ct_Service.getAllSortedData(pageable1);
			return ct_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/commencementType")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<CommencementType> cts = ct_Service.listAll1();
		ResponseEntity<Object> ct_response= ResponseHandler.generateResponse(true, HttpStatus.OK, cts);
		return ct_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	
	@GetMapping("/commencementType/{commencement_id}")
	public ResponseEntity<Object> get(@PathVariable Integer commencement_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	CommencementType cts = ct_Service.get(commencement_id);
	    	ResponseEntity<Object> cts_response= ResponseHandler.generateResponse(true, HttpStatus.OK, cts);
			return cts_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	
	@PutMapping("/commencementType/{commencement_id}")
	public ResponseEntity<Object> update(@RequestBody @Valid CommencementType ct, @PathVariable Integer commencement_id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	
	    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    	ct.setModified_by(jwtDetails.getUserId());
	    	ct.setModified_username(jwtDetails.getUserName());
	    	ct_Service.saveCommencementTypes(ct);
	    	ResponseEntity<Object> cts_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return cts_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	@DeleteMapping("/commencementType/{commencement_id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer commencement_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			ct_Service.delete(commencement_id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateCommencementType/{commencement_id}")
	public ResponseEntity<Object> activate(@PathVariable Integer commencement_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			ct_Service.delete1(commencement_id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getCommencementTypeDetails")
	public ResponseEntity<Object> getCommencementTypeDetails() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> cts = ct_Service.getCommencementTypeDetails();
		ResponseEntity<Object> ct_response= ResponseHandler.generateResponse(true, HttpStatus.OK, cts);
		return ct_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
}
