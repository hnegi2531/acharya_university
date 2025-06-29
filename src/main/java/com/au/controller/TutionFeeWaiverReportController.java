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
import com.au.dto.TutionFeeWaiverDto;
import com.au.model.TutionFeeWaiverReport;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.TutionFeeWaiverReportService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class TutionFeeWaiverReportController {
	
		Logger log = LoggerFactory.getLogger(TutionFeeWaiverReportController.class);
		
		@Autowired
		private TutionFeeWaiverReportService tut_fee_repo_ser;
		
		@Autowired
		private JwtTokenService jwt_service;
		
		@PostMapping("/tutionFeeWaiverReport")
		public ResponseEntity<Object> saveTutionFeeWaiverReport(@RequestBody @Valid TutionFeeWaiverDto tut_report, @RequestHeader("Authorization") String jwtToken)
				throws JsonParseException, JsonMappingException, IOException{
			if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails=jwt_service.callJwtToken(jwtToken);

				tut_report.getYear_sem().entrySet().stream().forEach(report -> {
					tut_report.setCreated_by(jwtDetails.getUserId());
					tut_report.setCreated_username(jwtDetails.getUserName());
				});
				tut_report.getTut_fee_wavier().setCreated_by(jwtDetails.getUserId());
				tut_report.getTut_fee_wavier().setCreated_username(jwtDetails.getUserName());
				List<TutionFeeWaiverReport> tut_fee_waiver_reports = tut_fee_repo_ser.saveTutionFeeWaiverReport(tut_report);
				ResponseEntity<Object> tut_fee_waiver_reports_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, tut_fee_waiver_reports);
				return tut_fee_waiver_reports_response;
			}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		@GetMapping("/activeTutionFeeWaiverReport")
		public ResponseEntity<Object> activeTutionFeeWaiverReportDetails(){
			if(RateLimitController.bucket.tryConsume(1)) {
				List<TutionFeeWaiverReport> tut_fee_waiver_report_list = tut_fee_repo_ser.activeTutionFeeWaiverReportDetails();
				ResponseEntity<Object> tut_fee_waiver_report_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, tut_fee_waiver_report_list);
				return tut_fee_waiver_report_list_response;
			}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		@GetMapping("/fetchAllTutionFeeWaiverReport")
		public ResponseEntity<Object> fetchAllTutionFeeWaiverReportDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
				@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
			if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> tut_fee_waiver_report_sorted = tut_fee_repo_ser.fetchAllTutionFeeWaiverReportDetails1(pageable, keyword);//,column,value);
					return tut_fee_waiver_report_sorted;
				}else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> tut_fee_waiver_report_pageable = tut_fee_repo_ser.fetchAllTutionFeeWaiverReportDetails2(pageable1);
					return tut_fee_waiver_report_pageable;
				}
			}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		@GetMapping("/tutionFeeWaiverReportById/{tution_fee_waiver_report_id}")
		public ResponseEntity<Object> getTutionFeeWaiverReportDetailById(@PathVariable Integer tution_fee_waiver_report_id){
			if(RateLimitController.bucket.tryConsume(1)) {
				try {

					TutionFeeWaiverReport tut_Fee_report = tut_fee_repo_ser.getTutionFeeWaiverReportDetailById(tution_fee_waiver_report_id);
					ResponseEntity<Object> tut_Fee_report_response_by_id = ResponseHandler.generateResponse(true,HttpStatus.OK, tut_Fee_report);
					return tut_Fee_report_response_by_id;

				} catch (NoSuchElementException e) {
					ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
					return response1;
				}
			} else {
				ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
				return rs;
			}
		}
		@PutMapping("/tutionFeeWaiverReport/{ids}")
		public ResponseEntity<Object> updateTutionFeeWaiverReportDetails(@RequestBody @Valid List<TutionFeeWaiverReport> tut_report,@PathVariable List<Integer> ids, @RequestHeader("Authorization") String jwtToken)
				throws JsonParseException, JsonMappingException, IOException {
			if(RateLimitController.bucket.tryConsume(1)) {
				try {
					// ProctorStudentAssignment existProduct = psas_service.get(id);
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					tut_report.stream().forEach(model -> {
						model.setModified_by(jwtDetails.getUserId());
						model.setModified_username(jwtDetails.getUserName());
					});
					tut_fee_repo_ser.updateTutionFeeWaiverReportDetails(tut_report);
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
		
		@DeleteMapping("/activateTutionFeeWaiverReport/{tution_fee_waiver_report_id}")
		public ResponseEntity<Object> delete1(@PathVariable Integer tution_fee_waiver_report_id) {
			if(RateLimitController.bucket.tryConsume(1)) {
				tut_fee_repo_ser.delete1(tution_fee_waiver_report_id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
			}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		@DeleteMapping("/deactivateTutionFeeWaiverReport/{tution_fee_waiver_report_id}")
		public ResponseEntity<Object> delete2(@PathVariable Integer tution_fee_waiver_report_id) {
			if(RateLimitController.bucket.tryConsume(1)) {
				tut_fee_repo_ser.delete2(tution_fee_waiver_report_id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
			}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		@GetMapping("/tutionFeeWaiverReportByTutionFeeWaiverId/{tution_fee_waiver_id}")
		public ResponseEntity<Object> getTutionFeeWaiverReportDetailByTutionFeeWaiverId(@PathVariable Integer tution_fee_waiver_id){
			if(RateLimitController.bucket.tryConsume(1)) {
				try {

					List<TutionFeeWaiverReport> tut_Fee_report = tut_fee_repo_ser.getTutionFeeWaiverReportDetailByTutionFeeWaiverId(tution_fee_waiver_id);
					ResponseEntity<Object> tut_Fee_report_response = ResponseHandler.generateResponse(true, HttpStatus.OK, tut_Fee_report);
					return tut_Fee_report_response;

				} catch (NoSuchElementException e) {
					ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
					return response1;
				}
			}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
			
	
}
