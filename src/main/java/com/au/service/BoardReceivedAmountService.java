package com.au.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.model.BoardReceivedAmount;
import com.au.repository.BoardReceivedAmountRepository;
import com.au.response.ResponseHandler;

@Service
public class BoardReceivedAmountService {
	
	
	@Autowired
	private BoardReceivedAmountRepository boardReceivedAmountRepository;
	
	
	@Autowired
	private JwtTokenService jwtService;

	public ResponseEntity<Object> saveBoardReceivedAmount(@Valid BoardReceivedAmount boardReceivedAmt,
			String jwtToken) {
		BoardReceivedAmount brdRecAmt=boardReceivedAmountRepository.save(boardReceivedAmt);
		return ResponseHandler.generateResponse(true, HttpStatus.CREATED, "Recieved amount is saved !!");
	}

	public ResponseEntity<Object> sortedAndSearchedDetails(Pageable pageable, Object keyword) {
		Page<Object> response = boardReceivedAmountRepository.sortedAndSearchedDetails(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public ResponseEntity<Object> sortedDetails(Pageable pageable) {
		Page<Object> response = boardReceivedAmountRepository.sortedDetails(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

}
