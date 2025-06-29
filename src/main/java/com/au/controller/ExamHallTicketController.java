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
import com.au.model.ExamHallTicket;
import com.au.response.ResponseHandler;
import com.au.service.ExamHallTicketService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class ExamHallTicketController {

	
	Logger log = LoggerFactory.getLogger(ExamHallTicketController.class);
	
 	@Autowired
    private ExamHallTicketService exam_ht_Service;

    @Autowired
    private JwtTokenService jwt_service;
    
    
	@PostMapping("/examHallTicket")
	public ResponseEntity<Object> saveExamHallTicket(@RequestBody @Valid ExamHallTicket eht,@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		eht.setCreated_by(jwtDetails.getUserId());
		eht.setCreated_username(jwtDetails.getUserName());
		ExamHallTicket examhallticket = exam_ht_Service.saveExamHallTicket(eht);
		ResponseEntity<Object> exam_ht_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, examhallticket);
		return exam_ht_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}
	}
    
	
	@GetMapping("/examHallTicket")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<ExamHallTicket> eht = exam_ht_Service.listAll1();
		ResponseEntity<Object> exam_details_response= ResponseHandler.generateResponse(true, HttpStatus.OK, eht);
		return exam_details_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/examHallTicket/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	ExamHallTicket eht = exam_ht_Service.get(id);
	    	ResponseEntity<Object> exam_ht_response= ResponseHandler.generateResponse(true, HttpStatus.OK, eht);
			return exam_ht_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/fetchAllExamHallTicket")
	public ResponseEntity<Object> getAllExamHallTicket(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> exam_ht_filtered =  exam_ht_Service.getAllDataFilteredByKeyword(pageable, keyword);
			return exam_ht_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> exam_ht_sorted = exam_ht_Service.getAllSortedData(pageable1);
			return exam_ht_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/examHallTicket/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid ExamHallTicket eht, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    	eht.setModified_by(jwtDetails.getUserId());
	    	eht.setModified_username(jwtDetails.getUserName());
	    	exam_ht_Service.updateExamHallTicket(eht);
	    	ResponseEntity<Object> exam_ht_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return exam_ht_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	

	@DeleteMapping("/examHallTicket/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			exam_ht_Service.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateExamHallTicket/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			exam_ht_Service.delete1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/getCandidateNameConcatWithApplicationNumber")
	public ResponseEntity<Object> getCandidateNameConcatWithApplicationNumber() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String,Object>> eht = exam_ht_Service.getCandidateNameConcatWithApplicationNumber();
		ResponseEntity<Object> employee_details_response= ResponseHandler.generateResponse(true, HttpStatus.OK, eht);
		return employee_details_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/getAdmitCardDetail")
	public ResponseEntity<Object> getAdmitCardDetail(@RequestParam(value="application_number") String application_number) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String,Object>> course_list = exam_ht_Service.getAdmitCardDetail(application_number);
			ResponseEntity<Object> employee_details_response = ResponseHandler.generateResponse(true, HttpStatus.OK, course_list);
			return employee_details_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
}
