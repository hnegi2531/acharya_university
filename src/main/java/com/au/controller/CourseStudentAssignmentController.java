package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

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
import com.au.dto.CourseStudentAssignmentDto;
import com.au.dto.JwtDetails;
import com.au.model.CourseStudentAssignment;
import com.au.response.ResponseHandler;
import com.au.service.CourseStudentAssignmentService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class CourseStudentAssignmentController {
	
	
	Logger log = LoggerFactory.getLogger(CourseStudentAssignmentController.class);
	
	@Autowired
	private CourseStudentAssignmentService cors_stu_assign_ser;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/courseStudentAssignment")
	public ResponseEntity<Object> saveCourseStudentAssignment(
			@RequestBody @Valid CourseStudentAssignmentDto cors_stu_dto,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException, Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			List<CourseStudentAssignment> cors_stu_assign_list=cors_stu_assign_ser.saveCourseStudentAssignment(cors_stu_dto,jwtDetails);
			ResponseEntity<Object> cors_stu_assign_list_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, cors_stu_assign_list);
			return cors_stu_assign_list_response;
			
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@GetMapping("/courseStudentAssignment")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<CourseStudentAssignment> cors_stu_assign_list = cors_stu_assign_ser.listAll();
			ResponseEntity<Object> cors_stu_assign_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, cors_stu_assign_list);
			return cors_stu_assign_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

	@GetMapping("/fetchAllCourseStudentAssignmentDetail")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> cors_stu_assign_sorted = cors_stu_assign_ser.listAll1(pageable, keyword);//,column,value);
					return cors_stu_assign_sorted;
				}else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> cors_stu_assign_pageable = cors_stu_assign_ser.listAll2(pageable1);
					return cors_stu_assign_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/courseStudentAssignment/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		try {
			CourseStudentAssignment product = cors_stu_assign_ser.get(id);
			ResponseEntity<Object> cors_stu_assign_response_by_id = ResponseHandler.generateResponse(true, HttpStatus.OK, product);
			return cors_stu_assign_response_by_id;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response1;
		}
	}

	@PutMapping("/courseStudentAssignment/{id}")
	public ResponseEntity<?> updateCourseStudentAssignment(@RequestBody @Valid CourseStudentAssignment course_stu_assign, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				course_stu_assign.setModified_by(jwtDetails.getUserId());
				course_stu_assign.setModified_username(jwtDetails.getUserName());
				cors_stu_assign_ser.updateCourseStudentAssignment(course_stu_assign);
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

	@DeleteMapping("/deactivateCourseStudentAssignment/{ids}")
	public ResponseEntity<Object> delete(@PathVariable List<Integer> ids) {
		if(RateLimitController.bucket.tryConsume(1)) {
			cors_stu_assign_ser.delete(ids);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateCourseStudentAssignment/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			cors_stu_assign_ser.delete1(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getStudentDetailsForCourseAssignment")
	public ResponseEntity<Object> getStudentDetailsForCourseAssignment(
			@RequestParam("course_assignment_id") Integer course_assignment_id, @RequestParam(value="ac_year_id",required=false) Integer ac_year_id,
			@RequestParam(value="program_specialization_id") Integer program_specialization_id,
			@RequestParam(value = "current_year", required = false) Integer current_year,
			@RequestParam(value = "current_sem", required = false) Integer current_sem) {
		if (RateLimitController.bucket.tryConsume(1)) {
			HashMap<String, Object> all_student_list = cors_stu_assign_ser.getStudentDetailsForCourseAssignment(course_assignment_id, ac_year_id, program_specialization_id,
							current_year, current_sem);
			ResponseEntity<Object> all_student_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, all_student_list);
			return all_student_list_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/courseStudentAssignmentIdsOnStudentIds/{student_ids}")
	public ResponseEntity<Object> courseStudentAssignmentIdsOnStudentIds(@PathVariable List<Integer> student_ids) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String,Object>> course_student_assignment_ids=cors_stu_assign_ser.courseStudentAssignmentIdsOnStudentIds(student_ids);
			ResponseEntity<Object> course_student_assignment_ids_response = ResponseHandler.generateResponse(true, HttpStatus.OK, course_student_assignment_ids);
			return course_student_assignment_ids_response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/coursesAssignedToStudent/{student_id}")
	public ResponseEntity<Object> coursesAssignedToStudent(@PathVariable Integer student_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Set<HashMap<String, Object>> course_student_assignment_ids=cors_stu_assign_ser.coursesAssignedToStudent(student_id);
			ResponseEntity<Object> course_student_assignment_ids_response = ResponseHandler.generateResponse(true, HttpStatus.OK, course_student_assignment_ids);
			return course_student_assignment_ids_response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/courseDetailsForStudentsAssignment/{program_specialization_id}/{year_sem}/{school_id}")
	public ResponseEntity<Object> courseDetailsForStudentsAssignment( @PathVariable Integer program_specialization_id,
			@PathVariable Integer year_sem,@PathVariable Integer school_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String,Object>> courses=cors_stu_assign_ser.courseDetailsForStudentsAssignment(program_specialization_id,
					  year_sem, school_id);
			ResponseEntity<Object> course_student_assignment_ids_response = ResponseHandler.generateResponse(true, HttpStatus.OK, courses);
			return course_student_assignment_ids_response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getcoursesAssignedToStudent/{student_id}")
	public ResponseEntity<Object> getcoursesAssignedToStudent(@PathVariable Integer student_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Integer> course_student_assignment_ids=cors_stu_assign_ser.getcoursesAssignedToStudent(student_id);
			ResponseEntity<Object> course_student_assignment_ids_response = ResponseHandler.generateResponse(true, HttpStatus.OK, course_student_assignment_ids);
			return course_student_assignment_ids_response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	

	@PostMapping("/assignMultipleCourseToStudent")
	public ResponseEntity<Object> assignMultipleCourseToStudent(
			@RequestBody @Valid CourseStudentAssignmentDto cors_stu_dto,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException, Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			List<CourseStudentAssignment> cors_stu_assign_list=cors_stu_assign_ser.assignMultipleCourseToStudent(cors_stu_dto,jwtDetails);
			ResponseEntity<Object> cors_stu_assign_list_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, cors_stu_assign_list);
			return cors_stu_assign_list_response;
			
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	
	@GetMapping("/getStudentDetailData/{course_assignment_id}/{internal_session_id}/{ac_year_id}")
	public ResponseEntity<Object> getStudentDetailData(@PathVariable Integer course_assignment_id,@PathVariable Integer internal_session_id,@PathVariable Integer ac_year_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> course_student_assignment_ids=cors_stu_assign_ser.getStudentDetailData(course_assignment_id,internal_session_id,ac_year_id);
			ResponseEntity<Object> course_student_assignment_ids_response = ResponseHandler.generateResponse(true, HttpStatus.OK, course_student_assignment_ids);
			return course_student_assignment_ids_response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

}
