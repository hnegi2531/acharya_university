package com.au.controller;

import java.io.IOException;
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
import com.au.dto.LeavePatternRequest;
import com.au.model.LeavePattern;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.LeavePatternService;
import com.au.service.TriggerService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class LeavePatternController {

	@Autowired
	private LeavePatternService leave_pattern_service;

	Logger log = LoggerFactory.getLogger(LeavePatternController.class);

	@Autowired
	private JwtTokenService jwt_service;

	@Autowired
	private TriggerService triggerService;

	@PostMapping("/LeavePattern")
	public ResponseEntity<Object> saveSubjectType(@RequestBody @Valid LeavePatternRequest leave,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			leave.setCreated_by(jwtDetails.getUserId());
			leave.setCreated_username(jwtDetails.getUserName());
			List<LeavePattern> leave_pattern = leave_pattern_service.saveLeavePattern(leave);
			ResponseEntity<Object> leave_pattern_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED,
					leave_pattern);
			return leave_pattern_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/LeavePattern")
	public ResponseEntity<Object> listAll() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<LeavePattern> leave_pattern_list = leave_pattern_service.listAll();
			ResponseEntity<Object> leave_pattern_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					leave_pattern_list);
			return leave_pattern_list_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchAllLeavePatternDetails")
	public ResponseEntity<Object> listAll1(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword) {
		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> leave_pattern_sorted = leave_pattern_service.listAll1(pageable, keyword);// ,column,value);
				return leave_pattern_sorted;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> leave_pattern_pageable = leave_pattern_service.listAll2(pageable1);
				return leave_pattern_pageable;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
		// return leave_pattern_service.listAll1();
	}

	@GetMapping("/LeavePattern/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				LeavePattern product = leave_pattern_service.get(id);
				ResponseEntity<Object> leave_pattern_response_by_id = ResponseHandler.generateResponse(true,
						HttpStatus.OK, product);
				return leave_pattern_response_by_id;
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

	@PutMapping("/LeavePattern/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid LeavePattern leave, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				LeavePattern existProduct = leave_pattern_service.get(id);
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

				leave.setModified_by(jwtDetails.getUserId());
				leave.setModified_username(jwtDetails.getUserName());

				leave_pattern_service.saveLeavePattern1(leave);
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
						HttpStatus.OK);
				return response;
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

	@DeleteMapping("/LeavePattern/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			leave_pattern_service.delete(id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateLeavePattern/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			leave_pattern_service.delete1(id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	/*
	 * @GetMapping("/fetchLeavePatternByYearAndSchool/{year}/{school_id}") public
	 * Map<String, Object> fetchLeavePatternByYearAndSchool(@PathVariable Integer
	 * year,@PathVariable Integer school_id) { return
	 * leave_pattern_service.fetchLeavePatternByYearAndSchool(year, school_id); }
	 */

	@GetMapping("/fetchLeavePatternByYearAndSchool/{year}/{school_id}")
	public ResponseEntity<Object> fetchLeavePatternByYearAndSchool(@PathVariable Integer year,
			@PathVariable Integer school_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			Map<String, Object> leave_pattern_by_year_school = leave_pattern_service
					.fetchLeavePatternByYearAndSchool(year, school_id);
			ResponseEntity<Object> leave_pattern_by_year_school_response = ResponseHandler.generateResponse(true,
					HttpStatus.OK, leave_pattern_by_year_school);
			return leave_pattern_by_year_school_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchLeavePatternYearshool")
	public ResponseEntity<Object> fetchLeavePatternYearshool() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> leave_pattern_year_school = leave_pattern_service.fetchLeavePatternYearshool();
			ResponseEntity<Object> leave_pattern_year_school_response = ResponseHandler.generateResponse(true,
					HttpStatus.OK, leave_pattern_year_school);
			return leave_pattern_year_school_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	/*
	 * @GetMapping(
	 * "/validationLeavePattern/{year}/{school_id}/{leave_id}/{emp_type_id}/{job_type_id}")
	 * public Boolean validationLeavePattern(@PathVariable Integer
	 * year, @PathVariable Integer school_id,
	 * 
	 * @PathVariable Integer leave_id, @PathVariable Integer
	 * emp_type_id, @PathVariable Integer job_type_id) { return
	 * leave_pattern_service.validationLeavePattern(year, school_id, leave_id,
	 * emp_type_id, job_type_id); }
	 */
	@GetMapping("/fetchLeavePatternByYear/{year}/{school_id}")
	public ResponseEntity<Object> fetchLeavePatternByYear(@PathVariable Integer year, @PathVariable Integer school_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> leave_pattern_by_year_school_id = leave_pattern_service
					.fetchLeavePatternByYear(year, school_id);
			ResponseEntity<Object> leave_pattern_by_year_school_id_response = ResponseHandler.generateResponse(true,
					HttpStatus.OK, leave_pattern_by_year_school_id);
			return leave_pattern_by_year_school_id_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PostMapping("/copyLeavePattern/{prev_year}/{next_year}/{school_id}")
	public ResponseEntity<Object> copyLeavePattern(@PathVariable Integer prev_year, @PathVariable Integer next_year,
			@PathVariable Integer school_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			System.out.println("controller=" + prev_year + "\t" + next_year + "\t" + school_id);
			List<LeavePattern> copy_leave_pattern = leave_pattern_service.copyLeavePattern(prev_year, next_year,
					school_id);
			ResponseEntity<Object> copy_leave_pattern_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					copy_leave_pattern);
			return copy_leave_pattern_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PostMapping("/createEmployeeLeaveKitty")
	public ResponseEntity<Object> createEmployeeLeaveKitty(@RequestParam(required = false) Integer empId) {
		triggerService.createEmployeeLeaveKitty(empId);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "leave Kitty Started");

	}

	/*
	 * @PostMapping("/copyLeavePattern") public List<LeavePattern>
	 * copyLeavePattern(@RequestBody CopyLeavePatternDto c) {
	 * System.out.println("---------ttt---------------------------"+c);
	 * System.out.println("deded-"+c.getPrev_year()+"\t"+c.getNext_year()+"\t"+c.
	 * getSchool_id()); return
	 * leave_pattern_service.copyLeavePattern(c.getPrev_year(), c.getNext_year(),
	 * c.getSchool_id()); }
	 */

}
