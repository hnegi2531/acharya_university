package com.au.controller;

import java.io.IOException;
import java.util.List;
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
import com.au.model.SubjectWorkLoadType;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.SubjectWorkLoadTypeService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
//@RequestMapping("/api")
public class SubjectWorkLoadTypeController {

	@Autowired
	private SubjectWorkLoadTypeService a_service;
	
	Logger log = LoggerFactory.getLogger(AliasNameController.class);
	
	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/SubjectWorkLoadType")
	public ResponseEntity<Object> saveAliasName(@RequestBody @Valid SubjectWorkLoadType bs,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			SubjectWorkLoadType swt = a_service.saveSubjectWorkLoadType(bs);
			ResponseEntity<Object> swt_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, swt);
			return swt_response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
			
	}

	@GetMapping("/fetchAllSubjectWorkLoadType")
	public ResponseEntity<Object> getAllSubjectWorkLoadTypeDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> swt_filtered =  a_service.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
			return swt_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> swt_sorted = a_service.getAllSortedData(pageable1);
			return swt_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/SubjectWorkLoadType/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {

			SubjectWorkLoadType swt = a_service.get(id);
			ResponseEntity<Object> swt_response= ResponseHandler.generateResponse(true, HttpStatus.OK, swt);
			return swt_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/SubjectWorkLoadType/{id}")
	public ResponseEntity<Object> update(@RequestBody SubjectWorkLoadType bs, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		try {
			SubjectWorkLoadType existProduct = a_service.get(id);
			bs.setModifiedBy(jwtDetails.getUserId());
			bs.setModifiedUsername(jwtDetails.getUserName());
			a_service.saveSubjectWorkLoadType(bs);
			ResponseEntity<Object> swt_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return swt_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/SubjectWorkLoadType/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		a_service.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchWorkLoadTypesDetails1/{subjectId}/{subject_work_load_type_id}")
	public ResponseEntity<Object> getSubjectWorkLoadTypeBySubjectId(@PathVariable Integer subjectId,
			@PathVariable Integer subject_work_load_type_id){
		if(RateLimitController.bucket.tryConsume(1)) {
			SubjectWorkLoadType swt = a_service.getSubjectWorkLoadTypeBySubjectId(subjectId,subject_work_load_type_id);
			ResponseEntity<Object> swt_response= ResponseHandler.generateResponse(true, HttpStatus.OK, swt);
			return swt_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}


	@PutMapping("/updateSubWorkLoadDetails")
	public ResponseEntity<Object> update1(@RequestBody List<SubjectWorkLoadType> dto, 
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			//SubjectWorkLoadType existProduct = a_service.get(id);		
			a_service.saveSubjectWorkLoadType1(dto,jwtToken);
			ResponseEntity<Object> swt_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return swt_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
			
			
	}
	
	@GetMapping("/fetchSubjectWorkLoad/{subjectId}")
	public ResponseEntity<Object> getSubjectWorkLoadTypeBySubjectIdAndworkLoadId(@PathVariable Integer subjectId) {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<SubjectWorkLoadType> swt = a_service.getSubjectWorkLoadTypeBySubjectId(subjectId);
		ResponseEntity<Object> swt_response= ResponseHandler.generateResponse(true, HttpStatus.OK, swt);
		return swt_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
}
