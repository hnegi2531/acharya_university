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
import com.au.dto.SubjectAssignmentDto;
import com.au.model.SubjectAssignment;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.SubjectAssignmentService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
//@RequestMapping("/api")
public class SubjectAssignmentController {

	Logger log = LoggerFactory.getLogger(SubjectAssignmentController.class);

	@Autowired
	private SubjectAssignmentService s_service;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/SubjectAssignment")
	public ResponseEntity<Object> saveAcademicSchoolVision(@RequestBody @Valid SubjectAssignmentDto r,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, Exception {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<SubjectAssignment> sa = s_service.saveSubjectAssignment(r,jwtToken);
			ResponseEntity<Object> sa_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, sa);
			return sa_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchAllSubjectAssignment")
	public ResponseEntity<Object> getSubjectAssignment(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value = "school_id", required = false) Integer school_id,
			@RequestParam(value = "dept_id", required = false) Integer dept_id,
			@RequestParam(value = "userId", required = false) Integer userId,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> sa_filtered =  s_service.getAllDataFilteredByKeyword(pageable, keyword ,school_id ,dept_id ,userId);//,column,value);
			return sa_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> sa_sorted = s_service.getAllSortedData(pageable1,school_id ,dept_id ,userId);
			return sa_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/fetchAllSubjectAssignmentBasedOnSchoolIdAndCreatedBy")
	public ResponseEntity<Object> fetchAllSubjectAssignmentBasedOnSchoolIdAndCreatedBy(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort,
			@RequestParam(value="school_id",required = false) Integer school_id,
			@RequestParam(value="createdBy",required = false) Integer createdBy,
			@RequestParam(value="user_id",required = false) Integer user_id,
			@RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> sa_filtered =  s_service.fetchAllSubjectAssignmentBasedOnSchoolIdAndCreatedByByKeyword(pageable, keyword,school_id,createdBy ,user_id);//,column,value);
			return sa_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> sa_sorted = s_service.fetchAllSubjectAssignmentBasedOnSchoolIdAndCreatedByData(pageable1,school_id,createdBy,user_id);
			return sa_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	

	@GetMapping("/SubjectAssignment/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				SubjectAssignment sa = s_service.get(id);
				ResponseEntity<Object> sa_response = ResponseHandler.generateResponse(true, HttpStatus.OK, sa);
				return sa_response;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
						HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/SubjectAssignment/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid SubjectAssignment r, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException,Exception {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				s_service.get(id);
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				r.setModifiedBy(jwtDetails.getUserId());
				r.setModifiedUsername(jwtDetails.getUserName());
				s_service.saveSubjectAssignment1(r);
				ResponseEntity<Object> sa_response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
						HttpStatus.OK);
				return sa_response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
						HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/SubjectAssignment/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			s_service.delete(id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateSubjectAssignment/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			s_service.delete1(id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchAllSubjectDetails")
	public ResponseEntity<Object> findSubjectDetails() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> sa_list = s_service.fetchAllDetails();
			ResponseEntity<Object> sa_response = ResponseHandler.generateResponse(true, HttpStatus.OK, sa_list);
			return sa_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}

	}

	@GetMapping("/fetchSubjectAssign")
	public ResponseEntity<Object> getSubjectAssignIndex() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> sa_list = s_service.getSubjectAssignIndex();
			ResponseEntity<Object> sa_response = ResponseHandler.generateResponse(true, HttpStatus.OK, sa_list);
			return sa_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchSubWorkloadBySubtype")
	public ResponseEntity<Object> fetchSubWorkLoadBySubtype() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> sa_list = s_service.fetchSubWorkLoadBySubtype();
			ResponseEntity<Object> sa_response = ResponseHandler.generateResponse(true, HttpStatus.OK, sa_list);
			return sa_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchSubWorkloadByWorkloadtype")
	public ResponseEntity<Object> fetchSubWorkLoadByWorkloadtype() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> sa_list = s_service.fetchSubWorkLoadByWorkloadtype();
			ResponseEntity<Object> sa_response = ResponseHandler.generateResponse(true, HttpStatus.OK, sa_list);
			return sa_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/subjectAssignIndexDetails1")
	public ResponseEntity<Object> subjectAssignIndexDetails1() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> sa_list = s_service.subjectAssignIndexDetails1();
			ResponseEntity<Object> sa_response = ResponseHandler.generateResponse(true, HttpStatus.OK, sa_list);
			return sa_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/courseUnassignedDetails/{user_id}")
	public ResponseEntity<Object> subjectUnassignedDetails(@PathVariable Integer user_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> sa_list = s_service.subjectUnassignedDetails(user_id);
			ResponseEntity<Object> sa_response = ResponseHandler.generateResponse(true, HttpStatus.OK, sa_list);
			return sa_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getAssignedCourses/{user_id}")
	public ResponseEntity<Object> getAssignedCoursesDetails(@PathVariable Integer user_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> sa_list = s_service.getAssignedCoursesDetails(user_id);
			ResponseEntity<Object> sa_response = ResponseHandler.generateResponse(true, HttpStatus.OK, sa_list);
			return sa_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getCourseNameBySubjectAssignmentId/{subject_assignment_id}")
	public ResponseEntity<Object> getCourseNameBySubjectAssignmentId(@PathVariable Integer subject_assignment_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			Map<String, Object> sa_list = s_service.getCourseNameBySubjectAssignmentId(subject_assignment_id);
			ResponseEntity<Object> sa_response = ResponseHandler.generateResponse(true, HttpStatus.OK, sa_list);
			return sa_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/getSubjectAssignmentDetailsData/{user_id}")
	public ResponseEntity<Object> getSubjectAssignmentDetailsData(@PathVariable Integer user_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> sa_list = s_service.getSubjectAssignmentDetailsData(user_id);
			ResponseEntity<Object> sa_response = ResponseHandler.generateResponse(true, HttpStatus.OK, sa_list);
			return sa_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}	
	
	@GetMapping("/getCourseAssignmentEmployeeBasedOnUserIdAndYearSem/{user_id}/{year_sem}")
	public ResponseEntity<Object> getCourseAssignmentEmployeeBasedOnUserId(@PathVariable Integer user_id, @PathVariable Integer year_sem) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String,Object>> cos = s_service.getCourseAssignmentEmployeeBasedOnUserIdAndYearSem(user_id, year_sem);
		ResponseEntity<Object> co_response= ResponseHandler.generateResponse(true, HttpStatus.OK, cos);
		return co_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
}
