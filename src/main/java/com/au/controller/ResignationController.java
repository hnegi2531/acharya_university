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
import com.au.model.Resignation;
import com.au.repository.DepartmentRepository;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.ResignationService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class ResignationController {
	
	Logger log = LoggerFactory.getLogger(ResignationController.class);
	
	@Autowired
	private ResignationService resignation_service;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@Autowired
	private DepartmentRepository deptrepo;
	
	@PostMapping("/resignation")
	public ResponseEntity<Object> saveResignation(@RequestBody @Valid Resignation res,@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				res.setCreated_by(jwtDetails.getUserId());
				res.setCreated_username(jwtDetails.getUserName());
				Resignation resignation = resignation_service.saveResignation(res);
				ResponseEntity<Object> resignation_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, resignation);
				return resignation_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}	

	@GetMapping("/resignation")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<Resignation> resignation_list = resignation_service.listAll();
				ResponseEntity<Object> resignation_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, resignation_list);
				return resignation_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@GetMapping("/fetchAllResignationDetails")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					ResponseEntity<Object> resignation_details_sorted = resignation_service.listAll1(pageable, keyword);//,column,value);
					return resignation_details_sorted;
				}else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					ResponseEntity<Object> resignation_details_pageable = resignation_service.listAll2(pageable1);
					return resignation_details_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
	
	@GetMapping("/fetchAllResignationDetailsBasedOnUserId")
	public ResponseEntity<Object> fetchAllResignationDetailsBasedOnUserId(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="UserId") Integer UserId,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
			Integer count = deptrepo.countBasedOnHodId(UserId);
				if(keyword != null) {	
					if(count > 0) {
						System.out.println("!!!!!!!!!!!!!!! hod_id");
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						ResponseEntity<Object> resignation_details_sorted = resignation_service.listAllWithKeyword(pageable, keyword,UserId );//,column,value);
						return resignation_details_sorted;
						}else {
							System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ leaveApprover");
							Pageable pageable = PageRequest.of(page, page_size,sorted);
							ResponseEntity<Object> resignation_details_sorted = resignation_service.listAllWithLeaveApprKeyword(pageable, keyword ,UserId);//,column,value);
							return resignation_details_sorted;
						}
				}else {
					if(count > 0) {
						System.out.println("!!!!!!!!!!!!!!! hod_id");
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						ResponseEntity<Object> resignation_details_pageable = resignation_service.listAllWithOutKeyword(pageable1,UserId );
						return resignation_details_pageable;
						}else {
							System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ leaveApprover");
							Pageable pageable1 = PageRequest.of(page, page_size,sorted);
							ResponseEntity<Object> resignation_details_pageable = resignation_service.listAllLeaveApprWithOutKeyword(pageable1,UserId );
							return resignation_details_pageable;
					} 
				}
		}
				else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
	
	@GetMapping("/fetchAllResignationHistoryDetails")
	public ResponseEntity<Object> fetchAllResignationHistoryDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					ResponseEntity<Object> resignation_details_sorted = resignation_service.fetchAllResignationHistoryDetails(pageable, keyword);//,column,value);
					return resignation_details_sorted;
				}else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					ResponseEntity<Object> resignation_details_pageable = resignation_service.fetchAllResignationHistoryDetails1(pageable1);
					return resignation_details_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

	@GetMapping("/resignation/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				Resignation product = resignation_service.get(id);
				ResponseEntity<Object> resignation_response_by_id = ResponseHandler.generateResponse(true,HttpStatus.OK, product);
				return resignation_response_by_id;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}		
	}

	@PutMapping("/resignation/{id}")
	public ResponseEntity<Object> update(@RequestBody Resignation res, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					res.setModified_by(jwtDetails.getUserId());
					res.setModified_username(jwtDetails.getUserName());
					resignation_service.updateResignation(res);
					ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
					return response;
				} catch (NoSuchElementException e) {
					ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
					return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@DeleteMapping("/resignation/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				resignation_service.delete(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}		
	}

	@DeleteMapping("/activateResignation/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				resignation_service.delete1(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}		
	}
	
	@GetMapping("/checkEmpIdIsAlreadyPresentOrNot/{emp_id}")
	public ResponseEntity<Object> checkEmpIdIsAlreadyPresentOrNot(@PathVariable Integer emp_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Integer employee = resignation_service.checkEmpIdIsAlreadyPresentOrNot(emp_id);
			ResponseEntity<Object> employee_response= ResponseHandler.generateResponse(true, HttpStatus.OK, employee);
			return employee_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	
	@GetMapping("/checkNoDuesStatus/{resignation_id}")
	public ResponseEntity<Object> checkNoDuesStatus(@PathVariable Integer resignation_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Boolean nodues = resignation_service.checkNoDuesStatus(resignation_id);
			ResponseEntity<Object> nodues_response= ResponseHandler.generateResponse(true, HttpStatus.OK, nodues);
			return nodues_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	
	@GetMapping("/getAlResignationWithRelievingNo/{resignation_id}")
	public ResponseEntity<Object> getAlResignationWithRelievingNo(@PathVariable Integer resignation_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> bit = resignation_service.getAlResignationWithRelievingNo(resignation_id);
			ResponseEntity<Object> bit_response= ResponseHandler.generateResponse(true, HttpStatus.OK, bit);

			return bit_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getAllResignationDetailsData/{resignation_id}")
	public ResponseEntity<Object> getAllResignationDetailsData(@PathVariable Integer resignation_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> bit = resignation_service.getAllResignationDetailsData(resignation_id);
			ResponseEntity<Object> bit_response= ResponseHandler.generateResponse(true, HttpStatus.OK, bit);

			return bit_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
}
