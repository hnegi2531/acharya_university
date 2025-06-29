package com.au.controller;

import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
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

import com.au.response.ResponseHandler;
import com.au.service.StoreIndentRequestHistoryService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.au.dto.JwtDetails;
import com.au.service.JwtTokenService;
import com.au.model.StoreIndentRequestHistory;


@RestController
@RequestMapping("/api/${secretkey5}")
@CrossOrigin
public class StoreIndentRequestHistoryController {

	
 	@Autowired
    private StoreIndentRequestHistoryService sir_history_Service;

    @Autowired
    private JwtTokenService jwt_service;
    
    
    
    
    @PostMapping("/storeIndentRequestHistory")
	public ResponseEntity<Object> saveStoreIndentRequest(@RequestBody @Valid List<StoreIndentRequestHistory> sir,@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		sir.stream().forEach(a -> {

			a.setCreated_by(jwtDetails.getUserId());
			a.setCreated_username(jwtDetails.getUserName());
			});
		List<StoreIndentRequestHistory> store_indent_req = sir_history_Service.saveStoreIndentRequest(sir);
		ResponseEntity<Object> store_indent_req_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, store_indent_req);
		return store_indent_req_response;
	} else {
		ResponseEntity<Object> response=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return response;
		}
	} 
    
    @GetMapping("/fetchAllStoreIndentRequestHistory")
	public ResponseEntity<Object> getItemApproverdata(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="requested_by",required = false) Integer requested_by,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> store_indent_req_filtered =  sir_history_Service.getAllDataFilteredByKeyword1(pageable, keyword ,requested_by);
			return store_indent_req_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> store_indent_req_sorted = sir_history_Service.getAllSortedData1(pageable1,requested_by);
			return store_indent_req_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	} 
}
