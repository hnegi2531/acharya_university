package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
import com.au.dto.IntakeAssignmentDto;
import com.au.dto.JwtDetails;
import com.au.model.IntakeAssignment;
import com.au.response.ResponseHandler;
import com.au.service.IntakeAssignmentService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class IntakeAssignmentController {
	
	Logger log = LoggerFactory.getLogger(IntakeAssignmentController.class);

	@Autowired
	private IntakeAssignmentService intake_assign_service;

	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/intakeAssignment")
	public ResponseEntity<Object> saveIntakeAssignment(@RequestBody @Valid IntakeAssignmentDto iad,@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			iad.getIntake_assignment().stream().forEach(ia -> {
				ia.setCreated_by(jwtDetails.getUserId());
				ia.setCreated_username(jwtDetails.getUserName());
			});
			List<IntakeAssignment> intake_assign_list = intake_assign_service.saveIntakeAssignment(iad);
			ResponseEntity<Object> intake_assign_list_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, intake_assign_list);
			return intake_assign_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@PostMapping("/copiedIntakeAssignment")
	public ResponseEntity<Object> copiedIntakeAssignmentCopied(@RequestBody @Valid IntakeAssignment ia,@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				ia.setCreated_by(jwtDetails.getUserId());
				ia.setCreated_username(jwtDetails.getUserName());
			IntakeAssignment intake_assign_list = intake_assign_service.copiedIntakeAssignmentCopied(ia);
			ResponseEntity<Object> intake_assign_list_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, intake_assign_list);
			return intake_assign_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/intakeAssignment")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<IntakeAssignment> intake_assign_list = intake_assign_service.listAll();
				ResponseEntity<Object> intake_assign_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK,intake_assign_list);
				return intake_assign_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/fetchAllIntakeAssignment")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> intake_assign_sorted = intake_assign_service.listAll1(pageable, keyword);//,column,value);
					return intake_assign_sorted;
				}else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> intake_assign_pageable = intake_assign_service.listAll2(pageable1);
					return intake_assign_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/intakeAssignment/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				IntakeAssignment product = intake_assign_service.get(id);
				ResponseEntity<Object> intake_assign_response_by_id = ResponseHandler.generateResponse(true,HttpStatus.OK, product);
				return intake_assign_response_by_id;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}		
	}
	
	@PutMapping("/intakeAssignment/{id}")
	public ResponseEntity<Object> update(@RequestBody IntakeAssignment ia, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
					intake_assign_service.get(id);
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					ia.setModified_by(jwtDetails.getUserId());
					ia.setModified_username(jwtDetails.getUserName());
					intake_assign_service.updateIntakeAssignment(ia);
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
	
	@DeleteMapping("/intakeAssignment/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			intake_assign_service.delete(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@DeleteMapping("/activateIntakeAssignment/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			intake_assign_service.delete1(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	
	@GetMapping("/intakeAssignmentAndPermitDetailsOnAcademicYear/{ac_year_id}")
	public ResponseEntity<Object> intakeAssignmentAndPermitDetailsOnAcademicYear(@PathVariable Integer ac_year_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<HashMap<String,Object>> intake_assign_and_permit = intake_assign_service.intakeAssignmentAndPermitDetailsOnAcademicYear(ac_year_id);
				return ResponseHandler.generateResponse(true,HttpStatus.OK, intake_assign_and_permit);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
		}		
	}
	
	@GetMapping("/intakeAssignmentAndPermitDetails/{intake_id}")
	public ResponseEntity<Object> getSingleIntakeAssignmentAndPermitDetailsData(@PathVariable Integer intake_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<HashMap<String,Object>> intake_assign_and_permit = intake_assign_service.intakeAssignmentAndPermitDetails(intake_id);
				return ResponseHandler.generateResponse(true,HttpStatus.OK, intake_assign_and_permit);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
		}		
	}
	
	@GetMapping("/intakeAssignmentDetailsForGridView/{ac_year_id}/{school_id}/{graduation_id}")
	public ResponseEntity<Object> intakeAssignmentDetailsForGridView(@PathVariable Integer ac_year_id,@PathVariable Integer school_id,@PathVariable Integer graduation_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<HashMap<String,Object>> intake_assign_details = intake_assign_service.intakeAssignmentDetailsForGridView(ac_year_id,school_id,graduation_id);
				ResponseEntity<Object> intake_assign_details_response = ResponseHandler.generateResponse(true,HttpStatus.OK, intake_assign_details);
				return intake_assign_details_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/intakeAssignmentDetails/{intake_id}")
	public ResponseEntity<Object> intakeAssignmentDetails(@PathVariable Integer intake_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<HashMap<String,Object>> intake_assign_and_permit = intake_assign_service.intakeAssignmentDetails(intake_id);
				ResponseEntity<Object> intake_assign_and_permit_response = ResponseHandler.generateResponse(true,HttpStatus.OK, intake_assign_and_permit);
				return intake_assign_and_permit_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/intakeAssignmentProgramSpecializationDetails/{ac_year_id}/{school_id}/{program_id}")
	public ResponseEntity<Object> intakeAssignmentProgramSpecializationDetails(@PathVariable Integer ac_year_id, @PathVariable Integer school_id, @PathVariable Integer program_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<Integer> intake_assign_specialization_id = intake_assign_service.intakeAssignmentProgramSpecializationDetails(ac_year_id,school_id,program_id);
				ResponseEntity<Object> intake_assign_specialization_id_response = ResponseHandler.generateResponse(true,HttpStatus.OK, intake_assign_specialization_id );
				return intake_assign_specialization_id_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/intakeNotAssignedfeeAdmissionCategory/{acYearId}/{schoolId}/{programSpecializationId}")
	public ResponseEntity<Object> intakeNotAssignedfeeAdmissionCategory(@PathVariable Integer acYearId, @PathVariable Integer schoolId, @PathVariable Integer programSpecializationId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String,Object>> intakeAssignfeeAdmissionCategory = intake_assign_service.intakeNotAssignedfeeAdmissionCategory(acYearId,schoolId,programSpecializationId);
				return ResponseHandler.generateResponse(true,HttpStatus.OK, intakeAssignfeeAdmissionCategory );
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}		
	}
	
}
