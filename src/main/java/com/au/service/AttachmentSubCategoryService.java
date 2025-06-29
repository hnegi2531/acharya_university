package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.au.exception.ResourceNotFoundException;
import com.au.model.AttachmentSubCategory;
import com.au.repository.AttachmentSubCategoryRepository;
import com.au.response.ResponseHandler;

@Service
@Transactional
public class AttachmentSubCategoryService {

	@Autowired
	private AttachmentSubCategoryRepository a_repo;

	
//	public List<AttachmentSubCategory> listAll(){
//		return a_repo.findAll();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword){
		
		Page<Object> response1 = a_repo.findAll1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable){
		
		Page<Object> response = a_repo.findAll2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
		
	}
	
	public AttachmentSubCategory saveAttachmentSubCategory(AttachmentSubCategory a) {
		return a_repo.save(a);
	}
	
	public AttachmentSubCategory get(Integer id) {
        return a_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("AttachmentSubCategory Not Found:"+id));
    }
     
    public void delete(Integer id) {
    	AttachmentSubCategory ay = a_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("AttachmentSubCategory Not Found:"+id));    	
    	a_repo.delete(ay);
    }
    
    public AttachmentSubCategory getAttachCategory(String as_name) {
    	return a_repo.getAttachCategory(as_name);
    }
    
}

