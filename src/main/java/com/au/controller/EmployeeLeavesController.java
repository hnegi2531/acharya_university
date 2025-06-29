package com.au.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.au.response.ResponseHandler;
import com.au.model.LeaveKitty;
import com.au.model.LeaveType;
import com.au.service.EmployeeLeavesService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class EmployeeLeavesController {

	Logger log = LoggerFactory.getLogger(EmployeeLeavesController.class);
	
	@Autowired
	private EmployeeLeavesService empLeaveService;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/LeaveKitty")
	public ResponseEntity<Object> saveDept(@RequestBody @Valid LeaveKitty empleaves,@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		empleaves.setCreated_by(jwtDetails.getUserId());
		empleaves.setCreated_username(jwtDetails.getUserName());
		LeaveKitty empleave = empLeaveService.save_Department(empleaves);
		ResponseEntity<Object> empleaves_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, empleave);
		return empleaves_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}
	}
	

	@GetMapping("/fetchAllEmpLeavesDetail")
	public ResponseEntity<Object> getAllDept(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> empleaves_filtered =  empLeaveService.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
			return empleaves_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> empleaves_sorted = empLeaveService.getAllSortedData(pageable1);
			return empleaves_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/emp_leaves")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<LeaveKitty> empleave = empLeaveService.listAll();
		ResponseEntity<Object> empleaves_response= ResponseHandler.generateResponse(true, HttpStatus.OK, empleave);
		return empleaves_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/emp_leaves/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	LeaveKitty empleave = empLeaveService.get(id);
	    	ResponseEntity<Object> empleaves_response= ResponseHandler.generateResponse(true, HttpStatus.OK, empleave);
			return empleaves_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/empleaves/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid LeaveKitty empleaves, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    	empleaves.setModified_by(jwtDetails.getUserId());
	    	empleaves.setModified_username(jwtDetails.getUserName());
	    	empLeaveService.saveEmpLeaves(empleaves);
	    	ResponseEntity<Object> empleaves_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return empleaves_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	@DeleteMapping("/emp_leaves/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			empLeaveService.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activate_emp_leaves/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			empLeaveService.delete1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/leaveTypesAvailableForEmployees/{emp_id}")
	public ResponseEntity<Object> leaveTypesAvailableForEmployees(@PathVariable Integer emp_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<LeaveType> empleave = empLeaveService.leaveTypesAvailableForEmployees(emp_id);
		ResponseEntity<Object> empleaves_response= ResponseHandler.generateResponse(true, HttpStatus.OK, empleave);
		return empleaves_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}
	}
	
	
	@GetMapping("/getTimeTableDetailsForEmployeesInEmployeeLeave/{emp_id}/{from_date}/{to_date}")
	public ResponseEntity<Object> getTimeTableDetailsForEmployees(@PathVariable Integer emp_id, @PathVariable String from_date,
			@PathVariable String to_date) throws ParseException {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> empleave = empLeaveService.getTimeTableDetailsForEmployees(emp_id, from_date, to_date);
		ResponseEntity<Object> empleaves_response= ResponseHandler.generateResponse(true, HttpStatus.OK, empleave);
		return empleaves_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}
	}
	
	@GetMapping("/getLeaveApproversForEmployees/{emp_id}")
	public ResponseEntity<Object> getLeaveApproversForEmployees(@PathVariable Integer emp_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			HashMap<String, Object> empleave = empLeaveService.getLeaveApproversForEmployees(emp_id);
		ResponseEntity<Object> empleaves_response= ResponseHandler.generateResponse(true, HttpStatus.OK, empleave);
		return empleaves_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}
	}

	
}
