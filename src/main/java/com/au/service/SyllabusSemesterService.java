package com.au.service;

import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.SyllabusSemester;
import com.au.repository.SyllabusSemesterRepository;
import com.au.response.ResponseHandler;

@Service
public class SyllabusSemesterService {

	@Autowired
	private SyllabusSemesterRepository sy_repo;
	
	public List<SyllabusSemester> listAll(){
		return sy_repo.findAll1();
	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = sy_repo.fetchAllDetails1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response1 = sy_repo.fetchAllDetails2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public SyllabusSemester save_Syllabus_Semester(SyllabusSemester syllabus) {
		return sy_repo.save(syllabus);
	}
	
	public SyllabusSemester get(Integer id) {
        return sy_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("Syllabus semester Not Found:"+id));
    }
     
    public void delete(Integer id) {
    	SyllabusSemester ss = sy_repo.findById(id)
		.orElseThrow(()-> new ResourceNotFoundException("Syllabus semester Not Found:"+id));
		sy_repo.update(id);
    }
    
    public void delete1(Integer id) {
    	SyllabusSemester ss = sy_repo.findById(id)
		.orElseThrow(()-> new ResourceNotFoundException("Syllabus semester Not Found:"+id));
		sy_repo.update1(id);
    }

	
}
