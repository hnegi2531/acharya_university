package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.ScholarshipApprovalStatus;
import com.au.model.ScholarshipApprovalStatusHistory;
import com.au.repository.ScholarshipApprovalStatusHistoryRepository;
import com.au.repository.ScholarshipApprovalStatusRepository;
import com.au.repository.StudentDetailsRepository;
import com.au.repository.StudentDueRepository;
import com.au.repository.StudentTranscriptSubmissionRepository;
import com.au.response.ResponseHandler;



@Service
public class ScholarshipApprovalStatusHistoryService {
	
	@Autowired
	private ScholarshipApprovalStatusHistoryRepository s_repo;
	
	@Autowired
	private StudentDetailsRepository stu_repo;
	
	@Autowired
	private StudentTranscriptSubmissionRepository sts_repo;
	
	@Autowired
	private StudentDueRepository studentDueRepository;
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword){
		
		Page<Object> response1 = s_repo.findAll1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable){
		
		Page<Object> response = s_repo.findAll2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public ScholarshipApprovalStatusHistory save_ScholarshipApprovalStatusHistory(ScholarshipApprovalStatusHistory s) {
		return s_repo.save(s);
	}
	
	public ScholarshipApprovalStatusHistory get(Integer id) {
        return s_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("ScholarshipApprovalStatusHistory Not Found:"+id));
    }

	public List<ScholarshipApprovalStatusHistory> getScholarshipApprovalStatusHistoryData(Integer scholarship_id) {
		List<ScholarshipApprovalStatusHistory> scholarship_data = s_repo.getScholarshipApprovalStatusHistoryData(scholarship_id);
		return scholarship_data;
	}

}
