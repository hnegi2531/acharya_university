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
import com.au.model.CourseObjective;
import com.au.model.Department;
import com.au.model.TransportMaintenance;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.TransportMaintenanceService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class TransportMaintenanceController {

	Logger log = LoggerFactory.getLogger(TransportMaintenanceController.class);
	
	
	@Autowired
	private TransportMaintenanceService transport_service;

	@Autowired
	private JwtTokenService jwt_service;
	
	
	
	@PostMapping("/saveTransportMaintenance")
	public ResponseEntity<Object> saveTransportMaintenance(@RequestBody @Valid TransportMaintenance tm,@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			tm.setCreated_by(jwtDetails.getUserId());
			tm.setCreated_username(jwtDetails.getUserName());

			TransportMaintenance tms = transport_service.saveTransportMaintenance(tm);
			ResponseEntity<Object> tm_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, tms);
		return tm_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getAllActiveTransportMaintenance")
	public ResponseEntity<Object> getAllActiveTransportMaintenance() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<TransportMaintenance> tms = transport_service.getAllActiveTransportMaintenance();
		ResponseEntity<Object> tm_response= ResponseHandler.generateResponse(true, HttpStatus.OK, tms);
		return tm_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}	
	
	
	@GetMapping("/getTransportMaintenance/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	TransportMaintenance tms = transport_service.get(id);
	    	ResponseEntity<Object> tm_response= ResponseHandler.generateResponse(true, HttpStatus.OK, tms);
			return tm_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	
	@PutMapping("/updateTransportMaintenance/{transport_maintenance_id}")
	public ResponseEntity<Object> updateTransportMaintenance(@RequestBody @Valid TransportMaintenance tm, @PathVariable Integer transport_maintenance_id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    	tm.setModified_by(jwtDetails.getUserId());
	    	tm.setModified_username(jwtDetails.getUserName());
	    	transport_service.updateTransportMaintenance(tm);
	    	ResponseEntity<Object> tm_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return tm_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	
	@DeleteMapping("/deactiveTransportMaintenance/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			transport_service.deactivate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@DeleteMapping("/activateTransportMaintenance/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			transport_service.activate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
		
	@GetMapping("/fetchAllTransportMaintenance")
	public ResponseEntity<Object> getAllDept(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value = "dept_id", required = false) Integer dept_id,@RequestParam(value = "user_id", required = false) Integer user_id,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> oc_filtered =  transport_service.getAllDataFilteredByKeyword(pageable, keyword ,dept_id ,user_id  );
			return oc_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> oc_sorted = transport_service.getAllSortedData(pageable1 ,dept_id ,user_id  );
			return oc_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	
	@GetMapping("/fetchAllTransportMaintenanceBasedONDeptId")
	public ResponseEntity<Object> fetchAllTransportMaintenanceBasedONDeptId(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value = "dept_id", required = false) Integer dept_id,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> oc_filtered =  transport_service.getAllDataFilteredByKeywordDeptId(pageable, keyword ,dept_id);
			return oc_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> oc_sorted = transport_service.getAllSortedDataDeptId(pageable1 ,dept_id);
			return oc_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/transportMaintenanceById/{id}")
	public ResponseEntity<Object> transportMaintenanceById(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	Map<String, Object> tms = transport_service.transportMaintenanceById(id);
	    	ResponseEntity<Object> tm_response= ResponseHandler.generateResponse(true, HttpStatus.OK, tms);
			return tm_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	
	
	@GetMapping("/fetchAllTransportMaintenanceHistory")
	public ResponseEntity<Object> fetchAllTransportMaintenanceHistory(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value = "dept_id", required = false) Integer dept_id,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> oc_filtered =  transport_service.getAllDataFilteredByKeywordHistory(pageable, keyword ,dept_id);
			return oc_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> oc_sorted = transport_service.getAllSortedDataHistory(pageable1 ,dept_id);
			return oc_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
}
