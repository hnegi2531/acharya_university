package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.model.Vendor;
import com.au.model.VendorHistory;
import com.au.repository.VendorHistoryRepository;
import com.au.response.ResponseHandler;

@Service
public class VendorHistoryService {

	@Autowired
	private VendorHistoryRepository vendorHistoryRepository;
	
	
	public VendorHistory saveVendorHistory(VendorHistory ven) throws Exception {
			return vendorHistoryRepository.save(ven);
		}


	public List<VendorHistory> getActiveVendorDetails(){
			return vendorHistoryRepository.findAll1();
	}
	
	
public ResponseEntity<Object> getAllVendorDetails1(Pageable pageable, Object keyword){
		
		Page<Object> response1 = vendorHistoryRepository.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> getAllVendorDetails2(Pageable pageable){
		
		Page<Object> response = vendorHistoryRepository.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
}
	
}
