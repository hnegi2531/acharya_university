package com.au.controller;

import java.io.IOException;
import java.util.List;
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
import com.au.model.MealType;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.MealTypeService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class MealTypeController {
	
	Logger log = LoggerFactory.getLogger(MealTypeController.class);

	@Autowired
	private JwtTokenService jwt_service;

	@Autowired
	private MealTypeService mts_ser;

	@PostMapping("/mealType")
	public ResponseEntity<Object> saveCatMealType(@RequestBody @Valid MealType cmt,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			cmt.setCreated_by(jwtDetails.getUserId());
			cmt.setCreated_username(jwtDetails.getUserName());
			MealType meal_type = mts_ser.saveMealType(cmt);
			ResponseEntity<Object> meal_type_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					meal_type);
			return meal_type_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/getMealType")
	public ResponseEntity<Object> listAll1() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<MealType> meal_list = mts_ser.listAll1();
			ResponseEntity<Object> meal_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					meal_list);
			return meal_list_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/getOnlyEndUserMealType")
	public ResponseEntity<Object> getOnlyEndUserMealType() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<MealType> meal_list = mts_ser.getOnlyEndUserMealType();
			ResponseEntity<Object> meal_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					meal_list);
			return meal_list_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllMealTypeDetails")
	public ResponseEntity<Object> fetchAllMealTypeDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> list_sorted = mts_ser.fetchAllMealTypeDetails(pageable, keyword);//,column,value);
				return list_sorted;
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> list_pageable = mts_ser.fetchAllMealTypeDetails1(pageable1);
				return list_pageable;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

//	@GetMapping("fetchAllDateById/{mess_meal_type}")
//	public ResponseEntity<Object> get(@PathVariable Integer mess_meal_type) {
//		if(RateLimitController.bucket.tryConsume(1)) {
//			List<Map<String, Object>> meal_list = mts_ser.listAll3(mess_meal_type);
//			ResponseEntity<Object> meal_list_response= ResponseHandler.generateResponse(true, HttpStatus.OK, meal_list);
//			return meal_list_response;
//			} else {
//				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//				return rs;
//			}
//	}

	@GetMapping("fetchMessMealTypeContents/{mess_meal_type}")
	public ResponseEntity<Object> get2(@PathVariable String mess_meal_type) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<MealType> meal_list = mts_ser.listAll4(mess_meal_type);
			ResponseEntity<Object> meal_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					meal_list);
			return meal_list_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/mealType/{meal_id}")
	public ResponseEntity<Object> get1(@PathVariable Integer meal_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				MealType mlr = mts_ser.get(meal_id);
				ResponseEntity<Object> catmealtype_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
						mlr);
				return catmealtype_response;
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

	@PutMapping("/updateMealType/{meal_id}")
	public ResponseEntity<Object> updateCatMealType(@RequestBody MealType cmt, @PathVariable Integer meal_id,@RequestHeader("Authorization")  String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				
				  JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				  cmt.setModified_by(jwtDetails.getUserId());
				  cmt.setModified_username(jwtDetails.getUserName());
				 
				mts_ser.saveUpdateMealType(cmt);
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

	@DeleteMapping("/deactivateMealType/{meal_id}")
	public ResponseEntity<Object> delete(@PathVariable Integer meal_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			mts_ser.delete(meal_id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateMealType/{meal_id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer meal_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			mts_ser.delete1(meal_id);
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
