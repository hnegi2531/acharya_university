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

import com.au.dto.CourseAssignmentEmployeeDto;
import com.au.dto.JwtDetails;
import com.au.model.Course;
import com.au.model.CourseAssignmentEmployee;
import com.au.response.ResponseHandler;
import com.au.service.CourseAssignmentEmployeeService;
import com.au.service.CourseService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class CourseAssignmentEmployeeController {
		Logger log = LoggerFactory.getLogger(CourseAssignmentEmployeeController.class);
	
		@Autowired
		private CourseAssignmentEmployeeService courseAssignmentEmployeeService;
	
		@Autowired
		private JwtTokenService jwt_service;
		
		
	@PostMapping("/saveCourseAssignmentEmployee")
	public ResponseEntity<Object> saveCourseAssignmentEmployee(@RequestBody @Valid CourseAssignmentEmployeeDto courseEmployee, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException, Exception {
		 if (RateLimitController.bucket.tryConsume(1)) {
		        JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		        courseEmployee.setCreated_by(jwtDetails.getUserId());
		        courseEmployee.setCreated_username(jwtDetails.getUserName());

		        // Call the updated service method to save course assignments
		        List<CourseAssignmentEmployee> savedAssignments = courseAssignmentEmployeeService.saveCourseAssignmentEmployees(courseEmployee);

		        // Generate a response with the list of created assignments
		        ResponseEntity<Object> course_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, savedAssignments);
		        return course_response;
		    } else {
		        ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		        return rs;
		    }
	   } 
	
	
	
	@GetMapping("/getAllActiveCourseAssignmentEmployee")
	public ResponseEntity<Object> getAllActiveCourseAssignmentEmployee() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<CourseAssignmentEmployee> course_list = courseAssignmentEmployeeService.getAllActiveCourseAssignmentEmployee();
			ResponseEntity<Object> course_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, course_list);
			return course_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
	
	@GetMapping("/CourseAssignmentEmployee/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		try {
			CourseAssignmentEmployee product = courseAssignmentEmployeeService.get(id);
			ResponseEntity<Object> course_response_by_id = ResponseHandler.generateResponse(true, HttpStatus.OK, product);
			return course_response_by_id;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response1;
		}
	}

	@PutMapping("/updateCourseAssignmentEmployee/{id}")
	public ResponseEntity<?> update(@RequestBody @Valid CourseAssignmentEmployee course, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
					//Course existProduct = course_service.get(id);
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				course.setModified_by(jwtDetails.getUserId());
				course.setModified_username(jwtDetails.getUserName());
				courseAssignmentEmployeeService.saveCourse1(course);
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
	
	
	@DeleteMapping("/deactivateCourseAssignmentEmployee/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			courseAssignmentEmployeeService.deactivate(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateCourseAssignmentEmployee/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			courseAssignmentEmployeeService.activate(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/fetchAllCourseAssignmentEmployee")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort,@RequestParam(value="user_id") Integer user_id, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> course_sorted = courseAssignmentEmployeeService.listAll1(pageable, keyword, user_id);//,column,value);
					return course_sorted;
				}else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> course_pageable = courseAssignmentEmployeeService.listAll2(pageable1, user_id);
					return course_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	
	@GetMapping("/getCourseAssignmentEmployeeBasedOnUserId/{user_id}")
	public ResponseEntity<Object> getCourseAssignmentEmployeeBasedOnUserId(@PathVariable Integer user_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String,Object>> cos = courseAssignmentEmployeeService.getCourseAssignmentEmployeeBasedOnUserId(user_id);
		ResponseEntity<Object> co_response= ResponseHandler.generateResponse(true, HttpStatus.OK, cos);
		return co_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	
	@GetMapping("/getCountOfCourseBasedOnUserId/{user_id}")
	public ResponseEntity<Object> getCountOfCourseBasedOnUserId(@PathVariable Integer user_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Map<String,Object> cos = courseAssignmentEmployeeService.getCountOfCourseBasedOnUserId(user_id);
		ResponseEntity<Object> co_response= ResponseHandler.generateResponse(true, HttpStatus.OK, cos);
		return co_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
}
