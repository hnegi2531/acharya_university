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
import com.au.dto.VoucherHeadRequest;
import com.au.model.VoucherHead;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.VoucherHeadService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;



@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
//@RequestMapping("/api")
public class VoucherHeadController {

	Logger log = LoggerFactory.getLogger(VoucherHeadController.class);
	
	@Autowired
	private VoucherHeadService vs;
	
	@Autowired
	private JwtTokenService jwt_service;

	
	@PostMapping("/VoucherHead")
	public ResponseEntity<Object> saveVoucherHead(@RequestBody @Valid VoucherHeadRequest vou,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				vou.setCreated_by(jwtDetails.getUserId());
				vou.setCreated_username(jwtDetails.getUserName());
				List<VoucherHead> list_voucher_head = vs.getAllVouchers(vou);
				ResponseEntity<Object> list_voucher_head_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, list_voucher_head);
				return list_voucher_head_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@PostMapping("/saveVoucherHead")
	public ResponseEntity<Object> saveVoucherHead(@RequestBody @Valid VoucherHead vou,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				vou.setCreated_by(jwtDetails.getUserId());
				vou.setCreated_username(jwtDetails.getUserName());
				VoucherHead vh = vs.save_VoucherHead(vou);
				ResponseEntity<Object> voucher_head_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, vh);
				return voucher_head_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

//	@GetMapping("/fetchAllVoucherHeadDetail")
//	public  List<HashMap<String, Object>>  listAll(){
//		return vs.listAll();
//	}
	
