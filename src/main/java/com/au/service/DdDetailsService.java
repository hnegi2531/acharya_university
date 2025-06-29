package com.au.service;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.au.exception.ResourceNotFoundException;
import com.au.model.DdDetails;
import com.au.repository.DdDetailsRepository;
import com.au.response.ResponseHandler;

@Service
public class DdDetailsService {
	
	@Autowired
	private DdDetailsRepository dd_repo;
	
	public DdDetails saveDdDetails(DdDetails dd) {
		return dd_repo.save(dd);
	}
	
	public List<DdDetails> getActiveDetails(){
		return dd_repo.getActiveDetails();
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {

		Page<Object> dd_filtered_response = dd_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, dd_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> dd_sorted_response = dd_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, dd_sorted_response);
	}
	
	public DdDetails getDdDetailsbyId(Integer dd_id){
		return dd_repo.findById(dd_id).orElseThrow(() -> new ResourceNotFoundException(" Dd Details Not Found " + dd_id));
	}
	
	public DdDetails updateDdDetails(DdDetails dd) {
		return dd_repo.save(dd);
	}
	
	public void delete1(Integer dd_id) {
		dd_repo.delete1(dd_id);
	}
	
	public void delete2(Integer dd_id) {
		dd_repo.delete2(dd_id);
	}

}
