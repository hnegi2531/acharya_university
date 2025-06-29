package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.CreditSystem;
import com.au.repository.CreditSystemRepository;
import com.au.response.ResponseHandler;

@Service
public class CreditSystemService {

	@Autowired
	private CreditSystemRepository credit_repo;

	public List<CreditSystem> listAll() {
		return credit_repo.findAll1();
	}

//	public List<HashMap<String, Object>> listAll1() {
//		return credit_repo.fetchAllDetails();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = credit_repo.fetchAllDetails1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = credit_repo.fetchAllDetails2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
		
	}

	public CreditSystem save_Credit_System(CreditSystem academic) {
		return credit_repo.save(academic);
	}

	public CreditSystem get(Integer id) {
		return credit_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Credit System Not Found:" + id));
	}

	public void delete(Integer id) {
		CreditSystem cs = credit_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Credit System Not Found:" + id));
		credit_repo.update(id);
	}

	public void delete1(Integer id) {
		CreditSystem cs = credit_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Credit System Not Found:" + id));
		credit_repo.update1(id);
	}

}
