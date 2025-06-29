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
import com.au.model.SectionAssignment;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.SectionAssignmentService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
//@RequestMapping("/api")
public class SectionAssignmentController {

	@Autowired
	private SectionAssignmentService sectionAssignmentService;

	Logger log = LoggerFactory.getLogger(SectionAssignmentController.class);

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/SectionAssignment")
	public ResponseEntity<Object> saveSectionAssignment(@RequestBody @Valid SectionAssignment s,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException,Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			s.setCreated_by(jwtDetails.getUserId());
			s.setCreated_username(jwtDetails.getUserName());
			SectionAssignment section_assignment = sectionAssignmentService.saveSectionAssignment(s);
			ResponseEntity<Object> section_assignment_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, section_assignment);
			return section_assignment_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/SectionAssignment")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<SectionAssignment> section_assignment_list = sectionAssignmentService.listAll();
			ResponseEntity<Object> section_assignment_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, section_assignment_list);
			return section_assignment_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchAllSectionAssignmentDetails")
	public ResponseEntity<Object> fetchAllSectionAssignmentDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value = "ac_year_id", required = false) Integer ac_year_id,
			@RequestParam(value = "school_id", required = false) Integer school_id,
			@RequestParam(value = "program_id", required = false) Integer program_id,
			@RequestParam(value = "program_specialization_id", required = false) Integer program_specialization_id,
			@RequestParam(value = "section_id", required = false) Integer section_id,
			@RequestParam(value = "current_year_sem", required = false) Integer current_year_sem,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> section_assignment_sorted = sectionAssignmentService.fetchAllSectionAssignmentDetails1(pageable,ac_year_id,school_id,program_id,
						program_specialization_id,section_id,current_year_sem, keyword);
				return section_assignment_sorted;
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> section_assignment_pageable = sectionAssignmentService.fetchAllSectionAssignmentDetails2(pageable1,ac_year_id,school_id,program_id,
						program_specialization_id,section_id,current_year_sem);
				return section_assignment_pageable;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllSectionAssignmentDetailsBasedOnSchoolId")
	public ResponseEntity<Object> fetchAllSectionAssignmentDetailsBasedOnSchoolId(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="school_id",required = false) Integer school_id,
			@RequestParam(value="dept_id",required = false) Integer dept_id,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> section_assignment_sorted = sectionAssignmentService.fetchAllSectionAssignmentDetailsBasedOnSchoolId(pageable,school_id, keyword ,dept_id);//,column,value);
				return section_assignment_sorted;
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> section_assignment_pageable = sectionAssignmentService.fetchAllSectionAssignmentDetailsBasedOnSchoolId1(pageable1,school_id ,dept_id);
				return section_assignment_pageable;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllSectionAssignmentDetailsBasedOnUserId")
	public ResponseEntity<Object> fetchAllSectionAssignmentDetailsBasedOnUserId(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="created_by") Integer created_by,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> section_assignment_sorted = sectionAssignmentService.fetchAllSectionAssignmentDetailsBasedOnUserId(pageable,created_by, keyword);//,column,value);
				return section_assignment_sorted;
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> section_assignment_pageable = sectionAssignmentService.fetchAllSectionAssignmentDetailsBasedOnUserId1(pageable1,created_by);
				return section_assignment_pageable;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/SectionAssignment/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				SectionAssignment product = sectionAssignmentService.get(id);
				ResponseEntity<Object> section_assignment_response_by_id = ResponseHandler.generateResponse(true,HttpStatus.OK, product);
				return section_assignment_response_by_id;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/SectionAssignment/{id}/{removed_student_ids}")
	public ResponseEntity<Object> update(@RequestBody @Valid SectionAssignment s, @PathVariable Integer id,
			@PathVariable List<Integer> removed_student_ids, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				sectionAssignmentService.get(id);
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

				s.setModified_by(jwtDetails.getUserId());
				s.setModified_username(jwtDetails.getUserName());

				sectionAssignmentService.updateSectionAssignment(s,removed_student_ids);
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
						HttpStatus.OK);
				return response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
						HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/SectionAssignmentOfStudentFromIndex/{section_assignment_id}")
	public ResponseEntity<Object> sectionAssignmentOfStudentFromIndex(@RequestBody @Valid SectionAssignment s, @PathVariable Integer section_assignment_id,
			 @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				sectionAssignmentService.get(section_assignment_id);
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

				s.setModified_by(jwtDetails.getUserId());
				s.setModified_username(jwtDetails.getUserName());

				sectionAssignmentService.sectionAssignmentOfStudentFromIndex(s);
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
						HttpStatus.OK);
				return response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
						HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/SectionAssignment/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			sectionAssignmentService.delete(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateSectionAssignment/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			sectionAssignmentService.delete1(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
	@GetMapping("/sectionNameDetails/{ac_year_id}/{school_id}/{program_id}/{program_specialization_id}/{current_year_sem}")
	public ResponseEntity<Object> getSectionName(@PathVariable Integer ac_year_id,@PathVariable Integer school_id,
			@PathVariable Integer program_id,@PathVariable Integer program_specialization_id,@PathVariable Integer current_year_sem){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> section_names = sectionAssignmentService.getSectionNames(ac_year_id,school_id,program_id,program_specialization_id,current_year_sem);
			ResponseEntity<Object> section_names_response = ResponseHandler.generateResponse(true,HttpStatus.OK, section_names);
			return section_names_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/studentDetailsForPromoting/{section_assignment_id}")
	public ResponseEntity<Object> studentDetailsForPromoting(@PathVariable Integer section_assignment_id){
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>>  student_details_for_promoting=sectionAssignmentService.studentDetailsForPromoting(section_assignment_id);
			ResponseEntity<Object> student_details_for_promoting_response = ResponseHandler.generateResponse(true,
					HttpStatus.CREATED, student_details_for_promoting);
			return student_details_for_promoting_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	
	@PostMapping("/promotingOfSectionToStudents/{section_assignment_id}/{new_student_ids}")
	public ResponseEntity<Object> promotingOfAssinedSectionStudents(@PathVariable Integer section_assignment_id,
			@PathVariable String new_student_ids,@RequestHeader("Authorization") String jwtToken) throws JsonParseException, JsonMappingException, IOException, Exception {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			SectionAssignment section_assignment = sectionAssignmentService.promotingOfAssinedSectionStudents(section_assignment_id,
					new_student_ids,jwtDetails);
			ResponseEntity<Object> section_assignment_response = ResponseHandler.generateResponse(true,
					HttpStatus.CREATED, section_assignment);
			return section_assignment_response;
		} else {
			
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	//As of Now Not In Use
	@GetMapping("/fetchCountOfStudentsAssignedToSection/{section_id}")
	public ResponseEntity<Object> getCountOfStudentsAssignedToSection(@PathVariable Integer section_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Integer count_of_students = sectionAssignmentService.getCountOfStudentsAssignedToSection(section_id);
			ResponseEntity<Object> total_no_students_in_section = ResponseHandler.generateResponse(true, HttpStatus.OK, count_of_students);
			return total_no_students_in_section;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/assignedStudentDetailsBySectionAssignmentId/{section_assignment_id}")
	public ResponseEntity<Object> assignedStudentDetailsBySectionAssignmentId(@PathVariable Integer section_assignment_id) {
//	if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String,Object>> assignedStudents = sectionAssignmentService.assignedStudentDetailsBySectionAssignmentId(section_assignment_id);
			ResponseEntity<Object> assignedStudentsResponse = ResponseHandler.generateResponse(true, HttpStatus.OK, assignedStudents);
			return assignedStudentsResponse;
//			
//		} else {
//			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}
	}
	
	@GetMapping("/studentDetailsBasedOnSectionAssignmentId/{section_assignment_id}")
	public ResponseEntity<Object> studentDetailsBasedOnSectionAssignmentId(@PathVariable Integer section_assignment_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String,Object>> assignedStudents = sectionAssignmentService.studentDetailsBasedOnSectionAssignmentId(section_assignment_id);
			ResponseEntity<Object> assignedStudentsResponse = ResponseHandler.generateResponse(true, HttpStatus.OK, assignedStudents);
			return assignedStudentsResponse;
			
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/sectionDetailsByAcademicYearAndSpecializationId/{ac_year_id}/{program_specialization_id}/{current_year_sem}")
	public ResponseEntity<Object> sectionDetailsByAcademicYearAndSection(@PathVariable Integer ac_year_id,@PathVariable Integer program_specialization_id,@PathVariable Integer current_year_sem){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> section_names = sectionAssignmentService.sectionDetailsByAcademicYearAndSection(ac_year_id,program_specialization_id,current_year_sem);
			ResponseEntity<Object> section_names_response = ResponseHandler.generateResponse(true,HttpStatus.OK, section_names);
			return section_names_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/assignedStudentBySectionAssignmentId/{sectionAssignmentId}")
	public ResponseEntity<Object> assignedStudentBySectionAssignmentId(@PathVariable Integer sectionAssignmentId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> studentDetails = 
					sectionAssignmentService.assignedStudentBySectionAssignmentId(sectionAssignmentId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK,studentDetails);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}
	
	
	@GetMapping("/getCourseDetailData")
	public ResponseEntity<Object> getCourseDetailData(@RequestParam(value="school_id",required = false) Integer school_id ,
	@RequestParam(value="ac_year_id",required = false) Integer ac_year_id,
	@RequestParam(value="program_specialization_id",required = false) Integer program_specialization_id,
	@RequestParam(value="year_sem",required = false) Integer year_sem) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> studentDetails = 
					sectionAssignmentService.getCourseDetailData(school_id,ac_year_id,program_specialization_id,year_sem);
			return ResponseHandler.generateResponse(true, HttpStatus.OK,studentDetails);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}
	
	
	@GetMapping("/getSectionDetailData")
	public ResponseEntity<Object> getSectionDetailData(@RequestParam(value="school_id",required = false) Integer school_id ,
	@RequestParam(value="ac_year_id",required = false) Integer ac_year_id,
	@RequestParam(value="program_specialization_id",required = false) Integer program_specialization_id,
	@RequestParam(value="current_year_sem",required = false) Integer current_year_sem) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> studentDetails = 
					sectionAssignmentService.getSectionDetailData(school_id,ac_year_id,program_specialization_id,current_year_sem);
			return ResponseHandler.generateResponse(true, HttpStatus.OK,studentDetails);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}
	
}
