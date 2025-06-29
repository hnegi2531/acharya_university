package com.au.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.JwtDetails;
import com.au.model.SectionAssignment;
import com.au.model.SectionAssignmentHistory;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.SectionAssignmentHistoryService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class SectionAssignmentHistoryController {
	
	Logger log = LoggerFactory.getLogger(SectionAssignmentHistoryController.class);
	
	@Autowired
	private SectionAssignmentHistoryService sec_assign_his_ser;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	
	@PostMapping("/sectionAssignmentHistory")
	public ResponseEntity<Object> saveSectionAssignmentHistory(@RequestBody @Valid SectionAssignmentHistory sah,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException, Exception {
		
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			sah.setCreated_by(jwtDetails.getUserId());
			sah.setCreated_username(jwtDetails.getUserName());
			SectionAssignmentHistory section_assignment_history = sec_assign_his_ser.SectionAssignmentHistory(sah);
			ResponseEntity<Object> section_assignment_history_response = ResponseHandler.generateResponse(true,
					HttpStatus.CREATED, section_assignment_history);
			return section_assignment_history_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/sectionAssignmentHistoryOnSectionAssignmentId/{section_assignment_id}")
	public ResponseEntity<Object> sectionAssignmentHistoryOnSectionAssignmentId(
			@PathVariable Integer section_assignment_id) {
		
		if (RateLimitController.bucket.tryConsume(1)) {
			List<SectionAssignmentHistory> section_assignment_history_list = sec_assign_his_ser
					.sectionAssignmentHistoryOnSectionAssignmentId(section_assignment_id);
			ResponseEntity<Object> section_assignment_history_list_response = ResponseHandler.generateResponse(true,
					HttpStatus.OK, section_assignment_history_list);
			return section_assignment_history_list_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
}
