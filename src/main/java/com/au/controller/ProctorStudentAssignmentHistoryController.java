package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.JwtDetails;
import com.au.model.ProctorStudentAssignmentHistory;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.ProctorStudentAssignmentHistoryService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey7}")
@CrossOrigin
public class ProctorStudentAssignmentHistoryController {
	

	Logger log = LoggerFactory.getLogger(ProctorStudentAssignmentController.class);

	@Autowired
	private JwtTokenService jwt_service;

	@Autowired
	private ProctorStudentAssignmentHistoryService psas_service;
	
	@PostMapping("/ProctorStudentAssignmentHistory")
	public ResponseEntity<Object> saveProctorStudentAssignment(@RequestBody List<ProctorStudentAssignmentHistory> prochistory,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		if(RateLimitController.bucket.tryConsume(1)) {
			prochistory.stream().forEach(ph -> {
				
				ph.setCreated_by(jwtDetails.getUserId());
				ph.setCreated_username(jwtDetails.getUserName());
			});
			List<ProctorStudentAssignmentHistory> proc_stu_assignment = psas_service.saveProctorStudentAssignments(prochistory, jwtToken);
			ResponseEntity<Object> proc_stu_assignment_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, proc_stu_assignment);
			return proc_stu_assignment_response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
	}
	
	@GetMapping("/fetchAllProctorStudentAssignmentHistoryDetail")
	public ResponseEntity<Object> getAllProctorHeadDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			 @RequestParam(value="UserId", required = false) Integer userId,
		        @RequestParam(value="school_id", required = false) Integer school_id,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> proc_stu_assignment_filtered =  psas_service.getAllDataFilteredByKeyword(pageable, keyword,userId ,school_id);//,column,value);
			return proc_stu_assignment_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> proc_stu_assignment_sorted = psas_service.getAllSortedData(pageable1,userId ,school_id);
			return proc_stu_assignment_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/proctorStudentAssignmentHistoryDetailByUserId/{userId}")
	public ResponseEntity<Object> proctorStudentAssignmentHistoryDetailByUserId(@PathVariable Integer userId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>>  proctorStudentAssignmentHistoryDetail = psas_service.proctorStudentAssignmentHistoryDetailByUserId(userId);
			ResponseEntity<Object> proctorStudentAssignmentHistoryDetailResponse= ResponseHandler.generateResponse(true, HttpStatus.OK, proctorStudentAssignmentHistoryDetail);
			return proctorStudentAssignmentHistoryDetailResponse;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}


}
