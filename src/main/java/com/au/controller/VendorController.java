package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

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
import com.au.model.Vendor;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.VendorService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey5}")
@CrossOrigin
public class VendorController {

	Logger log = LoggerFactory.getLogger(VendorController.class);

	@Autowired
	private VendorService vendorService;

	@Autowired
	private JwtTokenService jwtService;

	@PostMapping("/vendor")
	public ResponseEntity<Object> saveVendor(@RequestBody @Valid Vendor vendor,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException, ConstraintViolationException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwtService.callJwtToken(jwtToken);
			vendor.setCreated_by(jwtDetails.getUserId());
			vendor.setCreated_username(jwtDetails.getUserName());
			Vendor ven = vendorService.saveVendor(vendor);
			return ResponseHandler.generateResponse(true, HttpStatus.CREATED, ven);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@GetMapping("/vendor")
	public ResponseEntity<Object> getAllVendorDetails(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword) {
		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				return vendorService.getAllVendorDetails1(pageable, keyword);
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				return vendorService.getAllVendorDetails2(pageable1);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}

	@GetMapping("/vendorActiveDetails")
	public ResponseEntity<Object> getActiveVendorDetails() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Vendor> vendorList = vendorService.getActiveVendorDetails();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, vendorList);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@GetMapping("/vendorById/{id}")
	public ResponseEntity<Object> getVendorDetailsById(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				Vendor product = vendorService.getVendorDetailsById(id);
				return ResponseHandler.generateResponse(true, HttpStatus.OK, product);

			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@PutMapping("/vendor/{vendor_id}")
	public ResponseEntity<Object> update(@RequestBody @Valid Vendor vendor, @PathVariable Integer vendor_id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwtService.callJwtToken(jwtToken);
				vendor.setModified_by(jwtDetails.getUserId());
				vendor.setModified_username(jwtDetails.getUserName());
				vendorService.updateVendorDetails(vendor);
				return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@DeleteMapping("/vendor/{vendor_id}")
	public ResponseEntity<Object> delete(@PathVariable Integer vendor_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			vendorService.delete(vendor_id);
			return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@DeleteMapping("/activateVendor/{vendor_id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer vendor_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			vendorService.delete1(vendor_id);
			return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@GetMapping("/getVoucherHeadNewDataFromVendor")
	public ResponseEntity<Object> getVoucherHeadNewDataFromVendor() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> listOfVoucherHeadNew = vendorService.getVoucherHeadNewDataFromVendor();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, listOfVoucherHeadNew);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

}
