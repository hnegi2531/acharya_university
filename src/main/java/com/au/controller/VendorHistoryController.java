package com.au.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.hibernate.exception.ConstraintViolationException;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.JwtDetails;
import com.au.model.Vendor;
import com.au.model.VendorHistory;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.VendorHistoryService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey5}")
@CrossOrigin
public class VendorHistoryController {
	Logger log = LoggerFactory.getLogger(VendorHistoryController.class);

	@Autowired
	private VendorHistoryService vendorHistoryService;

	@Autowired
	private JwtTokenService jwtService;
	
	
	@PostMapping("/vendorHistory")
	public ResponseEntity<Object> saveVendorHistory(@RequestBody @Valid VendorHistory vendor,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException, ConstraintViolationException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwtService.callJwtToken(jwtToken);
			vendor.setCreated_by(jwtDetails.getUserId());
			vendor.setCreated_username(jwtDetails.getUserName());
			VendorHistory ven = vendorHistoryService.saveVendorHistory(vendor);
			return ResponseHandler.generateResponse(true, HttpStatus.CREATED, ven);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@GetMapping("/vendorHistoryActiveDetails")
	public ResponseEntity<Object> getActiveVendorDetails() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<VendorHistory> vendorList = vendorHistoryService.getActiveVendorDetails();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, vendorList);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}	
	
	
	@GetMapping("/fetchVendorHistoryDetails")
	public ResponseEntity<Object> getAllVendorDetails(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword) {
		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				return vendorHistoryService.getAllVendorDetails1(pageable, keyword);
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				return vendorHistoryService.getAllVendorDetails2(pageable1);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}
}
