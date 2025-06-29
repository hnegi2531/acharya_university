package com.au.controller;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import com.au.model.FeeReceipt;
import com.au.repository.FeeReceiptRepository;
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
import com.au.model.DdDetails;
import com.au.response.ResponseHandler;
import com.au.service.DdDetailsService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
//@RequestMapping("/api")
public class DdDetailsController {
	
		Logger log=LoggerFactory.getLogger(DdDetailsController.class);
		
		@Autowired
		private DdDetailsService dd_ser;
		
		@Autowired
		private JwtTokenService jwt_service;

		@Autowired
		private FeeReceiptRepository feeReceiptRepository;


		
		@PostMapping("/ddDetails")
		public ResponseEntity<Object> saveDdDetails(@RequestBody @Valid DdDetails dd, @RequestHeader("Authorization") String jwtToken)
				throws JsonParseException, JsonMappingException, IOException{
			if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails=jwt_service.callJwtToken(jwtToken);
			dd.setCreated_by(jwtDetails.getUserId());
			dd.setCreated_username(jwtDetails.getUserName());

			FeeReceipt feeReceipt = feeReceiptRepository.getLatestFeeReceiptForDD("DD");
			dd.setFee_receipt(feeReceipt.getFee_receipt_id());
			dd.setCleared_status(false);

			feeReceipt.setBank_id(dd.getDeposited_into());
			feeReceiptRepository.save(feeReceipt);
			DdDetails dddetails = dd_ser.saveDdDetails(dd);
			ResponseEntity<Object> dddetails_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, dddetails);
			return dddetails_response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		@GetMapping("/activeDdDetails")
		public ResponseEntity<Object> getActiveDetails(){
			if(RateLimitController.bucket.tryConsume(1)) {
			List<DdDetails> dd_list = dd_ser.getActiveDetails();
			ResponseEntity<Object> dd_list_response= ResponseHandler.generateResponse(true, HttpStatus.OK, dd_list);
			return dd_list_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
		}
		
		@GetMapping("/fetchAllDdDetails")
		public ResponseEntity<Object> getAllDdDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
				@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
			
			if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> dd_filtered =  dd_ser.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
				return dd_filtered;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> dd_sorted = dd_ser.getAllSortedData(pageable1);
				return dd_sorted;
			}
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		@GetMapping("/ddDetailsById/{dd_id}")
		public ResponseEntity<Object> getDdDetailsbyId(@PathVariable Integer dd_id){
			if(RateLimitController.bucket.tryConsume(1)) {
			try {
					DdDetails dd= dd_ser.getDdDetailsbyId(dd_id);
					ResponseEntity<Object> dd_response= ResponseHandler.generateResponse(true, HttpStatus.OK, dd);
					return dd_response;

				} catch (NoSuchElementException e) {
					ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
					return response;
				}
				} else {
					ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
					return rs;
				}
		}
		
		@PutMapping("/updateDdDtails/{dd_id}")
		public ResponseEntity<Object> updateDdDetails(@RequestBody @Valid DdDetails dd, @PathVariable Integer dd_id,
				@RequestHeader("Authorization") String jwtToken) throws JsonParseException, JsonMappingException, IOException{
			if(RateLimitController.bucket.tryConsume(1)) {
			try {
				  JwtDetails jwtDetails=jwt_service.callJwtToken(jwtToken);
				  dd.setModified_by(jwtDetails.getUserId());
				  dd.setModified_username(jwtDetails.getUserName());
				 // dd.setCleared_status(true);
				  DdDetails dd1=dd_ser.updateDdDetails(dd);
				  ResponseEntity<Object> dd1_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
					return dd1_response;
				} catch (NoSuchElementException e) {
					ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
					return response;
				}
				} else {
					ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
					return rs;
				}
		}
		
		@DeleteMapping("/ddDetails/{dd_id}")
		public ResponseEntity<Object> delete1(@PathVariable Integer dd_id) {
			if(RateLimitController.bucket.tryConsume(1)) {
			dd_ser.delete1(dd_id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		@DeleteMapping("/activateDdDetails/{dd_id}")
		public ResponseEntity<Object> delete2(@PathVariable Integer dd_id) {
			if(RateLimitController.bucket.tryConsume(1)) {
			dd_ser.delete2(dd_id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}

}
