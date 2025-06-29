package com.au.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.au.exception.ResourceNotFoundException;
import com.au.model.CourseBranch;
import com.au.repository.CourseBranchRepository;
import com.au.response.ResponseHandler;

@Service
@Transactional
public class CourseBranchService {

	@Autowired
	private CourseBranchRepository cb_repo;
	
//	public List<CourseBranch> listAll(){
//		return cb_repo.findAll();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword){
		Page<Object> response1 = cb_repo.findAll1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable){
		Page<Object> response = cb_repo.findAll2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
		
	}
	
	public CourseBranch save_CourseBranch(CourseBranch cb) {
		return cb_repo.save(cb);
	}
	
	public CourseBranch get(Integer id) {
        return cb_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("CourseBranch Not Found:"+id));
    }
     
    public void delete(Integer id) {
    	CourseBranch ay = cb_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("CourseBranch Not Found:"+id));    	
    	cb_repo.delete(ay);
    }
	
}
