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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.JwtDetails;
import com.au.model.InfrastructureFloors;
import com.au.response.ResponseHandler;
import com.au.service.InfrastructureFloorsService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class InfrastructureFloorsController {
	
	@Autowired
	private InfrastructureFloorsService floors_ser;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	
//	@PostMapping("/floors")
//	public ResponseEntity<Object> saveFloors(@Valid @RequestBody InfrastructureFloors floors,
//			@RequestHeader("Authorization") String jwtToken)
//			throws Exception, JsonParseException, JsonMappingException, IOException {
//		if (RateLimitController.bucket.tryConsume(1)) {
//			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
//			floors.setCreated_by(jwtDetails.getUserId());
//			floors.setCreated_username(jwtDetails.getUserName());
//			InfrastructureFloors f = floors_ser.saveFloors(floors);
//			ResponseEntity<Object> floors_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, f);
//			return floors_response;
//		} else {
//			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
//					ResponseHandler.message1);
//			return rs;
//		}
//	}
	
	@GetMapping("/getFloors/{block_id}")
	public ResponseEntity<Object> getFloors(@PathVariable Integer block_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				List<InfrastructureFloors> floors = floors_ser.getFloors(block_id);
				ResponseEntity<Object> floors_response = ResponseHandler.generateResponse(true, HttpStatus.OK,floors);
				return floors_response;
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
	
	@GetMapping("/floors")
	public ResponseEntity<Object> listAll() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<InfrastructureFloors> list_blocks = floors_ser.listAll();
			ResponseEntity<Object> list_blocks_response = ResponseHandler.generateResponse(true, HttpStatus.OK,list_blocks);
			return list_blocks_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllFloorDetails")
	public ResponseEntity<Object> listAll1(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword) {
		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> floors_sorted = floors_ser.getAllDataFilteredByKeyword(pageable,keyword);
				return floors_sorted;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> floors_pageable = floors_ser.getAllSortedData(pageable1);
				return floors_pageable;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/floors/{floor_id}")
	public ResponseEntity<Object> get(@PathVariable Integer floor_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				InfrastructureFloors floor = floors_ser.getFloor(floor_id);
				ResponseEntity<Object> floor_response = ResponseHandler.generateResponse(true, HttpStatus.OK,floor);
				return floor_response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/floors/{floor_id}")
	public ResponseEntity<Object> update(@RequestBody @Valid InfrastructureFloors f, @PathVariable Integer floor_id,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				f.setModified_by(jwtDetails.getUserId());
				f.setModified_username(jwtDetails.getUserName());
				floors_ser.saveUpdateFloor(f);
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
	
	@DeleteMapping("/deactivateFloor/{floor_id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer floor_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			floors_ser.deactivate(floor_id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateFloor/{floor_id}")
	public ResponseEntity<Object> activate(@PathVariable Integer floor_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			floors_ser.activate(floor_id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

}
