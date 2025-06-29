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
import com.au.model.ExamDetails;
import com.au.response.ResponseHandler;
import com.au.service.ExamDetailsService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class ExamDetailsController {

    Logger log = LoggerFactory.getLogger(ExamDetailsController.class);
	
     	@Autowired
	    private ExamDetailsService exam_details_Service;
	
	    @Autowired
	    private JwtTokenService jwt_service;
	
	    
	    
		@PostMapping("/examDetails")
		public ResponseEntity<Object> saveExamDetails(@RequestBody @Valid ExamDetails ed,@RequestHeader("Authorization") String jwtToken)
				throws Exception,JsonParseException, JsonMappingException, IOException {
			if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			ed.setCreated_by(jwtDetails.getUserId());
			ed.setCreated_username(jwtDetails.getUserName());
			ExamDetails examdetails = exam_details_Service.saveExamDetails(ed);
			ResponseEntity<Object> exam_details_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, examdetails);
			return exam_details_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
			}
		}
		
		
		
		@GetMapping("/examDetails")
		public ResponseEntity<Object> listAll() {
			if(RateLimitController.bucket.tryConsume(1)) {
				List<ExamDetails> ed = exam_details_Service.listAll1();
			ResponseEntity<Object> exam_details_response= ResponseHandler.generateResponse(true, HttpStatus.OK, ed);
			return exam_details_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
		}
		
		
		@GetMapping("/examDetails/{id}")
		public ResponseEntity<Object> get(@PathVariable Integer id) {
			if(RateLimitController.bucket.tryConsume(1)) {
		    try {
		    	
		    	ExamDetails ed = exam_details_Service.get(id);
		    	ResponseEntity<Object> exam_details_response= ResponseHandler.generateResponse(true, HttpStatus.OK, ed);
				return exam_details_response;
		    } catch (NoSuchElementException e) {
		    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		
		
		@GetMapping("/fetchAllExamDetails")
		public ResponseEntity<Object> getAllExamDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
				@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
			
			if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> exam_details_filtered =  exam_details_Service.getAllDataFilteredByKeyword(pageable, keyword);
				return exam_details_filtered;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> exam_details_sorted = exam_details_Service.getAllSortedData(pageable1);
				return exam_details_sorted;
			}
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		
		
		@PutMapping("/examDetails/{id}")
		public ResponseEntity<Object> update(@RequestBody @Valid ExamDetails ed, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
				throws JsonParseException, JsonMappingException, IOException {
			if(RateLimitController.bucket.tryConsume(1)) {
		    try {
		    	
		    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		    	ed.setModified_by(jwtDetails.getUserId());
		    	ed.setModified_username(jwtDetails.getUserName());
		    	exam_details_Service.updateExamDetails(ed);
		    	ResponseEntity<Object> exam_details_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return exam_details_response;
		    } catch (NoSuchElementException e) {
		    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			} 
		}
		
		
		@DeleteMapping("/examDetails/{id}")
		public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
			if(RateLimitController.bucket.tryConsume(1)) {
				exam_details_Service.delete(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}

		@DeleteMapping("/activateExamDetails/{id}")
		public ResponseEntity<Object> activate(@PathVariable Integer id) {
			if(RateLimitController.bucket.tryConsume(1)) {
				exam_details_Service.delete1(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		@GetMapping("/getExamCenterWithDate")
		public ResponseEntity<Object> getExamCenterWithDate() {
			if(RateLimitController.bucket.tryConsume(1)) {
				List<Map<String,Object>> eht = exam_details_Service.getExamCenterWithDate();
			ResponseEntity<Object> employee_details_response= ResponseHandler.generateResponse(true, HttpStatus.OK, eht);
			return employee_details_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
		}
		
		@GetMapping("/getExamDetailsForHallTicketGeneration")
		public ResponseEntity<Object> getExamDetailsForHallTicketGeneration() {
			if(RateLimitController.bucket.tryConsume(1)) {
				List<Map<String,Object>> exam_details_for_hall_ticket = exam_details_Service.getExamDetailsForHallTicketGeneration();
				ResponseEntity<Object> exam_details_for_hall_ticket_response = ResponseHandler.generateResponse(true, HttpStatus.OK, exam_details_for_hall_ticket);
				return exam_details_for_hall_ticket_response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
}