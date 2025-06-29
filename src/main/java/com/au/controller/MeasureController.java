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
import com.au.model.Measure;
import com.au.model.StoresStock;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.MeasureService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class MeasureController {
	
	Logger log = LoggerFactory.getLogger(MeasureController.class);
	
	@Autowired
	private MeasureService m_ser;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/measure")
	public ResponseEntity<Object> createMeasure(@RequestBody @Valid Measure ms , @RequestHeader("Authorization") String jwtToken) 
				throws Exception , JsonParseException ,JsonMappingException , IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			ms.setCreated_by(jwtDetails.getUserId());
			ms.setCreated_username(jwtDetails.getUserName());
			Measure measure = m_ser.createMeasure(ms);
			ResponseEntity<Object> measure_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, measure);
			return measure_response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
	}
	
	@GetMapping("/activeMeasure")
	public ResponseEntity<Object> getActiveMeasure(){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Measure> measure = m_ser.getActiveMeasure();
			ResponseEntity<Object> measure_response= ResponseHandler.generateResponse(true, HttpStatus.OK, measure);
			return measure_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllMeasure")
	public ResponseEntity<Object> getAllMeasureDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> measures_filtered =  m_ser.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
			return measures_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> measures_sorted = m_ser.getAllSortedData(pageable1);
			return measures_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/measure/{measure_id}")
	public  ResponseEntity<Object> get(@PathVariable Integer measure_id){
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
					Measure measure = m_ser.get(measure_id);
						ResponseEntity<Object> measure_response_by_id= ResponseHandler.generateResponse(true, HttpStatus.OK, measure);
						return measure_response_by_id;
				}catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@PutMapping("/updateMeasure/{measure_id}")
	public ResponseEntity<Object> update(@RequestBody Measure ms , @PathVariable Integer measure_id ,
				@RequestHeader("Authorization") String jwtToken)
				throws Exception , JsonParseException , JsonMappingException , IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			ms.setModified_by(jwtDetails.getUserId());
			ms.setModified_username(jwtDetails.getUserName());
			m_ser.update(ms);
			ResponseEntity<Object> measure_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return measure_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
		
	}
	
	@DeleteMapping("/measure/{measure_id}")
	public ResponseEntity<Object> delete(@PathVariable Integer measure_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		m_ser.delete(measure_id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@DeleteMapping("/activateMeasure/{measure_id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer measure_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		m_ser.delete1(measure_id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		 return response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}

}
