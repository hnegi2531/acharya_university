package com.au.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.au.exception.ResourceNotFoundException;
import com.au.model.ApplicantDetails;
import com.au.model.CourseObjective;
import com.au.repository.ApplicantDetailsRepository;
import com.au.response.ResponseHandler;

@Service
@Transactional
public class ApplicantDetailsService {

	@Autowired
	private ApplicantDetailsRepository a_repo;
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword){
		Page<Object> response1 = a_repo.findAll1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable){
		Page<Object> response = a_repo.findAll2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
		//return a_repo.findAll();
	}
	
	public List<ApplicantDetails> saveApplicantDetails(List<ApplicantDetails> a) {
		return a_repo.saveAll(a);
	}
	
	public List<ApplicantDetails> update(List<ApplicantDetails> cos) {
		return a_repo.saveAll(cos);
	}
	
	public ApplicantDetails get(Integer id) {
        return a_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("ApplicantDetails Not Found:"+id));
    }
     
    public ResponseEntity<Object> delete(Integer id) {
    	ApplicantDetails ay = a_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("ApplicantDetails Not Found:"+id));    	
    	a_repo.delete(ay);
    	ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
    }

	public List<Map<String, Object>> getApplicationDetails(Integer studentId) {
		return a_repo.getApplicationDetails(studentId);
	}
    
}
