package com.au.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.au.exception.ResourceNotFoundException;
import com.au.model.CourseBranchAssignment;
import com.au.repository.CourseBranchAssignmentRepository;
import com.au.response.ResponseHandler;

@Service
@Transactional
public class CourseBranchAssignmentService {

	@Autowired
	private CourseBranchAssignmentRepository cba_repo;
	
//	public List<CourseBranchAssignment> listAll(){
//		return cba_repo.findAll();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword){
		Page<Object> response1 = cba_repo.findAll1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable){
		Page<Object> response = cba_repo.findAll2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public CourseBranchAssignment save_CourseBranchAssignment(CourseBranchAssignment academic) {
		return cba_repo.save(academic);
	}
	
	public CourseBranchAssignment get(Integer id) {
        return cba_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("CourseBranchAssignment Not Found:"+id));
    }
     
    public void delete(Integer id) {
    	CourseBranchAssignment ay = cba_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("CourseBranchAssignment Not Found:"+id));    	
    	cba_repo.delete(ay);
    }
}