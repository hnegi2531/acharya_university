package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.CreditCategory;
import com.au.repository.CreditCategoryRepository;
import com.au.response.ResponseHandler;

@Service
public class CreditCategoryService {

	@Autowired
	private CreditCategoryRepository credit_repo;

	public List<CreditCategory> listAll() {
		return credit_repo.findAll1();
	}

//	public List<Map<String, Object>> fetchAllCreditCategoryDetail() {
//		return credit_repo.fetchAllCreditCategoryDetail();
//	}
	
	public ResponseEntity<Object> fetchAllCreditCategoryDetail1(Pageable pageable, Object keyword) {
		Page<Object> response1 = credit_repo.fetchAllCreditCategoryDetail1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> fetchAllCreditCategoryDetail2(Pageable pageable) {
		Page<Object> response = credit_repo.fetchAllCreditCategoryDetail2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public CreditCategory saveCreditCategory(CreditCategory c) {
		return credit_repo.save(c);
	}

	public CreditCategory get(Integer id) {

		return credit_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("CreditCategory id Not Found:" + id));
	}

	public void delete(Integer designation_id) {
		CreditCategory ay = credit_repo.findById(designation_id)
				.orElseThrow(() -> new ResourceNotFoundException("CreditCategory id Not Found:" + designation_id));
		credit_repo.updateCreditCategory(designation_id);
	}

	public void delete1(Integer designation_id) {
		CreditCategory ay = credit_repo.findById(designation_id)
				.orElseThrow(() -> new ResourceNotFoundException("CreditCategory id Not Found:" + designation_id));
		credit_repo.updateCreditCategory1(designation_id);
	}

}
