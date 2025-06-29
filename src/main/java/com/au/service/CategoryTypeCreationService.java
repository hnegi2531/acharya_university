package com.au.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.CategoriesDTO;
import com.au.exception.ResourceNotFoundException;
import com.au.model.CategoryTypeCreation;
import com.au.repository.CategoryTypeCreationRepository;
import com.au.response.ResponseHandler;


@Service
public class CategoryTypeCreationService {
	
	@Autowired
	private CategoryTypeCreationRepository category_type_creation_repo;
	
	public CategoryTypeCreation savecategoryTypeCreation(CategoryTypeCreation ctc) {
		
		if(category_type_creation_repo.countOfCategoryTypeCreation(ctc.getCategory_name())>=1) 
				throw new RuntimeException("Category Type Creation Already Exist");
		else if(category_type_creation_repo.countOfCategoryTypeShortName(ctc.getCategory_name_sort())>=1) 
			throw new RuntimeException("Short Name Already Exist");
	
				return category_type_creation_repo.save(ctc);	

	}
	
	public List<CategoryTypeCreation> listAll() {
		return category_type_creation_repo.findAll1();
	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = category_type_creation_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = category_type_creation_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
		
	}
	
	public CategoryTypeCreation get(Integer id) {
		return category_type_creation_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category Type Creation:" + id));
	}
	
	public CategoryTypeCreation updateCategoryTypeCreation(CategoryTypeCreation ctc) {
//		if(category_type_creation_repo.countOfCategoryTypeCreation(ctc.getCategory_name())>=1) 
//			throw new RuntimeException("Category Type Creation Already Exist");
//	else if(category_type_creation_repo.countOfCategoryTypeShortName(ctc.getCategory_name_sort())>=1) 
//		throw new RuntimeException("Short Name Already Exist");
		
			return category_type_creation_repo.save(ctc);
		
	}
	
	public void delete(Integer id) {
		category_type_creation_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category Type Creation Not Found:" + id));
		category_type_creation_repo.deactivateCategoryTypeCreation(id);
	}
	
	public void delete1(Integer id) {
		category_type_creation_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category Type Creation Not Found:" + id));
		category_type_creation_repo.activateCategoryTypeCreation(id);

	}

	public ResponseEntity<Object> getCategoriesForFrro() {
		List<CategoriesDTO> categoriesDTOs=category_type_creation_repo.getCategoriesForFrro();
		   return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS",
				   categoriesDTOs);	
	}

	public List<Map<String, Object>> getCategoriesForPaymentType() {
		return category_type_creation_repo.getCategoriesForPaymentType();
	}

}
