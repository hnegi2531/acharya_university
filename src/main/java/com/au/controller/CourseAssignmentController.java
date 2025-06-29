package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.validation.Valid;
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

import com.au.dto.CourseAssignmentRequest;
import com.au.dto.JwtDetails;
import com.au.model.CourseAssignment;
import com.au.response.ResponseHandler;
import com.au.service.CourseAssignmentService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class CourseAssignmentController {
	
	@Autowired
	private CourseAssignmentService cas_service;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/CourseAssignment")
	public ResponseEntity<Object> saveCourseAssignment(@RequestBody @Valid CourseAssignmentRequest car,
			@RequestHeader("Authorization") String jwtToken)throws JsonParseException,JsonMappingException,IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			car.setCreated_by(jwtDetails.getUserId());
			car.setCreated_username(jwtDetails.getUserName());
			CourseAssignment course_assignment = cas_service.save_CourseAssignment(car);
			ResponseEntity<Object> course_assignment_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, course_assignment);
			return course_assignment_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

	@GetMapping("/CourseAssignment")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<CourseAssignment> list_course_assignment = cas_service.listAll();
			ResponseEntity<Object> list_course_assignment_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_course_assignment);
			return list_course_assignment_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

	@GetMapping("/fetchAllCourseAssignmentDetails")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> course_assignment_sorted = cas_service.listAll1(pageable, keyword);//,column,value);
				return course_assignment_sorted;
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> course_assignment_pageable = cas_service.listAll2(pageable1);
				return course_assignment_pageable;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
		
	}
	
	@GetMapping("/fetchAllCourseAssignmentDetailsBasedOnUserId")
	public ResponseEntity<Object> fetchAllCourseAssignmentDetailsBasedOnUserId(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort,@RequestParam(value="user_id") Integer user_id, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> course_assignment_sorted = cas_service.listAll1BasedOnUseraId(pageable, keyword, user_id);//,column,value);
				return course_assignment_sorted;
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> course_assignment_pageable = cas_service.listAll2BasedOnUseraId(pageable1, user_id);
				return course_assignment_pageable;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
		
	}

	@GetMapping("/CourseAssignment/{course_assignment_id}")
	public ResponseEntity<Object> get(@PathVariable Integer course_assignment_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				List<HashMap<String, Object>> product = cas_service.fetch(course_assignment_id);
				ResponseEntity<Object> course_assignment_response_by_id = ResponseHandler.generateResponse(true, HttpStatus.OK, product);
				return course_assignment_response_by_id;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response1;
			}	
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
			 
	}

	@PutMapping("/CourseAssignment/{id}")
	public ResponseEntity<Object> update(@RequestBody CourseAssignment ca, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				// CourseAssignment existProduct = cas.get(id);
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				ca.setModified_by(jwtDetails.getUserId());
				ca.setModified_username(jwtDetails.getUserName());
				cas_service.updateCourseAssignment(ca);
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,HttpStatus.OK);
				return response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/CourseAssignment/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			cas_service.delete(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateCourseAssignment/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			cas_service.delete1(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllCourseSyllabusRelatedDetails/{syllabus_id}")
	public ResponseEntity<Object> fetchAllDetailsBySyllabusId(@PathVariable Integer syllabus_id){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> details_by_syllabus_id = cas_service.fetchAllSyllabusDetails(syllabus_id);
			ResponseEntity<Object> details_by_syllabus_id_response = ResponseHandler.generateResponse(true, HttpStatus.OK, details_by_syllabus_id);
			return details_by_syllabus_id_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllCourseCreditRelatedDetails/{ac_year_id}/{school_id}/{program_id}/{program_specialization_id}/{course_id}")
	public ResponseEntity<Object> fetchAllDetailByIds(@PathVariable Integer ac_year_id, @PathVariable Integer school_id,
			@PathVariable Integer program_id, @PathVariable Integer program_specialization_id,
			@PathVariable Integer course_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			HashMap<String, Object> details_by_ids = cas_service.fetchCreditDetails(ac_year_id, school_id, program_id,program_specialization_id, course_id);
			ResponseEntity<Object> details_by_ids_response = ResponseHandler.generateResponse(true, HttpStatus.OK,details_by_ids);
			return details_by_ids_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllCourseAmountRelatedDetails/{ac_year_id}/{school_id}/{program_id}/{dept_id}/{program_specialization_id}/{course_id}/{course_category_id}/{year_sem}")
	public ResponseEntity<Object> fetchAllAmountDetail(@PathVariable Integer ac_year_id,
			@PathVariable Integer school_id, @PathVariable Integer program_id,@PathVariable Integer dept_id,
			@PathVariable Integer program_specialization_id, @PathVariable Integer course_id,
			@PathVariable Integer course_category_id, @PathVariable String year_sem) {
		if (RateLimitController.bucket.tryConsume(1)) {
			HashMap<String, Object> course_amount_details_by_ids = cas_service.fetchAmountDetails(ac_year_id, school_id, program_id,
					dept_id, program_specialization_id, course_id,course_category_id, year_sem);
			ResponseEntity<Object> course_amount_details_by_ids_response = ResponseHandler.generateResponse(true, HttpStatus.OK,course_amount_details_by_ids);
			return course_amount_details_by_ids_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchCourseAssignment/{ac_year_id}")
	public ResponseEntity<Object> fetchAllAmountDetail(@PathVariable Integer ac_year_id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException,JsonMappingException,IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			Integer created_by=jwtDetails.getUserId();
			List<HashMap<String, Object>> amount_details_by_ac_year_id = cas_service.fetchCourseAssignmentDetails(ac_year_id,created_by);
			ResponseEntity<Object> amount_details_by_ac_year_id_response = ResponseHandler.generateResponse(true, HttpStatus.OK,amount_details_by_ac_year_id);
			return amount_details_by_ac_year_id_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllCourseDetailsForCourseMappingIndex/{school_id}/{program_specialization_id}/{year_sem}")
	public ResponseEntity<Object> fetchAllCourseDetailsForCourseMappingIndex(@PathVariable Integer school_id,@PathVariable Integer program_specialization_id,@PathVariable Integer year_sem) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> course_amount_details_by_ids = cas_service.fetchAllCourseDetailsForCourseMappingIndex(school_id,program_specialization_id,year_sem);
			ResponseEntity<Object> course_amount_details_by_ids_response = ResponseHandler.generateResponse(true, HttpStatus.OK,course_amount_details_by_ids);
			return course_amount_details_by_ids_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}
	}
	
	// For Mobile App
		@GetMapping("/getfetchCourseDetail/{year_sem}/{student_id}")
		public ResponseEntity<Object> fetchCourseDetail(@PathVariable Integer year_sem,@PathVariable Integer student_id) {
			if(RateLimitController.bucket.tryConsume(1)) {
				List<Map<String, Object>> coursepattern = cas_service.fetchCourseDetail(year_sem,student_id);
			ResponseEntity<Object> coursepattern_response= ResponseHandler.generateResponse(true, HttpStatus.OK, coursepattern);
			return coursepattern_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
			}
		}
		
	// For Mobile App
		@GetMapping("/fetchSyllabusDetails/{course_assignment_id}")
		public ResponseEntity<Object> fetchSyllabusDetails(@PathVariable Integer course_assignment_id) {
			if(RateLimitController.bucket.tryConsume(1)) {
				List<Map<String, Object>> coursepattern = cas_service.fetchSyllabusDetails(course_assignment_id);
			ResponseEntity<Object> coursepattern_response= ResponseHandler.generateResponse(true, HttpStatus.OK, coursepattern);
			return coursepattern_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
			}
		}
		
		// For Mobile App	
		@GetMapping("/fetchCourseObjective/{course_assignment_id}")
		public ResponseEntity<Object> fetchCourseObjective(@PathVariable Integer course_assignment_id) {
			if(RateLimitController.bucket.tryConsume(1)) {
				List<Map<String, Object>> coursepattern = cas_service.fetchCourseObjective(course_assignment_id);
			ResponseEntity<Object> coursepattern_response= ResponseHandler.generateResponse(true, HttpStatus.OK, coursepattern);
			return coursepattern_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
			}
		}
		
		// For Mobile App
		@GetMapping("/fetchCourseOutcome/{course_assignment_id}")
		public ResponseEntity<Object> fetchCourseOutcome(@PathVariable Integer course_assignment_id) {
			if(RateLimitController.bucket.tryConsume(1)) {
				List<Map<String, Object>> coursepattern = cas_service.fetchCourseOutcome(course_assignment_id);
			ResponseEntity<Object> coursepattern_response= ResponseHandler.generateResponse(true, HttpStatus.OK, coursepattern);
			return coursepattern_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
			}
		}
		
		//for mobile app
		@GetMapping("/referenceBooksDetail/{course_assignment_id}")
		public ResponseEntity<Object> getReferenceBooksDetail(@PathVariable Integer course_assignment_id){

			if(RateLimitController.bucket.tryConsume(1)) {
				List<Map<String, Object>> referencebooks = cas_service.getReferenceBooksDetail(course_assignment_id);
			ResponseEntity<Object> referencebooks_response= ResponseHandler.generateResponse(true, HttpStatus.OK, referencebooks);
			return referencebooks_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
		}
		
		@GetMapping("/getCourseDetailsData/{emp_id}")
		public ResponseEntity<Object> getCourseDetailsData(@PathVariable Integer emp_id,
			@RequestHeader("Authorization") String jwtToken)
				throws JsonParseException,JsonMappingException,IOException {
		 if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			List<Map<String, Object>> getCourseDetailsData = cas_service.getCourseDetailsData(emp_id);
			ResponseEntity<Object> amount_details_by_ac_year_id_response = ResponseHandler.generateResponse(true, HttpStatus.OK,getCourseDetailsData);
			return amount_details_by_ac_year_id_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
			}
		}
		
		
		@GetMapping("/getAllActiveCourseDetailsData")
		public ResponseEntity<Object> getAllActiveCourseDetailsData()
				throws JsonParseException,JsonMappingException,IOException {
		 if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> getCourseDetailsData = cas_service.getAllActiveCourseDetailsData();
			ResponseEntity<Object> amount_details_by_ac_year_id_response = ResponseHandler.generateResponse(true, HttpStatus.OK,getCourseDetailsData);
			return amount_details_by_ac_year_id_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
			}
		}	
		
		
		@GetMapping("/fetchAllCourseDetail/{program_id}/{program_specialization_id}/{year_sem}")
		public ResponseEntity<Object> fetchAllCourseDetail(@PathVariable Integer program_id,@PathVariable Integer program_specialization_id,
				@PathVariable Integer year_sem) {
		if (RateLimitController.bucket.tryConsume(1)) {
				List<HashMap<String, Object>> course_details = cas_service.fetchAllCourseDetail(program_id,program_specialization_id,
						year_sem);
		ResponseEntity<Object> course_details_response = ResponseHandler.generateResponse(true, HttpStatus.OK,course_details);
		return course_details_response;
		} else {
		ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
				return rs;
			}
		}
		
		@GetMapping("/allCourseDetailFromCourseAssignment")
		public ResponseEntity<Object> allCourseDetailFromCourseAssignment() {
			if (RateLimitController.bucket.tryConsume(1)) {
				List<HashMap<String, Object>> courseDetails = cas_service.allCourseDetailFromCourseAssignment();
				return ResponseHandler.generateResponse(true, HttpStatus.OK,courseDetails);
			} else {
				return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			}
		}
		
		
		@GetMapping("/getCourseAssignmentDetailsBasedOnUserId/{user_id}")
		public ResponseEntity<Object> getCourseAssignmentDetailsBasedOnUserId(@PathVariable Integer user_id)
				throws JsonParseException,JsonMappingException,IOException {
		 if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> getCourseDetailsData = cas_service.getCourseAssignmentDetailsBasedOnUserId(user_id);
			ResponseEntity<Object> amount_details_by_ac_year_id_response = ResponseHandler.generateResponse(true, HttpStatus.OK,getCourseDetailsData);
			return amount_details_by_ac_year_id_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
			}
		}
		
}
