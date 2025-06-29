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
import com.au.model.VisaExpiryHistory;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.VisaExpiryHistoryService;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class VisaExpiryHistoryController {

Logger log = LoggerFactory.getLogger(VisaExpiryHistoryController.class);
	
	@Autowired
	private VisaExpiryHistoryService visaExpiryHistoryService;

	@Autowired
	private JwtTokenService jwtTokenService;
	
	
	@PostMapping("/saveVisaExpiryHistory")
	public ResponseEntity<Object> saveVisaExpiryHistory(@RequestBody @Valid List<VisaExpiryHistory> veh,@RequestHeader("Authorization") 
	String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
			veh.stream().forEach(f -> {

			f.setCreated_by(jwtDetails.getUserId());
			f.setCreated_username(jwtDetails.getUserName());
			});
		List<VisaExpiryHistory> visaExpiryHistory = visaExpiryHistoryService.saveVisaExpiryHistory(veh);
		return ResponseHandler.generateResponse(true, HttpStatus.CREATED, visaExpiryHistory);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
		
	}
	
	@GetMapping("/getVisaExpiryHistory")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<VisaExpiryHistory> visaExpiryHistory = visaExpiryHistoryService.listAll1();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, visaExpiryHistory);
	} else {
		return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
	}
	}	
	
	@GetMapping("/visaExpiryHistory/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	VisaExpiryHistory visaExpiryHistory = visaExpiryHistoryService.get(id);
	    	return ResponseHandler.generateResponse(true, HttpStatus.OK, visaExpiryHistory);
	    } catch (NoSuchElementException e) {
	    	return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
		}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
    @PutMapping("/updateVisaExpiryHistory/{id}")
		public ResponseEntity<Object> update(@RequestBody @Valid List<VisaExpiryHistory> veh, @PathVariable List<Integer> id,
				@RequestHeader("Authorization") String jwtToken)
				throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
		 try {
		   veh.stream().forEach(p -> {

		    p.setCreated_by(jwtDetails.getUserId());
			p.setCreated_username(jwtDetails.getUserName());
			});
		   visaExpiryHistoryService.updateVisaExpiryHistory(veh);
		   return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
	  } catch (NoSuchElementException e) {
		  return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
		  }
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		} 
	}
	
	@DeleteMapping("/deactivateVisaExpiryHistory/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			visaExpiryHistoryService.delete(id);
			return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	
	@DeleteMapping("/activateVisaExpiryHistory/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			visaExpiryHistoryService.delete1(id);
			return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}	
	
	@GetMapping("/fetchAllVisaExpiryHistory")
	public ResponseEntity<Object> getAllDept(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			return visaExpiryHistoryService.getAllDataFilteredByKeyword(pageable, keyword);
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			return visaExpiryHistoryService.getAllSortedData(pageable1);
		}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@GetMapping("/getVisaExpiryHistoryData/{empId}")
	public ResponseEntity<Object> getVisaExpiryHistoryData(@PathVariable Integer empId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> visaExpiryistory = visaExpiryHistoryService.getVisaExpiryHistoryData(empId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, visaExpiryistory );
	} else {
		return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
	}
	}		
}
