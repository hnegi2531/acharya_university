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
import com.au.dto.StudentMarksDto;
import com.au.dto.StudentMarksLockDto;
import com.au.model.StudentMarks;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.StudentMarksService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class StudentMarksController {
	
	Logger log = LoggerFactory.getLogger(StudentMarksController.class);

	@Autowired
	private StudentMarksService student_marks_service;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/studentMarks")
	public ResponseEntity<Object> saveStudentMark(@RequestBody @Valid List<StudentMarks> stm,@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			stm.stream().forEach(sm ->{
				sm.setCreated_by(jwtDetails.getUserId());
				sm.setCreated_username(jwtDetails.getUserName());
			});
			List<StudentMarks> student_marks = student_marks_service.saveStudentMark(stm);
			ResponseEntity<Object> student_marks_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, student_marks);
			return student_marks_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	
	@PostMapping("/createExternalStudentMark")
	public ResponseEntity<Object> createExternalStudentMark(@RequestBody @Valid StudentMarksDto studentMarksDto,
			@RequestHeader("Authorization") String jwtToken)
					throws JsonParseException, JsonMappingException, IOException,Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

			studentMarksDto.setCreated_by(jwtDetails.getUserId());
			studentMarksDto.setCreated_username(jwtDetails.getUserName());
			List<StudentMarks> budget = student_marks_service.createExternalStudentMark(studentMarksDto,jwtToken);
			ResponseEntity<Object> budget_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, budget);
		return budget_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllExternalStudentMarksDetail")
	public ResponseEntity<Object> fetchAllExternalStudentMarksDetail(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> student_marks_sorted = student_marks_service.fetchAllExternalStudentMarksDetaillistAll1(pageable, keyword);//,column,value);
					return student_marks_sorted;
				}else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> student_marks_pageable = student_marks_service.fetchAllExternalStudentMarksDetaillistAll2(pageable1);
					return student_marks_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		//return pr_service.listAll1();
	}
	
	
	@GetMapping("/studentMarks")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<StudentMarks> list_student_marks = student_marks_service.listAll();
				ResponseEntity<Object> list_student_marks_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_student_marks);
				return list_student_marks_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@GetMapping("/fetchAllStudentMarksDetail")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> student_marks_sorted = student_marks_service.listAll1(pageable, keyword);//,column,value);
					return student_marks_sorted;
				}else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> student_marks_pageable = student_marks_service.listAll2(pageable1);
					return student_marks_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		//return pr_service.listAll1();
	}

	@GetMapping("/studentMarks/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				StudentMarks product = student_marks_service.get(id);
				ResponseEntity<Object> student_marks_response_by_id = ResponseHandler.generateResponse(true,HttpStatus.OK, product);
				return student_marks_response_by_id;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}		
	}

	@PutMapping("/studentMarks/{id}")
	public ResponseEntity<Object> update(@RequestBody StudentMarks stm, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
					student_marks_service.get(id);
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					stm.setModified_by(jwtDetails.getUserId());
					stm.setModified_username(jwtDetails.getUserName());
					student_marks_service.updateStudentMarks(stm);
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

	@DeleteMapping("/studentMarks/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				student_marks_service.delete(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}		
	}

	@DeleteMapping("/activateStudentMarks/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				student_marks_service.delete1(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}		
	}
	
	
	@GetMapping("/coursecodeConcateWithName/{ac_year_id}/{year_sem}")
	public ResponseEntity<Object> coursecodeConcateWithName(@PathVariable Integer ac_year_id,@PathVariable Integer year_sem){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String,Object>> list_psp = student_marks_service.coursecodeConcateWithName(ac_year_id,year_sem);
					ResponseEntity<Object> list_psp_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_psp);
					return list_psp_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}			
	}	
	
	
	@GetMapping("/getCourseAssignmentDetails/{ac_year_id}/{program_assignment_id}/{year_sem}")
	public ResponseEntity<Object> getCourseAssignmentDetails(@PathVariable Integer ac_year_id,@PathVariable Integer program_assignment_id,@PathVariable Integer year_sem){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String,Object>> list_psp = student_marks_service.getCourseAssignmentDetails(ac_year_id,program_assignment_id,year_sem);
					ResponseEntity<Object> list_psp_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_psp);
					return list_psp_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}			
	}
	
	@GetMapping("/getInternalDetailsData/{ac_year_id}/{internal_id}/{year_sem}/{program_specialization_id}/{course_assignment_id}")
	public ResponseEntity<Object> getInternalDetailsData(@PathVariable Integer ac_year_id, 
			@PathVariable Integer internal_id, @PathVariable Integer year_sem,@PathVariable Integer program_specialization_id,@PathVariable Integer course_assignment_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	List<Map<String, Object>> data = student_marks_service.getInternalDetailsData(ac_year_id,internal_id, year_sem,program_specialization_id,course_assignment_id);
		ResponseEntity<Object> response= ResponseHandler.generateResponse(true, HttpStatus.OK, data);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getStudentMarkDetails/{student_id}/{internal_session_id}")
	public ResponseEntity<Object> getStudentMarkDetails(@PathVariable Integer student_id,@PathVariable Integer internal_session_id)
			throws JsonParseException,JsonMappingException,IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> studentMark = student_marks_service.getStudentMarkDetails(student_id,internal_session_id);
			ResponseEntity<Object> studentMark_response = ResponseHandler.generateResponse(true, HttpStatus.OK,studentMark);
			return studentMark_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}
	}	
	
	@GetMapping("/getStudentMarkDetailsByInternalSessionId/{internal_session_id}")
	public ResponseEntity<Object> getStudentMarkDetailsByInternalSessionId(@PathVariable Integer internal_session_id)
			throws JsonParseException,JsonMappingException,IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<StudentMarks> studentMark = student_marks_service.getStudentMarkDetailsByInternalSessionId(internal_session_id);
			ResponseEntity<Object> studentMark_response = ResponseHandler.generateResponse(true, HttpStatus.OK,studentMark);
			return studentMark_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}
	}	

	@GetMapping("/getStudentMarkDetailsBasedOnProctor/{id}")
	public ResponseEntity<Object> getStudentMarkDetailsBasedOnProctor(@PathVariable Integer id)
			throws JsonParseException,JsonMappingException,IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> studentMark = student_marks_service.getStudentMarkDetailsBasedOnProctor(id);
			ResponseEntity<Object> studentMark_response = ResponseHandler.generateResponse(true, HttpStatus.OK,studentMark);
			return studentMark_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}
	}	
	
	@GetMapping("/getAllActiveInternal/{student_id}")
	public ResponseEntity<Object> getAllActiveInternal(@PathVariable Integer student_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> isas = student_marks_service.getAllActiveInternal(student_id);
			ResponseEntity<Object> internalSessionAssignment_response= ResponseHandler.generateResponse(true, HttpStatus.OK, isas);
			return internalSessionAssignment_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
/* ---------------------------  API FOR MOBILE APP ------------------------ */
	//For Mobile App
	@GetMapping("/getScorecardData/{student_id}/{current_year_sem}")
	public ResponseEntity<Object> getScorecardData(@PathVariable Integer student_id, @PathVariable Integer current_year_sem) {
		if(RateLimitController.bucket.tryConsume(1)) {
			
				List<Map<String, Object>> product = student_marks_service.getScorecardData(student_id, current_year_sem);
				ResponseEntity<Object> student_marks_response_by_id = ResponseHandler.generateResponse(true,HttpStatus.OK, product);
				return student_marks_response_by_id;

		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}		
	}
	
	//For Mobile App
	@GetMapping("/getGraphicalScorecardData/{current_year_sem}/{total_marks_internal}/{internal_id}/{course_id}")
	public ResponseEntity<Object> getGraphicalScorecardData(@PathVariable Integer current_year_sem,
			 @PathVariable Integer total_marks_internal, @PathVariable Integer internal_id, @PathVariable Integer course_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			
				Map<String, Object> product = student_marks_service.getGraphicalScorecardData(current_year_sem,
						total_marks_internal, internal_id, course_id);
				ResponseEntity<Object> student_marks_response_by_id = ResponseHandler.generateResponse(true,HttpStatus.OK, product);
				return student_marks_response_by_id;

		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/getScorecardDataOnInternal/{student_id}/{current_year_sem}/{internal_id}")
	public ResponseEntity<Object> getScorecardDataOnInternal(@PathVariable Integer student_id, @PathVariable Integer current_year_sem,@PathVariable Integer internal_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			
				List<Map<String, Object>> product = student_marks_service.getScorecardDataOnInternal(student_id, current_year_sem,internal_id);
				ResponseEntity<Object> student_marks_response_by_id = ResponseHandler.generateResponse(true,HttpStatus.OK, product);
				return student_marks_response_by_id;

		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/getStudentMarksWithFilteredData")
	public ResponseEntity<Object> getStudentMarksWithFilteredData(@RequestParam(value="ac_year_id", required = false) Integer ac_year_id,
			@RequestParam(value="school_id", required = false) Integer school_id,
			@RequestParam(value="dept_id", required = false) Integer dept_id,
			@RequestParam(value="program_specialization_id", required = false) Integer program_specialization_id,
			@RequestParam(value="internal_short_name", required = false) String internal_short_name) {
		if(RateLimitController.bucket.tryConsume(1)) {
			
				List<Map<String, Object>> product = student_marks_service.getStudentMarksWithFilteredData(ac_year_id,school_id,dept_id, program_specialization_id, internal_short_name);
				ResponseEntity<Object> student_marks_response_by_id = ResponseHandler.generateResponse(true,HttpStatus.OK, product);
				return student_marks_response_by_id;

		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}		
	}
	
	
	@GetMapping("/fetchFromStudentMarksWithFilteredData")
	public ResponseEntity<Object> fetchAllAdministrationEmailIdsDetail(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size, 
			@RequestParam(value="ac_year_id", required = false) Integer ac_year_id,
			@RequestParam(value="school_id", required = false) Integer school_id,
			@RequestParam(value="dept_id", required = false) Integer dept_id,
			@RequestParam(value="program_specialization_id", required = false) Integer program_specialization_id,
			@RequestParam(value="internal_short_name", required = false) String internal_short_name,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> oc_filtered =  student_marks_service.getAllDataFilteredByKeyword(pageable, keyword ,ac_year_id,school_id,dept_id, program_specialization_id, internal_short_name);
			return oc_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> oc_sorted = student_marks_service.getAllSortedData(pageable1,ac_year_id,school_id,dept_id, program_specialization_id, internal_short_name);
			return oc_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	

	@GetMapping("/fetchStudentInternalsReportWithFilteredData")
	public ResponseEntity<Object> fetchStudentInternalsReportWithFilteredData(@RequestParam(value="student_id", required = false) Integer student_id,
																	   @RequestParam(value="current_sem", required = false) Integer current_sem,
																	   @RequestParam(value="current_year", required = false) Integer current_year,
																	   @RequestParam(value="course_assignment_id", required = false) Integer course_assignment_id,
																	   @RequestParam(value="internal_short_name", required = false) String internal_short_name) {

		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> studentInternalsReport = student_marks_service.fetchStudentInternalsReportWithFilteredData(student_id, current_sem, current_year, course_assignment_id, internal_short_name);
			ResponseEntity<Object> studentInternalsReportResponse = ResponseHandler.generateResponse(true,HttpStatus.OK, studentInternalsReport);
			return studentInternalsReportResponse;

		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

//	@GetMapping("/fetchFromStudentMarksAndAttendanceWithFilteredData")
//	public ResponseEntity<Object> fetchFromStudentMarksAndAttendanceWithFilteredData(
//																	   @RequestParam(value="ac_year_id", required = false) Integer ac_year_id,
//																	   @RequestParam(value="current_sem", required = false) Integer current_sem,
//																	   @RequestParam(value="current_year", required = false) Integer current_year,
//																	   @RequestParam(value="school_id", required = false) Integer school_id,
//																	   @RequestParam(value="dept_id", required = false) Integer dept_id,
//																	   @RequestParam(value="program_specialization_id", required = false) Integer program_specialization_id,
//																	   @RequestParam(value="internal_short_name", required = false) String internal_short_name) {
//
//		if(RateLimitController.bucket.tryConsume(1)) {
//			List<Map<String, Object>> studentInternalsReport = student_marks_service.fetchFromStudentMarksAndAttendanceWithFilteredData(ac_year_id, current_sem, current_year, school_id,dept_id, program_specialization_id, internal_short_name);
//			ResponseEntity<Object> studentInternalsReportResponse = ResponseHandler.generateResponse(true,HttpStatus.OK, studentInternalsReport);
//			return studentInternalsReportResponse;
//
//		} else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}
//	}
//	
	
	@GetMapping("/fetchFromStudentMarksAndAttendanceWithFilteredData")
	public ResponseEntity<Object> fetchFromStudentMarksAndAttendanceWithFilteredData(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size, 
			  @RequestParam(value="ac_year_id", required = false) Integer ac_year_id,
			   @RequestParam(value="current_sem", required = false) Integer current_sem,
			   @RequestParam(value="current_year", required = false) Integer current_year,
			   @RequestParam(value="school_id", required = false) Integer school_id,
			   @RequestParam(value="dept_id", required = false) Integer dept_id,
			   @RequestParam(value="program_specialization_id", required = false) Integer program_specialization_id,
			   @RequestParam(value="internal_short_name", required = false) String internal_short_name,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> oc_filtered =  student_marks_service.getStudentMarksAndAttendanceWithFilteredDataKeyword(pageable, keyword,ac_year_id,current_sem,current_year,school_id,
					dept_id,program_specialization_id,internal_short_name);
			return oc_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> oc_sorted = student_marks_service.getStudentMarksAndAttendanceWithFilteredDataData(pageable1,ac_year_id,current_sem,current_year,school_id,
					dept_id,program_specialization_id,internal_short_name);
			return oc_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/fetchStudentMarksData")
	public ResponseEntity<Object> fetchStudentMarksData(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size, 
			  @RequestParam(value="ac_year_id", required = false) Integer ac_year_id,
			   @RequestParam(value="current_sem", required = false) Integer current_sem,
			   @RequestParam(value="current_year", required = false) Integer current_year,
			   @RequestParam(value="school_id", required = false) Integer school_id,
			   @RequestParam(value="program_specialization_id", required = false) Integer program_specialization_id,
			   @RequestParam(value="course_assignment_id", required = false) Integer course_assignment_id,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> oc_filtered =  student_marks_service.fetchStudentMarksDataWithKeyword(pageable, keyword,ac_year_id,current_sem,current_year,school_id,
					program_specialization_id,course_assignment_id);
			return oc_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> oc_sorted = student_marks_service.fetchStudentMarksDataWithoutKeyword(pageable1,ac_year_id,current_sem,current_year,school_id,
					program_specialization_id,course_assignment_id);
			return oc_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@PutMapping("/updateStudentMarksLock")
	public ResponseEntity<Object> updateStudentMarksLock(
	        @RequestBody StudentMarksLockDto dto,
	        @RequestParam List<Integer> marksIds,
	        @RequestHeader("Authorization") String jwtToken)
	        throws JsonParseException, JsonMappingException, IOException {

	    if (RateLimitController.bucket.tryConsume(1)) {
	        try {
	            student_marks_service.updateStudentMarksLock(dto, marksIds, jwtToken);
	            return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
	        } catch (NoSuchElementException e) {
	            return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
	        }
	    } else {
	        return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
	    }
	}
	
}
