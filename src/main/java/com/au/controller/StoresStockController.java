package com.au.controller;

import java.io.IOException;
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

import com.au.model.StoresStock;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.StoresStockService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey5}")
@CrossOrigin
public class StoresStockController {
	
	@Autowired
	private StoresStockService storesStockService;
	
	@Autowired
	private JwtTokenService jwtTokenService;
	
	@PostMapping("/StoresStock")
	public ResponseEntity<Object> createStoresStock(@RequestBody @Valid StoresStock storesStock, 
			@RequestHeader("Authorization") String jwtToken) 
					throws JsonParseException, JsonMappingException, IOException, Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
				storesStock.setCreated_by(jwtDetails.getUserId());
				storesStock.setCreated_username(jwtDetails.getUserName());
				StoresStock storesStockResponse = storesStockService.saveStoresStock(storesStock);
				return ResponseHandler.generateResponse(true, HttpStatus.CREATED, storesStockResponse);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}		
	}
	
	@GetMapping("/StoresStock")
	public ResponseEntity<Object> getActiveDetails(){
		if(RateLimitController.bucket.tryConsume(1)) {
				List<StoresStock> listStoresStock = storesStockService.getActiveDetails();
				return ResponseHandler.generateResponse(true, HttpStatus.OK, listStoresStock);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}		
	}
	
	@GetMapping("/fetchAllStoresStockDetails")
	public ResponseEntity<Object> getAllDetails(@RequestParam(value="page") Integer page,@RequestParam(value="pageSize") Integer pageSize,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, pageSize,sorted);
						return storesStockService.getAllDetails1(pageable, keyword);
				}else {
						Pageable pageable1 = PageRequest.of(page, pageSize,sorted);
						return storesStockService.getAllDetails2(pageable1);
				}
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@GetMapping("/StoresStock/{stock_type_id}")
	public  ResponseEntity<Object> get(@PathVariable Integer stock_type_id){
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
						StoresStock product = storesStockService.get(stock_type_id);
						return ResponseHandler.generateResponse(true, HttpStatus.OK, product);
				}catch (NoSuchElementException e) {
					return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@PutMapping("/UpdateStoresStock/{stock_type_id}")
	public ResponseEntity<Object> update(@RequestBody StoresStock storesStock, @PathVariable Integer stock_type_id, 
			@RequestHeader("Authorization") String jwtToken)
					throws JsonParseException, JsonMappingException, IOException, Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
						JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
						storesStock.setModified_by(jwtDetails.getUserId());
						storesStock.setModified_username(jwtDetails.getUserName());
						storesStockService.saveUpdate(storesStock);
						return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				}catch(NoSuchElementException e) {
					return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				}
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}		
		
	}
	
	@DeleteMapping("/StoresStock/{stock_type_id}")
	public ResponseEntity<Object> delete(@PathVariable Integer stock_type_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				storesStockService.delete(stock_type_id);
				return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		}else {	
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@DeleteMapping("/activateStoresStock/{stock_type_id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer stock_type_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				storesStockService.delete1(stock_type_id);
				return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@GetMapping("/allStoresStockDetails")
	public ResponseEntity<Object> allStoresStockDetails(@RequestParam(value="page") Integer page,@RequestParam(value="pageSize") Integer pageSize,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, pageSize,sorted);
						return storesStockService.allStoresStockDetails1(pageable, keyword);
				}else {
						Pageable pageable1 = PageRequest.of(page, pageSize,sorted);
						return storesStockService.allStoresStockDetails2(pageable1);
				}
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	

}
