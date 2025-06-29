package com.au.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.au.model.FarmTeachingSubjects;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.FarmTeachingSubjectsRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;

@Service
public class FarmTeachingSubjectsService {
	
	@Autowired
	private FarmTeachingSubjectsRepository pr_repo;
	
	@Autowired
	private EmployeeDetailsRepository empDetail_repo;
	
	@Autowired
	private UserAuthenticationRepository uar_repo;
	
	private Logger logger = LoggerFactory.getLogger(FarmTeachingSubjectsService.class);
	
	
	public List<FarmTeachingSubjects> createFarmTeachingSubjects(List<FarmTeachingSubjects> fts) throws Exception {
		
		List<FarmTeachingSubjects> data = new ArrayList<FarmTeachingSubjects>();
		
		
		return pr_repo.saveAll(fts);
	}

	public List<FarmTeachingSubjects> listAll() {
		return pr_repo.findAll1();
	}

	public FarmTeachingSubjects get(Integer teachingSubjectId) {
		return pr_repo.findById(teachingSubjectId)
				.orElseThrow(() -> new ResourceNotFoundException("FarmTeachingSubjects Not Found:" + teachingSubjectId));
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {

		Page<Object> profile_research_filtered_response = pr_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, profile_research_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> profile_research_response = pr_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, profile_research_response);
	}

	public FarmTeachingSubjects updateFarmTeachingSubjects(FarmTeachingSubjects pr) {
		return pr_repo.save(pr);
	}

	public void delete(Integer teachingSubjectId) {
		FarmTeachingSubjects sir = pr_repo.findById(teachingSubjectId)
				.orElseThrow(() -> new ResourceNotFoundException("FarmTeachingSubjects Not Found:" + teachingSubjectId));
		pr_repo.updateFarmTeachingSubjects(teachingSubjectId);
	}

	public void delete1(Integer teachingSubjectId) {
		FarmTeachingSubjects sir = pr_repo.findById(teachingSubjectId)
				.orElseThrow(() -> new ResourceNotFoundException("FarmTeachingSubjects Not Found:" + teachingSubjectId));
		pr_repo.updateFarmTeachingSubjects1(teachingSubjectId);
	}

}
