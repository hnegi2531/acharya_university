package com.au.service;

import com.au.model.ClassCommencementDetails;
import com.au.model.CommencementType;
import com.au.model.Department;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.repository.CommencementTypeRepository;
import com.au.repository.DepartmentAssignmentRepository;
import com.au.repository.DepartmentRepository;
import com.au.response.ResponseHandler;

@Service
public class CommencementTypeService {

	@Autowired
	private CommencementTypeRepository ct_repo;
	
	
	public CommencementType saveCommencementType(CommencementType ct) throws Exception {

		if (getCommencementType(ct.getCommencement_type()) >= 1)
			throw new Exception("Commencement Type already exist");
		 else {
			 ct_repo.save(ct);
		}
		return ct;
	}
	
	private Integer getCommencementType(String commencement_type) {
		return ct_repo.getcountOfCommencementType(commencement_type);
	}
	
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> ct_filtered_response = ct_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, ct_filtered_response);
	}

	
	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> ct_sorted_response = ct_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, ct_sorted_response);
	}
	
	
	public List<CommencementType> listAll1() {
		return ct_repo.findAll11();
	}
	
	
	public CommencementType get(Integer id) {
		return ct_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Commencement Type Not Found:" + id));
	}

	public CommencementType saveCommencementTypes(CommencementType cts) {
		return ct_repo.save(cts);
	}
	
	public void delete(Integer id) {
		CommencementType ct = ct_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("CommencementType Not Found:" + id));
		ct_repo.updateCommencementType(id);
	}

	public void delete1(Integer id) {
		CommencementType ct = ct_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("CommencementType Not Found:" + id));
		ct_repo.updateCommencementType1(id);
	}

	public List<HashMap<String, Object>> getCommencementTypeDetails() {
		return ct_repo.getCommencementTypeDetails();
	}	

}
