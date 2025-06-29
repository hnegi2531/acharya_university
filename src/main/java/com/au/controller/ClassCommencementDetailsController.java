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

import com.au.dto.ClassCommencementDetailsDto;
import com.au.dto.JwtDetails;
import com.au.model.ClassCommencementDetails;
import com.au.response.ResponseHandler;
import com.au.service.ClassCommencementDetailsService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class ClassCommencementDetailsController {
	
Logger log = LoggerFactory.getLogger(DepartmentController.class);
	
	@Autowired
	private ClassCommencementDetailsService ccd_service;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/classCommencementDetails")
	public ResponseEntity<Object> saveClassCommencementDetails(@RequestBody @Valid ClassCommencementDetailsDto ccd,@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			ccd.setCreated_by(jwtDetails.getUserId());
			ccd.setCreated_username(jwtDetails.getUserName());
			return ccd_service.saveClassCommencementDetails(ccd);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);

		}
	}
	
	@GetMapping("/fetchAllClassCommencementDetails")
	public ResponseEntity<Object> getAllClassCommencementDetails(
			@RequestParam(value="page") Integer page,
			@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort,
			@RequestParam(value="keyword",required = false) Object keyword,
		 	@RequestParam(value="ac_year_id", required = false) Integer ac_year_id,
			@RequestParam(value="school_id", required = false) Integer school_id,
			@RequestParam(value="program_assignment_id", required = false) Integer program_assignment_id,
			@RequestParam(value="program_specialization_id", required = false) Integer program_specialization_id,
			@RequestParam(value="year_sem", required = false) Integer year_sem
    ) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted, keyword");
				return ccd_service.getAllDataFilteredByKeyword(pageable, keyword, ac_year_id, school_id, program_assignment_id,
						program_specialization_id, year_sem);
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				return ccd_service.getAllSortedData(pageable1, ac_year_id, school_id, program_assignment_id,
						program_specialization_id, year_sem);
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/classCommencementDetails")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<ClassCommencementDetails> ccd = ccd_service.listAll1();
		ResponseEntity<Object> department_response= ResponseHandler.generateResponse(true, HttpStatus.OK, ccd);
		return department_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	
	@GetMapping("/classCommencementDetails/{class_commencement_details_id}")
	public ResponseEntity<Object> get(@PathVariable Integer class_commencement_details_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	ClassCommencementDetails ccd = ccd_service.get(class_commencement_details_id);
	    	ResponseEntity<Object> ccd_response= ResponseHandler.generateResponse(true, HttpStatus.OK, ccd);
			return ccd_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/classCommencementDetails/{class_commencement_details_id}")
	public ResponseEntity<Object> update(@RequestBody @Valid ClassCommencementDetails ccd, @PathVariable Integer class_commencement_details_id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	//Department existProduct = deptService.get(id);
	    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    	ccd.setModified_by(jwtDetails.getUserId());
	    	ccd.setModified_username(jwtDetails.getUserName());
	    	ccd_service.saveClassCommencementDetails(ccd);
	    	ResponseEntity<Object> ccd_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return ccd_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	
	@DeleteMapping("/deactivateClassCommencementDetails/{class_commencement_details_id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer class_commencement_details_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			ccd_service.delete(class_commencement_details_id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateclassCommencementDetails/{class_commencement_details_id}")
	public ResponseEntity<Object> activate(@PathVariable Integer class_commencement_details_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			ccd_service.delete1(class_commencement_details_id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getClassCommencementDetails/{school_id}/{student_id}/{year_sem}")
	public ResponseEntity<Object> getClassCommencementDetails(@PathVariable Integer school_id, 
			@PathVariable Integer student_id,@PathVariable Integer year_sem){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String ,Object>> commencement_details = ccd_service.getClassCommencementDetails(school_id,student_id,year_sem);
			ResponseEntity<Object> academic_calender_response = ResponseHandler.generateResponse(true, HttpStatus.OK, commencement_details);
			return academic_calender_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}
	}
	
	@GetMapping("/getClassCommencementDetailsForValidatingTimeTable/{ac_year}/{school_id}/{year_sem}/{commencement_id}/{program_specialization_id}")
	public ResponseEntity<Object> getClassCommencementDetailsForValidatingTimeTable(@PathVariable Integer ac_year, @PathVariable Integer school_id, 
			@PathVariable Integer year_sem,@PathVariable Integer commencement_id,@PathVariable Integer program_specialization_id){
		if(RateLimitController.bucket.tryConsume(1)) {
			ClassCommencementDetails commencement_details = ccd_service.getClassCommencementDetailsForValidatingTimeTable(ac_year,school_id,year_sem,commencement_id,program_specialization_id);
			ResponseEntity<Object> academic_calender_response = ResponseHandler.generateResponse(true, HttpStatus.OK, commencement_details);
			return academic_calender_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}
	}

}
