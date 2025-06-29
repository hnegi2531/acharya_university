package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.validation.Valid;

import com.au.dto.InternalAttendanceDto;
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
import com.au.model.InternalAttendance;
import com.au.response.ResponseHandler;
import com.au.service.InternalAttendanceService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class InternalAttendanceController {

	
	Logger log = LoggerFactory.getLogger(InternalAttendanceController.class);
	

	@Autowired
	private InternalAttendanceService ia_Service;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	
	@PostMapping("/internalAttendance")
	public ResponseEntity<Object> saveInternalAttendance(@RequestBody @Valid List<InternalAttendance> internalAttendance,@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		internalAttendance.stream().forEach(ia -> {
		ia.setCreated_by(jwtDetails.getUserId());
		ia.setCreated_username(jwtDetails.getUserName());
		});
		List<InternalAttendance> ias = ia_Service.saveInternalAttendance(internalAttendance);
		ResponseEntity<Object> ia_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, ias);
		return ia_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}
	}
	
	
	@GetMapping("/fetchAllInternalAttendanceDetail")
	public ResponseEntity<Object> getAllDept(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> ia_filtered =  ia_Service.getAllDataFilteredByKeyword(pageable, keyword);
			return ia_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> ia_sorted = ia_Service.getAllSortedData(pageable1);
			return ia_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/internalAttendance")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<InternalAttendance> ias = ia_Service.listAll1();
		ResponseEntity<Object> ia_response= ResponseHandler.generateResponse(true, HttpStatus.OK, ias);
		return ia_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	

	@GetMapping("/internalAttendance/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	InternalAttendance ias = ia_Service.get(id);
	    	ResponseEntity<Object> ia_response= ResponseHandler.generateResponse(true, HttpStatus.OK, ias);
			return ia_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	
	@PutMapping("/internalAttendance/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid InternalAttendance internalAttendance, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

	    	ia_Service.saveInternalAttendances(internalAttendance);
	    	ResponseEntity<Object> ias_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return ias_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	
	@DeleteMapping("/internalAttendance/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			ia_Service.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateinternalAttendance/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			ia_Service.delete1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getInternalAttendanceDetailsOfStudent/{internal_session_id}")
	public ResponseEntity<Object> getInternalAttendanceDetailsOfStudent(@PathVariable Integer internal_session_id) {
		try {
			List<HashMap<String, Object>> internalAttendanceDetails= ia_Service.getInternalAttendanceDetailsOfStudent(internal_session_id);
			ResponseEntity<Object> attendanceReportResponse = ResponseHandler.generateResponse(true,HttpStatus.OK, internalAttendanceDetails);
			return attendanceReportResponse;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
					HttpStatus.NOT_FOUND);
			return response1;
		}
	}
	
	@GetMapping("/getInternalAttendanceDetailsOfStudentList/{internal_session_id}/{emp_id}")
	public ResponseEntity<Object> getInternalAttendanceDetailsOfStudentList(@PathVariable List<Integer> internal_session_id ,@PathVariable Integer emp_id) {
		try {
			List<Map<String, Object>> internalAttendanceDetails= ia_Service.getInternalAttendanceDetailsOfStudentList(internal_session_id,emp_id);
			ResponseEntity<Object> attendanceReportResponse = ResponseHandler.generateResponse(true,HttpStatus.OK, internalAttendanceDetails);
			return attendanceReportResponse;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
					HttpStatus.NOT_FOUND);
			return response1;
		}
	}

	@PutMapping("/updateInternalAttendance")
	public ResponseEntity<Object> updateInternalAttendance(@RequestBody @Valid List<InternalAttendanceDto> internalAttendanceDto, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

				ia_Service.updateInternalAttendances(internalAttendanceDto, jwtDetails);
				ResponseEntity<Object> ias_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return ias_response;
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
