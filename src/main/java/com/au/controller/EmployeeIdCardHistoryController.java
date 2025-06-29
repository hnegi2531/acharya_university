package com.au.controller;

import java.util.List;
import java.util.Map;

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
import com.au.dto.EmployeeIdCardHistoryDetailsDto;
import com.au.dto.EmployeeIdCardHistoryDto;
import com.au.response.ResponseHandler;
import com.au.service.EmployeeIdCardHistoryService;


@RestController
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class EmployeeIdCardHistoryController {
	
	
	Logger log = LoggerFactory.getLogger(EmployeeIdCardHistoryController.class);
	
	@Autowired
	private EmployeeIdCardHistoryService employeeIdCardHistoryService;
	
	
	@PostMapping("/employeeIdCardCreationWithHistory")
	public ResponseEntity<Object> employeeIdCardCreationWithHistory(@RequestBody @Valid List<EmployeeIdCardHistoryDto> employeeIdCardHistoryDto,@RequestHeader("Authorization") String jwtToken){
		
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				ResponseEntity<Object> employeeIdCardHistoryResponse=employeeIdCardHistoryService.employeeIdCardCreationWithHistory(employeeIdCardHistoryDto,jwtToken);
				return	ResponseHandler.generateResponse(true,employeeIdCardHistoryResponse.getStatusCode(),employeeIdCardHistoryResponse.getBody());
				
			}catch(Exception e) {
				return ResponseHandler.generateResponse(false,HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
			}
		} else {
			return	ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
		}		
		
	}
	
	@PostMapping("/employeeDuplicateIdCardCreationWithHistory")
	public ResponseEntity<Object> employeeDuplicateIdCardCreationWithHistory(@RequestBody @Valid List<EmployeeIdCardHistoryDto> employeeIdCardHistoryDto,@RequestHeader("Authorization") String jwtToken){
		
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				employeeIdCardHistoryService.employeeDuplicateIdCardCreationWithHistory(employeeIdCardHistoryDto,jwtToken);
				return	ResponseHandler.generateResponse(true, HttpStatus.CREATED,"Created Successfully");
			}catch(Exception e) {
				return ResponseHandler.generateResponse(false,HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
			}
		} else {
			return	ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
		}		
		
	}
	
	@GetMapping("/employeeIdCardHistoryDetails")
	public ResponseEntity<Object> employeeIdCardHistoryDetails() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<EmployeeIdCardHistoryDetailsDto>  emp = employeeIdCardHistoryService.employeeIdCardHistoryDetails();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, emp);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

}
