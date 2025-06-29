package com.au.service;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.au.dto.ScholarshipAttachmentStudentidUpdateDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.Graduation;
import com.au.model.Scholarship;
import com.au.model.ScholarshipApprovalStatus;
import com.au.model.ScholarshipAttachment;
import com.au.repository.ScholarshipAttachmentRepository;
import com.au.response.ResponseHandler;

@Service
public class ScholarshipAttachmentService {

	@Autowired
	private ScholarshipAttachmentRepository s_repo;

//	public List<ScholarshipAttachment> listAll(){
//		return s_repo.findAll();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword){
		
		Page<Object> response1 = s_repo.findAll1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable){
		
		Page<Object> response = s_repo.findAll2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public ScholarshipAttachment saveScholarshipAttachment(ScholarshipAttachment s) {
		return s_repo.save(s);
	}
	
	public ScholarshipAttachment get(Integer id) {
        return s_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("ScholarshipAttachment Not Found:"+id));
    }
     
    public void delete(Integer id) {
    	ScholarshipAttachment ay = s_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("ScholarshipAttachment  Not Found:"+id));    	
    	s_repo.delete(ay);
    }
    
    public void deactivateScholarshipAttachment(Integer id) {
 
		s_repo.update(id);
	}

	public void activateScholarshipAttachment(Integer id) {

		s_repo.update1(id);
	}

	public ScholarshipAttachment update1(@Valid ScholarshipAttachmentStudentidUpdateDto s) {
		ScholarshipAttachment sattach = s_repo.getScholarshipAttachByCid(s.getS4().getCandidate_id());
		sattach.setStudent_id(s.getS4().getStudent_id());
		s_repo.save(sattach);
		return sattach;
	}
	
	public ScholarshipAttachment getDetailByCandidateId(Integer candidate_id) {
		return s_repo.getDetailByCandidateId(candidate_id);
	}
	
  
}
