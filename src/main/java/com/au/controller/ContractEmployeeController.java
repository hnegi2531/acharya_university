package com.au.controller;

import java.io.IOException;
import java.util.List;
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
import com.au.model.EmployeeDetails;
import com.au.response.ResponseHandler;
import com.au.service.ContractEmployeeService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class ContractEmployeeController {

	Logger log = LoggerFactory.getLogger(ContractEmployeeController.class);

	@Autowired
	private ContractEmployeeService contractEmployeeService_service;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/ContractEmployee")
	public ResponseEntity<Object> saveContractEmployee(@RequestBody @Valid EmployeeDetails contractEmployee,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		contractEmployee.setCreated_by(jwtDetails.getUserId());
		contractEmployee.setCreated_username(jwtDetails.getUserName());
		EmployeeDetails c_emp = contractEmployeeService_service.saveContractEmployee(contractEmployee);
		ResponseEntity<Object> c_emp_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, c_emp);
		return c_emp_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/ContractEmployee")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<EmployeeDetails> c_emp_list = contractEmployeeService_service.listAll();
			ResponseEntity<Object> c_emp_list_response= ResponseHandler.generateResponse(true, HttpStatus.OK, c_emp_list);
			return c_emp_list_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/ContractEmployee/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {

			EmployeeDetails cb = contractEmployeeService_service.get(id);
			ResponseEntity<Object> cb_response= ResponseHandler.generateResponse(true, HttpStatus.OK, cb);
			return cb_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/ContractEmployee/{id}")
	public ResponseEntity<Object> update(@RequestBody EmployeeDetails cb, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			EmployeeDetails existProduct = contractEmployeeService_service.get(id);
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			cb.setModified_by(jwtDetails.getUserId());
			cb.setModified_username(jwtDetails.getUserName());
			contractEmployeeService_service.saveContractEmployees(cb);
			ResponseEntity<Object> cb_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return cb_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/ContractEmployee/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		contractEmployeeService_service.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		 return response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}

}
