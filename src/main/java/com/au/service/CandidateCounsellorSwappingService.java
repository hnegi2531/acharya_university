package com.au.service;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.au.model.CandidateCounsellorSwapping;
import com.au.repository.CandidateCounsellorSwappingRepository;
import com.au.response.ResponseHandler;

@Service
public class CandidateCounsellorSwappingService {
	
	@Autowired
	private CandidateCounsellorSwappingRepository ccs_repo;
	
	
	public CandidateCounsellorSwapping saveCandidateCounsellorSwapping(@Valid CandidateCounsellorSwapping candidateCounsellorSwapping) {
		
		CandidateCounsellorSwapping swap =  ccs_repo.save(candidateCounsellorSwapping);
		return swap;
	}
	
	
	public List<CandidateCounsellorSwapping> listAll() {
		return ccs_repo.findAll();
	}

	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		
		Page<Object> response1 = ccs_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		
		Page<Object> response = ccs_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}


	public CandidateCounsellorSwapping get(Integer id) {
		
		return ccs_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("CandidateCounsellorSwapping id Not Found:" + id));
	}
	
	public List<HashMap<String, Object>> findAllByCandidateId(Integer candidateId) {
		return ccs_repo.findAllByCandidateId(candidateId);
	}




}
