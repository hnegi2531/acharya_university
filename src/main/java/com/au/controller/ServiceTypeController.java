package com.au.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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
import com.au.dto.ServiceTypeDeptTag;
import com.au.dto.ServiceTypeDto;
import com.au.model.Department;
import com.au.model.EventCreation;
import com.au.model.ServiceType;
import com.au.model.ServiceTypeDeptTags;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.ServiceTypeService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/ServiceType")
@CrossOrigin
public class ServiceTypeController {
	
	@Autowired
	private ServiceTypeService service;
	
	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping
	public ResponseEntity<Object> saveServiceType(@RequestBody @Valid ServiceType request,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			ServiceType response = service.saveServiceType(request, jwtToken);
			return ResponseHandler.generateResponse(true, HttpStatus.CREATED, response);

		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid ServiceTypeDto request, @PathVariable Long id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				service.updateServiceType(id, request, jwtToken);
				return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
						HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/updateServiceType/{id}")
	public ResponseEntity<Object> updateServiceType(@RequestBody @Valid ServiceType st, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	//Department existProduct = deptService.get(id);
	    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    	st.setModified_by(jwtDetails.getUserId());
	    	st.setModified_username(jwtDetails.getUserName());
	    	service.updateServiceType(st);
	    	ResponseEntity<Object> department_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return department_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}

	@GetMapping
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> job_profile_sorted = service.listAll1(pageable, keyword);//,column,value);
			return job_profile_sorted;
		}else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> job_profile_pageable = service.listAll2(pageable1);
			return job_profile_pageable;
		}

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deactivateServiceType(@PathVariable Long id, @RequestParam Boolean active) {
		if (RateLimitController.bucket.tryConsume(1)) {
			service.deleteServiceType(id, active);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PostMapping("/assignDeptTagToService")
	public ResponseEntity<Object> assignDeptTagToService(@RequestBody @Valid ServiceTypeDeptTag dto,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			List<ServiceTypeDeptTags> response = service.assignDeptTagToService(dto, jwtDetails);
			return ResponseHandler.generateResponse(true, HttpStatus.CREATED, response);

		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getAllServiceDept")
	public ResponseEntity<Object> getAllServiceDept(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword) {
		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			ResponseEntity<Object> list = service.getAllServiceDept(pageable, keyword);
//			ResponseEntity<Object> response= ResponseHandler.generateResponse(true, HttpStatus.OK, list);
			return list;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@DeleteMapping("/deactivateServiceTypeDeptTag/{id}")
	public ResponseEntity<Object> deactivateServiceTypeDeptTag(@PathVariable Long id, @RequestParam Boolean active) {
		if (RateLimitController.bucket.tryConsume(1)) {
			service.deleteServiceTypeDeptTag(id, active);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/getAllServiceByDeptTag/{dept_id}")
	public ResponseEntity<Object> getAllServiceByDeptTag(@PathVariable Integer dept_id ){
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> allServiceByDeptTag = service.getAllServiceByDeptTag(dept_id);
			ResponseEntity<Object> response = ResponseHandler.generateResponse(true, HttpStatus.OK, allServiceByDeptTag);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getServiceTypeById/{id}")
	public ResponseEntity<Object> get(@PathVariable Long id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			ServiceType product = service.get(id);
			ResponseEntity<Object> program_type_response_by_id= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
			return program_type_response_by_id;
	    } catch (NoSuchElementException e) {
			ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response1;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/getAllActiveServiceType")
	public ResponseEntity<Object> getAllActiveServiceType() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<ServiceType> department = service.getAllActiveServiceType();
		ResponseEntity<Object> department_response= ResponseHandler.generateResponse(true, HttpStatus.OK, department);
		return department_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}	
	
	@GetMapping("/getAllServiceTypeById/{id}")
	public ResponseEntity<Object> getAllServiceTypeById(@PathVariable Long id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			List<Map<String, Object>> product = service.getAllServiceTypeById(id);
			ResponseEntity<Object> program_type_response_by_id= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
			return program_type_response_by_id;
	    } catch (NoSuchElementException e) {
			ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response1;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@GetMapping("/getAllActiveServiceTypeOnlyevent")
	public ResponseEntity<Object> getAllActiveServiceTypeOnlyevent() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<ServiceType> department = service.getAllActiveServiceTypeOnlyevent();
		ResponseEntity<Object> department_response= ResponseHandler.generateResponse(true, HttpStatus.OK, department);
		return department_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}	
	
}
