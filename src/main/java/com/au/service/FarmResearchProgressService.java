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
import com.au.model.FarmResearchProgress;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.FarmResearchProgressRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;

@Service
public class FarmResearchProgressService {
	
	@Autowired
	private FarmResearchProgressRepository rp_repo;
	
	@Autowired
	private EmployeeDetailsRepository empDetail_repo;
	
	@Autowired
	private UserAuthenticationRepository uar_repo;
	
	private Logger logger = LoggerFactory.getLogger(FarmResearchProgressService.class);
	
	
	public List<FarmResearchProgress> createFarmResearchProgress(List<FarmResearchProgress> fts) throws Exception {

		return rp_repo.saveAll(fts);
	}

	public List<FarmResearchProgress> listAll() {
		return rp_repo.findAll1();
	}

	public FarmResearchProgress get(Integer researchProgressId) {
		return rp_repo.findById(researchProgressId)
				.orElseThrow(() -> new ResourceNotFoundException("FarmResearchProgress Not Found:" + researchProgressId));
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {

		Page<Object> profile_research_filtered_response = rp_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, profile_research_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> profile_research_response = rp_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, profile_research_response);
	}

	public FarmResearchProgress updateFarmResearchProgress(FarmResearchProgress pr) {
		return rp_repo.save(pr);
	}

	public void delete(Integer researchProgressId) {
		FarmResearchProgress sir = rp_repo.findById(researchProgressId)
				.orElseThrow(() -> new ResourceNotFoundException("FarmResearchProgress Not Found:" + researchProgressId));
		rp_repo.updateFarmResearchProgress(researchProgressId);
	}

	public void delete1(Integer researchProgressId) {
		FarmResearchProgress sir = rp_repo.findById(researchProgressId)
				.orElseThrow(() -> new ResourceNotFoundException("FarmResearchProgress Not Found:" + researchProgressId));
		rp_repo.updateFarmResearchProgress1(researchProgressId);
	}


}
