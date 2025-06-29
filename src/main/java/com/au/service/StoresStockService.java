package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;

import com.au.model.StoresStock;
import com.au.repository.StoresStockRepository;
import com.au.response.ResponseHandler;

@Service
public class StoresStockService {
	
	@Autowired
	private StoresStockRepository ss_repo;
	
	public StoresStock saveStoresStock(StoresStock ss) throws Exception {
		if(ss_repo.getCountStockTypename(ss.getStock_type_name()) >= 1) {
			throw new Exception("Store Name Already Exist");
		} else if(ss_repo.getCountStockTypeShortName(ss.getStock_type_short_name()) >= 1) {
			throw new Exception("Short Name Already Exist");
		}else {
			return ss_repo.save(ss);
		}
	}
	
	public List<StoresStock> getActiveDetails(){
		return ss_repo.findAll1();
	}
	
//	public List<StoresStock> getAllDetails(){
//		return ss_repo.findAll();
//	}
	
	public ResponseEntity<Object> getAllDetails1(Pageable pageable, Object keyword){
		
		Page<Object> response1 = ss_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> getAllDetails2(Pageable pageable){
		
		Page<Object> response = ss_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public StoresStock get(Integer stock_type_id) {
		return  ss_repo.findById(stock_type_id).orElseThrow(() -> new ResourceNotFoundException("Stores Stock Not Found:" + stock_type_id));
	}
	
	public StoresStock saveUpdate(StoresStock ss) {
		return ss_repo.save(ss);
	}
	
	public void delete(Integer stock_type_id) {
		ss_repo.findById(stock_type_id)
		.orElseThrow(() -> new ResourceNotFoundException("Stores Stock Not Found:" + stock_type_id));
			ss_repo.update(stock_type_id);
	}
	
	public void delete1(Integer stock_type_id) {
		ss_repo.findById(stock_type_id)
		.orElseThrow(() -> new ResourceNotFoundException("Stores Stock Not Found:" + stock_type_id));
			ss_repo.update1(stock_type_id);
	}
	
	public ResponseEntity<Object> allStoresStockDetails1(Pageable pageable, Object keyword){
		
		Page<Object> response1 = ss_repo.allStoresStockDetails1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> allStoresStockDetails2(Pageable pageable){
		
		Page<Object> response = ss_repo.allStoresStockDetails2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	

}
