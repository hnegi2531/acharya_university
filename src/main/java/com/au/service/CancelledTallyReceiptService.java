package com.au.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.repository.CancelledTallyReceiptRepository;
import com.au.response.ResponseHandler;



@Service
public class CancelledTallyReceiptService {
	
	@Autowired
	private CancelledTallyReceiptRepository cancel_tally_rec_repo;
	
	public ResponseEntity<Object> getAllCancelledTallyReceipt(Pageable pageable, Object keyword){
		Page<Object> response1 = cancel_tally_rec_repo.getAllCancelledTallyReceipt(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> getAllCancelledTallyReceipt2(Pageable pageable){
		Page<Object> response = cancel_tally_rec_repo.getAllCancelledTallyReceipt2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

}
