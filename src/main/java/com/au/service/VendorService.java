package com.au.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.Vendor;
import com.au.repository.VendorRepository;
import com.au.response.ResponseHandler;

@Service
public class VendorService {
	
	@Autowired
	private VendorRepository vendorRepository;
	
	public Vendor saveVendor(Vendor ven) throws Exception {
		if(vendorRepository.vendorCountOnVendorName(ven.getVendor_name())>0){
			throw new Exception("Name already Exist");
		}else if(vendorRepository.vendorCountOnVendorEmail(ven.getVendor_email())>0) {
			throw new Exception("Email already Exist");
		}else if(vendorRepository.vendorCountOnVendorAccountNo(ven.getAccount_no())>0){
			throw new Exception("Account number already Exist");
		}else {
			return vendorRepository.save(ven);
		}
	}
	
	
	public ResponseEntity<Object> getAllVendorDetails1(Pageable pageable, Object keyword){
		
		Page<Object> response1 = vendorRepository.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> getAllVendorDetails2(Pageable pageable){
		
		Page<Object> response = vendorRepository.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
}
	
	public List<Vendor> getActiveVendorDetails(){
			return vendorRepository.findAll1();
	}
	
	public Vendor getVendorDetailsById(Integer id){
		return vendorRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("vendor_Details Not Found:" + id));
	}
	
	public Vendor updateVendorDetails(Vendor vendor){
			return vendorRepository.save(vendor);
	}
	
	public void delete(Integer vendor_id) {
		 vendorRepository.findById(vendor_id)
				.orElseThrow(() -> new ResourceNotFoundException("Vendor Not Found:" + vendor_id));
		vendorRepository.updateVendor(vendor_id);
	}

	public void delete1(Integer vendor_id) {
		vendorRepository.findById(vendor_id)
				.orElseThrow(() -> new ResourceNotFoundException("Vendor Not Found:" + vendor_id));
		vendorRepository.updateVendor1(vendor_id);
		
	}


	public List<HashMap<String, Object>> getVoucherHeadNewDataFromVendor() {
		return vendorRepository.getVoucherHeadNewDataFromVendor();
	}
	
//	public List<Map<String, Object>> fetchUnassignedSchoolDetails(Integer vendor_id) {
//		return vendorRepository.fetchUnassignedSchoolDetails(vendor_id);
//	}

}
