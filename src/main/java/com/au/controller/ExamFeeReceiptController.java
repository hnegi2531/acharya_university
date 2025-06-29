package com.au.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.ExamFeeReceiptDto;
import com.au.dto.FeePaymentWindowDto;
import com.au.response.ResponseHandler;
import com.au.service.ExamFeeReceiptService;
import com.au.service.FeePaymentWindowService;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
public class ExamFeeReceiptController {
	
	Logger log=LoggerFactory.getLogger(ExamFeeReceiptController.class);
	
	@Autowired
	private ExamFeeReceiptService examFeeReceiptService;
	
	
	@PostMapping("/examFeeReceipt")
	public ResponseEntity<Object> createExamFeeReceipt(
			@RequestBody @Valid ExamFeeReceiptDto examFeeReceiptDto,
			@RequestHeader("Authorization") String jwtToken) {

		if (RateLimitController.bucket.tryConsume(1)) {

			return examFeeReceiptService.createExamFeeReceipt(examFeeReceiptDto, jwtToken);

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}
	
	@GetMapping("/getExamFeeReceipt")
	public ResponseEntity<Object> getExamFeeReceipt() {

		if (RateLimitController.bucket.tryConsume(1)) {

			return examFeeReceiptService.getExamFeeReceipt();

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}
	
	@GetMapping("/getExamFeeReceiptForRceiptByFeeRceiptId/{feeReceiptId}")
	public ResponseEntity<Object> getExamFeeReceiptForRceiptByFeeRceiptId(@PathVariable Integer feeReceiptId) {

		if (RateLimitController.bucket.tryConsume(1)) {

			return examFeeReceiptService.getExamFeeReceiptForRceiptByFeeRceiptId(feeReceiptId);

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}

}
