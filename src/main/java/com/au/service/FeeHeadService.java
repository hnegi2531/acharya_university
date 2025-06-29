package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.FeeHead;
import com.au.repository.FeeHeadRepository;
import com.au.response.ResponseHandler;

@Service
public class FeeHeadService {

	@Autowired
	private FeeHeadRepository s_repo;
	
	public List<FeeHead> listAll() {
		return s_repo.findAll1();
	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		//return s_repo.findAll2();
		Page<Object> response1 = s_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		//return s_repo.findAll2();
		Page<Object> response = s_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public FeeHead saveFeeHead(FeeHead s) throws Exception{
		if(s_repo.countOfFeeHead(s.getFee_head())>=1) {
			throw new Exception("Fee head Already Exist");
		}else {
			return s_repo.save(s);
		}	
	}
	
	public FeeHead saveFeeHead1(FeeHead s) {
		return s_repo.save(s);
	}

	public FeeHead get(Integer id) {
		return s_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("FeeHead Not Found:" + id));
	}

	public void delete(Integer id) {
		FeeHead cc = s_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FeeHead Not Found:" + id));
		s_repo.updateFeeHead(id);
	}

	public void delete1(Integer id) {
		FeeHead cc = s_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FeeHead Not Found:" + id));
		s_repo.updateFeeHead1(id);

	}

}
