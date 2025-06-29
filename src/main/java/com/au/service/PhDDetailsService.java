package com.au.service;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.PhD_Details;
import com.au.repository.PhDDetailsRepository;
import com.au.response.ResponseHandler;

@Service
public class PhDDetailsService {

	private Logger logger = LoggerFactory.getLogger(VendorAttachmentService.class);

	@Autowired
	private PhDDetailsRepository phd_repo;

	public PhD_Details save_PhDDetails(PhD_Details phddetails) {
		return phd_repo.save(phddetails);
	}

	public List<PhD_Details> listAllActivePhdDetails() {
		return phd_repo.findAllActivePhdDetails();
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {

		Page<Object> roles_filtered_response = phd_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> roles_sorted_response = phd_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
	}

	public List<PhD_Details> listAllPhdDetails() {
		return phd_repo.findAll();
	}

	public PhD_Details get(Integer phd_id) {
		return phd_repo.findById(phd_id)
				.orElseThrow(() -> new ResourceNotFoundException("PhD Details Not Found " + phd_id));
	}

	public PhD_Details saveUpdatePhdDetails(PhD_Details phd_id) {
		return phd_repo.save(phd_id);
	}

	public void deactivatePhdDetails(Integer phd_id) {
		phd_repo.findById(phd_id).orElseThrow(() -> new RuntimeException("PhD Details Not Found:" + phd_id));
		phd_repo.updateDeactivatePhdDetails(phd_id);
	}

	public void activatePhdDetails(Integer phd_id) {
		phd_repo.findById(phd_id).orElseThrow(() -> new RuntimeException("PhD Details Not Found:" + phd_id));
		phd_repo.updateActivatePhdDetails(phd_id);
	}
	
	public List<HashMap<String,Object>> get1(Integer job_id) {
		List<HashMap<String,Object>> job = phd_repo.findByJobId(job_id);
		
		if(job.size() != 0) {
			return job;
		} else {
			throw new RuntimeException(" Phd Details For this Job Id Not Found " + job_id);
		}
				
	}

}
