package com.au.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.au.model.AdministrationEmailIds;
import com.au.repository.AdministrationEmailIdsRepository;
import com.au.response.ResponseHandler;

@Service
public class AdministrationEmailIdsService {

	@Autowired
	private AdministrationEmailIdsRepository adEmailIds_repo;

	public List<AdministrationEmailIds> saveAdministrationEmailIds(@Valid List<AdministrationEmailIds> ademail) {
		return	adEmailIds_repo.saveAll(ademail);
	}
	
	public List<AdministrationEmailIds> listAll1() {
		return adEmailIds_repo.findAll11();
	}

	public AdministrationEmailIds get(Integer id) {
	return adEmailIds_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Administration Email_Id Not Found:" + id));
	}
	
	public AdministrationEmailIds updateAdministrationEmailIds(AdministrationEmailIds ademail) {
		return adEmailIds_repo.save(ademail);
	}	

	public void deactivate(Integer id) {
		AdministrationEmailIds ademail = adEmailIds_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Course Objective Not Found:" + id));
		adEmailIds_repo.deactivateAdministrationEmailIds(id);
	}
	
	public void activate(Integer id) {
		AdministrationEmailIds ademail = adEmailIds_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Course Objective Not Found:" + id));
		adEmailIds_repo.activateAdministrationEmailIds(id);
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> oc_filtered_response = adEmailIds_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> oc_sorted_response = adEmailIds_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_sorted_response);
	}		
}
