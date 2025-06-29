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
import com.au.model.EmployeeType;
import com.au.repository.EmployeeTypeRepository;
import com.au.response.ResponseHandler;

@Service
public class EmployeeTypeService {

	@Autowired
	private EmployeeTypeRepository s_repo;
	
	public List<EmployeeType> listAll() {
		return s_repo.findAll1();
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> roles_filtered_response = s_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> roles_sorted_response = s_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
	}

	public EmployeeType saveEmployeeType(EmployeeType s) {
		
		if(s_repo.existsByEmpType(s.getEmpType())) {
			throw new RuntimeException("Emp Type already exist");
		}
		if(s_repo.existsByEmpTypeShortName(s.getEmpTypeShortName())) {
			throw new RuntimeException("Short Name already exist");
		}
		
		return s_repo.save(s);

	}

	public EmployeeType get(Integer id) {
		return s_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("EmployeeType Not Found:" + id));
	}

	public void delete(Integer id) {
		EmployeeType cc = s_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("EmployeeType Not Found:" + id));
		s_repo.updateEmployeeType(id);
	}

	public void delete1(Integer id) {
		EmployeeType cc = s_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("EmployeeType Not Found:" + id));
		s_repo.updateEmployeeType1(id);

	}

	public EmployeeType saveEmployeeTypes(@Valid EmployeeType r) {
		return s_repo.save(r);
		
		
	}

}
