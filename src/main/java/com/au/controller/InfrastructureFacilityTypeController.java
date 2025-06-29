package com.au.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
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
import com.au.model.InfrastructureFacilityType;
import com.au.response.ResponseHandler;
import com.au.service.InfrastructureFacilityTypeService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class InfrastructureFacilityTypeController {

	Logger log = LoggerFactory.getLogger(JobTypeController.class);

	@Autowired
	private JwtTokenService jwt_service;

	@Autowired
	private InfrastructureFacilityTypeService facility_ser;

	@PostMapping("/facilityType")
	public ResponseEntity<Object> saveFacilityType(@Valid @RequestBody InfrastructureFacilityType facility,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			facility.setCreated_by(jwtDetails.getUserId());
			facility.setCreated_username(jwtDetails.getUserName());
			InfrastructureFacilityType ift = facility_ser.saveFacilityType(facility);
			ResponseEntity<Object> facility_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, ift);
			return facility_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/facilityType")
	public ResponseEntity<Object> listAll() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<InfrastructureFacilityType> list_facility_type = facility_ser.listAll();
			ResponseEntity<Object> list_facility_type_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					list_facility_type);
			return list_facility_type_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchAllFacilityTypeDetail")
	public ResponseEntity<Object> listAll1(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword) {
		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> facility_type_sorted = facility_ser.getAllDataFilteredByKeyword(pageable,
						keyword);
				return facility_type_sorted;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> facility_type_pageable = facility_ser.getAllSortedData(pageable1);
				return facility_type_pageable;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
		// return j_service.listAll1();
	}

	@GetMapping("/facilityType/{facility_type_id}")
	public ResponseEntity<Object> get(@PathVariable Integer facility_type_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				InfrastructureFacilityType facility = facility_ser.get(facility_type_id);
				ResponseEntity<Object> facility_type_response = ResponseHandler.generateResponse(true, HttpStatus.OK,facility);
				return facility_type_response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
						HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/facilityType/{facility_type_id}")
	public ResponseEntity<Object> update(@RequestBody @Valid InfrastructureFacilityType ift, @PathVariable Integer facility_type_id,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				ift.setModified_by(jwtDetails.getUserId());
				ift.setModified_username(jwtDetails.getUserName());
				facility_ser.saveUpdatefacilitytype(ift);
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,HttpStatus.OK);
				return response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/deactivateFacilityType/{facility_type_id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer facility_type_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			facility_ser.deactivate(facility_type_id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateFacilityType/{facility_type_id}")
	public ResponseEntity<Object> activate(@PathVariable Integer facility_type_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			facility_ser.activate(facility_type_id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/getFacilityTypeBasedOnEvent")
	public ResponseEntity<Object> getFacilityTypeBasedOnEvent() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> list_facility_type = facility_ser.getFacilityTypeBasedOnEvent();
			ResponseEntity<Object> list_facility_type_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					list_facility_type);
			return list_facility_type_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/getEventRoomAvailability")
	public ResponseEntity<Object> getEventRoomAvailability( @RequestParam("facility_type_id") Integer facility_type_id,
            @RequestParam("month") Integer month,
            @RequestParam("year") Integer year) {
		if(RateLimitController.bucket.tryConsume(1)) {
			 LocalDate startDate = LocalDate.of(year, month, 1);  // First day of the month
		        LocalDate endDate = YearMonth.of(year, month).atEndOfMonth();  // Last day of the month
			Map<String, Object> event_details = facility_ser.getEventRoomAvailability(facility_type_id, startDate, endDate);
			ResponseEntity<Object> event_details_response = ResponseHandler.generateResponse(true, HttpStatus.OK, event_details);
			return event_details_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
	
	@GetMapping("/getFacilityTypeForTimeTable")
	public ResponseEntity<Object> getFacilityTypeForTimeTable() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> list_facility_type = facility_ser.getFacilityTypeForTimeTable();
			ResponseEntity<Object> list_facility_type_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					list_facility_type);
			return list_facility_type_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

}
