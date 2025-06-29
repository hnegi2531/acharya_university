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

import com.au.dto.ExternalMarksAssignmentDto;
import com.au.dto.JwtDetails;
import com.au.model.Budget;
import com.au.model.ExternalMarksAssignment;
import com.au.response.ResponseHandler;
import com.au.service.ExternalMarksAssignmentService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class ExternalMarksAssignmentController {

	Logger log = LoggerFactory.getLogger(ExternalMarksAssignmentController.class);
	
	@Autowired
	private ExternalMarksAssignmentService externalMarksAssignmentService;

	@Autowired
	private JwtTokenService jwt_service;
	
	
	@PostMapping("/saveExternalMarksAssignment")
	public ResponseEntity<Object> saveExternalMarksAssignment(@RequestBody @Valid ExternalMarksAssignmentDto marks,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			List<ExternalMarksAssignment> mark = externalMarksAssignmentService.saveExternalMarksAssignment(marks, jwtDetails);
			ResponseEntity<Object> conferences_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, mark);
		return conferences_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	
	@GetMapping("/getAllActiveExternalMarksAssignment")
	public ResponseEntity<Object> getAllActiveExternalMarksAssignment() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<ExternalMarksAssignment> mark = externalMarksAssignmentService.getAllActiveExternalMarksAssignment();
		ResponseEntity<Object> mark_response= ResponseHandler.generateResponse(true, HttpStatus.OK, mark);
		return mark_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/getExternalMarksAssignment/{id}")
	public ResponseEntity<Object> get(@PathVariable List<Integer> id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	List<ExternalMarksAssignment> externalMarksAssignment= externalMarksAssignmentService.get(id);
	    	ResponseEntity<Object> externalMarksAssignment_response= ResponseHandler.generateResponse(true, HttpStatus.OK, externalMarksAssignment);
			return externalMarksAssignment_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@PutMapping("/updateExternalMarksAssignment/{id}")
	public ResponseEntity<Object> updateExternalMarksAssignment(@RequestBody @Valid List<ExternalMarksAssignment> externalMarksAssignment,
			@PathVariable List<Integer> id, @RequestHeader("Authorization") String jwtToken) throws Exception{
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				externalMarksAssignment.stream().forEach(ema -> {

					ema.setCreated_by(jwtDetails.getUserId());
					ema.setCreated_username(jwtDetails.getUserName());
				});
				externalMarksAssignmentService.updateExternalMarksAssignment(externalMarksAssignment);
				return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			} catch (JsonParseException | JsonMappingException e) {
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			} catch (IOException e) {
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@DeleteMapping("/deActivateExternalMarksAssignment/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			externalMarksAssignmentService.deactivate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@DeleteMapping("/activateExternalMarksAssignment/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			externalMarksAssignmentService.activate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/fetchAllExternalMarksAssignment")
	public ResponseEntity<Object> fetchAllExternalMarksAssignment(@RequestParam(value="page") Integer page,
			@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> oc_filtered =  externalMarksAssignmentService.getAllDataFilteredByKeyword(pageable, keyword);
			return oc_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> oc_sorted = externalMarksAssignmentService.getAllSortedData(pageable1);
			return oc_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
}
