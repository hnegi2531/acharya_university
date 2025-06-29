package com.au.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import com.au.dto.JwtDetails;
import com.au.dto.PurchaseItemsHistoryDto;
import com.au.model.Vendor;
import com.au.response.ResponseHandler;
import com.au.service.PurchaseItemsHistoryService;

@RestController
@RequestMapping("/api/${secretkey12}")
public class PurchaseItemsHistoryController {
	
	Logger log=LoggerFactory.getLogger(PurchaseItemsHistoryController.class);
	
	@Autowired
	private PurchaseItemsHistoryService purchaseItemsHistoryService;
	
	@PostMapping("/cratePurchaseItemHistory")
	public ResponseEntity<Object> cratePurchaseItemHistory(@RequestBody @Valid List<PurchaseItemsHistoryDto> purchaseItemsHistoryDto, @RequestHeader("Authorization") String jwtToken){
	if(RateLimitController.bucket.tryConsume(1)) {
			return purchaseItemsHistoryService.cratePurchaseItemHistory(purchaseItemsHistoryDto,jwtToken);
	}else {
		return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
	}		
}

}
