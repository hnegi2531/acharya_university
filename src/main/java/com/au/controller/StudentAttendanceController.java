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
import com.au.model.StudentAttendance;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.StudentAttendanceService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import com.au.controller.RateLimitController;

@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class StudentAttendanceController {
	
	Logger log = LoggerFactory.getLogger(StudentAttendanceController.class);
	
	@Autowired
	private StudentAttendanceService studentAttendanceService;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	
	@PostMapping("/studentAttendance")
	public ResponseEntity<Object> saveStudentAttendance(@RequestBody @Valid List<StudentAttendance> studentAttandance,
			@RequestHeader("Authorization") String jwtToken, @RequestParam(value="sectionAssignmentId",required = false) Integer sectionAssignmentId,
	 @RequestParam(value="batchAssignmentId",required = false) Integer batchAssignmentId)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			studentAttandance.stream().forEach(studentAttandance1 -> {
			studentAttandance1.setCreated_by(jwtDetails.getUserId());
			studentAttandance1.setCreated_username(jwtDetails.getUserName());
			});
			if(sectionAssignmentId !=null) {
			List<StudentAttendance> studentAttendance = studentAttendanceService.saveStudentAttendance(studentAttandance,sectionAssignmentId);
			return ResponseHandler.generateResponse(true, HttpStatus.CREATED, studentAttendance);
			}
			else {
			List<StudentAttendance> studentAttendanceBatch = studentAttendanceService.saveStudentAttendanceBatch(studentAttandance,batchAssignmentId);
			return ResponseHandler.generateResponse(true, HttpStatus.CREATED, studentAttendanceBatch);
			}
			
			
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			
		}	
	}

	@GetMapping("/studentAttendance")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<StudentAttendance> student_attendance_list = studentAttendanceService.listAll();
			ResponseEntity<Object> student_attendance_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, student_attendance_list);
			return student_attendance_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

	@GetMapping("/fetchAllStudentAttendanceDetail")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> student_attendance_sorted = studentAttendanceService.listAll1(pageable, keyword);//,column,value);
					return student_attendance_sorted;
				}else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> student_attendance_pageable = studentAttendanceService.listAll2(pageable1);
					return student_attendance_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		//return course_service.listAll1();
	}

	@GetMapping("/studentAttendance/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		try {
			StudentAttendance product = studentAttendanceService.get(id);
			ResponseEntity<Object> student_attendance_response_by_id = ResponseHandler.generateResponse(true, HttpStatus.OK, product);
			return student_attendance_response_by_id;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response1;
		}
	}

	@PutMapping("/studentAttendance/{id}")
	public ResponseEntity<?> update(@RequestBody @Valid List<StudentAttendance> studentAttandance, @PathVariable List<Integer> id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				studentAttandance.stream().forEach(p -> {

					p.setModified_by(jwtDetails.getUserId());
					p.setModified_username(jwtDetails.getUserName());
				});
				studentAttendanceService.updateStudentAttendance(studentAttandance,id);
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

	@DeleteMapping("/studentAttendance/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			studentAttendanceService.delete(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateStudentAttendance/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			studentAttendanceService.delete1(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/studentAttendanceDetailsForMobileApp/{student_id}")
	public ResponseEntity<Object> studentAttendanceDetailsForMobileApp(@PathVariable Integer student_id) {
		try {
			List<Map<String,Object>> product = studentAttendanceService.studentAttendanceDetailsForMobileApp(student_id);
			ResponseEntity<Object> student_attendance_response_by_id = ResponseHandler.generateResponse(true, HttpStatus.OK, product);
			return student_attendance_response_by_id;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response1;
		}
	}
	
	@GetMapping("/studentAttendanceDetailsOfAllCourse/{student_id}/{course_id}")
	public ResponseEntity<Object> studentAttendanceDetailsOfAllCourseForAbsentAndPresent(@PathVariable Integer student_id,@PathVariable Integer course_id) {
		try {
			List<Map<String,Object>> attendance_of_all_course = studentAttendanceService.studentAttendanceDetailsOfAllCourseForAbsentAndPresent(student_id,course_id);
			ResponseEntity<Object> attendance_of_all_course_response = ResponseHandler.generateResponse(true, HttpStatus.OK, attendance_of_all_course);
			return attendance_of_all_course_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response1;
		}
	}
	
	//For mobile app
	@GetMapping("/studentAttendanceDetails/{student_id}/{year_or_sem}")
	public ResponseEntity<Object> studentAttendanceDetails(@PathVariable Integer student_id, @PathVariable Integer year_or_sem) {
		try {
			List<Map<String,Object>> product = studentAttendanceService.studentAttendanceDetails(student_id, year_or_sem);
			ResponseEntity<Object> student_attendance_response_by_id = ResponseHandler.generateResponse(true, HttpStatus.OK, product);
			return student_attendance_response_by_id;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response1;
		}
	}

	//For Mobile App
	@GetMapping("/getPresentAbsentData/{student_id}")
	public ResponseEntity<Object> getPresentAbsentData(@PathVariable Integer student_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
					
			List<Map<String, Object>> product = studentAttendanceService.getPresentAbsentData(student_id);
			ResponseEntity<Object> student_marks_response_by_id = ResponseHandler.generateResponse(true,HttpStatus.OK, product);
			return student_marks_response_by_id;

			} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
			}		
		}
	
	//For Mobile App
	@GetMapping("/checkPresentOrAbsentForFeedback/{student_id}/{time_table_id}/{time_slots_id}")
	public ResponseEntity<Object> getCheckPresentOrAbsentForFeedback(@PathVariable Integer student_id, @PathVariable Integer time_table_id
			, @PathVariable Integer time_slots_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
					
		Boolean student_attendance = studentAttendanceService.getCheckPresentOrAbsentForFeedback(student_id, time_table_id, time_slots_id);
		  if(student_attendance==true) {	
			ResponseEntity<Object> student_attendance_response = ResponseHandler.generateResponse(true,HttpStatus.OK, student_attendance);
			return student_attendance_response;
		   } else {
				ResponseEntity<Object> student_attendance_response = ResponseHandler.generateResponse(true,HttpStatus.OK, student_attendance);
				return student_attendance_response;
			}
		}
			else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
			}		
		}
	
	@GetMapping("/getFacultiesForFeedback/{student_id}")
	public ResponseEntity<Object> getFacultiesForFeedback(@PathVariable Integer student_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
					
			List<Map<String, Object>> product = studentAttendanceService.getFacultiesForFeedback(student_id);
			ResponseEntity<Object> faculties = ResponseHandler.generateResponse(true,HttpStatus.OK, product);
			return faculties;

			} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
			}		
		}
	
	@GetMapping("/getAssignedCoursesForFeedback/{user_id}")
	public ResponseEntity<Object> getAssignedCoursesDetails(@PathVariable Integer user_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> sa_list = studentAttendanceService.getAssignedCoursesDetails(user_id);
			ResponseEntity<Object> sa_response = ResponseHandler.generateResponse(true, HttpStatus.OK, sa_list);
			return sa_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getCourseDetailsForAttendanceReport/{ac_year_id}/{program_assignment_id}/{program_id}/{current_year_sem}")
	public ResponseEntity<Object> getDetailedStudentAttendanceReportSectionwise(@PathVariable Integer ac_year_id,
			@PathVariable Integer program_assignment_id, @PathVariable Integer program_id, @PathVariable Integer current_year_sem) {
		try {
			List<Map<String, Object>> getDetailedStudentAttendanceReportSectionwise = studentAttendanceService
					.getCourseDetailsForAttendanceReport(ac_year_id,program_assignment_id,program_id,current_year_sem);
			return ResponseHandler.generateResponse(true,HttpStatus.OK, getDetailedStudentAttendanceReportSectionwise);
		} catch (NoSuchElementException e) {
			return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/getDetailedStudentAttendanceReportSectionwise/{ac_year_id}/{program_assignment_id}/{program_id}/{program_specialization_id}/{section_id}/{current_year_sem}/{course_id}/{course_assignment_id}")
	public ResponseEntity<Object> getDetailedStudentAttendanceReportSectionwise(@PathVariable Integer ac_year_id,
			@PathVariable Integer program_assignment_id, @PathVariable Integer program_id,
			@PathVariable Integer program_specialization_id,@PathVariable Integer section_id,
			@PathVariable Integer current_year_sem,@PathVariable Integer course_id,@PathVariable Integer course_assignment_id) {
		try {
			List<Map<String, Object>> getDetailedStudentAttendanceReportSectionwise = studentAttendanceService
					.getDetailedStudentAttendanceReportSectionwise(ac_year_id,program_assignment_id,program_id,
							program_specialization_id,section_id,current_year_sem,course_id,course_assignment_id);
			return ResponseHandler.generateResponse(true,HttpStatus.OK, getDetailedStudentAttendanceReportSectionwise);
		} catch (NoSuchElementException e) {
			return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
		}
	}
	
	
	@GetMapping("/getAttendanceReportForStudentProfileByStudentId/{studentId}/{year_or_sem}")
	public ResponseEntity<Object> getAttendanceReportForStudentProfileByStudentId(@PathVariable Integer studentId,@PathVariable Integer year_or_sem) {
		try {
			List<Map<String, Object>> attendanceReport= studentAttendanceService.getAttendanceReportForStudentProfileByStudentId(studentId,year_or_sem);
			return ResponseHandler.generateResponse(true,HttpStatus.OK, attendanceReport);
		} catch (NoSuchElementException e) {
			return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
		}
	}
	
	
	@GetMapping("/studentAttendanceReportSectionwise/{acYearId}/{programAssignmentId}/{programId}/{programSpecializationId}/{sectionId}/{currentYearSem}")
	public ResponseEntity<Object> studentAttendanceReportSectionwise(@PathVariable Integer acYearId,
			@PathVariable Integer programAssignmentId, @PathVariable Integer programId,
			@PathVariable Integer programSpecializationId,@PathVariable Integer sectionId,
			@PathVariable Integer currentYearSem) {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				List<Map<String, Object>> attendanceReport = studentAttendanceService.studentAttendanceReportSectionwise(acYearId,programAssignmentId,programId,
						programSpecializationId,sectionId,currentYearSem);
				return ResponseHandler.generateResponse(true,
						HttpStatus.OK, attendanceReport);
			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
						HttpStatus.NOT_FOUND);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);

		}
	}
			
	@GetMapping("/studentAttendanceDetailsForReport/{time_table_id}")
	public ResponseEntity<Object> studentAttendanceDetailsForReport(@PathVariable Integer time_table_id) {
		try {
			List<Map<String,Object>> studentAttendanceReport = studentAttendanceService.studentAttendanceDetailsForReport(time_table_id);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, studentAttendanceReport);
			
		} catch (NoSuchElementException e) {
			return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			
		}
	}
	
	
	@GetMapping("/getStudentAttendanceReportSectionwise/{ac_year_id}/{program_assignment_id}/{program_id}/{program_specialization_id}/{section_id}/{current_year_sem}")
	public ResponseEntity<Object> getStudentAttendanceReportSectionwise(@PathVariable Integer ac_year_id,
			@PathVariable Integer program_assignment_id, @PathVariable Integer program_id,
			@PathVariable Integer program_specialization_id,@PathVariable Integer section_id,
			@PathVariable Integer current_year_sem) {
		try {
			List<Map<String, Object>> getStudentAttendanceReportSectionwise = studentAttendanceService
					.getStudentAttendanceReportSectionwise(ac_year_id,program_assignment_id,program_id,
							program_specialization_id,section_id,current_year_sem);
			return ResponseHandler.generateResponse(true,
					HttpStatus.OK, getStudentAttendanceReportSectionwise);
		} catch (NoSuchElementException e) {
			return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
					HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/studentDetailsForAttendanceByTimeTable/{timeTableId}")
	public ResponseEntity<Object> studentDetailsForAttendanceByTimeTable(@PathVariable Integer timeTableId) {
		try {
			return studentAttendanceService.studentDetailsForAttendanceByTimeTable(timeTableId);
			
		} catch (NoSuchElementException e) {
			return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			
		}
	}
	
	@GetMapping("/getDetailedStudentAttendanceOfStudentByCourse/{student_id}/{ac_year_id}/{course_assignment_id}/{course_id}/{current_year_sem}")
	public ResponseEntity<Object> getDetailedStudentAttendanceOfStudentByCourse(@PathVariable Integer student_id,
			@PathVariable Integer ac_year_id,@PathVariable Integer course_assignment_id,
			@PathVariable Integer course_id,@PathVariable Integer current_year_sem) {
		try {
			
			List<Map<String, Object>> studentAttendanceReports = studentAttendanceService
					.getDetailedStudentAttendanceOfStudentByCourse(student_id,ac_year_id,course_assignment_id,course_id,current_year_sem);
			return ResponseHandler.generateResponse(true,HttpStatus.OK, studentAttendanceReports);
		} catch (NoSuchElementException e) {
			return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/getAttendanceData/{student_id}/{course_id}/{year_or_sem}")
	public ResponseEntity<Object> getAttendanceData(@PathVariable Integer student_id, @PathVariable Integer course_id, @PathVariable Integer year_or_sem) {
		if(RateLimitController.bucket.tryConsume(1)) {
					
			Map<String, Object> product = studentAttendanceService.getAttendanceData(student_id, course_id, year_or_sem);
			ResponseEntity<Object> student_marks_response_by_id = ResponseHandler.generateResponse(true,HttpStatus.OK, product);
			return student_marks_response_by_id;

			} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
			}		
		}
	
	@GetMapping("/getDetailedStudentAttendanceReportSectionwiseForEmployee")
	public ResponseEntity<Object> getDetailedStudentAttendanceReportSectionwiseForEmployee(@RequestParam(value="ac_year_id",required = true) Integer ac_year_id,
			@RequestParam(value="emp_id",required = true) Integer emp_id, @RequestParam(value="course_assignment_id",required = true) Integer course_assignment_id, 
			@RequestParam(value="section_id",required = false) Integer section_id) {
		try {
			Map<Object, List<Map<String, Object>>> getDetailedStudentAttendanceReportSectionwiseForEmployee = studentAttendanceService
					.getDetailedStudentAttendanceReportSectionwiseForEmployee(ac_year_id, emp_id, course_assignment_id, section_id);
			return ResponseHandler.generateResponse(true,HttpStatus.OK, getDetailedStudentAttendanceReportSectionwiseForEmployee);
		} catch (NoSuchElementException e) {
			return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
		}
	}
	
}
