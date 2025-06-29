package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.JwtDetails;
import com.au.dto.ProctorStudentAssignmentDto;
import com.au.model.EmployeeDetailsHistory;
import com.au.model.ProctorStudentAssignment;
import com.au.response.ResponseHandler;
import com.au.service.EmployeeDetailsHistoryService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class EmployeeDetailsHistoryController {
	
	@Autowired
	private EmployeeDetailsHistoryService emp_details_his_ser;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/employeeDetailsHistory")
	public ResponseEntity<Object> saveEmployeeDetailsHistory(@RequestBody @Valid EmployeeDetailsHistory emp_his,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			emp_his.setCreated_by(jwtDetails.getUserId());
			emp_his.setCreated_username(jwtDetails.getUserName());
			EmployeeDetailsHistory emp_details_his = emp_details_his_ser.saveEmployeeDetailsHistory(emp_his);
			ResponseEntity<Object> emp_details_his_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, emp_details_his);
			return emp_details_his_response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
	}
	
	@GetMapping("/employeeDetailsHistoryOnEmpId/{emp_id}")
	public ResponseEntity<Object> employeeDetailsHistoryOnEmpId(@PathVariable Integer emp_id){
		if(RateLimitController.bucket.tryConsume(1)) {
			EmployeeDetailsHistory employeeHistory = emp_details_his_ser.employeeDetailsHistoryOnEmpId(emp_id);
			ResponseEntity<Object> emp_id_his_details_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, employeeHistory);
			return emp_id_his_details_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}			
	}

	@DeleteMapping("/deActivateEmployeeDetailsHistory/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			emp_details_his_ser.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateEmployeeDetailsHistory/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			emp_details_his_ser.delete1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllEmployeeDetailsHistory")
	public ResponseEntity<Object> getAllBankImportTransaction(@RequestParam(value="page") Integer page,
			@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> employeeHistory_filtered =  emp_details_his_ser.getAllDataFilteredByKeyword(pageable, keyword);
			return employeeHistory_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> employeeHistory_sorted = emp_details_his_ser.getAllSortedData(pageable1);
			return employeeHistory_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	@GetMapping("/employeeDetailsHistory/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
			EmployeeDetailsHistory employeeHistory = emp_details_his_ser.get(id);
	    	ResponseEntity<Object> employeeHistory_response= ResponseHandler.generateResponse(true, HttpStatus.OK, employeeHistory);
			return employeeHistory_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	@GetMapping("/getAllActiveemployeeDetailsHistory")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<EmployeeDetailsHistory> employeeHistory = emp_details_his_ser.listAll1();
		ResponseEntity<Object> employeeHistory_response= ResponseHandler.generateResponse(true, HttpStatus.OK, employeeHistory);
		return employeeHistory_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}		
	
	@GetMapping("/fetchAllEmployeeDetailsHistoryByEmpId")
	public ResponseEntity<Object> getAllBankImportTransaction(@RequestParam(value="page") Integer page,
			@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort,@RequestParam(value="keyword",required = false) Object keyword,
			 @RequestParam(value="emp_id") Integer emp_id) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> employeeHistory_filtered =  emp_details_his_ser.getAllDataFilteredByKeywordByEmpId(pageable, keyword, emp_id);
			return employeeHistory_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> employeeHistory_sorted = emp_details_his_ser.getAllSortedDataByEmpId(pageable1, emp_id);
			return employeeHistory_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
}
