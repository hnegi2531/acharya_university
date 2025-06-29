package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.CourtCases;
import com.au.repository.CourtCasesHistoryRepository;
import com.au.repository.CourtCasesRepository;
import com.au.response.ResponseHandler;

@Service
public class CourtCasesService {
	
	@Autowired
	private CourtCasesRepository court_cases_repo;
	
	@Autowired
	private CourtCasesHistoryService court_cases_his_ser;
	
	public CourtCases saveCourtCases(CourtCases court_cases) {
		if(court_cases_repo.countOfCaseNo(court_cases.getCase_no())>=1) {
			throw new RuntimeException("Case Number Already Exist");
		}else {
			return court_cases_repo.save(court_cases);
		}
	}
	
	public List<CourtCases> getActiveDetails() {
		return court_cases_repo.findAll1();
	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = court_cases_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = court_cases_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public CourtCases getDetailById(Integer court_cases_id) {
		return court_cases_repo.findById(court_cases_id).orElseThrow(() -> new ResourceNotFoundException("Court Case Not Found:" + court_cases_id));
	}
	
	public CourtCases updateDetailCourtCasesUpdateOnlyById(CourtCases court_cases) {
		court_cases_repo.findById(court_cases.getCourt_cases_id()).orElseThrow(() -> 
							new ResourceNotFoundException("Court Case Not Found:" + court_cases.getCourt_cases_id()));
		if(court_cases_repo.countforUpdateCaseNo(court_cases.getCourt_cases_id(),court_cases.getCase_no())>=1) {
			throw new RuntimeException("Case Number Already Exist");
		}else {
			return court_cases_repo.save(court_cases);
		}

	}
	
	public CourtCases updateDetailById(CourtCases court_cases) {
		CourtCases court_cases_for_history=court_cases_repo.findById(court_cases.getCourt_cases_id()).orElseThrow(() -> 
							new ResourceNotFoundException("Court Case Not Found:" + court_cases.getCourt_cases_id()));
		court_cases_his_ser.saveDataInCourtCaseHistory(court_cases_for_history);
		return court_cases_repo.save(court_cases);

	}
	
	public void deactivate(Integer court_cases_id) {
		court_cases_repo.findById(court_cases_id)
				.orElseThrow(() -> new ResourceNotFoundException("Court Case Not Found:" + court_cases_id));
		court_cases_repo.deactivate(court_cases_id);
	}
	
	public void activate(Integer court_cases_id) {
		court_cases_repo.findById(court_cases_id)
				.orElseThrow(() -> new ResourceNotFoundException("Court Case Not Found:" + court_cases_id));
		court_cases_repo.activate(court_cases_id);
	}

}
