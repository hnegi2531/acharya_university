package com.au.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.JwtDetails;
import com.au.model.CourseAssignmentEmployee;
import com.au.model.CourseObjective;
import com.au.model.Department;
import com.au.model.FeeTemplateRemarks;
import com.au.model.FeeTemplateRemarks;
import com.au.response.ResponseHandler;
import com.au.service.FeeTemplateRemarksService;
import com.au.service.FeeTemplateRemarksService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
public class FeeTemplateRemarksController {

	Logger log = LoggerFactory.getLogger(FeeTemplateRemarksController.class);
	
	@Autowired
	private FeeTemplateRemarksService feeTemplateRemarksService;

	@Autowired
	private JwtTokenService jwt_service;
	
	
	
	@PostMapping("/saveFeeTemplateRemarks")
	public ResponseEntity<Object> saveFeeTemplateRemarks(@RequestBody @Valid List<FeeTemplateRemarks> co,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			co.stream().forEach(f -> {

			f.setCreated_by(jwtDetails.getUserId());
			f.setCreated_username(jwtDetails.getUserName());
			});
			List<FeeTemplateRemarks> cos = feeTemplateRemarksService.saveFeeTemplateRemarks(co);
			ResponseEntity<Object> feeTemplateRemarks_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, cos);
		return feeTemplateRemarks_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/AllActiveFeeTemplateRemarks")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<FeeTemplateRemarks> cos = feeTemplateRemarksService.listAll1();
		ResponseEntity<Object> co_response= ResponseHandler.generateResponse(true, HttpStatus.OK, cos);
		return co_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	
	@GetMapping("/getFeeTemplateRemarks/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		try {
			FeeTemplateRemarks product = feeTemplateRemarksService.get(id);
			ResponseEntity<Object> course_response_by_id = ResponseHandler.generateResponse(true, HttpStatus.OK, product);
			return course_response_by_id;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response1;
		}
	}

	@PutMapping("/updateFeeTemplateRemarks/{id}")
	public ResponseEntity<Object> updateFeeTemplateRemarks(@RequestBody @Valid List<FeeTemplateRemarks> courseObjective,
			@PathVariable List<Integer> id, @RequestHeader("Authorization") String jwtToken) throws Exception{
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				courseObjective.stream().forEach(p -> {

					p.setCreated_by(jwtDetails.getUserId());
					p.setCreated_username(jwtDetails.getUserName());
				});
				feeTemplateRemarksService.updateFeeTemplateRemarks(courseObjective);
				return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			} catch (JsonParseException | JsonMappingException e) {
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			} catch (IOException e) {
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	
	@DeleteMapping("/deactivateFeeTemplateRemarks/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			feeTemplateRemarksService.deactivate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@DeleteMapping("/activateFeeTemplateRemarks/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			feeTemplateRemarksService.activate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/getFeeTemplateRemarksDetails/{fee_template}")
	public ResponseEntity<Object> getFeeTemplateRemarksDetails(@PathVariable Integer fee_template) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String,Object>> list_syllabus = feeTemplateRemarksService.getFeeTemplateRemarksDetails(fee_template);
			ResponseEntity<Object> list_syllabus_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_syllabus);
			return list_syllabus_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
}
