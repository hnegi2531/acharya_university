package com.au.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.BankGroup;
import com.au.model.CourseObjective;
import com.au.model.Department;
import com.au.repository.BankGroupRepository;
import com.au.repository.CourseObjectiveRepository;
import com.au.response.ResponseHandler;

@Service
public class BankGroupService {

	@Autowired
	private BankGroupRepository bg_repo;
	
	
	public BankGroup createBankGroup(@Valid BankGroup bankGroup) throws Exception {
		return	bg_repo.save(bankGroup);
	}
	
	
	public List<BankGroup> listAll() {
		return bg_repo.listAll();
	}
	
	public BankGroup get(Integer id) {
		return bg_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("bank Group Not Found:" + id));
	}
	
	public BankGroup updateBankGroup(@Valid BankGroup cos) {
		return bg_repo.save(cos);
	}
	
	
	public void deactivate(Integer id) {
		BankGroup co = bg_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Bank Group Not Found:" + id));
		bg_repo.deactivate(id);
	}
	
	public void activate(Integer id) {
		BankGroup co = bg_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Bank Group Not Found:" + id));
		bg_repo.activate(id);
	}
	
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> oc_filtered_response = bg_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> oc_sorted_response = bg_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_sorted_response);
	}	
}
