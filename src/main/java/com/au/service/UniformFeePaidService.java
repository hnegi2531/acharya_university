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
import com.au.model.UniformFeePaid;
import com.au.repository.UniformFeePaidRepository;
import com.au.response.ResponseHandler;

@Service
public class UniformFeePaidService {
	
	@Autowired
	private UniformFeePaidRepository ufp_repo;
	
	
	@Autowired
	private JwtTokenService jwt_service;

	private Logger logger = LoggerFactory.getLogger(UniformFeePaidService.class);

	public UniformFeePaid createUniformFeePaid(UniformFeePaid pr, JwtDetails jwtDetails) throws Exception {	
		return	ufp_repo.save(pr);

	}

	public List<UniformFeePaid> listAll() {
		return ufp_repo.findAll1();
	}

	public UniformFeePaid get(Integer uniformFeePaidId) {
		return ufp_repo.findById(uniformFeePaidId)
				.orElseThrow(() -> new ResourceNotFoundException("UniformFeePaid Not Found:" + uniformFeePaidId));
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {

		Page<Object> UniformFeePaid_filtered_response = ufp_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, UniformFeePaid_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> UniformFeePaid_response = ufp_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, UniformFeePaid_response);
	}

	public UniformFeePaid updateUniformFeePaid(UniformFeePaid pr) {
		return	ufp_repo.save(pr);
	    
	}

	public void delete(Integer uniformFeePaidId) {
		UniformFeePaid sir = ufp_repo.findById(uniformFeePaidId)
				.orElseThrow(() -> new ResourceNotFoundException("UniformFeePaid Not Found:" + uniformFeePaidId));
		ufp_repo.updateUniformFeePaid(uniformFeePaidId);
	}

	public void delete1(Integer uniformFeePaidId) {
		UniformFeePaid sir = ufp_repo.findById(uniformFeePaidId)
				.orElseThrow(() -> new ResourceNotFoundException("UniformFeePaid Not Found:" + uniformFeePaidId));
		ufp_repo.updateUniformFeePaid1(uniformFeePaidId);
	}

}
