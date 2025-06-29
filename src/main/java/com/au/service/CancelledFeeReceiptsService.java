package com.au.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.repository.CancelledFeeReceiptsRepository;
import com.au.response.ResponseHandler;

import java.time.LocalDate;


@Service
public class CancelledFeeReceiptsService {
	
	@Autowired
	private CancelledFeeReceiptsRepository cancel_repo;
	
	public ResponseEntity<Object> getAllCancelledReceipt(Pageable pageable, Object keyword, Integer schoolId, LocalDate start, LocalDate end){
		Page<Object> response1 = cancel_repo.getAllCancelledReceipt(pageable, keyword, schoolId, start, end);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> getAllCancelledReceipt2(Pageable pageable,Integer schoolId, LocalDate start, LocalDate end){
		Page<Object> response = cancel_repo.getAllCancelledReceipt2(pageable, schoolId, start, end);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

}
