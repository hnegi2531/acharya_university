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

import com.au.dto.HolidayCalenderRequest;
import com.au.dto.JwtDetails;
import com.au.model.HolidayCalender;
import com.au.response.ResponseHandler;
import com.au.service.HolidayCalenderHistoryService;
import com.au.service.HolidayCalenderService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RequestMapping("/api")
@RestController
@CrossOrigin
public class HolidayCalenderController {

	@Autowired
	private HolidayCalenderService s_service;

	Logger log = LoggerFactory.getLogger(HolidayCalenderController.class);

	@Autowired
	private HolidayCalenderHistoryService hchs_ser;
	
	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/HolidayCalender")
	public ResponseEntity<Object> saveSubjectType(@RequestBody @Valid HolidayCalenderRequest r,
			@RequestHeader("Authorization") String jwtToken)throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			r.setCreatedBy(jwtDetails.getUserId());
			r.setCreatedUsername(jwtDetails.getUserName());
			List<HolidayCalender> hc = s_service.saveHolidayCalender(r);
			ResponseEntity<Object> hc_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, hc);
			return hc_response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
	}

	@GetMapping("/HolidayCalender")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HolidayCalender> hc_list = s_service.listAll();
			ResponseEntity<Object> hc_list_response= ResponseHandler.generateResponse(true, HttpStatus.OK, hc_list);
			return hc_list_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchAllHolidayCalenderDetails")
	public ResponseEntity<Object> getAllHolidayCalenderDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> holiday_filtered =  s_service.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
			return holiday_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> holiday_sorted = s_service.getAllSortedData(pageable1);
			return holiday_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/HolidayCalender/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			HolidayCalender hc = s_service.get(id);
			ResponseEntity<Object> hc_response= ResponseHandler.generateResponse(true, HttpStatus.OK, hc);
			return hc_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/HolidayCalender/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid HolidayCalender r, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			HolidayCalender existProduct = s_service.get(id);
			System.out.println("((((()))))))))))))))))   " +existProduct);
		
			hchs_ser.saveToHolidayCalenderHistory(existProduct); //for saving data in HolidayCalenderHistory table
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			r.setModifiedBy(jwtDetails.getUserId());
			r.setModifiedUsername(jwtDetails.getUserName());
			s_service.saveHolidayCalender1(r);
			ResponseEntity<Object> hc_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return hc_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/HolidayCalender/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		s_service.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateHolidayCalender/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		s_service.delete1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchHolidayCalenderDetails")
	public ResponseEntity<Object> fetchHolidayCalenderDetails() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> hc = s_service.fetchHolidayCalenderDetails();
			ResponseEntity<Object> hc_response= ResponseHandler.generateResponse(true, HttpStatus.OK, hc);
			return hc_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchHolidayCalenderDetails1")
	public ResponseEntity<Object> fetchHolidayCalenderDetails1() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> hc = s_service.fetchHolidayCalenderDetails1();
			ResponseEntity<Object> hc_response= ResponseHandler.generateResponse(true, HttpStatus.OK, hc);
			return hc_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
	@GetMapping("/fetchHolidayCalendarDetailsOnSchoolId/{schoolId}")
	public ResponseEntity<Object> fetchHolidayCalenderDetailsOnSchooId(@PathVariable Integer schoolId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> holiday_details = s_service.fetchHolidayCalenderDetailsOnSchooId(schoolId);
			ResponseEntity<Object> holiday_details_response = ResponseHandler.generateResponse(true, HttpStatus.OK, holiday_details);
			return holiday_details_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
	@GetMapping("/fetchHolidayAndEventDetailsForAcademicCalendarOnSchoolId/{schoolId}")
	public ResponseEntity<Object> fetchHolidayAndEventDetailsForAcademicCalendarOnSchoolId(@PathVariable Integer schoolId){
	if(RateLimitController.bucket.tryConsume(1)) {
		HashMap<String ,Object> academic_calender = s_service.fetchHolidayAndEventDetailsForAcademicCalendarOnSchoolId(schoolId);
		ResponseEntity<Object> academic_calender_response = ResponseHandler.generateResponse(true, HttpStatus.OK, academic_calender);
		return academic_calender_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/listAllHolidayCalenderData")
	public List<Map<String, Object>> listAllHolidayCalenderData(@RequestParam(value = "schoolId" ,required = false) Integer schoolId,
			@RequestParam(value = "deptId" ,required = false) Integer deptId , @RequestParam(value = "jobTypeId" ,required = false) Integer jobTypeId) {

			if (schoolId != null) {
				List<Map<String, Object>> hc_list = s_service.listAllHolidayCalenderDataWith(schoolId,deptId,jobTypeId);
			return hc_list;
			}else {
				List<Map<String, Object>> hc1_list = s_service.listAllHolidayCalenderDataWithOutSchoolId();
				return hc1_list;
			}
			
		}
	
}
