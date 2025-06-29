package com.au.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.FeeTemplateSubAmountHistory;
import com.au.repository.FeeTemplateSubAmountHistoryRepository;
import com.au.response.ResponseHandler;

@Service
public class FeeTemplateSubAmountHistoryService {

	@Autowired
	private FeeTemplateSubAmountHistoryRepository ftsahr_repo;

//	public List<FeeTemplateSubAmountHistory> listAll() {
//		return ftsahr_repo.findAll();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		
		Page<Object> response1 = ftsahr_repo.findAll1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		
		Page<Object> response =ftsahr_repo.findAll2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public FeeTemplateSubAmountHistory saveFeeTemplateSubAmountHistory(@Valid FeeTemplateSubAmountHistory hc) {
		return ftsahr_repo.save(hc);
	}

	public FeeTemplateSubAmountHistory get(Integer id) {
		return ftsahr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FeeTemplateSubAmountHistory ID Not Found:" + id));
	}

	public void delete(Integer id) {
		FeeTemplateSubAmountHistory ms = ftsahr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("HodComments ID Not Found:" + id));
		ftsahr_repo.delete(ms);
	}

	public List<Map<String, Object>> listAll1(Integer fee_template_id) {
		return ftsahr_repo.findAll1(fee_template_id);
	}

}
