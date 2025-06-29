package com.au.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.Batch;
import com.au.repository.BatchRepository;
import com.au.response.ResponseHandler;

@Service
//@EnableCaching
public class BatchService {

	@Autowired
	private BatchRepository s_repo;
	
//	@Cacheable(value="batchs")
	public List<Batch> listAll() {
		return s_repo.findAll1();
	}
	
//	@Cacheable(value="batchs")
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = s_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
//		LinkedHashMap<String,Object> bank_response = ResponseHandler.generateResponseOFRedisForIndex(true, HttpStatus.OK, response1);
//		return bank_response;
	}
	
//	@Cacheable(value="batchs")
	public ResponseEntity<Object>  listAll2(Pageable pageable) {
		Page<Object> response = s_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
//		LinkedHashMap<String,Object> bank_response = ResponseHandler.generateResponseOFRedisForIndex(true, HttpStatus.OK, response);
//		return bank_response;
	}

//	@CacheEvict(value="batchs", allEntries=true)
	public Batch saveBatch(Batch s) throws Exception{
		if(s_repo.countOfBatchName(s.getBatch_name())>=1) {
			throw new Exception("Batch Name Already Exist");
		}else if(s_repo.countOfBatchShortName(s.getBatch_short_name())>=1) {
			throw new Exception("Short Name Already Exist");
		}else {
			return s_repo.save(s);
		}	

	}
	
//	@Cacheable(value="batchs")
	public Batch get(Integer id) {
		return s_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Batch Not Found:" + id));
	}
	
//	@CacheEvict(value="batchs", allEntries=true)
	public Batch updateBatch(Batch s) {
			return s_repo.save(s);	
	}
//	@CacheEvict(value="batchs", allEntries=true)
	public void delete(Integer id) {
		Batch cc = s_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Batch Not Found:" + id));
		s_repo.updateBatch(id);
	}
	
//	@CacheEvict(value="batchs", allEntries=true)
	public void delete1(Integer id) {
		Batch cc = s_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Batch Not Found:" + id));
		s_repo.updateBatch1(id);
	}

}
