package com.au.controller;

import java.io.IOException;
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
import com.au.model.ContraVoucher;
import com.au.model.DraftPaymentVoucher;
import com.au.model.PaymentVoucher;
import com.au.response.ResponseHandler;
import com.au.service.ContraVoucherService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
public class ContraVoucherController {

	Logger log = LoggerFactory.getLogger(PaymentVoucherController.class);

	@Autowired
	private ContraVoucherService contraVoucherService;

	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/saveContraVoucher")
	public ResponseEntity<Object> saveDraftPaymentVoucher(@RequestBody @Valid List<ContraVoucher> cv,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			cv.stream().forEach(p -> {

				p.setCreated_by(jwtDetails.getUserId());
				p.setCreated_username(jwtDetails.getUserName());
			});
			List<ContraVoucher> contraVoucher = contraVoucherService.savePaymentVoucher(cv,jwtToken);
			ResponseEntity<Object> contraVoucher_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED,
					contraVoucher);
			return contraVoucher_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/allActiveContraVoucher")
	public ResponseEntity<Object> allActiveContraVoucher() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<ContraVoucher> pv = contraVoucherService.allActiveContraVoucher();
			ResponseEntity<Object> PaymentVoucher_response = ResponseHandler.generateResponse(true, HttpStatus.OK, pv);
			return PaymentVoucher_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/getContraVoucher/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				ContraVoucher pv = contraVoucherService.get(id);
				ResponseEntity<Object> PaymentVoucher_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
						pv);
				return PaymentVoucher_response;
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
	
	@PutMapping("/updateContraVoucher/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid List<ContraVoucher> contraVoucher,
			@PathVariable List<Integer> id, @RequestHeader("Authorization") String jwtToken) throws Exception{
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				contraVoucher.stream().forEach(p -> {

					p.setCreated_by(jwtDetails.getUserId());
					p.setCreated_username(jwtDetails.getUserName());
				});
				contraVoucherService.updateContraVoucher(contraVoucher);
				return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			} catch (JsonParseException | JsonMappingException e) {
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			} catch (IOException e) {
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@DeleteMapping("/deactivateContraVoucher/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			contraVoucherService.deactivate(id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateContraVoucher/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			contraVoucherService.activate(id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllContraVoucher")
	public ResponseEntity<Object> fetchAllContraVoucher(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> program_type_sorted = contraVoucherService.fetchAllContraVoucher(pageable, keyword);//,column,value);
				return program_type_sorted;
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> program_type_pageable = contraVoucherService.fetchAllContraVoucherWoKeyword(pageable1);
				return program_type_pageable;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/getBalanceAmountAndClosingCase/{selected_date}")
	public ResponseEntity<?> getBalanceAmountAndClosingCase(@PathVariable("selected_date") String selected_date) {

		Map<String, Object> balance = contraVoucherService.getBalanceAmountAndClosingCase(selected_date);
		return new ResponseEntity<>(balance, HttpStatus.OK);

	}	
	
	@GetMapping("/getInsData/{selected_date}")
	public ResponseEntity<?> getInsData(@PathVariable("selected_date") String selected_date) {

		Map<String, Object> balance = contraVoucherService.getInsData(selected_date);
		return new ResponseEntity<>(balance, HttpStatus.OK);

	}
	
	@GetMapping("/getContraVoucherData/{voucher_no}/{school_id}/{financial_year_id}")
	public ResponseEntity<Object> getContraVoucherData(@PathVariable Integer voucher_no,
			@PathVariable Integer school_id, @PathVariable Integer financial_year_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> program = contraVoucherService.getContraVoucherData(voucher_no, school_id,
					financial_year_id);
			ResponseEntity<Object> program_response = ResponseHandler.generateResponse(true, HttpStatus.OK, program);
			return program_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
}
