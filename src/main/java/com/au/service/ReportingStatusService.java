package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.ReportingStatus;
import com.au.repository.ReportingStatusRepository;
import com.au.response.ResponseHandler;

@Service
public class ReportingStatusService {

	@Autowired
	private ReportingStatusRepository r_repo;
	
	public List<ReportingStatus> listAll() {
		return r_repo.findAll1();
	}

	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = r_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = r_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public ReportingStatus save_ReportingStatus(ReportingStatus r) throws Exception {		
		return r_repo.save(r);
	}

	public ReportingStatus get(Integer id) {
		return r_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ReportingStatus Not Found:" + id));
	}

	public void delete(Integer id) {
		ReportingStatus ay = r_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ReportingStatus Not Found:" + id));
		r_repo.updateReportingStatus(id);
	}

	public void delete1(Integer id) {
		ReportingStatus ay = r_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ReportingStatus Not Found:" + id));
		r_repo.updateReportingStatus1(id);
	}

}
