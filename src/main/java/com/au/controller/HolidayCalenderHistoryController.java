package com.au.controller;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

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
import com.au.model.HolidayCalenderHistory;
import com.au.response.ResponseHandler;
import com.au.service.HolidayCalenderHistoryService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class HolidayCalenderHistoryController {
	
	@Autowired
	private HolidayCalenderHistoryService hchs_ser;
	
	@Autowired
	private JwtTokenService jwt_service;
	

	@PostMapping("/HolidayCalenderHistory")
	public ResponseEntity<Object> saveHolidayCalenderHistory(@RequestBody @Valid HolidayCalenderHistory h,
			@RequestHeader("Authorization") String jwtToken)throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			h.setCreatedBy(jwtDetails.getUserId());
			h.setCreatedUsername(jwtDetails.getUserName());
			HolidayCalenderHistory hch = hchs_ser.saveHolidayCalenderHistory(h);
			ResponseEntity<Object> hch_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, hch);
			return hch_response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
	}

	@GetMapping("/getHolidayCalenderHistory")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HolidayCalenderHistory> hc_list = hchs_ser.listAll();
			ResponseEntity<Object> hc_list_response= ResponseHandler.generateResponse(true, HttpStatus.OK, hc_list);
			return hc_list_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchAllHolidayCalenderHistory")
	public ResponseEntity<Object> getAllHolidayCalenderDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> holiday_filtered =  hchs_ser.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
			return holiday_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> holiday_sorted = hchs_ser.getAllSortedData(pageable1);
			return holiday_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/HolidayCalenderHistoryById/{hch_id}")
	public ResponseEntity<Object> get(@PathVariable Integer hch_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			HolidayCalenderHistory hch = hchs_ser.get(hch_id);
			ResponseEntity<Object> hch_response= ResponseHandler.generateResponse(true, HttpStatus.OK, hch);
			return hch_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/updateHolidayCalenderHistory/{hch_id}")
	public ResponseEntity<Object> update(@RequestBody @Valid HolidayCalenderHistory hch, @PathVariable Integer hch_id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			HolidayCalenderHistory existProduct = hchs_ser.get(hch_id);
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			hch.setModifiedBy(jwtDetails.getUserId());
			hch.setModifiedUsername(jwtDetails.getUserName());
			hchs_ser.saveHolidayCalenderHistory1(hch);
			ResponseEntity<Object> hch_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return hch_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/HolidayCalenderHistory/{hch_id}")
	public ResponseEntity<Object> delete(@PathVariable Integer hch_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			hchs_ser.deactivate(hch_id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateHolidayCalenderHistory/{hch_id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer hch_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			hchs_ser.activate(hch_id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
}
