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

import com.au.dto.JwtDetails;
import com.au.model.DoctorWarden;
import com.au.response.ResponseHandler;
import com.au.service.DoctorWardenService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey6}")
@CrossOrigin
public class DoctorWardenController {

	@Autowired
	private DoctorWardenService s_service;

	Logger log = LoggerFactory.getLogger(DoctorWardenController.class);
	
	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/Doctor")
	public ResponseEntity<Object> saveDoctorWarden(@RequestBody @Valid DoctorWarden r,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				r.setCreatedBy(jwtDetails.getUserId());
				r.setCreatedUsername(jwtDetails.getUserName());
				DoctorWarden dw = s_service.saveDoctor(r);
				ResponseEntity<Object> doctor_warden_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, dw);
				return doctor_warden_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@GetMapping("/Doctor")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<DoctorWarden> dw_list = s_service.listAll();
				ResponseEntity<Object> dw_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, dw_list);
				return dw_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@GetMapping("/fetchAllDoctorDetails")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> doctor_warden_sorted = s_service.listAll2(pageable, keyword);//,column,value);
						return doctor_warden_sorted;
				}else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> doctor_warden_pageable = s_service.listAll3(pageable1);
						return doctor_warden_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/Doctor/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {

						DoctorWarden product = s_service.get(id);
						ResponseEntity<Object> dw_response_by_id = ResponseHandler.generateResponse(true, HttpStatus.OK, product);
						return dw_response_by_id;
				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@PutMapping("/Doctor/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid DoctorWarden r, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
					DoctorWarden existProduct = s_service.get(id);
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

					r.setModifiedBy(jwtDetails.getUserId());
					r.setModifiedUsername(jwtDetails.getUserName());
			
					DoctorWarden dw = s_service.saveDoctorWarden(r);
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
	

	@DeleteMapping("/Doctor/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			s_service.delete(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateDoctor/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				s_service.delete1(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}

	@GetMapping("/fetchWardens")
	public ResponseEntity<Object> fetchWardens(){
		if(RateLimitController.bucket.tryConsume(1)) {
				List<DoctorWarden> warden_list = s_service.fetchWardens();
				ResponseEntity<Object> warden_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, warden_list);
				return warden_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/allUnassignedUsers")
	public ResponseEntity<Object> fetchUnassignedUsers() {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<Map<String, Object>> UnassignedUsers=s_service.fetchUnassignedUsers();
		ResponseEntity<Object> UnassignedUsers_response= ResponseHandler.generateResponse(true, HttpStatus.OK, UnassignedUsers);
		return UnassignedUsers_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/allUnassignedBlocksWithDoctor")
	public ResponseEntity<Object> fetchDoctorUnassignedBlocks() {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<Map<String, Object>> doctorUnassignedBlocks=s_service.fetchDoctorUnassignedBlocks();
		ResponseEntity<Object> doctorUnassignedBlocks_response= ResponseHandler.generateResponse(true, HttpStatus.OK, doctorUnassignedBlocks);
		return doctorUnassignedBlocks_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/allUnassignedBlocksWithWarden")
	public ResponseEntity<Object> UnassignedBlocksWithWarden() {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<Map<String, Object>> unassignedBlocksWithWarden=s_service.UnassignedBlocksWithWarden();
		ResponseEntity<Object> unassignedBlocksWithWarden_response= ResponseHandler.generateResponse(true, HttpStatus.OK, unassignedBlocksWithWarden);
		return unassignedBlocksWithWarden_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/allUnassignedFloorsWithWarden/{block_id}")
	public ResponseEntity<Object> UnassignedFloorsWithWarden(@PathVariable Integer block_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<Map<String, Object>> unassignedFloorsWithWarden=s_service.UnassignedFloorsWithWarden(block_id);
		ResponseEntity<Object> unassignedFloorsWithWarden_response= ResponseHandler.generateResponse(true, HttpStatus.OK, unassignedFloorsWithWarden);
		return unassignedFloorsWithWarden_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/floorsAssignedToWarden/{user_id}")
	public ResponseEntity<Object> floorsAssignedToWarden(@PathVariable Integer user_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<HashMap<String, Object>> floors_assigned_to_warden=s_service.floorsAssignedToWarden(user_id);
		ResponseEntity<Object> floors_assigned_to_warden_response= ResponseHandler.generateResponse(true, HttpStatus.OK, floors_assigned_to_warden);
		return floors_assigned_to_warden_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
}
