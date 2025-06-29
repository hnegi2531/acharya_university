package com.au.controller;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.JobFileRequest;
import com.au.dto.JwtDetails;
import com.au.model.LeaveType;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.LeaveTypeService;


@RequestMapping("/api")
@RestController
@CrossOrigin
public class LeaveTypeController {

	@Autowired
	private LeaveTypeService s_service;
	
	Logger log = LoggerFactory.getLogger(LeaveTypeController.class);
	
	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/LeaveType")
	public ResponseEntity<Object> saveLeaveType(@RequestBody @Valid LeaveType r,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				r.setCreated_by(jwtDetails.getUserId());
				r.setCreated_username(jwtDetails.getUserName());
				LeaveType leave_type = s_service.saveLeaveType(r);
				ResponseEntity<Object> leave_type_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, leave_type);
				return leave_type_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
			
	}
	
	@PostMapping(value = "/leaveTypeUploadFile")
	public ResponseEntity<Object> uploadFile(@ModelAttribute JobFileRequest jobfilerequest) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// JwtDetails jwtdetails= (JwtDetails) auth.getDetails();
		// System.out.println(jwtdetails.getUserId());
		System.out.println("Hello-------" + auth.getDetails());
		log.debug("Message For Attachment");
		s_service.uploadFile(jobfilerequest.getFile(), jobfilerequest.getLeave_id());
		 ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}
	
	@GetMapping(path = "/leaveTypeFileviews")
	public ResponseEntity<ByteArrayResource> viewFiles(@RequestParam("fileName") final String fileName) {
		try {
			final byte[] data = s_service.viewFiles(fileName);
			final ByteArrayResource resource = new ByteArrayResource(data);
			return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/pdf")
					.header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
					.header("Cache-Control", "no-cache").body(resource);
		} catch (NoSuchFileException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().contentLength(0).body(null);
		}
	}

	@GetMapping("/LeaveType")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<LeaveType> list_leave_type = s_service.listAll();
				ResponseEntity<Object> list_leave_type_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_leave_type);
				return list_leave_type_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@GetMapping("/fetchAllLeaveTypeDetails")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> leave_type_sorted = s_service.listAll1(pageable, keyword);//,column,value);
						return leave_type_sorted;
				}else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> leave_type_pageable = s_service.listAll2(pageable1);
						return leave_type_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		//return s_service.listAll1();
	}
	
	@GetMapping("/LeaveType/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {

						LeaveType product = s_service.get(id);
						ResponseEntity<Object> leave_type_response_by_id = ResponseHandler.generateResponse(true, HttpStatus.OK, product);
						return leave_type_response_by_id;
				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}				
	}

	@PutMapping("/LeaveType/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid LeaveType r, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
						LeaveType existProduct = s_service.get(id);
						JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

						r.setModified_by(jwtDetails.getUserId());
						r.setModified_username(jwtDetails.getUserName());

						s_service.saveUpdateLeaveType(r);
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

	@DeleteMapping("/LeaveType/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
					s_service.delete(id);
					ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
					return response;
			}else {	
					ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
					return rs;
			}
	}

	@DeleteMapping("/activateLeaveType/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				s_service.delete1(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	//for type dropdown in holiday calender creation
	@GetMapping("/getHolidayTypeLeaves")
	public ResponseEntity<Object> getHolidayTypeLeaves() {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<LeaveType> list_leave_type = s_service.getHolidayTypeLeaves();
				ResponseEntity<Object> list_leave_type_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_leave_type);
				return list_leave_type_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/getLeaveTypeForLeaveAndAttendence")
	public ResponseEntity<Object> getLeaveTypeForLeaveAndAttendence() {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<LeaveType> list_leave_type = s_service.getLeaveTypeForLeaveAndAttendence();
				ResponseEntity<Object> list_leave_type_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_leave_type);
				return list_leave_type_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}	
	
	@GetMapping("/getLeaveIdOfVacationLeave/{leaveTypeShort}")
	public ResponseEntity<Object> getLeaveIdOfVacationLeave(@PathVariable String leaveTypeShort,
			@RequestHeader("Authorization") String jwtToken) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Integer vacationLeaveId = s_service.getLeaveIdOfVacationLeave(leaveTypeShort);
				ResponseEntity<Object> list_leave_type_response = ResponseHandler.generateResponse(true, HttpStatus.OK, vacationLeaveId);
				return list_leave_type_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}	

}
