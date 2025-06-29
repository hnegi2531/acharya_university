package com.au.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.NoSuchElementException;
import javax.validation.Valid;
import javax.validation.constraints.AssertFalse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.au.model.BiometricAttendance;
import com.au.response.ResponseHandler;
import com.au.service.BiometricAttendanceService;
import com.au.service.TriggerService;


//@RequestMapping("/api")
@RequestMapping("/api/${secretkey2}")
@RestController
@CrossOrigin
public class BiometricAttendanceController {

	@Autowired
	private BiometricAttendanceService bio_service;

	@Autowired
	private TriggerService triggerService;

	Logger log = LoggerFactory.getLogger(BiometricAttendanceController.class);

	@PostMapping("/BiometricAttendance")
	public ResponseEntity<Object> saveCourseAssignment(@RequestBody @Valid BiometricAttendance b)
			throws ParseException {
		if (RateLimitController.bucket.tryConsume(1)) {
			BiometricAttendance bio = bio_service.saveBiometricAttendance(b);
			ResponseEntity<Object> bio_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, bio);
			return bio_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}

	}

	@GetMapping("/BiometricAttendance")
	public ResponseEntity<Object> listAll() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<BiometricAttendance> bio = bio_service.listAll();
			ResponseEntity<Object> bio_response = ResponseHandler.generateResponse(true, HttpStatus.OK, bio);
			return bio_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/BiometricAttendance/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				BiometricAttendance bio = bio_service.get(id);
				ResponseEntity<Object> bio_response = ResponseHandler.generateResponse(true, HttpStatus.OK, bio);
				return bio_response;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
						HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/BiometricAttendance/{id}")
	public ResponseEntity<Object> update(@RequestBody BiometricAttendance b, @PathVariable Integer id)
			throws ParseException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				BiometricAttendance bio = bio_service.get(id);
				bio_service.saveBiometricAttendance(b);
				ResponseEntity<Object> bio_response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
						HttpStatus.OK);
				return bio_response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
						HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/BiometricAttendance/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			bio_service.delete(id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PostMapping("/biometricAttendenceTrigger")
	public ResponseEntity<?> biometricAttendanceTrigger(@RequestParam("month") Integer month, @RequestParam("year") Integer year,
														@RequestParam(required = false) Integer empId) {
		String triggerName = "biometricAttendanceTrigger";
		String status = triggerService.getStatusOfTheTrigger(triggerName);
		if(status.equals("ACTIVE")){
			return ResponseHandler.generateResponse(true, HttpStatus.CONFLICT, "BioMetric Attendance Trigger already running", null);
		}
		triggerService.biometricAttendanceTrigger(month, year, empId);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "BioMetric Attendance Trigger Started", null);
	}

	@PostMapping(value = "/uploadBiometricTransactionDetails", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Object> uploadBiometricTransactionDetails(@RequestPart("file") MultipartFile file)
			throws ParseException, IOException {
		return bio_service.uploadBiometricTransactionDetails(file);

	}

	@PostMapping("/employeeAttendanceTrigger")
	public ResponseEntity<?> attendanceSheetTrigger(@RequestParam("month") Integer month,
			@RequestParam("year") Integer year,@RequestParam(value="empId",required = false) Integer empId,
			@RequestParam(value="schoolId",required =false) Integer schoolId) {
		String triggerName = "employeeAttendanceTrigger";
		String status = triggerService.getStatusOfTheTrigger(triggerName);
		if(status.equals("ACTIVE")){
			return ResponseHandler.generateResponse(true, HttpStatus.CONFLICT, "Attendance Trigger already running", null);
		}
		triggerService.attendanceSheetTrigger(month, year,empId,schoolId);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "Attendance Trigger Started", null);
	}
	
	@GetMapping("/getBiometricAttendanceByEmp_id/{month}/{year}/{emp_id}")
	public ResponseEntity<Object> getBiometricAttendanceByEmp_id(@PathVariable Integer month,@PathVariable Integer year,
			@PathVariable Integer emp_id)  throws ParseException {
		if(RateLimitController.bucket.tryConsume(1)) {
			
			List<BiometricAttendance> employeeDetails=bio_service.getBiometricAttendanceByEmp_id(month,year,emp_id);
			ResponseEntity<Object> employeeDetailsResponse= ResponseHandler.generateResponse(true, HttpStatus.OK, employeeDetails);
			return employeeDetailsResponse;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@PostMapping("/bioTransTrigger")
	public ResponseEntity<?> bioTransTrigger(@RequestParam("date") String date, @RequestParam(required = false) Integer empId) {
		String triggerName = "bioTransTrigger";
		String status = triggerService.getStatusOfTheTrigger(triggerName);
		if(status.equals("ACTIVE")){
			return ResponseHandler.generateResponse(true, HttpStatus.CONFLICT, "BioTransTrigger already running", null);
		}
		triggerService.bioTransTrigger(date, empId);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "Attendance Trigger Started", null);
	}
	
	@PostMapping("/bioTransMonthWiseTrigger")
	public ResponseEntity<?> bioTransMonthWiseTrigger(@RequestParam("month") Integer month,@RequestParam("year") Integer year,
													  @RequestParam(required = false) Integer empId) {
		String triggerName = "bioTransMonthWiseTrigger";
		String status = triggerService.getStatusOfTheTrigger(triggerName);
		if(status.equals("ACTIVE")){
			return ResponseHandler.generateResponse(true, HttpStatus.CONFLICT, "BioTransMonthWiseTrigger already running", null);
		}
		triggerService.bioTransMonthWiseTrigger(month,year, empId);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "Attendance Trigger Started", null);
	}
	
	@GetMapping("/getBiometricAttendanceDataByEmp_idAndDate/{date}/{emp_id}")
	public ResponseEntity<Object> getBiometricAttendanceDataByEmp_idAndDate(@PathVariable String date,
			@PathVariable Integer emp_id)  throws ParseException {
		if(RateLimitController.bucket.tryConsume(1)) {
			
			BiometricAttendance employeeDetails=bio_service.getBiometricAttendanceDataByEmp_idAndDate(date,emp_id);
			ResponseEntity<Object> employeeDetailsResponse= ResponseHandler.generateResponse(true, HttpStatus.OK, employeeDetails);
			return employeeDetailsResponse;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@PostMapping("/bioTransactionTrigger")
	public ResponseEntity<Object> bioTransactionTrigger(@RequestParam("date") String date, @RequestParam(required = false) Integer empId) {
		return bio_service.bioTransactionTrigger(date, empId);
		
	}

}
