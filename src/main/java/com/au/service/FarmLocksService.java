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
import com.au.model.FarmLocks;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.FarmLocksRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;

@Service
public class FarmLocksService {
	
	@Autowired
	private FarmLocksRepository fl_repo;
	
	@Autowired
	private EmployeeDetailsRepository empDetail_repo;
	
	@Autowired
	private UserAuthenticationRepository uar_repo;
	
	private Logger logger = LoggerFactory.getLogger(FarmLocksService.class);
	
	
	public FarmLocks createFarmLocks(FarmLocks fts) throws Exception {

		return fl_repo.save(fts);
	}

	public List<FarmLocks> listAll() {
		return fl_repo.findAll1();
	}

	public FarmLocks get(Integer lockId) {
		return fl_repo.findById(lockId)
				.orElseThrow(() -> new ResourceNotFoundException("FarmLocks Not Found:" + lockId));
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {

		Page<Object> profile_research_filtered_response = fl_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, profile_research_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> profile_research_response = fl_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, profile_research_response);
	}

	public FarmLocks updateFarmLocks(FarmLocks pr) {
		return fl_repo.save(pr);
	}

	public void delete(Integer lockId) {
		FarmLocks sir = fl_repo.findById(lockId)
				.orElseThrow(() -> new ResourceNotFoundException("FarmLocks Not Found:" + lockId));
		fl_repo.updateFarmLocks(lockId);
	}

	public void delete1(Integer lockId) {
		FarmLocks sir = fl_repo.findById(lockId)
				.orElseThrow(() -> new ResourceNotFoundException("FarmLocks Not Found:" + lockId));
		fl_repo.updateFarmLocks1(lockId);
	}
	
//	public FarmLocks updateFarmLocks(String challengesFaced) {
//		FarmLocks sir = fl_repo.findById(lockId)
//		.orElseThrow(() -> new ResourceNotFoundException("FarmLocks Not Found:" + lockId));
//		return fl_repo.save(pr);
//	}

}
