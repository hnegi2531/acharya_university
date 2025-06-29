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
import com.au.model.MealBill;
import com.au.model.MealType;
import com.au.model.StoreIndentRequest;
import com.au.model.StudentDocumentAudit;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.MealBillService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class MealBillController {
	
	Logger log = LoggerFactory.getLogger(MealBillController.class);

	@Autowired
	private JwtTokenService jwt_service;

	@Autowired
	private MealBillService mealBillService;

	
	@PostMapping("/createMealBill")
	public ResponseEntity<Object> createMealBill(@RequestBody @Valid List<MealBill> mealBill,@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		mealBill.stream().forEach(a -> {

			a.setCreated_by(jwtDetails.getUserId());
			a.setCreated_username(jwtDetails.getUserName());
			});
		List<MealBill> mb = mealBillService.createMealBill(mealBill);
		ResponseEntity<Object> mealBill_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, mb);
		return mealBill_response;
	} else {
		ResponseEntity<Object> response=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return response;
		}
	}
	
	
	@GetMapping("/getAllActiveMealBill")
	public ResponseEntity<Object> getAllActiveMealBill() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<MealBill> meal_list = mealBillService.getAllActiveMealBill();
			ResponseEntity<Object> meal_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					meal_list);
			return meal_list_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/getMealBill/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				MealBill dps = mealBillService.get(id);
				ResponseEntity<Object> tm_response = ResponseHandler.generateResponse(true, HttpStatus.OK, dps);
				return tm_response;
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

	@PutMapping("/updateMealBill/{meal_bill_id}")
	public ResponseEntity<Object> updateMealBill(@RequestBody MealBill cmt, @PathVariable Integer meal_bill_id,@RequestHeader("Authorization")  String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				
				  JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				  cmt.setModified_by(jwtDetails.getUserId());
				  cmt.setModified_username(jwtDetails.getUserName());
				 
				  mealBillService.updateMealBill(cmt);
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
	
	
	@DeleteMapping("/deactivateMealBill/{meal_bill_id}")
	public ResponseEntity<Object> delete(@PathVariable Integer meal_bill_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			mealBillService.deactivate(meal_bill_id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateMealBill/{meal_bill_id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer meal_bill_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			mealBillService.activate(meal_bill_id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/fetchAllMealBillDetails")
	public ResponseEntity<Object> fetchAllMealBillDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort,@RequestParam(value="bill_number") String bill_number, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> list_sorted = mealBillService.fetchAllMealTypeDetails(pageable,bill_number, keyword);//,column,value);
				return list_sorted;
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> list_pageable = mealBillService.fetchAllMealTypeDetails1(pageable1,bill_number);
				return list_pageable;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/fetchAllMealBillDetailsGrouped")
	public ResponseEntity<Object> fetchAllMealBillDetailsGrouped(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> list_sorted = mealBillService.fetchAllMealBillDetailsGrouped(pageable, keyword);//,column,value);
				return list_sorted;
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> list_pageable = mealBillService.fetchAllMealBillDetailsGrouped1(pageable1);
				return list_pageable;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	
	@GetMapping("/checkExitingData")
	public ResponseEntity<Object> checkExitingData(@RequestParam(value = "month_year") String month_year,@RequestParam(value = "voucher_head_new_id") Integer voucher_head_new_id,
			@RequestParam(value = "school_id") Integer school_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			String data = mealBillService.checkExitingData(month_year,voucher_head_new_id,school_id);
			ResponseEntity<Object> student_response= ResponseHandler.generateResponse(true, HttpStatus.OK, data);
			return student_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
}
