package com.au.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

import com.au.dto.InternalFacultyRoomAssignmentDto;
import com.au.dto.InternalFacultyRoomAssignmentDtoUpdate;
import com.au.dto.JwtDetails;
import com.au.model.InternalFacultyRoomAssignment;
import com.au.response.ResponseHandler;
import com.au.service.InternalFacultyRoomAssignmentService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;




@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class InternalFacultyRoomAssignmentController {
	
Logger log = LoggerFactory.getLogger(InternalTimeTableController.class);
	

	@Autowired
	private InternalFacultyRoomAssignmentService itt_service;

	@Autowired
	private JwtTokenService jwt_service;
	
	

	@PostMapping("/internalFacultyRoomAssignment")
	public ResponseEntity<Object> saveInternalFacultyRoomAssignment(@RequestBody @Valid InternalFacultyRoomAssignmentDto internalFacultyRoomAssignmentDto,@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {

			List<InternalFacultyRoomAssignment> itts = itt_service.saveInternalFacultyRoomAssignment(internalFacultyRoomAssignmentDto, jwtToken);
			ResponseEntity<Object> internalTimeTable_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, itts);
		return internalTimeTable_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/internalFacultyRoomAssignment")
	public ResponseEntity<Object> listAllActiveInternalTimeTable() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<InternalFacultyRoomAssignment> itts = itt_service.listAllActiveInternalFacultyRoomAssignment();
			ResponseEntity<Object> internalTimeTable_response= ResponseHandler.generateResponse(true, HttpStatus.OK, itts);
			return internalTimeTable_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/fetchAllInternalFacultyRoomAssignment")
	public ResponseEntity<Object> getAllInternalTypesDetails1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
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
			ResponseEntity<Object> internalTimeTable_filtered =  itt_service.getAllDataFilteredByKeyword(pageable, keyword ,ac_year_id,school_id,dept_id, program_specialization_id, internal_short_name);
			return internalTimeTable_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> internalTimeTable_sorted = itt_service.getAllSortedData(pageable1,ac_year_id,school_id,dept_id, program_specialization_id, internal_short_name);
			return internalTimeTable_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/internalFacultyRoomAssignment/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	InternalFacultyRoomAssignment itts = itt_service.get(id);
	    	ResponseEntity<Object> internalTimeTable_response= ResponseHandler.generateResponse(true, HttpStatus.OK, itts);
			return internalTimeTable_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@PutMapping("/internalFacultyRoomAssignment/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid InternalFacultyRoomAssignmentDtoUpdate itt, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {

	    	itt_service.saveInternalFacultyRoomAssignments(itt, jwtToken);
	    	ResponseEntity<Object> internalTimeTable_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return internalTimeTable_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	@PutMapping("/updateEmploeeForInternalFacultyRoomAssignment/{internal_room_assignment_ids}/{new_emp_id}")
	public ResponseEntity<Object> update(@PathVariable List<Integer> internal_room_assignment_ids,@PathVariable Integer new_emp_id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {

	    	itt_service.saveInternalFacultyRoomAssignments(internal_room_assignment_ids, new_emp_id, jwtToken);
	    	ResponseEntity<Object> internalTimeTable_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return internalTimeTable_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	@PutMapping("/updateRoomForInternalFacultyRoomAssignment/{internal_room_assignment_ids}/{new_room_id}")
	public ResponseEntity<Object> updateRoomForInternalFacultyRoomAssignment(@PathVariable List<Integer> internal_room_assignment_ids,@PathVariable Integer new_room_id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {

	    	itt_service.saveInternalFacultyRoomAssignments1(internal_room_assignment_ids, new_room_id, jwtToken);
	    	ResponseEntity<Object> internalTimeTable_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return internalTimeTable_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	
	@DeleteMapping("/internalFacultyRoomAssignment/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			itt_service.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateInternalFacultyRoomAssignment/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			itt_service.delete1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/internalFacultyRoomAssignmentForAllData/{school_id}/{program_id}/{program_specialization_id}/{ac_year_id}/{year_sem}")
	public ResponseEntity<Object> listAllActiveInternalTimeTableData(@PathVariable Integer school_id,@PathVariable Integer program_id,@PathVariable Integer program_specialization_id,@PathVariable Integer ac_year_id,@PathVariable Integer year_sem) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> itts1 = itt_service.listAllActiveInternalFacultyRoomAssignmentData(school_id,program_id,program_specialization_id,ac_year_id,year_sem);
			ResponseEntity<Object> internalTimeTable_response= ResponseHandler.generateResponse(true, HttpStatus.OK, itts1);
			return internalTimeTable_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/internalFacultyRoomAssignmentDataBasisOfDOE/{internal_session_id}")
	public ResponseEntity<Object> listAllActiveInternalTimeTableDataBasisOfDOE(@PathVariable Integer internal_session_id) throws ParseException {
		if(RateLimitController.bucket.tryConsume(1)) {
			
			List<Map<String, Object>> itts1 = itt_service.listAllActiveInternalFacultyRoomAssignmentDataBasisOfDOE(internal_session_id);
			ResponseEntity<Object> internalTimeTable_response= ResponseHandler.generateResponse(true, HttpStatus.OK, itts1);
			return internalTimeTable_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getCoursesForInternalsFromTimeTable")
	public ResponseEntity<Object> getCoursesForInternals(@RequestParam(value ="school_id") Integer school_id,@RequestParam(value ="program_specialization_id") Integer program_specialization_id,
			@RequestParam(value ="ac_year_id") Integer ac_year_id,@RequestParam(value ="current_year",required = false) Integer current_year,
			@RequestParam(value ="current_sem",required = false) Integer current_sem) {
		if(RateLimitController.bucket.tryConsume(1)) {
			if(current_sem != null) {
				Set<Map<String, Object>> coursesOnSem = itt_service.getCoursesForInternalsOnSem(school_id, ac_year_id,program_specialization_id, current_sem);
				return ResponseHandler.generateResponse(true, HttpStatus.OK, coursesOnSem);
			}else {
				Set<Map<String, Object>> coursesOnYear = itt_service.getCoursesForInternalsOnyear(school_id, ac_year_id,program_specialization_id, current_year);
				return ResponseHandler.generateResponse(true, HttpStatus.OK, coursesOnYear);
			}
			
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
		
	}
	
	@GetMapping("/getUnoccupiedEmployeesForInternals1/{time_slots_id}/{date}")
	public ResponseEntity<Object> listOfStudentDetails(@PathVariable Integer time_slots_id,@PathVariable String date) throws ParseException {
		if(RateLimitController.bucket.tryConsume(1)) {
			
//			DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
//			Date selected_date=df.parse(date);
			List<Map<String, Object>> itts1 = itt_service.getUnoccupiedEmployeesForInternals(time_slots_id,date);
			ResponseEntity<Object> internalTimeTable_response= ResponseHandler.generateResponse(true, HttpStatus.OK, itts1);
			return internalTimeTable_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getRoomsForInternals")
	public ResponseEntity<Object> getRoomsForInternals() throws ParseException {
		if(RateLimitController.bucket.tryConsume(1)) {
			
//			DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
//			Date selected_date=df.parse(date);
			List<Map<String, Object>> itts1 = itt_service.getRoomsForInternals();
			ResponseEntity<Object> internalTimeTable_response= ResponseHandler.generateResponse(true, HttpStatus.OK, itts1);
			return internalTimeTable_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
//	@GetMapping("/getStudentDetailsToView/{internal_timetable_assignment_id}")
//	public ResponseEntity<Object> listOfStudentDetails(@PathVariable Integer internal_timetable_assignment_id) throws ParseException {
//		if(RateLimitController.bucket.tryConsume(1)) {
//			
//			List<Map<String, Object>> itts1 = itt_service.listOfStudentDetails(internal_timetable_assignment_id);
//			ResponseEntity<Object> internalTimeTable_response= ResponseHandler.generateResponse(true, HttpStatus.OK, itts1);
//			return internalTimeTable_response;
//		
//		}else {
//
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}
//	}
//	
//	@GetMapping("/internalTimeTableAssignmentDetailsByUserId/{userId}")
//	public ResponseEntity<Object> internalTimeTableAssignmentDetailsByUserId(@PathVariable Integer userId) {
//		if(RateLimitController.bucket.tryConsume(1)) {
//			List<Map<String, Object>> list_itta = itt_service.internalTimeTableAssignmentDetailsByUserId(userId);
//			ResponseEntity<Object> list_itta_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_itta);
//			return list_itta_response;
//		}else {
//
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}
//	}
//	
	@GetMapping("/internalTimeTableAssignmentDetailsByEmployeeId/{employeeId}")
	public ResponseEntity<Object> internalTimeTableAssignmentDetailsByEmployeeId(@PathVariable Integer employeeId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> list_itta = itt_service.internalTimeTableAssignmentDetailsByEmployeeId(employeeId);
			ResponseEntity<Object> list_itta_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_itta);
			return list_itta_response;
		}else {

			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/internalTimeTableAssignmentDetailsByInternalSessionId/{internal_session_id}")
	public ResponseEntity<Object> internalTimeTableAssignmentDetailsByInternalSessionId(@PathVariable Integer internal_session_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> list_itta = itt_service.internalTimeTableAssignmentDetailsByInternalSessionId(internal_session_id);
			ResponseEntity<Object> list_itta_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_itta);
			return list_itta_response;
		}else {

			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getStudentIdsByInternalSessionId/{internal_session_id}")
	public ResponseEntity<Object> getStudentIdsByInternalSessionId(@PathVariable Integer internal_session_id) throws ParseException {
		if(RateLimitController.bucket.tryConsume(1)) {
			
			String itts1 = itt_service.getStudentIdsByInternalSessionId(internal_session_id);
			ResponseEntity<Object> internalTimeTable_response= ResponseHandler.generateResponse(true, HttpStatus.OK, itts1);
			return internalTimeTable_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("internalTimeTableAssignmentDetailsByLecturerEmployeeId")
	public ResponseEntity<Object> getAllInternalTypesDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword, @RequestParam(value="user_id",required = false) Integer user_id) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> internalTimeTable_filtered =  itt_service.getAllDataFilteredByKeyword1(pageable, keyword, user_id);
			return internalTimeTable_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> internalTimeTable_sorted = itt_service.getAllSortedData1(pageable1, user_id);
			return internalTimeTable_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
//	
//	@GetMapping("/checkInternalExamAttendanceStatus/{internalTimetableAssignmentId}")
//	public ResponseEntity<Object> checkInternalExamAttendanceStatus(@PathVariable Integer internalTimetableAssignmentId) throws ParseException {
//		if (RateLimitController.bucket.tryConsume(1)) {
//			Map<String, Object> examAttendanceStatus = itt_service.checkInternalExamAttendanceStatus(internalTimetableAssignmentId);
//			ResponseEntity<Object> checkAttendanceStatusResponse = ResponseHandler.generateResponse(true, HttpStatus.OK, examAttendanceStatus);
//			return checkAttendanceStatusResponse;
//			
//		} else {
//			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
//					ResponseHandler.message1);
//			return rs;
//		}
//	}	
//	
//	
//	@GetMapping("/internalStudentDetailsData/{time_slots_id}/{room_id}/{date}")
//	public ResponseEntity<Object> internalStudentDetailsData(@PathVariable Integer time_slots_id,@PathVariable Integer room_id,@PathVariable String date) throws ParseException {
//		if(RateLimitController.bucket.tryConsume(1)) {
//			List<Map<String, Object>> itta = itt_service.internalStudentDetailsData(time_slots_id,room_id,date);
//			ResponseEntity<Object> itta_response= ResponseHandler.generateResponse(true, HttpStatus.OK, itta);
//			return itta_response;
//		} else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}
//	}	
	

}
