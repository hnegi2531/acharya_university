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
import com.au.model.AttachmentCategory;
import com.au.repository.AttachmentCategoryRepository;
import com.au.response.ResponseHandler;

@Service
@Transactional
public class AttachmentCategoryService {

	@Autowired
	private AttachmentCategoryRepository a_repo;


	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword){
		Page<Object> response1 = a_repo.findAll1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	
	public ResponseEntity<Object> listAll2(Pageable pageable){
		Page<Object> response = a_repo.findAll2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public AttachmentCategory saveAttachmentCategory(AttachmentCategory a) {
		return a_repo.save(a);
	}
	
	public AttachmentCategory get(Integer id) {
        return a_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("AttachmentCategory Not Found:"+id));
    }
     
    public ResponseEntity<Object> delete(Integer id) {
    	AttachmentCategory ay = a_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("AttachmentCategory Not Found:"+id));    	
    	a_repo.delete(ay);
    	ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
    }
    
}
