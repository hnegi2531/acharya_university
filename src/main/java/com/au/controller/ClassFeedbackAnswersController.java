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

import com.au.dto.ClassFeedbackAnswersDTO;
import com.au.dto.JwtDetails;
import com.au.model.ClassFeedbackAnswers;
import com.au.model.StudentClassFeedback;
import com.au.response.ResponseHandler;
import com.au.service.ClassFeedbackAnswersService;
import com.au.service.JwtTokenService;
import com.au.service.StudentClassFeedbackService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@CrossOrigin
@RequestMapping("/api/${secretkey1}")
public class ClassFeedbackAnswersController {
	
	Logger log = LoggerFactory.getLogger(DepartmentController.class);
	
	@Autowired
	private ClassFeedbackAnswersService cfa_ser;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/classFeedbackAnswers")
	public ResponseEntity<Object> saveClassFeedbackAnswers(@RequestBody @Valid ClassFeedbackAnswersDTO cfa,@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

			cfa.setCreated_by(jwtDetails.getUserId());
			cfa.setCreated_username(jwtDetails.getUserName());
	
		List<ClassFeedbackAnswers> feedback = cfa_ser.saveClassFeedbackAnswers(cfa);
		ResponseEntity<Object> feedback_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, feedback);
		return feedback_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}
	}
	
	
	@GetMapping("/classFeedbackAnswers")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<ClassFeedbackAnswers> feedback = cfa_ser.listAllFeedback();
		ResponseEntity<Object> feedback_response= ResponseHandler.generateResponse(true, HttpStatus.OK, feedback);
		return feedback_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/classFeedbackAnswers/{class_feedback_answers_id}")
	public ResponseEntity<Object> getClassFeedbackAnswers(@PathVariable Integer class_feedback_answers_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	ClassFeedbackAnswers feedback = cfa_ser.getClassFeedbackAnswers(class_feedback_answers_id);
	    	ResponseEntity<Object> feedback_response= ResponseHandler.generateResponse(true, HttpStatus.OK, feedback);
			return feedback_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/updateClassFeedbackAnswers/{class_feedback_answers_id}")
	public ResponseEntity<Object> updateClassFeedbackAnswers(@RequestBody @Valid ClassFeedbackAnswers cfa, @PathVariable Integer class_feedback_answers_id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	//Department existProduct = deptService.get(id);
	    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    	cfa.setModified_by(jwtDetails.getUserId());
	    	cfa.setModified_username(jwtDetails.getUserName());
	    	cfa_ser.updateClassFeedbackAnswers(cfa);
	    	ResponseEntity<Object> scf_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return scf_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	@DeleteMapping("/deactivateClassFeedbackAnswers/{class_feedback_answers_id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer class_feedback_answers_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			cfa_ser.delete(class_feedback_answers_id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateClassFeedbackAnswers/{class_feedback_answers_id}")
	public ResponseEntity<Object> activate(@PathVariable Integer class_feedback_answers_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			cfa_ser.delete1(class_feedback_answers_id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getClassFeedbackAnswersDetailsData")
	public ResponseEntity<Object> getClassFeedbackAnswersDetailsData() {
	    if (RateLimitController.bucket.tryConsume(1)) {
	        List<Map<String, Object>> responseData = cfa_ser.getClassFeedbackAnswersDetailsData();
	        return ResponseHandler.generateResponse(true, HttpStatus.OK, responseData);
	    } else {
	        return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
	    }
	}
	
	@GetMapping("/classFeedbackAnswersEmployeeDetails")
	public ResponseEntity<Object> fetchAllExternalStudentMarksDetail(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value = "ac_year_id", required = false) Integer ac_year_id,
			@RequestParam(value = "school_id", required = false) Integer school_id,
			@RequestParam(value = "dept_id", required = false) Integer dept_id,
			@RequestParam(value = "emp_id", required = false) Integer emp_id,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> student_marks_sorted = cfa_ser.classFeedbackAnswersEmployeeDetailsKeyword(pageable,ac_year_id,school_id,dept_id,emp_id, keyword);//,column,value);
					return student_marks_sorted;
				}else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> student_marks_pageable = cfa_ser.classFeedbackAnswersEmployeeDetailsWithoutKeyword(pageable1,ac_year_id,school_id,dept_id,emp_id);
					return student_marks_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/getFeedbackRatingReport/{employeeId}")
	public ResponseEntity<Object> getFeedbackRatingReport(@PathVariable Integer employeeId) {
	    if (RateLimitController.bucket.tryConsume(1)) {
	        Map<String, Object> responseData = cfa_ser.getFeedbackRatingReport(employeeId);
	        return ResponseHandler.generateResponse(true, HttpStatus.OK, responseData);
	    } else {
	        return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
	    }
	}
	
	
	@GetMapping("/getFeedbackRatingReportSectionWise/{employeeId}/{course_id}")
	public ResponseEntity<Object> getFeedbackRatingReportSectionWise(@PathVariable Integer employeeId,@PathVariable Integer course_id) {
	    if (RateLimitController.bucket.tryConsume(1)) {
	        Map<String, Object> responseData = cfa_ser.getFeedbackRatingReportSectionWise(employeeId,course_id);
	        return ResponseHandler.generateResponse(true, HttpStatus.OK, responseData);
	    } else {
	        return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
	    }
	}
	
	@GetMapping("/getCourseDetailsDataFromFeedBack/{employeeId}")
	public ResponseEntity<Object> getCourseDetailsDataFromFeedBack(@PathVariable Integer employeeId) {
	    if (RateLimitController.bucket.tryConsume(1)) {
	    	List<Map<String, Object>> responseData = cfa_ser.getCourseDetailsDataFromFeedBack(employeeId);
	        return ResponseHandler.generateResponse(true, HttpStatus.OK, responseData);
	    } else {
	        return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
	    }
	}
	
	@GetMapping("/getFeedbackRatingReportForEmployee")
	public ResponseEntity<Object> getFeedbackRatingReportForEmployee(@RequestParam(value = "employee_id", required = false) Integer employeeId,
			@RequestParam(value = "course_id", required = false) Integer course_id) {
	    if (RateLimitController.bucket.tryConsume(1)) {
	        Map<String, Object> responseData = cfa_ser.getFeedbackRatingReportForEmployee(employeeId,course_id);
	        return ResponseHandler.generateResponse(true, HttpStatus.OK, responseData);
	    } else {
	        return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
	    }
	}

}
