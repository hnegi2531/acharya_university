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
import com.au.model.InternalTimeTable;
import com.au.response.ResponseHandler;
import com.au.service.InternalTimeTableService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class InternalTimeTableController {

	Logger log = LoggerFactory.getLogger(InternalTimeTableController.class);
	

	@Autowired
	private InternalTimeTableService itt_service;

	@Autowired
	private JwtTokenService jwt_service;
	
	

//	@PostMapping("/internalTimeTable")
//	public ResponseEntity<Object> saveInternalTimeTable(@RequestBody @Valid InternalTimeTable itt,@RequestHeader("Authorization") String jwtToken)
//			throws Exception {
//		if(RateLimitController.bucket.tryConsume(1)) {
//			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
//			itt.setCreated_by(jwtDetails.getUserId());
//			itt.setCreated_username(jwtDetails.getUserName());
//			InternalTimeTable itts = itt_service.saveInternalTimeTable(itt);
//			ResponseEntity<Object> internalTimeTable_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, itt);
//		return internalTimeTable_response;
//		} else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}
//	}
//	
//	@GetMapping("/internalTimeTable")
//	public ResponseEntity<Object> listAllActiveInternalTimeTable() {
//		if(RateLimitController.bucket.tryConsume(1)) {
//			List<InternalTimeTable> itts = itt_service.listAllActiveInternalTimeTable();
//			ResponseEntity<Object> internalTimeTable_response= ResponseHandler.generateResponse(true, HttpStatus.OK, itts);
//			return internalTimeTable_response;
//		} else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}
//	}
//	
//	
//	@GetMapping("/fetchAllInternalTimeTable")
//	public ResponseEntity<Object> getAllInternalTypesDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
//			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
//		
//		if(RateLimitController.bucket.tryConsume(1)) {
//		Sort sorted = Sort.by(Direction.DESC, sort );
//		if(keyword != null) {	
//			Pageable pageable = PageRequest.of(page, page_size,sorted);
//			System.out.println("page, page_size, sorted, keyword");
//			ResponseEntity<Object> internalTimeTable_filtered =  itt_service.getAllDataFilteredByKeyword(pageable, keyword);
//			return internalTimeTable_filtered;
//		} else {
//			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
//			System.out.println("page, page_size, sorted");
//			ResponseEntity<Object> internalTimeTable_sorted = itt_service.getAllSortedData(pageable1);
//			return internalTimeTable_sorted;
//		}
//		} else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}
//	}
//	
//	@GetMapping("/internalTimeTable/{id}")
//	public ResponseEntity<Object> get(@PathVariable Integer id) {
//		if(RateLimitController.bucket.tryConsume(1)) {
//	    try {
//	    	
//	    	InternalTimeTable itts = itt_service.get(id);
//	    	ResponseEntity<Object> internalTimeTable_response= ResponseHandler.generateResponse(true, HttpStatus.OK, itts);
//			return internalTimeTable_response;
//	    } catch (NoSuchElementException e) {
//	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
//			return response;
//		}
//		} else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}
//	}
//	
//	
//	@PutMapping("/internalTimeTable/{id}")
//	public ResponseEntity<Object> update(@RequestBody @Valid InternalTimeTable itt, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
//			throws JsonParseException, JsonMappingException, IOException {
//		if(RateLimitController.bucket.tryConsume(1)) {
//	    try {
//	    	
//	    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
//	    	itt.setModified_by(jwtDetails.getUserId());
//	    	itt.setModified_username(jwtDetails.getUserName());
//	    	itt_service.saveInternalTimeTables(itt);
//	    	ResponseEntity<Object> internalTimeTable_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
//			return internalTimeTable_response;
//	    } catch (NoSuchElementException e) {
//	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
//			return response;
//		}
//		} else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		} 
//	}
//	
//	
//	@DeleteMapping("/internalTimeTable/{id}")
//	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
//		if(RateLimitController.bucket.tryConsume(1)) {
//			itt_service.delete(id);
//		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
//		return response;
//		} else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}
//	}
//
//	@DeleteMapping("/activateInternalTimeTable/{id}")
//	public ResponseEntity<Object> activate(@PathVariable Integer id) {
//		if(RateLimitController.bucket.tryConsume(1)) {
//			itt_service.delete1(id);
//		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
//		return response;
//		} else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}
//	}
//	
//	@GetMapping("/internalTimeTableForAllData/{school_id}/{program_id}/{program_specialization_id}/{ac_year_id}/{year_sem}")
//	public ResponseEntity<Object> listAllActiveInternalTimeTableData(@PathVariable Integer school_id,@PathVariable Integer program_id,@PathVariable Integer program_specialization_id,@PathVariable Integer ac_year_id,@PathVariable Integer year_sem) {
//		if(RateLimitController.bucket.tryConsume(1)) {
//			List<Map<String, Object>> itts1 = itt_service.listAllActiveInternalTimeTableData(school_id,program_id,program_specialization_id,ac_year_id,year_sem);
//			ResponseEntity<Object> internalTimeTable_response= ResponseHandler.generateResponse(true, HttpStatus.OK, itts1);
//			return internalTimeTable_response;
//		} else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}
//	}
//	
//	@GetMapping("/internalTimeTableDataBasisOfDOE/{internal_id}")
//	public ResponseEntity<Object> listAllActiveInternalTimeTableDataBasisOfDOE(@PathVariable Integer internal_id) throws ParseException {
//		if(RateLimitController.bucket.tryConsume(1)) {
//			
//			List<Map<String, Object>> itts1 = itt_service.listAllActiveInternalTimeTableDataBasisOfDOE(internal_id);
//			ResponseEntity<Object> internalTimeTable_response= ResponseHandler.generateResponse(true, HttpStatus.OK, itts1);
//			return internalTimeTable_response;
//		} else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}
//	}
//	
//	
}
