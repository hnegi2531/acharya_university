package com.au.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.CategoryTypeCreation;
import com.au.model.CategoryTypeDetails;
import com.au.repository.CategoryTypeDetailsRepository;
import com.au.response.ResponseHandler;

@Service
public class CategoryTypeDetailsService {

	@Autowired
	private CategoryTypeDetailsRepository ctd_repo;

	public CategoryTypeDetails saveCategoryTypeDetails(CategoryTypeDetails ctd) {

		if(ctd_repo.countOfCategoryTypeCreation(ctd.getCategory_type_id(), ctd.getCategory_detail())>=1) 
				throw new RuntimeException("Category Details Already Exist");

		return ctd_repo.save(ctd);
	}

	public List<CategoryTypeDetails> listAll() {
		return ctd_repo.findAll1();
	}
	
	public List<Map<String, Object>> categoryTypeDetailsOnBonafide() {
		return ctd_repo.categoryTypeDetailsOnBonafide();
	}

	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = ctd_repo.findAll2(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);

	}

	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = ctd_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);

	}

	public CategoryTypeDetails get(Integer id) {
		return ctd_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category Type Details:" + id));
	}

	public CategoryTypeDetails updateCategoryTypeDetails(CategoryTypeDetails ctd) {
//		if(ctd_repo.countOfCategoryTypeCreation(ctd.getCategory_type_id(), ctd.getCategory_detail())>=1) 
//			throw new RuntimeException("Category Details Already Exist");
		
		return ctd_repo.save(ctd);
		
	}

	public void delete(Integer id) {
		ctd_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category Type Details Not Found:" + id));
		ctd_repo.deactivateCategoryTypeDetails(id);
	}

	public void delete1(Integer id) {
		ctd_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category Type Details Not Found:" + id));
		ctd_repo.activateCategoryTypeDetails(id);

	}
	
	public List<CategoryTypeDetails> categoryTypeDetailsForReasonFeeExcemption(){
		return ctd_repo.categoryTypeDetailsForReasonFeeExcemption();
	}

	public List<String> categoryTypeCreationDataByShortName(String category_name_sort) {
		return ctd_repo.getCategoryData(category_name_sort);
	}

}
