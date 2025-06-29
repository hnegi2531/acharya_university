package com.au.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.au.response.ResponseHandler;
import com.au.service.TimeTableEmployeeService;


@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class TimeTableEmployeeController {
	
	@Autowired
	private TimeTableEmployeeService tte_ser;
	
	@DeleteMapping("/deactivateTimeTableEmployee/{time_table_employee_ids}")
	public ResponseEntity<Object> delete(@PathVariable List<Integer> time_table_employee_ids) {
		if(RateLimitController.bucket.tryConsume(1)) {
			tte_ser.delete(time_table_employee_ids);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateTimeTableEmployee/{time_table_employee_ids}")
	public ResponseEntity<Object> delete1(@PathVariable List<Integer> time_table_employee_ids) {
		if(RateLimitController.bucket.tryConsume(1)) {
			tte_ser.delete1(time_table_employee_ids);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/timeTableEmployeeDetailsOnTimeTableId/{time_table_id}")
	public ResponseEntity<Object> timeTableEmployeeDetailsOnTimeTableId(@PathVariable Integer time_table_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> time_table_employee = tte_ser.timeTableEmployeeDetailsOnTimeTableId(time_table_id);
			ResponseEntity<Object> time_table_employee_response = ResponseHandler.generateResponse(true, HttpStatus.OK, time_table_employee);
			return time_table_employee_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	

}
