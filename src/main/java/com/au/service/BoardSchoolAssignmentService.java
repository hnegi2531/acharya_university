package com.au.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.au.exception.ResourceNotFoundException;
import com.au.model.Board_School_Assignment;
import com.au.repository.BoardSchoolAssignmentRepository;
import com.au.response.ResponseHandler;

@Service
@Transactional
public class BoardSchoolAssignmentService {

	@Autowired
	private BoardSchoolAssignmentRepository bs_repo;

	public List<Board_School_Assignment> listAll(){
		return bs_repo.findAll1();
	}
	
//	public List<HashMap<String, Object>> listAll1(){
//		return bs_repo.fetchAllDetail();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword){
		Page<Object> response1 = bs_repo.fetchAllDetail1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable){
		Page<Object> response = bs_repo.fetchAllDetail2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
		
	}
	
	public Board_School_Assignment save_Board_School_Assignment(Board_School_Assignment bsa) {
		return bs_repo.save(bsa);
	}
	
	public Board_School_Assignment get(Integer id) {
        return bs_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("Board_School_Assignment Not Found:"+id));
    }
     
    public void delete(Integer id) {
    	Board_School_Assignment ay = bs_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("Board_School_Assignment Not Found:"+id));    	
    	bs_repo.update(id);
    }
    
    public void delete1(Integer id) {
    	Board_School_Assignment ay = bs_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("Board_School_Assignment Not Found:"+id));    	
    	bs_repo.update1(id);
    }

    public Integer getBoardBySchool(Integer school_id){
    	return bs_repo.getBoardBySchool(school_id);
    }
    
    public List<HashMap<String, Object>> getBoardSchool(Integer school_id){
    	return bs_repo.getBoardSchool(school_id);
    }
    
}
