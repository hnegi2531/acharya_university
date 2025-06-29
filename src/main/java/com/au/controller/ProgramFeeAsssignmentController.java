package com.au.controller;


import java.io.IOException;
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
import com.au.model.ProgramFeeAsssignment;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.ProgramFeeAsssignmentService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class ProgramFeeAsssignmentController {

	
	Logger log = LoggerFactory.getLogger(ProgramFeeAsssignmentController.class);
	
	@Autowired
	private ProgramFeeAsssignmentService pfa_Service;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	
	@PostMapping("/programFeeAsssignment")
	public ResponseEntity<Object> saveProgramFeeAsssignment(@RequestBody @Valid ProgramFeeAsssignment pfa,@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			pfa.setCreated_by(jwtDetails.getUserId());
			pfa.setCreated_username(jwtDetails.getUserName());
			ProgramFeeAsssignment pfa1 = pfa_Service.saveProgramFeeAsssignment(pfa);
			ResponseEntity<Object> pfa1_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, pfa1);
		return pfa1_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
		
	}
	
	
	@GetMapping("/programFeeAsssignment")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<ProgramFeeAsssignment> pfa = pfa_Service.listAll1();
		ResponseEntity<Object> pfa_response= ResponseHandler.generateResponse(true, HttpStatus.OK, pfa);
		return pfa_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	
	@GetMapping("/fetchAllProgramFeeAsssignment")
	public ResponseEntity<Object> getAllProgramFeeAsssignment(@RequestParam(value="page") Integer page,
			@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> pfa_filtered =  pfa_Service.getAllDataFilteredByKeyword(pageable, keyword);
			return pfa_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> pfa_sorted = pfa_Service.getAllSortedData(pageable1);
			return pfa_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/programFeeAsssignment/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {

			ProgramFeeAsssignment pfa = pfa_Service.get(id);
			ResponseEntity<Object> pfa_response= ResponseHandler.generateResponse(true, HttpStatus.OK, pfa);
			return pfa_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	
	@PutMapping("/programFeeAsssignment/{id}")
	public ResponseEntity<Object> update(@RequestBody ProgramFeeAsssignment pfa, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			pfa.setModified_by(jwtDetails.getUserId());
			pfa.setModified_username(jwtDetails.getUserName());
			ProgramFeeAsssignment pfa1 = pfa_Service.updateProgramFeeAssignment(pfa);
			ResponseEntity<Object> pfa_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return pfa_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/programFeeAsssignment/{id}")
	public ResponseEntity<Object> deactivateRole(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			pfa_Service.deactivate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateProgramFeeAsssignment/{id}")
	public ResponseEntity<Object> activateRole(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			pfa_Service.activate(id);
		 ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		 return response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/getProgramConcat")
	public ResponseEntity<Object> getProgramConcat() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String,Object>> cos = pfa_Service.getProgramConcat();
		ResponseEntity<Object> co_response= ResponseHandler.generateResponse(true, HttpStatus.OK, cos);
		return co_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
}

