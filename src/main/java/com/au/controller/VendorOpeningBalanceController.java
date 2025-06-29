package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

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
import com.au.dto.VendorOpeningBalanceDto;
import com.au.model.VendorOpeningBalance;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.VendorOpeningBalanceService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey5}")
@CrossOrigin
public class VendorOpeningBalanceController {
	
	@Autowired
	private VendorOpeningBalanceService vobs_ser;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/vendorOpeningBalance")
	public ResponseEntity<Object> saveVendorOpeningBalance(@RequestBody @Valid VendorOpeningBalanceDto vobd,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			vobd.setCreated_by(jwtDetails.getUserId());
			vobd.setCreated_username(jwtDetails.getUserName());
			List<VendorOpeningBalance> vob = vobs_ser.saveVendorOpeningBalance(vobd);
			ResponseEntity<Object> vob_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, vob);
			return vob_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	
	}
	
	@GetMapping("/getVendorOpeningBalance")
	public ResponseEntity<Object> ListAll1() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<VendorOpeningBalance> vob = vobs_ser.listAll1();
			ResponseEntity<Object> vob_response= ResponseHandler.generateResponse(true, HttpStatus.OK, vob);
			return vob_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllVendorOpeningbalance")
	public ResponseEntity<Object> getAllVendorOpeningbalance(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> vob_filtered =  vobs_ser.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
			return vob_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> vob_sorted = vobs_ser.getAllSortedData(pageable1);
			return vob_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/VendorOpeningBalance/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	VendorOpeningBalance product = vobs_ser.get(id);
	    	ResponseEntity<Object> product_response= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
			return product_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}  
	}

	@PutMapping("/UpdateVendorOpeningBalance/{id}")
	public ResponseEntity<Object> update(@RequestBody VendorOpeningBalance v,@PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	//VoucherHead existProduct = vs.get(id);
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			v.setModified_by(jwtDetails.getUserId());
			v.setModified_username(jwtDetails.getUserName());
			vobs_ser.save_VendorOpeningBalance(v);
			ResponseEntity<Object> ven_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return ven_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}   
	}
	
	
	@PutMapping("/UpdateVendorOpeningBalance1/{ob_ids}")
	public ResponseEntity<Object> update(@RequestBody List<VendorOpeningBalance> vob,@PathVariable List<Integer> ob_ids,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				//VoucherHead existProduct = vs.get(id);
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				vobs_ser.update_VendorOpeningBalance(vob,jwtToken);
				ResponseEntity<Object> ven_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return ven_response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}   
	}
	
	
	
	
	@DeleteMapping("/VendorOpeningBalance/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		vobs_ser.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@DeleteMapping("/activateVendorOpeningBalance/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		vobs_ser.delete1(id);
		 ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		 return response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/getVendorOpeningBalance/{voucher_head_new_id}")
	public ResponseEntity<Object> getVendorOpeningBalanceByVoucherHeadId(@PathVariable Integer voucher_head_new_id) throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
			List<VendorOpeningBalance> vob = vobs_ser.getVendorOpeningBalanceByVoucherHeadId(voucher_head_new_id);
			ResponseEntity<Object> vob_response= ResponseHandler.generateResponse(true, HttpStatus.OK, vob);
			return vob_response;
			
			} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}  
	}	
	
}