	@GetMapping("/fetchAllVoucherHeadDetail")
	public  ResponseEntity<Object>  listAll(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> voucher_head_sorted = vs.listAll1(pageable, keyword);//,column,value);
						return voucher_head_sorted;
				}else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> voucher_head_pageable = vs.listAll2(pageable1);
						return voucher_head_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		//return vs.listAll();
	}
	
	@GetMapping("/VoucherHead")
	public ResponseEntity<Object> listAll1(){
		if(RateLimitController.bucket.tryConsume(1)) {
				List<VoucherHead> list_voucher_head = vs.listAll1();
				ResponseEntity<Object> list_vocher_head_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_voucher_head);
				return list_vocher_head_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	
	@GetMapping("/VoucherHead/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
	    	
						VoucherHead product = vs.get(id);
						ResponseEntity<Object> program_type_response_by_id= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
						return program_type_response_by_id;
	        	
				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@PutMapping("/VoucherHead/{id}")
	public ResponseEntity<Object> update(@RequestBody VoucherHead v,@PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
	    	//VoucherHead existProduct = vs.get(id);
						JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
						v.setModified_by(jwtDetails.getUserId());
						v.setModified_username(jwtDetails.getUserName());
						vs.save_VoucherHead(v);
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
	
	
	@DeleteMapping("/VoucherHead/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				vs.delete(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@DeleteMapping("/activateVoucherHead/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				vs.delete1(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@PutMapping("/updateVoucherHead/{id}")
	public ResponseEntity<Object> update(@PathVariable Integer id,@RequestBody VoucherHead voucher_type) {
		if(RateLimitController.bucket.tryConsume(1)) {
				vs.update(id,voucher_type.getVoucher_type());
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/FetchVoucherHead/{school_id}")
	 public ResponseEntity<Object> getVoucherBySchoolId(@PathVariable Integer school_id){
		if(RateLimitController.bucket.tryConsume(1)) {
				HashMap<String, Object> vocher_head_details = vs.getVoucherBySchoolId(school_id);
				ResponseEntity<Object> vocher_head_response_by_school_id = ResponseHandler.generateResponse(true, HttpStatus.OK, vocher_head_details);
				return vocher_head_response_by_school_id;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
		
		
		@GetMapping("/FetchVoucherHeadBasedOnType")
		 public ResponseEntity<Object> FetchVoucherHeadBasedOnType(){
			if(RateLimitController.bucket.tryConsume(1)) {
					HashMap<String, Object> vocher_head_details = vs.FetchVoucherHeadBasedOnType();
					ResponseEntity<Object> vocher_head_response_by_school_id = ResponseHandler.generateResponse(true, HttpStatus.OK, vocher_head_details);
					return vocher_head_response_by_school_id;
			}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		
	 }
	/*
	@GetMapping("/FetchVouHead/{vh}/{sid}")
	public List<VoucherHead> findByVouHeadSchoolId(@PathVariable String vh,@PathVariable Integer sid) {
		return vs.findByVouHeadSchoolId(vh,sid);
	}
	*/
	
	@GetMapping("/FetchVouHead/{voucher_head_new_id}/{vt}")
	public ResponseEntity<Object> findByVouHeadSchoolId(@PathVariable Integer voucher_head_new_id,@PathVariable String vt) {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<Integer> school_id = vs.findByVouHeadSchoolId(voucher_head_new_id,vt);
				ResponseEntity<Object> school_id_response = ResponseHandler.generateResponse(true, HttpStatus.OK, school_id);
				return school_id_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/fetch1/{voucher_head_new_id}/{sid}")
	public ResponseEntity<Object> findByVouHeadSchoolId(@PathVariable Integer voucher_head_new_id,@PathVariable Integer sid) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Integer count_voucher_head = vs.findByVouHeadSchoolId1(voucher_head_new_id, sid);
				ResponseEntity<Object> count_voucher_head_response = ResponseHandler.generateResponse(true, HttpStatus.OK, count_voucher_head );
				return count_voucher_head_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	
	}

	@GetMapping("/fetch2/{voucher_head_new_id}")
	public ResponseEntity<Object> getSchoolByVHead(@PathVariable Integer voucher_head_new_id){
		if(RateLimitController.bucket.tryConsume(1)) {
				List<Integer> list_of_school_id = vs.getSchoolByVHead(voucher_head_new_id);
				ResponseEntity<Object> list_of_school_id_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_of_school_id );
				return list_of_school_id_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	/*
	@GetMapping("/fetch5")
	public List<HashMap<String, Object>>  getSchoolByVHead123(){
		return vs.getSchoolByVHead123();
	}
	*/
	
	@GetMapping("/fetchHostelDetailsBySchoolHostel/{school_id}")
	public ResponseEntity<Object> fetchHostelDetailsBySchoolHostel(@PathVariable Integer school_id){
		if(RateLimitController.bucket.tryConsume(1)) {
				List<VoucherHead> list_of_voucher_head = vs.fetchHostelDetailsBySchoolHostel(school_id);
				ResponseEntity<Object> list_of_voucher_head_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_of_voucher_head );
				return list_of_voucher_head_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/getAllJournalTypeExceptInflow")
	public ResponseEntity<Object> getAllJournalTypeExceptInflow(){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> list_voucher_head = vs.getAllJournalTypeExceptInflow();
				ResponseEntity<Object> list_vocher_head_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_voucher_head);
				return list_vocher_head_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/getDataForDisplayingLedgerAmount/{financial_year_id}")
	public ResponseEntity<Object> getDataForDisplayingLedgerAmount(@PathVariable Integer financial_year_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<Object, Object>> details= vs.getDataForDisplayingLedgerAmount(financial_year_id);
			ResponseEntity<Object> details_response = ResponseHandler.generateResponse(true, HttpStatus.OK, details);
			return details_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests !!");
			return rs;
		}
	}
	
	@GetMapping("/getInFlowVoucherHead")
	public ResponseEntity<Object> getInFlowVoucherHead(){
		if(RateLimitController.bucket.tryConsume(1)) {
		List<Object> voucherHearName=	vs.getInFlowVoucherHead();
				ResponseEntity<Object> list_of_voucher_head_response = ResponseHandler.generateResponse(true, HttpStatus.OK, voucherHearName );
				return list_of_voucher_head_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
		
}
