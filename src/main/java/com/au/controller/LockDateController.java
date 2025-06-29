package com.au.controller;

import java.util.HashMap;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.LockDateRequestDTO;
import com.au.response.ResponseHandler;
import com.au.service.LockDateService;



@RestController
@RequestMapping("/api/lockScreen")
@CrossOrigin
public class LockDateController {
	
	@Autowired
	private LockDateService lockDateService;
	
	@PostMapping("/saveLockDates")
	public ResponseEntity<Object> saveLockDates(@RequestParam("token") String token,@RequestBody LockDateRequestDTO lockDateRequestDTO){
		return lockDateService.saveLockDates(token,lockDateRequestDTO);
	}

//	@PostMapping("/getLockDatesList")
//	public ResponseEntity<Object> getLockDatesList(@RequestBody LockDateRequestDTO lockDateRequestDTO){
//		return lockDateService.getLockDatesList(lockDateRequestDTO);
//	}
	
	@GetMapping("/getLockDateById/{id}")
	public ResponseEntity<Object> getLockDateById(@PathVariable("id") Integer lockId){
		return lockDateService.getLockDateById(lockId);
	}
	
	@DeleteMapping("/deleteLockDate/{id}")
	public ResponseEntity<Object> deleteLockDate(@PathVariable("id") Integer lockId){
		return lockDateService.deleteLockDate(lockId);
	}
	
	@DeleteMapping("/activeLockDate/{id}")
	public ResponseEntity<Object> activeLockDate(@PathVariable("id") Integer lockId){
		return lockDateService.activeLockDate(lockId);
	}
	
	@PutMapping("/updateLockDates/{id}")
	public ResponseEntity<Object> updateLockDates(@PathVariable("id") Integer lockId,@RequestBody LockDateRequestDTO lockDateRequestDTO,@RequestParam("token") String token){
		return lockDateService.updateLockDates(lockId,lockDateRequestDTO,token);
	}
	
	@GetMapping("/getLockDatesList")
	public ResponseEntity<Object> getAllLockDateDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> lockDateList =  lockDateService.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
			return lockDateList;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> lockDateList = lockDateService.getAllSortedData(pageable1);
			return lockDateList;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getLockDateDetailsData/{lock_month}/{lock_year}")
	public ResponseEntity<Object> getLockDateDetailsData(@PathVariable Integer lock_month,@PathVariable Integer lock_year) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> lockDate_details = lockDateService.getLockDateDetailsData(lock_month,lock_year);
			ResponseEntity<Object> lockDate_details_response = ResponseHandler.generateResponse(true, HttpStatus.OK,lockDate_details);
			return lockDate_details_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
		

}
