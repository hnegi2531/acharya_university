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
import com.au.model.Course;
import com.au.response.ResponseHandler;
import com.au.service.CourseService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
//@RequestMapping("/api")
public class CourseController {

	Logger log = LoggerFactory.getLogger(CourseController.class);

	@Autowired
	private CourseService course_service;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/Course")
	public ResponseEntity<Object> saveCourse(@RequestBody @Valid Course course, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException, Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			course.setCreated_by(jwtDetails.getUserId());
			course.setCreated_username(jwtDetails.getUserName());
			Course co = course_service.saveCourse(course);
			ResponseEntity<Object> course_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, co);
			return course_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

	@GetMapping("/Course")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Course> course_list = course_service.listAll();
			ResponseEntity<Object> course_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, course_list);
			return course_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

	@GetMapping("/fetchAllCourseDetail")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> course_sorted = course_service.listAll1(pageable, keyword);//,column,value);
					return course_sorted;
				}else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> course_pageable = course_service.listAll2(pageable1);
					return course_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		//return course_service.listAll1();
	}

	@GetMapping("/Course/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		try {
			Course product = course_service.get(id);
			ResponseEntity<Object> course_response_by_id = ResponseHandler.generateResponse(true, HttpStatus.OK, product);
			return course_response_by_id;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response1;
		}
	}

	@PutMapping("/Course/{id}")
	public ResponseEntity<?> update(@RequestBody @Valid Course course, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
					//Course existProduct = course_service.get(id);
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				course.setModified_by(jwtDetails.getUserId());
				course.setModified_username(jwtDetails.getUserName());
				course_service.saveCourse1(course);
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

	@DeleteMapping("/Course/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			course_service.delete(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateCourse/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			course_service.delete1(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/completeCourses")
	public ResponseEntity<Object> allDetails(){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> all_details_by_course_code = course_service.details();
			ResponseEntity<Object> all_details_by_course_code_response = ResponseHandler.generateResponse(true, HttpStatus.OK, all_details_by_course_code);
			return all_details_by_course_code_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
	//Courses for Lesson Plan
	@GetMapping("/coursesForLessonPlan/{school_id}/{program_id}/{program_specialization_id}/{year_sem}")
	public ResponseEntity<Object> coursesForLessonPlan(@PathVariable Integer school_id, @PathVariable Integer program_id,
			@PathVariable Integer program_specialization_id, @PathVariable String year_sem) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String,Object>> course_list = course_service.coursesForLessonPlan(school_id,program_id,program_specialization_id,year_sem);
			ResponseEntity<Object> course_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, course_list);
			return course_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
	@GetMapping("/categoryTypeDetailsOnCatgoryTypeCreation")
	public ResponseEntity<Object> categoryTypeDetailsOnCatgoryTypeCreation(){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String,Object>> category_type_details_list=course_service.categoryTypeDetailsOnCatgoryTypeCreation();
			ResponseEntity<Object> category_type_details_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, category_type_details_list);
			return category_type_details_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getCourseConcat")
	public ResponseEntity<Object> getCourseConcat() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String,Object>> cos = course_service.getCourseConcat1();
		ResponseEntity<Object> co_response= ResponseHandler.generateResponse(true, HttpStatus.OK, cos);
		return co_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
}
