package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
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
import com.au.model.CmaFeeReceipt;
import com.au.model.PettyCash;
import com.au.response.ResponseHandler;
import com.au.service.CmaFeeReceiptService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
public class CmaFeeReceiptController {

	Logger log = LoggerFactory.getLogger(CmaFeeReceiptService.class);
		
		@Autowired
		private CmaFeeReceiptService cmaFeeReceiptService;
	
		@Autowired
		private JwtTokenService jwt_service;	
		
		
		@PostMapping("/createCmaFeeReceipt")
		public ResponseEntity<Object> createCmaFeeReceipt(@RequestBody @Valid CmaFeeReceipt cmaFeeReceipt,
				@RequestHeader("Authorization") String jwtToken)
				throws Exception {
			if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

				cmaFeeReceipt.setCreated_by(jwtDetails.getUserId());
				cmaFeeReceipt.setCreated_username(jwtDetails.getUserName());
				CmaFeeReceipt cmaFee_Receipt = cmaFeeReceiptService.createCmaFeeReceipt(cmaFeeReceipt);
				ResponseEntity<Object> cmaFeeReceipt_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, cmaFee_Receipt);
			return cmaFeeReceipt_response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		@PostMapping("/createMultipleCmaFeeReceipt")
		public ResponseEntity<Object> createMultipleCmaFeeReceipt(@RequestBody @Valid List<CmaFeeReceipt> cmaFeeReceipt,
				@RequestHeader("Authorization") String jwtToken)
				throws Exception {
			if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				cmaFeeReceipt.stream().forEach(cma -> {
					cma.setCreated_by(jwtDetails.getUserId());
					cma.setCreated_username(jwtDetails.getUserName());
				});
				
				List<CmaFeeReceipt> cmaFee_Receipt = cmaFeeReceiptService.createMultipleCmaFeeReceipt(cmaFeeReceipt);
				ResponseEntity<Object> cmaFeeReceipt_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, cmaFee_Receipt);
			return cmaFeeReceipt_response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		
		@GetMapping("/getAllCmaFeeReceipt")
		public ResponseEntity<Object> getAllCmaFeeReceipt() {
			if(RateLimitController.bucket.tryConsume(1)) {
				List<CmaFeeReceipt> ps = cmaFeeReceiptService.getAllCmaFeeReceipt();
			ResponseEntity<Object> pc_response= ResponseHandler.generateResponse(true, HttpStatus.OK, ps);
			return pc_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
		}
		
		
		@GetMapping("/getCmaFeeReceipt/{id}")
		public ResponseEntity<Object> get(@PathVariable Integer id) {
			if(RateLimitController.bucket.tryConsume(1)) {
		    try {
		    	
		    	CmaFeeReceipt cos = cmaFeeReceiptService.get(id);
		    	ResponseEntity<Object> co_response= ResponseHandler.generateResponse(true, HttpStatus.OK, cos);
				return co_response;
		    } catch (NoSuchElementException e) {
		    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		
		@PutMapping("/updateCmaFeeReceipt/{id}")
		public ResponseEntity<Object> updateCmaFeeReceipt(@RequestBody @Valid CmaFeeReceipt cmaFeeReceipt, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
				throws JsonParseException, JsonMappingException, IOException {
			if(RateLimitController.bucket.tryConsume(1)) {
		    try {
		    	
		    	//Department existProduct = deptService.get(id);
		    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		    	cmaFeeReceipt.setModified_by(jwtDetails.getUserId());
		    	cmaFeeReceipt.setModified_username(jwtDetails.getUserName());
		    	cmaFeeReceiptService.updateCmaFeeReceipt(cmaFeeReceipt);
		    	ResponseEntity<Object> cmaFeeReceipt_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return cmaFeeReceipt_response;
		    } catch (NoSuchElementException e) {
		    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			} 
		}
		
		@DeleteMapping("/deactivateCmaFeeReceipt/{id}")
		public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
			if(RateLimitController.bucket.tryConsume(1)) {
				cmaFeeReceiptService.deactivateCmaFeeReceipt(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		
		@DeleteMapping("/activateCmaFeeReceipt/{id}")
		public ResponseEntity<Object> activate(@PathVariable Integer id) {
			if(RateLimitController.bucket.tryConsume(1)) {
				cmaFeeReceiptService.activateCmaFeeReceipt(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		
		@GetMapping("/fetchAllCmaFeeReceipt")
		public ResponseEntity<Object> getAllDept(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
				@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
			
			if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> oc_filtered =  cmaFeeReceiptService.getAllDataFilteredByKeyword(pageable, keyword);
				return oc_filtered;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> oc_sorted = cmaFeeReceiptService.getAllSortedData(pageable1);
				return oc_sorted;
			}
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}		
		
		@GetMapping("/getCmaFeeReceiptByReceiptId/{cma_receipt_id}/{financial_year_id}/{student_id}")
		public ResponseEntity<Object> getCmaFeeReceiptByReceiptId(@PathVariable Integer cma_receipt_id, @PathVariable Integer financial_year_id, @PathVariable Integer student_id) {
			if(RateLimitController.bucket.tryConsume(1)) {
				List<HashMap<String, Object>> ps = cmaFeeReceiptService.getCmaFeeReceiptByReceiptId(cma_receipt_id, financial_year_id, student_id);
			ResponseEntity<Object> pc_response= ResponseHandler.generateResponse(true, HttpStatus.OK, ps);
			return pc_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
		}
		
		@GetMapping("/getUniformFeeReceiptByReceiptId/{uniform_receipt_no}/{financial_year_id}/{student_id}")
		public ResponseEntity<Object> getUniformFeeReceiptByReceiptId(@PathVariable Integer uniform_receipt_no, @PathVariable Integer financial_year_id, @PathVariable Integer student_id) {
			if(RateLimitController.bucket.tryConsume(1)) {
				List<HashMap<String, Object>> ps = cmaFeeReceiptService.getUniformFeeReceiptByReceiptId(uniform_receipt_no, financial_year_id, student_id);
			ResponseEntity<Object> pc_response= ResponseHandler.generateResponse(true, HttpStatus.OK, ps);
			return pc_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
		}
		
}
