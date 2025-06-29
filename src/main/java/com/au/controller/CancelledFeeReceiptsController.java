package com.au.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.response.ResponseHandler;
import com.au.service.CancelledFeeReceiptsService;
import com.au.service.JwtTokenService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;


@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
public class CancelledFeeReceiptsController {
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@Autowired
	private CancelledFeeReceiptsService cancel_ser;
	
	@GetMapping("/fetchAllCancelledReceipts")
	public ResponseEntity<Object> fetchAllCancelledReceipts(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword,@RequestParam(value = "school_id", required = false) Integer school_id,
															@RequestParam(value = "date_range", required = false) String dateRange,
															@RequestParam(value = "start_date", required = false) String startDate,
															@RequestParam(value = "end_date", required = false) String endDate){
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			Pageable pageable = PageRequest.of(page, page_size, sorted);

			// Create a filter for date range
			LocalDate start = null;
			LocalDate end = null;
			if (dateRange != null) {
				switch (dateRange) {
					case "week":
						start = LocalDate.now().minusWeeks(1);
						end = LocalDate.now();
						break;
					case "month":
						start = LocalDate.now().minusMonths(1);
						end = LocalDate.now();
						break;
					case "custom":
						if (startDate != null && endDate != null) {
							// Convert the input dates into LocalDate
							try {
								start = LocalDate.parse(startDate);
								end = LocalDate.parse(endDate);
								System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " +start);
								System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ " +end);
							} catch (DateTimeParseException e) {
								return ResponseEntity.badRequest().body("Invalid date format. Ensure the date format is 'yyyy-MM-dd'.");
							}
						}
						break;

					case "today": // Handle the "today" range
						start = LocalDate.now();
						end = LocalDate.now();
						System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + start);
						System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ " + end);
						break;
				}

			}

			if(keyword != null) {	
				//Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> tally_receipt_sorted = cancel_ser.getAllCancelledReceipt(pageable, keyword,school_id, start, end);//,column,value);
				return tally_receipt_sorted;
			}else {
				//Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> tally_receipt_pageable = cancel_ser.getAllCancelledReceipt2(pageable,school_id, start, end);
				return tally_receipt_pageable;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}


}
