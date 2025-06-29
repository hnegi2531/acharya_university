package com.au.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.JwtDetails;
import com.au.dto.ServiceTicketDto;
import com.au.model.ServiceTypeTicket;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.ServiceTypeService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/ServiceTicket")
@CrossOrigin
public class ServiceTicketCotroller {
	
	@Autowired
	private ServiceTypeService service;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping
	public ResponseEntity<Object> raiseServiceTicket(@RequestBody ServiceTicketDto dto,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			ServiceTypeTicket response = service.saveServiceTicket(dto, jwtDetails);
			return ResponseHandler.generateResponse(true, HttpStatus.CREATED, response);

		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping
	public ResponseEntity<Object> getAllTickectsRaised(@RequestParam(value = "fromDate") String fromDate,
			@RequestParam(value = "toDate") String toDate,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword) throws ParseException {
		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			ResponseEntity<Object> list = service.getAllTickectsRaised(pageable, keyword, fromDate, toDate);
			return list;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getAllServiceByUserId/{user_id}")
	public ResponseEntity<Object> getAllServiceByUserId(@PathVariable Integer user_id ){
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> allServiceByDeptTag = service.getAllServiceByUserId(user_id);
			ResponseEntity<Object> response = ResponseHandler.generateResponse(true, HttpStatus.OK, allServiceByDeptTag);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}	
	

}
