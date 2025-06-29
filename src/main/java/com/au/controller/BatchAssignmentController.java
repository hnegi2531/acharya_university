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

import com.au.dto.BatchAssignmentDto;
import com.au.dto.BatchAssignmentRequestDto;
import com.au.dto.JwtDetails;
import com.au.model.BatchAssignment;
import com.au.model.BatchProgramAssignment;
import com.au.model.FamilyStructure;
import com.au.model.Resignation;
import com.au.model.TimeIntervalTypes;
import com.au.response.ResponseHandler;
import com.au.service.BatchAssignmentService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class BatchAssignmentController {

	@Autowired
	private BatchAssignmentService s_service;
	
	Logger log = LoggerFactory.getLogger(BatchAssignmentController.class);
	
	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/BatchAssignment")
	public ResponseEntity<Object> saveSubjectType(@RequestBody @Valid BatchAssignmentDto r,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException,Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<BatchAssignment> batchAssignment = s_service.saveBatchAssignment(r,jwtToken);
			s_service.updateBatchProgramAssignment(r,batchAssignment);
			return ResponseHandler.generateResponse(true, HttpStatus.CREATED, batchAssignment);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@GetMapping("/fetchAllBatchAssignmentDetails")
	public ResponseEntity<Object> listAll(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value = "ac_year_id", required = false) Integer ac_year_id,
			@RequestParam(value = "school_id", required = false) Integer school_id,
			@RequestParam(value = "program_specialization_id", required = false) Integer program_specialization_id,
			@RequestParam(value = "batch_id", required = false) Integer batch_id,
			@RequestParam(value = "current_year_sem", required = false) Integer current_year_sem,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> batch_assignment_sorted = s_service.listAll1(pageable,ac_year_id,school_id,program_specialization_id,batch_id,current_year_sem, keyword);//,column,value);
				return batch_assignment_sorted;
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> batch_assignment_pageable = s_service.listAll2(pageable1,ac_year_id,school_id,program_specialization_id,batch_id,current_year_sem);
				return batch_assignment_pageable;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
		
	}	
	
	
	@GetMapping("/fetchAllBatchAssignmentDetailsBasedOnSchoolAndCreatedBy")
	public ResponseEntity<Object> fetchAllBatchAssignmentDetailsBasedOnSchoolAndCreatedBy(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, 
			@RequestParam(value="schoolId",required = false) Integer schoolId,
			@RequestParam(value="createdBy",required = false) Integer createdBy,
			@RequestParam(value="dept_id",required = false) Integer dept_id,
			@RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> batch_assignment_sorted = s_service.fetchAllBatchAssignmentDetailsBasedOnSchoolAndCreatedBylistAll1(pageable, keyword ,schoolId ,createdBy ,dept_id);//,column,value);
				return batch_assignment_sorted;
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> batch_assignment_pageable = s_service.fetchAllBatchAssignmentDetailsBasedOnSchoolAndCreatedBylistAll2(pageable1,schoolId ,createdBy ,dept_id);
				return batch_assignment_pageable;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
		
	}
	
	

	@GetMapping("/BatchAssignment/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				BatchAssignment product = s_service.get(id);
				ResponseEntity<Object> batch_assignment_response_by_id = ResponseHandler.generateResponse(true,HttpStatus.OK, product);
				return batch_assignment_response_by_id;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/BatchProgramAssignment/{batch_assignment_id}")
	public ResponseEntity<Object> getBatchProgramAssignment(@PathVariable Integer batch_assignment_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<BatchProgramAssignment> batch_list = s_service.getBatchProgramAssignment(batch_assignment_id);
			ResponseEntity<Object> batchAssignment_list = ResponseHandler.generateResponse(true, HttpStatus.OK, batch_list);
			return batchAssignment_list;
	}else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}	
	}

	@PutMapping("/BatchAssignment/{id}")
	public ResponseEntity<Object> BatchAssignment(@RequestBody BatchAssignment res, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					res.setModified_by(jwtDetails.getUserId());
					res.setModified_username(jwtDetails.getUserName());
					s_service.updateBatchAssignment(res);
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
	

	
	@DeleteMapping("/BatchAssignment/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			s_service.delete(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateBatchAssignment/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			s_service.delete1(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchStudentDetailForBatchAssignment")
	public ResponseEntity<Object> fetchStudentDetailForBatchAssignment(@RequestParam(value ="school_id") Integer school_id,@RequestParam(value ="program_specialization_id") List<Integer> program_specialization_id,
			@RequestParam(value ="current_sem",required = false) Integer current_sem,
			@RequestParam(value ="current_year",required = false) Integer current_year) {
		if (RateLimitController.bucket.tryConsume(1)) {
			if(current_sem != null) {
			List<Map<String, Object>> stu_details_on_sem = s_service.fetchStudentDetailForBatchAssignmentOnSem( school_id,program_specialization_id, current_sem);
			ResponseEntity<Object> student_response_on_sem = ResponseHandler.generateResponse(true, HttpStatus.OK,
					stu_details_on_sem);
			return student_response_on_sem;
			}else{
				List<Map<String, Object>> stu_details_on_year = s_service.fetchStudentDetailForBatchAssignmentOnYear(school_id,program_specialization_id,  current_year);
				ResponseEntity<Object> student_response_on_year = ResponseHandler.generateResponse(true, HttpStatus.OK,
						stu_details_on_year);
				return student_response_on_year;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchBatchAssignmentDetailForStudentFromIndex")
	public ResponseEntity<Object> fetchBatchAssignmentDetailForStudentFromIndex(@RequestParam(value ="school_id") Integer school_id, @RequestParam(value ="program_id") List<Integer> program_id,
			@RequestParam(value ="current_sem",required = false) Integer current_sem,@RequestParam(value ="current_year",required = false) Integer current_year,@RequestParam(value ="ac_year_id") Integer ac_year_id,@RequestParam(value ="batch_id") Integer batch_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			if(current_sem != null) {
				List<BatchAssignment> batch_assign_on_sem = s_service.fetchBatchAssignmentDetailForStudentFromIndexOnSem( school_id,program_id, current_sem,ac_year_id,batch_id);
			ResponseEntity<Object> batch_assign_response_on_sem = ResponseHandler.generateResponse(true, HttpStatus.OK,
					batch_assign_on_sem);
			return batch_assign_response_on_sem;
			}else{
				List<BatchAssignment> batch_assign_on_year = s_service.fetchBatchAssignmentDetailForStudentFromIndexOnYear(school_id,program_id, current_year,ac_year_id,batch_id);
				ResponseEntity<Object> batch_assign_response_on_year = ResponseHandler.generateResponse(true, HttpStatus.OK,
						batch_assign_on_year);
				return batch_assign_response_on_year;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchStudentDetailForBatchAssignmentFromIndex")
	public ResponseEntity<Object> fetchStudentDetailForBatchAssignmentFromIndex(@RequestParam(value ="school_id") Integer school_id,@RequestParam(value ="program_specialization_id") List<Integer> program_specialization_id,
			@RequestParam(value ="current_sem",required = false) Integer current_sem,@RequestParam(value ="current_year",required = false) Integer current_year,
			@RequestParam(value ="ac_year_id") Integer ac_year_id,@RequestParam(value ="student_ids") List<Integer> student_ids,@RequestParam(value ="unassigned_school_ids") List<Integer> unassigned_school_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			if(current_sem != null) {
				List<Object> student_details_on_sem = s_service.fetchStudentDetailForBatchAssignmentFromIndexOnSem( school_id,program_specialization_id, current_sem,ac_year_id,student_ids,unassigned_school_id);
			ResponseEntity<Object> student_details_response_on_sem = ResponseHandler.generateResponse(true, HttpStatus.OK,
					student_details_on_sem);
			return student_details_response_on_sem;
			}else{
				List<Object> student_details_on_year = s_service.fetchStudentDetailForBatchAssignmentFromIndexOnYear(school_id,program_specialization_id, current_year,ac_year_id,student_ids,unassigned_school_id);
				ResponseEntity<Object> student_details_response_on_year = ResponseHandler.generateResponse(true, HttpStatus.OK,
						student_details_on_year);
				return student_details_response_on_year;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/batchAssignmentUpdateFromIndexPage/{ids}")
	public ResponseEntity<Object> batchAssignmentUpdateFromIndexPage(@RequestBody @Valid List<BatchAssignment> r, @PathVariable List<Integer> ids,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				r.stream().forEach(batch_assign -> {
					batch_assign.setModified_by(jwtDetails.getUserId());
					batch_assign.setModified_username(jwtDetails.getUserName());
				});
				s_service.batchAssignmentUpdateFromIndexPage(r);
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
	
	@GetMapping("/fetchGuestStudentDetails")
	public ResponseEntity<Object> fetchGuestStudentDetails(@RequestParam(value ="school_id") Integer school_id,@RequestParam(value ="program_id") Integer program_id,
			@RequestParam(value ="current_sem",required = false) Integer current_sem,@RequestParam(value ="current_year",required = false) Integer current_year,
			@RequestParam(value ="ac_year_id") Integer ac_year_id,@RequestParam(value ="batch_id") Integer batch_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			if(current_sem != null) {
				List<Object> guest_student_details = s_service.fetchGuestStudentDetailsOnSem( school_id,program_id, current_sem,ac_year_id,batch_id);
			ResponseEntity<Object> guest_student_details_response_on_sem = ResponseHandler.generateResponse(true, HttpStatus.OK,
					guest_student_details);
			return guest_student_details_response_on_sem;
			}else{
				List<Object> guest_student_details = s_service.fetchGuestStudentDetailsOnYear(school_id,program_id, current_year,ac_year_id,batch_id);
				ResponseEntity<Object> guest_student_details_response_on_year = ResponseHandler.generateResponse(true, HttpStatus.OK,
						guest_student_details);
				return guest_student_details_response_on_year;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchUnAssignedStudentDetailsOfSchool")
	public ResponseEntity<Object> fetchUnAssignedStudentDetailsOfSchool(@RequestParam(value = "ac_year_id") Integer ac_year_id,
			@RequestParam(value = "school_id",required = false) Integer school_id,
			@RequestParam(value = "student_ids",required = false) List<Integer> student_ids,
			@RequestParam(value = "program_specialization_id") List<Integer> program_specialization_id,
			@RequestParam(value = "program_id") Integer program_id,
			@RequestParam(value = "current_year_sem") Integer current_year_sem,
			@RequestParam(value = "program_assignment_id") Integer program_assignment_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			if(student_ids != null) {
			List<Map<String, Object>> assigned_student_details = s_service
					.fetchUnAssignedStudentDetailsOfSchool(ac_year_id,school_id, student_ids,program_specialization_id,program_id,current_year_sem,program_assignment_id);
			ResponseEntity<Object> assigned_student_details_response = ResponseHandler.generateResponse(true,
					HttpStatus.OK, assigned_student_details);
			return assigned_student_details_response;
			}else {
				List<Map<String, Object>> assigned_student_details = s_service
						.fetchUnAssignedStudentDetailsOfSchoolWOStudent(ac_year_id,school_id,program_specialization_id,program_id,current_year_sem,program_assignment_id);
				ResponseEntity<Object> assigned_student_details_response = ResponseHandler.generateResponse(true,
						HttpStatus.OK, assigned_student_details);
				return assigned_student_details_response;
			}

		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
		
		@GetMapping("/fetchStudentDetailsForUpdate")
		public ResponseEntity<Object> fetchStudentDetailsForUpdate(@RequestParam(value ="student_ids") List<Integer> student_ids) {
			if (RateLimitController.bucket.tryConsume(1)) {
				List<Map<String, Object>> assigned_student_details = s_service.fetchStudentDetailsForUpdate(student_ids);
				ResponseEntity<Object> assigned_student_details_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
						assigned_student_details);
				return assigned_student_details_response;
				
			} else {
				ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
						ResponseHandler.message1);
				return rs;
			}
	}
		//As of Now Not In Use
		@GetMapping("/fetchCountOfStudentsAssignedToBatch/{batch_id}")
		public ResponseEntity<Object> getCountOfStudentsAssignedToBatch(@PathVariable Integer batch_id) {
			if(RateLimitController.bucket.tryConsume(1)) {
				Integer no_of_students = s_service.getCountOfStudentsAssignedToBatch(batch_id);
				ResponseEntity<Object> total_no_students_in_batch = ResponseHandler.generateResponse(true, HttpStatus.OK, no_of_students);
				return total_no_students_in_batch;
				
			} else {
				ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		@GetMapping("/assignedStudentDetailsByBatchAssignmentId/{batch_assignment_id}")
		public ResponseEntity<Object> assignedStudentDetailsByBatchAssignmentId(@PathVariable Integer batch_assignment_id) {
			if(RateLimitController.bucket.tryConsume(1)) {
				List<HashMap<String,Object>> assignedStudents = s_service.assignedStudentDetailsByBatchAssignmentId(batch_assignment_id);
				ResponseEntity<Object> assignedStudentsResponse = ResponseHandler.generateResponse(true, HttpStatus.OK, assignedStudents);
				return assignedStudentsResponse;
				
			} else {
				ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		@GetMapping("/studentDetailsWithBatchName/{batch_assignment_id}")
		public ResponseEntity<Object> studentDetailsWithBatchName(@PathVariable Integer batch_assignment_id) {
			if(RateLimitController.bucket.tryConsume(1)) {
				List<Map<String, Object>> assignedStudents = s_service.studentDetailsWithBatchName(batch_assignment_id);
				ResponseEntity<Object> assignedStudentsResponse = ResponseHandler.generateResponse(true, HttpStatus.OK, assignedStudents);
				return assignedStudentsResponse;
				
			} else {
				ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		@GetMapping("/batchByAcademicYearAndSpecializationId/{acYearId}/{programSpecializationId}/{currentYearSem}")
		public ResponseEntity<Object> batchByAcademicYearAndSpecializationId(@PathVariable Integer acYearId,@PathVariable Integer programSpecializationId,@PathVariable Integer currentYearSem){
			if(RateLimitController.bucket.tryConsume(1)) {
				List<Map<String, Object>> batchDetails = s_service.batchByAcademicYearAndSpecializationId(acYearId,programSpecializationId,currentYearSem);
				return ResponseHandler.generateResponse(true,HttpStatus.OK, batchDetails);
				 
			}else {
				return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			}
		}
		
		@GetMapping("/batchByAcademicYear/{acYearId}/{currentYearSem}")
		public ResponseEntity<Object> batchByAcademicYear(@PathVariable Integer acYearId,@PathVariable Integer currentYearSem){
			if(RateLimitController.bucket.tryConsume(1)) {
				List<Map<String, Object>> batchDetails = s_service.batchByAcademicYear(acYearId,currentYearSem);
				return ResponseHandler.generateResponse(true,HttpStatus.OK, batchDetails);
				 
			}else {
				return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			}
		}
		
		@GetMapping("/getBatchAssignmentUserId")
		public ResponseEntity<Object> getBatchAssignmentUserId(@RequestParam(value = "guest_uesr_ids") String guest_uesr_ids) {
			if(RateLimitController.bucket.tryConsume(1)) {
				List<Map<String, Object>> batchDetails = s_service.getBatchAssignmentUserId(guest_uesr_ids);
				return ResponseHandler.generateResponse(true,HttpStatus.OK, batchDetails);
				 
			}else {
				return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			}
		}		
		
}
