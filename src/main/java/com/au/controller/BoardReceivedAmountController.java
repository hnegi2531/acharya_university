package com.au.controller;

import java.io.IOException;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.FeeReceiptDto;
import com.au.model.BoardReceivedAmount;
import com.au.response.ResponseHandler;
import com.au.service.BoardReceivedAmountService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
public class BoardReceivedAmountController {
	
	Logger log=LoggerFactory.getLogger(BoardReceivedAmountController.class);
	
	@Autowired
	private BoardReceivedAmountService boardReceivedAmountService;
	
	@PostMapping("/boardReceivedAmount")
	public ResponseEntity<Object> saveBoardReceivedAmount(@RequestBody @Valid BoardReceivedAmount boardReceivedAmt, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException{
		if(RateLimitController.bucket.tryConsume(1)) {
				return  boardReceivedAmountService.saveBoardReceivedAmount(boardReceivedAmt,jwtToken);
				
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}		
	}
	
	@GetMapping("/allBoardReceivedAmountDetails")
	public ResponseEntity<Object> allBoardReceivedAmountDetails(@RequestParam(value="page") Integer page,@RequestParam(value="pageSize") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						return  boardReceivedAmountService.sortedAndSearchedDetails(pageable, keyword);
				}else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						return boardReceivedAmountService.sortedDetails(pageable1);
				}
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

}
