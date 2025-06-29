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
import com.au.model.ProgramAssigment;
import com.au.model.StdReportingStudentsHistory;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.StdReportingStudentsHistoryService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class StdReportingStudentsHistoryController {

	Logger log = LoggerFactory.getLogger(StdReportingStudentsHistoryController.class);
	
	@Autowired
	private StdReportingStudentsHistoryService s_service;
	
	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/StdReportingStudentsHistory")
	public ResponseEntity<Object> saveCourse(@RequestBody @Valid StdReportingStudentsHistory r,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		r.setCreated_by(jwtDetails.getUserId());
		r.setCreated_username(jwtDetails.getUserName());

		StdReportingStudentsHistory srsh = s_service.save_StdReportingStudentsHistory(r);
		ResponseEntity<Object> srsh_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, srsh);
		return srsh_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PostMapping("/createMultipleStdReportingStudentsHistory")
	public ResponseEntity<Object> createMultipleStdReportingStudentsHistory(@RequestBody @Valid List<StdReportingStudentsHistory> r,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		r.stream().forEach(r1 -> {
			
		r1.setCreated_by(jwtDetails.getUserId());
		r1.setCreated_username(jwtDetails.getUserName());
		});

		List<StdReportingStudentsHistory> srsh = s_service.saveMultipleStdReportingStudentsHistory(r);
		ResponseEntity<Object> srsh_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, srsh);
		return srsh_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/StdReportingStudentsHistory")
	public ResponseEntity<Object> getAllStdReportingStudentsHistory(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> reportingStudentsHistory_filtered =  s_service.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
			return reportingStudentsHistory_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> reportingStudentsHistory_sorted = s_service.getAllSortedData(pageable1);
			return reportingStudentsHistory_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	
	@GetMapping("/StdReportingStudentsHistory/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	StdReportingStudentsHistory srsh = s_service.get(id);
	    	ResponseEntity<Object> srsh_response= ResponseHandler.generateResponse(true, HttpStatus.OK, srsh);
			return srsh_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/StdReportingStudentsHistory/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid StdReportingStudentsHistory r, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	StdReportingStudentsHistory existProduct = s_service.get(id);
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

			r.setModified_by(jwtDetails.getUserId());
			r.setModified_username(jwtDetails.getUserName());
			
			s_service.save_StdReportingStudentsHistory(r);
			ResponseEntity<Object> srsh_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return srsh_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}   
	}
	
	
	@DeleteMapping("/StdReportingStudentsHistory/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		s_service.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/getAllStudentDetailsForHistoryIndex")
	public ResponseEntity<Object> getAllStudentDetailsForHistoryIndex(@RequestParam(value ="school_id") Integer school_id, @RequestParam(value ="program_id") Integer program_id,
			@RequestParam(value ="program_specialization_id") Integer program_specialization_id,@RequestParam(value ="current_sem",required = false) Integer current_sem,
			@RequestParam(value ="current_year",required = false) Integer current_year) {
		if (RateLimitController.bucket.tryConsume(1)) {
			if(current_sem != null) {
			List<Map<String, Object>> stu_details_on_sem = s_service.fetchAllStudentDetailsForHistoryIndexOnSem( school_id,program_id,program_specialization_id, current_sem);
			ResponseEntity<Object> student_response_on_sem = ResponseHandler.generateResponse(true, HttpStatus.OK,
					stu_details_on_sem);
			return student_response_on_sem;
			}else{
				List<Map<String, Object>> stu_details_on_year = s_service.fetchAllStudentDetailsForHistoryIndexOnYear(school_id, program_id,program_specialization_id, current_year);
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
	
	@GetMapping("/reportingStudentsHistoryByStudentId/{student_id}")
	public ResponseEntity<Object> fetchReportingStudentsHistoryByStudentId(@PathVariable Integer student_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	List<Map<String, Object>> srsh = s_service.fetchReportingStudentsHistoryByStudentId(student_id);
	    	ResponseEntity<Object> srsh_response= ResponseHandler.generateResponse(true, HttpStatus.OK, srsh);
			return srsh_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchProgramWithSpecializationBySchoolId/{school_id}")
	public ResponseEntity<Object> fetchProgramWithSpecializationBySchoolId(@PathVariable Integer school_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> prog_ass = s_service.fetchProgramWithSpecializationBySchoolId(school_id);
			ResponseEntity<Object> prog_ass_response= ResponseHandler.generateResponse(true, HttpStatus.OK, prog_ass);
			return prog_ass_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/FetchAcademicProgram1/{program_id}/{school_id}")
	public ResponseEntity<Object> getNumOfSemAndYearByProgram_IdAndSchool_Id(@PathVariable Integer program_id,@PathVariable Integer school_id)
	{
		if(RateLimitController.bucket.tryConsume(1)) {
			List<ProgramAssigment> prog_ass = s_service.getNumOfSemAndYearByProgram_IdAndSchool_Id(program_id,school_id);
			ResponseEntity<Object> prog_ass_response= ResponseHandler.generateResponse(true, HttpStatus.OK, prog_ass);
			return prog_ass_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
}
