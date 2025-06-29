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
import com.au.model.HrStatusHistory;
import com.au.repository.HrStatusHistoryRepository;
import com.au.response.ResponseHandler;

@Service
public class HrStatusHistoryService {

	@Autowired
	private HrStatusHistoryRepository hsh_repo;
	
	
	public HrStatusHistory saveHrStatusHistory(HrStatusHistory hrsh) throws Exception {
		return	hsh_repo.save(hrsh);
	}	
	
	
	public List<HrStatusHistory> listAll1() {
		return hsh_repo.findAll11();
	}	
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> roles_filtered_response = hsh_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> roles_sorted_response = hsh_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
	}	
	
	public HrStatusHistory get(Integer id) {
		return hsh_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hr Status History Not Found:" + id));
	}
	
	public HrStatusHistory updateHrStatusHistory(HrStatusHistory hrsh) {
		return hsh_repo.save(hrsh);
	}
	
	public void deactivateHrStatusHistory(Integer id) {
		HrStatusHistory hrsh = hsh_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Hr Status History Not Found:" + id));
		hsh_repo.deactivateHrStatusHistory(id);
	}

	public void activateHrStatusHistory(Integer id) {
		HrStatusHistory hrsh = hsh_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Hr Status History Not Found:" + id));
		hsh_repo.activateHrStatusHistory(id);
	}


	public List<HashMap<String, Object>> getJobprofileDetails(Integer job_id) {
		
		return hsh_repo.getJobprofileDetails(job_id);
	}	
}
