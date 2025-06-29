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
import com.au.model.AdministrationEmailIds;
import com.au.response.ResponseHandler;
import com.au.service.AdministrationEmailIdsService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class AdministrationEmailIdsController {


Logger log = LoggerFactory.getLogger(CourseObjectiveController.class);
	
	@Autowired
	private AdministrationEmailIdsService adEmailIds_service;

	@Autowired
	private JwtTokenService jwt_service;
	
	
	@PostMapping("/administrationEmailIds")
	public ResponseEntity<Object> saveAdministrationEmailIds(@RequestBody @Valid List<AdministrationEmailIds> ademail,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			ademail.stream().forEach(adEmailIds -> {
				adEmailIds.setCreated_by(jwtDetails.getUserId());
				adEmailIds.setCreated_username(jwtDetails.getUserName());
			});
			List<AdministrationEmailIds> administration = adEmailIds_service.saveAdministrationEmailIds(ademail);
			ResponseEntity<Object> administration_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, administration);
		return administration_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/administrationEmailIds")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<AdministrationEmailIds> administration = adEmailIds_service.listAll1();
		ResponseEntity<Object>administration_response= ResponseHandler.generateResponse(true, HttpStatus.OK, administration);
		return administration_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/administrationEmailIds/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	AdministrationEmailIds administration = adEmailIds_service.get(id);
	    	ResponseEntity<Object> administration_response= ResponseHandler.generateResponse(true, HttpStatus.OK, administration);
			return administration_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/updateAdministrationEmailIds/{id}")
	public ResponseEntity<Object> updateAdministrationEmailIds(@RequestBody @Valid AdministrationEmailIds ademail,
			@PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    	ademail.setModified_by(jwtDetails.getUserId());
	    	ademail.setModified_username(jwtDetails.getUserName());
	    	adEmailIds_service.updateAdministrationEmailIds(ademail);
	    	ResponseEntity<Object> ademail_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return ademail_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	@DeleteMapping("/deActivateAdministrationEmailIds/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			adEmailIds_service.deactivate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@DeleteMapping("/activateAdministrationEmailIds/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			adEmailIds_service.activate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	@GetMapping("/fetchAllAdministrationEmailIdsDetail")
	public ResponseEntity<Object> fetchAllAdministrationEmailIdsDetail(@RequestParam(value="page") Integer page,
			@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> oc_filtered =  adEmailIds_service.getAllDataFilteredByKeyword(pageable, keyword);
			return oc_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> oc_sorted = adEmailIds_service.getAllSortedData(pageable1);
			return oc_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}		
}
