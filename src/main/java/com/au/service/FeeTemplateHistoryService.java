package com.au.service;

import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.FeeTemplateHistory;
import com.au.repository.FeeTemplateHistoryRepository;
import com.au.response.ResponseHandler;

@Service
public class FeeTemplateHistoryService {
	
	@Autowired
	private FeeTemplateHistoryRepository fthr_repo;
	
//	public List<FeeTemplateHistory> listAll() {
//		return fthr_repo.findAll();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = fthr_repo.findAll1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = fthr_repo.findAll2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public FeeTemplateHistory get(Integer id) {
		return fthr_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("FeeTemplateHistory ID Not Found:" + id));
	}

	public List<HashMap<String, Object>> findByFeeTemplate(Integer fee_template_id) {
		return fthr_repo.allHistoryDetails(fee_template_id);
	}

	

}
