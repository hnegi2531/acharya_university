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
import com.au.model.LeadAssignment;
import com.au.repository.CandidateWalkinRepository;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.LeadAssignmentService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;



@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class LeadAssignmentController {
	
	@Autowired
	private LeadAssignmentService lead_ser;
	
	@Autowired
	private CandidateWalkinRepository can_repo;
	
	
	@Autowired
	private JwtTokenService jwt_service;
	
	Logger log = LoggerFactory.getLogger(AcademicSchoolController.class);
	
	@PostMapping("/saveLeadAssignment")
	public ResponseEntity<Object> saveLeadAssignment(@RequestBody @Valid LeadAssignment la,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				la.setCreated_by(jwtDetails.getUserId());
				la.setCreated_username(jwtDetails.getUserName());
				LeadAssignment leads = lead_ser.saveLeadAssignment(la);
				can_repo.updateCounselorStatus(leads.getCandidate_id());
				ResponseEntity<Object> leads_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, leads);
				return leads_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@GetMapping("/getActiveLeadAssignment")
	public ResponseEntity<Object> getActiveLeadAssignment() {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<LeadAssignment> leads = lead_ser.getActiveLeadAssignment();
				ResponseEntity<Object> leads_response1= ResponseHandler.generateResponse(true, HttpStatus.OK, leads);
				return leads_response1;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/fetchAllLeadAssignment")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> lead_details = lead_ser.listAll1(pageable, keyword);//,column,value);
						return lead_details;
				}else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> lead_details = lead_ser.listAll2(pageable1);
						return lead_details;
			}
			
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

	@GetMapping("/getLeadAssignmentById/{lead_assignment_id}")
	public ResponseEntity<Object> get(@PathVariable Integer lead_assignment_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
					try {

							LeadAssignment product = lead_ser.get(lead_assignment_id);
							ResponseEntity<Object> leads_response= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
							return leads_response;

					} catch (NoSuchElementException e) {
							ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
							return response1;
					}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@PutMapping("/updateLeadAssignment/{lead_assignment_id}")
	public ResponseEntity<Object> update(@RequestBody LeadAssignment la, @PathVariable Integer lead_assignment_id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {

				try {
						LeadAssignment existProduct = lead_ser.get(lead_assignment_id);
						JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
						la.setModified_by(jwtDetails.getUserId());
						la.setModified_username(jwtDetails.getUserName());
						lead_ser.saveLeadAssignment(la);
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

	@DeleteMapping("/deactivateLeadAssignment/{lead_assignment_id}")
	public ResponseEntity<Object> delete(@PathVariable Integer lead_assignment_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				return lead_ser.delete(lead_assignment_id);
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@DeleteMapping("/activateLeadAssignment/{lead_assignment_id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer lead_assignment_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				return lead_ser.delete1(lead_assignment_id);
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/getLeadsAssignedToUser/{user_id}")
	public ResponseEntity<Object> getLeadsAssignedToUser(@PathVariable Integer user_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
					try {

							List<Map<String, Object>> product = lead_ser.getLeadsAssignedToUser(user_id);
							ResponseEntity<Object> leads_response= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
							return leads_response;

					} catch (NoSuchElementException e) {
							ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
							return response1;
					}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@PutMapping("/updateMultipleLeadAssignment/{lead_assignment_ids}")
	public ResponseEntity<Object> updateMultipleLeadAssignment(@RequestBody List<LeadAssignment> la, @PathVariable List<Integer> lead_assignment_ids,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				try {
					la.stream().forEach(l->{
						l.setModified_by(jwtDetails.getUserId());
						l.setModified_username(jwtDetails.getUserName());
					});

						lead_ser.updateLeadAssignments(la);
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

}
