package com.au.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.au.response.ResponseHandler;
import com.au.service.EmployeeDetailsReportService;

@RestController
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class EmployeeDetailsReportController {

	Logger log = LoggerFactory.getLogger(EmployeeDetailsReportController.class);
	
	@Autowired
	private EmployeeDetailsReportService emp_detail_report_ser;
	
	
	
	@GetMapping(value = "/getEmployeeDetailsForReportOnGender")
	public ResponseEntity<Object> getEmployeeDetailsForReportONGender() {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				List<Map<String, Object>> emp_report = emp_detail_report_ser.getEmployeeDetailsForReportONGender();
				ResponseEntity<Object> emp_report_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp_report);
				return emp_report_response;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	} 
	
	@GetMapping(value = "/getEmployeeDetailsForReportOnDesignation")
	public ResponseEntity<Object> getEmployeeDetailsForReportOnDesignation() throws Exception{
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				List<Map<String, Object>> emp_report = emp_detail_report_ser.getEmployeeDetailsForReportOnDesignation();
				ResponseEntity<Object> emp_report_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp_report);
				return emp_report_response;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping(value = "/getEmployeeDetailsForReportOnDepartment")
	public ResponseEntity<Object> getEmployeeDetailsForReportOnDepartment() throws Exception{
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				List<Map<String, Object>> emp_report = emp_detail_report_ser.getEmployeeDetailsForReportOnDepartment();
				ResponseEntity<Object> emp_report_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp_report);
				return emp_report_response;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping(value = "/getEmployeeDetailsForReportOnEmployeeType")
	public ResponseEntity<Object> getEmployeeDetailsForReportOnEmployeeType() throws Exception{
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				List<Map<String, Object>> emp_report = emp_detail_report_ser.getEmployeeDetailsForReportOnEmployeeType();
				ResponseEntity<Object> emp_report_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp_report);
				return emp_report_response;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping(value = "/getEmployeeDetailsForReportOnJobType")
	public ResponseEntity<Object> getEmployeeDetailsForReportOnJobType() throws Exception{
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				List<Map<String, Object>> emp_report = emp_detail_report_ser.getEmployeeDetailsForReportOnJobType();
				ResponseEntity<Object> emp_report_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp_report);
				return emp_report_response;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping(value = "/getEmployeeDetailsForReportOnShift")
	public ResponseEntity<Object> getEmployeeDetailsForReportOnShift() throws Exception{
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				List<Map<String, Object>> emp_report = emp_detail_report_ser.getEmployeeDetailsForReportOnShift();
				ResponseEntity<Object> emp_report_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp_report);
				return emp_report_response;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping(value = "/getEmployeeDetailsForReportOnMaritalStatus")
	public ResponseEntity<Object> getEmployeeDetailsForReportOnMaritalStatus() throws Exception{
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				List<Map<String, Object>> emp_report = emp_detail_report_ser.getEmployeeDetailsForReportOnMaritalStatus();
				ResponseEntity<Object> emp_report_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp_report);
				return emp_report_response;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping(value = "/getEmployeeDetailsForReportOnExperienceInYear")
	public ResponseEntity<Object> getEmployeeDetailsForReportOnExperienceInYear() throws Exception{
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				List<Map<String, Object>> emp_report = emp_detail_report_ser.getEmployeeDetailsForReportOnExperienceInYear();
				ResponseEntity<Object> emp_report_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp_report);
				return emp_report_response;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping(value = "/getEmployeeDetailsForReportOnExperienceInMonth")
	public ResponseEntity<Object> getEmployeeDetailsForReportOnExperienceInMonth() throws Exception{
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				List<Map<String, Object>> emp_report = emp_detail_report_ser.getEmployeeDetailsForReportOnExperienceInMonth();
				ResponseEntity<Object> emp_report_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp_report);
				return emp_report_response;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping(value = "/getEmployeeDetailsForReportOnSchools")
	public ResponseEntity<Object> getEmployeeDetailsForReportOnSchools() throws Exception{
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				List<Map<String, Object>> emp_report = emp_detail_report_ser.getEmployeeDetailsForReportOnSchools();
				ResponseEntity<Object> emp_report_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp_report);
				return emp_report_response;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	//Note :- This api is not using by Developer, instead of it we written another api on the basis of particular
	@GetMapping(value = "/getEmployeeDetailsForReportOnJoiningDate")
	public ResponseEntity<Object> getEmployeeDetailsForReportOnJoiningDate() throws Exception{
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				List<Map<String, Object>> emp_report = emp_detail_report_ser.getEmployeeDetailsForReportOnJoiningDate();
				ResponseEntity<Object> emp_report_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp_report);
				return emp_report_response;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping(value = "/getEmployeeDetailsForReportOnDateOfBirth")
	public ResponseEntity<Object> getEmployeeDetailsForReportOnDateOfBirth() throws Exception{
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				List<Map<String, Object>> emp_report = emp_detail_report_ser.getEmployeeDetailsForReportOnDateOfBirth();
				ResponseEntity<Object> emp_report_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp_report);
				return emp_report_response;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping(value = "/getEmployeeDetailsForReportOnMonthWiseOfJoiningYear/{year}")
	public ResponseEntity<Object> getEmployeeDetailsForReportOnMonthWiseOfJoiningYear(@PathVariable Integer year) throws Exception{
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				List<Map<String, Object>> emp_report = emp_detail_report_ser.getEmployeeDetailsForReportOnMonthWiseOfJoiningYear(year);
				ResponseEntity<Object> emp_report_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp_report);
				return emp_report_response;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping(value = "/getEmployeeDetailsForReportOnMonthWiseOfJoiningYearOnSchool/{year}/{school_id}")
	public ResponseEntity<Object> getEmployeeDetailsForReportOnMonthWiseOfJoiningYearOnSchool(@PathVariable Integer year,@PathVariable Integer school_id) throws Exception{
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				List<Map<String, Object>> emp_report = emp_detail_report_ser.getEmployeeDetailsForReportOnMonthWiseOfJoiningYearOnSchool(year,school_id);
				ResponseEntity<Object> emp_report_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp_report);
				return emp_report_response;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping(value ="/getEmployeeRelievingReportDataOnMonthWise/{year}")
	public ResponseEntity<Object> getEmployeeRelievingReportDataOnMonthWise(@PathVariable Integer year) throws Exception{
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				List<Map<String, Object>> emp_report = emp_detail_report_ser.getEmployeeRelievingReportDataOnMonthWise(year);
				ResponseEntity<Object> emp_report_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp_report);
				return emp_report_response;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping(value = "/getEmployeeDetailsForReportOnMonthWiseOfJoiningYearInactiveData/{year}")
	public ResponseEntity<Object> getEmployeeDetailsForReportOnMonthWiseOfJoiningYearInactiveData(@PathVariable Integer year) throws Exception{
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				List<Map<String, Object>> emp_report = emp_detail_report_ser.getEmployeeDetailsForReportOnMonthWiseOfJoiningYearInactiveData(year);
				ResponseEntity<Object> emp_report_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp_report);
				return emp_report_response;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping(value ="/getEmployeeRelievingReportDataOnMonthWiseInactiveData/{year}")
	public ResponseEntity<Object> getEmployeeRelievingReportDataOnMonthWiseInactiveData(@PathVariable Integer year) throws Exception{
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				List<Map<String, Object>> emp_report = emp_detail_report_ser.getEmployeeRelievingReportDataOnMonthWiseInactiveData(year);
				ResponseEntity<Object> emp_report_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp_report);
				return emp_report_response;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping(value = "/getEmployeeDetailsForReportOnMonthWiseOfJoiningDateAndRelievingData/{year}")
	public ResponseEntity<Object> getEmployeeDetailsForReportOnMonthWiseOfJoiningDateAndRelievingData(@PathVariable Integer year) throws Exception{
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				HashMap<String,Object> emp_report = emp_detail_report_ser.getEmployeeDetailsForReportOnMonthWiseOfJoiningDateAndRelievingData(year);
				ResponseEntity<Object> emp_report_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp_report);
				return emp_report_response;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
}
