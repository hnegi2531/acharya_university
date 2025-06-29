package com.au.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.au.dto.JwtDetails;
import com.au.model.AddOnFee;
import com.au.repository.AddOnFeeRepository;
import com.au.response.ResponseHandler;

@Service
public class AddOnFeeService {
	
	@Autowired
	private AddOnFeeRepository a_repo;
	
	
	@Autowired
	private JwtTokenService jwt_service;

	private Logger logger = LoggerFactory.getLogger(AddOnFeeService.class);

	public AddOnFee createAddOnFee(AddOnFee pr, JwtDetails jwtDetails) throws Exception {	
		return	a_repo.save(pr);

	}

	public List<AddOnFee> listAll() {
		return a_repo.findAll1();
	}

	public AddOnFee get(Integer addOnFeeId) {
		return a_repo.findById(addOnFeeId)
				.orElseThrow(() -> new ResourceNotFoundException("AddOnFee Not Found:" + addOnFeeId));
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {

		Page<Object> AddOnFee_filtered_response = a_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, AddOnFee_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> AddOnFee_response = a_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, AddOnFee_response);
	}

	public AddOnFee updateAddOnFee(AddOnFee pr) {
		return	a_repo.save(pr);
	    
	}

	public void delete(Integer addOnFeeId) {
		AddOnFee sir = a_repo.findById(addOnFeeId)
				.orElseThrow(() -> new ResourceNotFoundException("AddOnFee Not Found:" + addOnFeeId));
		a_repo.updateAddOnFee(addOnFeeId);
	}

	public void delete1(Integer addOnFeeId) {
		AddOnFee sir = a_repo.findById(addOnFeeId)
				.orElseThrow(() -> new ResourceNotFoundException("AddOnFee Not Found:" + addOnFeeId));
		a_repo.updateAddOnFee1(addOnFeeId);
	}

}
