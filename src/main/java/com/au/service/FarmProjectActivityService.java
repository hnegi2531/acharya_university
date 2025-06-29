package com.au.service;

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
import com.au.model.FarmProjectActivity;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.FarmProjectActivityRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;

@Service
public class FarmProjectActivityService {
	
	
	@Autowired
	private FarmProjectActivityRepository pa_repo;
	
	@Autowired
	private EmployeeDetailsRepository empDetail_repo;
	
	@Autowired
	private UserAuthenticationRepository uar_repo;
	
	private Logger logger = LoggerFactory.getLogger(FarmProjectActivityService.class);
	
	
	public List<FarmProjectActivity> createFarmProjectActivity(List<FarmProjectActivity> fts) throws Exception {

		return pa_repo.saveAll(fts);
	}

	public List<FarmProjectActivity> listAll() {
		return pa_repo.findAll1();
	}

	public FarmProjectActivity get(Integer projectActivityId) {
		return pa_repo.findById(projectActivityId)
				.orElseThrow(() -> new ResourceNotFoundException("FarmProjectActivity Not Found:" + projectActivityId));
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {

		Page<Object> profile_research_filtered_response = pa_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, profile_research_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> profile_research_response = pa_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, profile_research_response);
	}

	public FarmProjectActivity updateFarmProjectActivity(FarmProjectActivity pr) {
		return pa_repo.save(pr);
	}

	public void delete(Integer projectActivityId) {
		FarmProjectActivity sir = pa_repo.findById(projectActivityId)
				.orElseThrow(() -> new ResourceNotFoundException("FarmProjectActivity Not Found:" + projectActivityId));
		pa_repo.updateFarmProjectActivity(projectActivityId);
	}

	public void delete1(Integer projectActivityId) {
		FarmProjectActivity sir = pa_repo.findById(projectActivityId)
				.orElseThrow(() -> new ResourceNotFoundException("FarmProjectActivity Not Found:" + projectActivityId));
		pa_repo.updateFarmProjectActivity1(projectActivityId);
	}


}
