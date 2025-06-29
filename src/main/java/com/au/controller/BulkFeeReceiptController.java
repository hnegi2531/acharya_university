package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import com.au.service.FeeReceiptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.au.dto.BulkFeeReceiptDto;
import com.au.dto.JwtDetails;
import com.au.model.BulkFeeReceipt;
import com.au.model.CancelledFeeReceipts;
import com.au.response.ResponseHandler;
import com.au.service.BulkFeeReceiptService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
public class BulkFeeReceiptController {
	
Logger log = LoggerFactory.getLogger(ApplicantDetailsController.class);
	
	@Autowired
	private BulkFeeReceiptService frc_ser;
	
	@Autowired
	private JwtTokenService jwt_service;

	@Autowired
	private FeeReceiptService feeReceiptService;
	
	@PostMapping("/bulkFeeReceipt")
	public ResponseEntity<Object> saveBulkFeeReceipt(@RequestBody @Valid BulkFeeReceiptDto frd, @RequestHeader("Authorization") String jwtToken)
			throws Exception{
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			frd.setCreated_by(jwtDetails.getUserId());
			frd.setCreated_username(jwtDetails.getUserName());
				List<BulkFeeReceipt> bulk_fee_receipt = frc_ser.saveBulkFeeReceipt(frd, jwtDetails);
				ResponseEntity<Object> bulk_fee_receipt_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, bulk_fee_receipt);
				return bulk_fee_receipt_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	
	@GetMapping("/activeBulkFeeReceipt")
	public ResponseEntity<Object> getActiveBullFeeReceipt(){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<BulkFeeReceipt> bulk_fee_receipt_list = frc_ser.listAll1();
			ResponseEntity<Object> fee_receipt_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, bulk_fee_receipt_list);
			return fee_receipt_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
//	@GetMapping("/feeReceipt")
//	public ResponseEntity<Object> getAllBulkFeeReceipt(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
//			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
//		if(RateLimitController.bucket.tryConsume(1)) {
//				Sort sorted = Sort.by(Direction.DESC, sort );
//				if(keyword != null) {	
//						Pageable pageable = PageRequest.of(page, page_size,sorted);
//						System.out.println("page, page_size, sorted, keyword");
//						ResponseEntity<Object> fee_receipt_sorted = frc_ser.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
//						return fee_receipt_sorted;
//				}else {
//						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
//						System.out.println("page, page_size, sorted");
//						ResponseEntity<Object> fee_receipt_pageable = frc_ser.getAllSortedData(pageable1);
//						return fee_receipt_pageable;
//				}
//		}else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}		
//		//return frc_ser.getAllFeeReceipt();
//	}
	
