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
import com.au.model.ClassFeedbackQuestions;
import com.au.response.ResponseHandler;
import com.au.service.ClassFeedbackQuestionsService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class ClassFeedbackQuestionsController {

Logger log = LoggerFactory.getLogger(StudentBonafideController.class);
	
	@Autowired
	private ClassFeedbackQuestionsService feedback_que_Service;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	
	@PostMapping("/classFeedbackQuestions")
	public ResponseEntity<Object> save_clsfeedbackque(@RequestBody @Valid ClassFeedbackQuestions feedbackque,@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		feedbackque.setCreated_by(jwtDetails.getUserId());
		feedbackque.setCreated_username(jwtDetails.getUserName());
		
		ClassFeedbackQuestions clsfeedbackque = feedback_que_Service.save_clsfeedbackque(feedbackque);
		
		ResponseEntity<Object> clsfeedbackque_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, clsfeedbackque);
		return clsfeedbackque_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}
	}
	
	@GetMapping("/classFeedbackQuestions")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<ClassFeedbackQuestions> clsfeedbackque = feedback_que_Service.listAll1();
		ResponseEntity<Object> clsfeedbackque_response= ResponseHandler.generateResponse(true, HttpStatus.OK, clsfeedbackque);
		return clsfeedbackque_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	
	@GetMapping("/fetchAllClassFeedbackQuestions")
	public ResponseEntity<Object> getAllClassFeedbackQuestions(@RequestParam(value="page") Integer page,
			@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> classFeedbackQuestions_filtered =  feedback_que_Service.getAllDataFilteredByKeyword(pageable, keyword);
			return classFeedbackQuestions_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> classFeedbackQuestions_sorted = feedback_que_Service.getAllSortedData(pageable1);
			return classFeedbackQuestions_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/classFeedbackQuestions/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	ClassFeedbackQuestions feedbackque = feedback_que_Service.get(id);
	    	ResponseEntity<Object> feedbackque_response= ResponseHandler.generateResponse(true, HttpStatus.OK, feedbackque);
			return feedbackque_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/classFeedbackQuestions/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid ClassFeedbackQuestions feedbackque, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    	feedbackque.setModified_by(jwtDetails.getUserId());
	    	feedbackque.setModified_username(jwtDetails.getUserName());
	    	
	    	feedback_que_Service.saveClassFeedbackQuestions(feedbackque);
	    	 
	    	ResponseEntity<Object> stdbonafide_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return stdbonafide_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	
	@DeleteMapping("/classFeedbackQuestions/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			feedback_que_Service.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateClassFeedbackQuestions/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			feedback_que_Service.delete1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getClassFeedbackQuestions/{school_id}")
	public ResponseEntity<Object> getClassFeedbackQuestions(@PathVariable Integer school_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<ClassFeedbackQuestions> clsfeedbackque = feedback_que_Service.getClassFeedbackQuestions(school_id);
		ResponseEntity<Object> clsfeedbackque_response= ResponseHandler.generateResponse(true, HttpStatus.OK, clsfeedbackque);
		return clsfeedbackque_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
}
