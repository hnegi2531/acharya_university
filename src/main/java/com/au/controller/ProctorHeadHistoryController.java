package com.au.controller;

import java.io.IOException;
import java.util.List;
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
import com.au.model.ProctorHeadHistory;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.ProctorHeadHistoryService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey7}")
@CrossOrigin
public class ProctorHeadHistoryController {

	@Autowired
	private ProctorHeadHistoryService phhs_service;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/ProctorHeadHistory")
	public ResponseEntity<Object> saveProctorHeadHistory1(@RequestBody List<ProctorHeadHistory> proctor_assign,
			@RequestHeader("Authorization") String jwtToken)
					throws Exception, JsonParseException, JsonMappingException, IOException {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				if(RateLimitController.bucket.tryConsume(1)) {
					proctor_assign.stream().forEach(f -> {
						f.setModified_username(jwtDetails.getUserName());
					});
					List<ProctorHeadHistory> proc_stu_assignment = phhs_service.saveProctorHeadHistory1(proctor_assign);
					ResponseEntity<Object> proc_stu_assignment_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, proc_stu_assignment);
					return proc_stu_assignment_response;
					} else {
						ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
						return rs;
					}
			}

	@PostMapping("/ProctorHeadHistory/{proctor_assign_id}")
	public void saveProctorHeadHistory(@PathVariable List<Integer> proctor_assign_id,
			@RequestHeader("Authorization") String jwtToken) throws Exception {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		 phhs_service.saveProctorHeadHistory(proctor_assign_id,jwtDetails.getUserName());
	}

	@GetMapping("/ProctorHeadHistory")
	public List<ProctorHeadHistory> listAll() {
		return phhs_service.listAll();
	}

	@GetMapping("/fetchProctorHeadHistoryDetail/{student_id}") // (Behalf of student_id)
	public List<ProctorHeadHistory> fetch1(@RequestBody @PathVariable Integer student_id) {
		return phhs_service.findByProctorHeadHistory(student_id);
	}
}
