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
import com.au.dto.ProctorStudentMeetingDto;
import com.au.model.ProctorStudentMeeting;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.ProctorStudentMeetingService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
@RequestMapping("/api/${secretkey7}")
@CrossOrigin
public class ProctorStudentMeetingController {
	
Logger log = LoggerFactory.getLogger(ReasonFeeExcemptionController.class);
	
	
	@Autowired
	private ProctorStudentMeetingService psm_ser;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/saveProctorStudentMeeting")
	public ResponseEntity<Object> saveProctorStudentMeeting(@RequestBody @Valid ProctorStudentMeetingDto r,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException,Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		r.setCreated_by(jwtDetails.getUserId());
		r.setCreated_username(jwtDetails.getUserName());
		List<ProctorStudentMeeting> psm = psm_ser.saveProctorStudentMeeting(r);
		ResponseEntity<Object> psm_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, psm);
		return psm_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllProctorStudentMeeting")
	public ResponseEntity<Object> fetchAllProctorStudentMeeting(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> psm_filtered =  psm_ser.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
			return psm_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> psm_sorted = psm_ser.getAllSortedData(pageable1);
			return psm_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	
	@GetMapping("/getProctorStudentMeeting/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	ProctorStudentMeeting product = psm_ser.get(id);
	    	ResponseEntity<Object> psm_response= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
			return psm_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}   
	}

	@PutMapping("/updateProctorStudentMeeting/{id}")
	public ResponseEntity<Object> updateProctorStudentMeeting(@RequestBody @Valid List<ProctorStudentMeeting> r, @PathVariable List<Integer> id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException,Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    //	ProctorStudentMeeting existProduct = psm_ser.get(id);
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	r.stream().forEach(r1 -> {
		r1.setModified_by(jwtDetails.getUserId());
		r1.setModified_username(jwtDetails.getUserName());
	});

			psm_ser.updateProctorStudentMeeting(r);
	    	ResponseEntity<Object> psm_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return psm_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@DeleteMapping("/deactivateProctorStudentMeeting/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			psm_ser.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateProctorStudentMeeting/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			psm_ser.delete1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		 return response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
//	@PostMapping("/sendTelegramMessageForMeeting/{student_ids}")
//	public ResponseEntity<Object> sendTelegramMessageForMeeting(@PathVariable List<Integer> student_ids, @RequestHeader("Authorization") String jwtToken)
//			throws JsonParseException, JsonMappingException, IOException,Exception {
//		if(RateLimitController.bucket.tryConsume(1)) {
//		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
//		String message = psm_ser.sendTelegramMessage(student_ids, jwtDetails);
//		ResponseEntity<Object> psm_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, message);
//		return psm_response;
//		} else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}
//	}
	
	@PostMapping("/sendEmailMessageForMeeting/{student_ids}/{user_id}/{agenda_of_meeting}/{description}/{date_of_meeting}")
	public ResponseEntity<Object> sendEmailMessageForMeeting(@PathVariable List<Integer> student_ids, @PathVariable Integer user_id, 
			@PathVariable String agenda_of_meeting,@PathVariable String description, 
			@PathVariable String date_of_meeting, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException,Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		Object message = psm_ser.sendEmailMessageForMeeting(student_ids,jwtDetails, user_id,agenda_of_meeting,description,date_of_meeting);
		ResponseEntity<Object> psm_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, message);
		return psm_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllProctorStudentMeetingBasedOnUserId")
	public ResponseEntity<Object> fetchAllProctorStudentMeetingBasedOnUserId(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword,
			@RequestParam(value="user_id") Integer user_id) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> psm_filtered =  psm_ser.getAllDataFilteredByKeyword22(pageable, keyword,user_id);//,column,value);
			return psm_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> psm_sorted = psm_ser.getAllSortedData22(pageable1,user_id);
			return psm_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	
	@GetMapping("/getAllMailHistoryBasedOnMentor/{emp_id}")
	public ResponseEntity<Object> getAllMailHistoryBasedOnMentor(@PathVariable Integer emp_id){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> proctorStudentAssignment = psm_ser.getAllMailHistoryBasedOnMentor(emp_id);
			ResponseEntity<Object> proctorStudentAssignment_response= ResponseHandler.generateResponse(true, HttpStatus.OK, proctorStudentAssignment);
			return proctorStudentAssignment_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	

}
