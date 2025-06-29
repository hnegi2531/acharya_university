package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.model.MealVendorAssignment;
import com.au.repository.MealVendorAssignmentRepository;
import com.au.response.ResponseHandler;

@Service
public class MealVendorAssignmentService {
	
	@Autowired
	private MealVendorAssignmentRepository mva_repo;
	

    public MealVendorAssignment saveMealVendorAssignment(MealVendorAssignment mva) throws Exception {
    	MealVendorAssignment mealVendor = null;
    	if(mva_repo.checkValidation(mva.getMeal_id(),mva.getVoucher_head_new_id()) >=1) {
    		mva_repo.updateRatePerCount(mva.getMeal_id(),mva.getVoucher_head_new_id(),mva.getRate_per_count());
    	}else {
    		 mealVendor = mva_repo.save(mva);
    	}
		return mealVendor;
    }
    	
    		
//    		throw new Exception("Combination of MealType, Vendor and Rate already exist !");
    	
    	
		
    
    
    public List<MealVendorAssignment> listAll1() {
		return mva_repo.findAll1();
	}
    
public ResponseEntity<Object> fetchAllMealVendorAssignmentDetails(Pageable pageable, Object keyword) {
		
		Page<Object> response1 = mva_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> fetchAllMealVendorAssignmentDetails1(Pageable pageable) {
		
		Page<Object> response = mva_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	
	public MealVendorAssignment get(Integer meal_vendor_assignment_id) {
		return mva_repo.findById(meal_vendor_assignment_id)
				.orElseThrow(() -> new RuntimeException("MealVendorAssignment Not Found:" + meal_vendor_assignment_id));
	}
	
    public MealVendorAssignment saveUpdateVendorAssignment(MealVendorAssignment mva) {
		
		return mva_repo.save(mva);
	}
	
	public void delete(Integer meal_vendor_assignment_id) {
		mva_repo.findById(meal_vendor_assignment_id)
				.orElseThrow(() -> new RuntimeException("MealVendorAssignment Not Found:" + meal_vendor_assignment_id));
		mva_repo.update(meal_vendor_assignment_id);
	}
	
	public void delete1(Integer meal_vendor_assignment_id) {
		mva_repo.findById(meal_vendor_assignment_id)
			.orElseThrow(() -> new RuntimeException("MealVendorAssignment Not Found:" + meal_vendor_assignment_id));
		mva_repo.update1(meal_vendor_assignment_id);
	}



}