	@GetMapping("/bulkFeeReceipt/{bulk_fee_receipt_id}")
	public ResponseEntity<Object> getFeeReceiptById(@PathVariable Integer bulk_fee_receipt_id){
		if(RateLimitController.bucket.tryConsume(1)) {
				try {

						BulkFeeReceipt  product = frc_ser.getFeeReceiptById(bulk_fee_receipt_id);
						ResponseEntity<Object> fee_receipt_response_by_id= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
						return fee_receipt_response_by_id;

				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@PutMapping("/bulkFeeReceipt/{bulk_fee_receipt_id}")
	public ResponseEntity<Object> updateFeeReceipt(@RequestBody @Valid BulkFeeReceipt frc, @PathVariable Integer bulk_fee_receipt_id ,
			@RequestHeader("Authorization") String jwtToken)throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
						JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
						frc.setModified_by(jwtDetails.getUserId());
						frc.setModified_username(jwtDetails.getUserName());
						frc_ser.updateFeeReceipt(frc);
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
	
	@DeleteMapping("/deactivateBulkfeeReceipt/{bulk_fee_receipt_id}")
	public ResponseEntity<Object> delete(@PathVariable Integer bulk_fee_receipt_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				frc_ser.deActivateFeeReceipt(bulk_fee_receipt_id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@DeleteMapping("/activateBulkFeeReceipt/{bulk_fee_receipt_id}")
	public ResponseEntity<Object> activateFeeReceipt(@PathVariable Integer bulk_fee_receipt_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				frc_ser.activateFeeReceipt(bulk_fee_receipt_id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	

//	@GetMapping("/getBulkFeeReceiptDataForCash")
//	public ResponseEntity<Object> getActiveBullFeeReceipt(){
//		if(RateLimitController.bucket.tryConsume(1)) {
//		List<BulkFeeReceipt> bulk_fee_receipt_list = frc_ser.listAll1();
//			ResponseEntity<Object> fee_receipt_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, bulk_fee_receipt_list);
//			return fee_receipt_list_response;
//		}else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}	

	
	@GetMapping("/getDataForDisplayingBulkFeeReceipt/{fee_receipt_id}/{transaction_type}/{financial_year_id}")
	public ResponseEntity<Object> getDataForDisplayingBulkFeeReceipt(@PathVariable Integer fee_receipt_id,
			@PathVariable String transaction_type,@PathVariable Integer financial_year_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			HashMap<String, Object> details_of_candidate= frc_ser.getDataForDisplayingBulkFeeReceipt(fee_receipt_id,transaction_type,financial_year_id);
			ResponseEntity<Object> details_of_candidate_response = ResponseHandler.generateResponse(true, HttpStatus.OK, details_of_candidate);
			return details_of_candidate_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests !!");
			return rs;
		}

	}
	
	@GetMapping("/getDataForDisplayingBulkFeeReceiptByAuid/{student_id}/{fee_receipt_id}/{transaction_type}/{financial_year_id}")
	public ResponseEntity<Object> getDataForDisplayingBulkFeeReceiptByAuid(@PathVariable Integer student_id, @PathVariable Integer fee_receipt_id,
			@PathVariable String transaction_type,@PathVariable Integer financial_year_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			HashMap<String, Object> details_of_candidate= frc_ser.getDataForDisplayingBulkFeeReceiptByStudentId(student_id, fee_receipt_id,transaction_type,financial_year_id);
			ResponseEntity<Object> details_of_candidate_response = ResponseHandler.generateResponse(true, HttpStatus.OK, details_of_candidate);
			return details_of_candidate_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests !!");
			return rs;
		}

	}
	
	@GetMapping("/getDataForDisplayingBulkFeeReceiptAndCancel")
	public ResponseEntity<Object> getDataForDisplayingBulkFeeReceiptAndCancel(@RequestParam(value="financial_year_id") Integer financial_year_id,
			@RequestParam(value="school_id") Integer school_id, @RequestParam(value="fee_receipt") String fee_receipt) {
		if(RateLimitController.bucket.tryConsume(1)) {
			HashMap<String, Object> details_of_candidate= frc_ser.getDataForDisplayingBulkFeeReceiptAndCancel(financial_year_id,school_id,fee_receipt);
			ResponseEntity<Object> details_of_candidate_response = ResponseHandler.generateResponse(true, HttpStatus.OK, details_of_candidate);
			return details_of_candidate_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests !!");
			return rs;
		}

	}
	
	@PostMapping("/cancelBulkFeeReceipt")
	public ResponseEntity<Object> cancelBulkFeeReceipt(@RequestBody @Valid CancelledFeeReceipts cfr, @RequestHeader("Authorization") String jwtToken) throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			String  message =	frc_ser.cancelBulkFeeReceipt(cfr, jwtToken);
				ResponseEntity<Object> response= ResponseHandler.generateResponse(true, HttpStatus.OK, message);
				return response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}

	@GetMapping("/getFeeReceiptId")
	public ResponseEntity<Object> getFeeReceiptId(@RequestParam("feeReceipt") Integer feeReceipt, @RequestParam("fcYear") Integer fcYear, @RequestParam("bulkId") Integer bulkId)
	{
		return  feeReceiptService.getFeeReceiptId(feeReceipt,fcYear,bulkId);

	}

}
