package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.model.MealType;
import com.au.repository.MealTypeRepository;
import com.au.response.ResponseHandler;
import com.au.exception.ResourceNotFoundException;

@Service
public class MealTypeService {
	
	@Autowired
	private MealTypeRepository mts_repo;
	
	public MealType saveMealType(MealType cmt) throws Exception {
		if(cmt.getFor_end_user()==true) {
			if(mts_repo.checkValidationForMealType(cmt.getMeal_type()) >= 1) {
				throw new Exception("MealType already created !");
		}
	  }
		return mts_repo.save(cmt);
	}
	
	public List<MealType> listAll1() {
		return mts_repo.findAll1();
	}
	
	public List<MealType> getOnlyEndUserMealType(){
		return mts_repo.getOnlyEndUserMealType();
	}
	
//	public List<Map<String, Object>> listAll3(Integer mess_meal_type) {
//		return mts_repo.findAll3(mess_meal_type);
//	}
	
	public List<MealType> listAll4(String mess_meal_type) {
		return mts_repo.findAll4(mess_meal_type);
	}
	
	public MealType get(Integer meal_id) {
		return mts_repo.findById(meal_id)
				.orElseThrow(() -> new ResourceNotFoundException("MealType Not Found:" + meal_id));
	}
	
	public MealType saveUpdateMealType(MealType cmt) {
		return mts_repo.save(cmt);
	}

	public void delete(Integer meal_id) {
		mts_repo.findById(meal_id).orElseThrow(() -> new RuntimeException("MealType Not Found:" + meal_id));
		mts_repo.update(meal_id);
	}

	public void delete1(Integer meal_id) {
		mts_repo.findById(meal_id).orElseThrow(() -> new RuntimeException("MealType Not Found:" + meal_id));
		mts_repo.update1(meal_id);
	}
	
	public ResponseEntity<Object> fetchAllMealTypeDetails(Pageable pageable, Object keyword) {
		
		Page<Object> response1 = mts_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> fetchAllMealTypeDetails1(Pageable pageable) {
		
		Page<Object> response = mts_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}



}
