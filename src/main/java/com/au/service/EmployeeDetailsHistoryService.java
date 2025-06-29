package com.au.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.au.model.EmployeeDetailsHistory;
import com.au.repository.EmployeeDetailsHistoryRepository;
import com.au.response.ResponseHandler;



@Service
public class EmployeeDetailsHistoryService {
	@Autowired
	private EmployeeDetailsHistoryRepository emp_details_his_repo;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	public EmployeeDetailsHistory saveEmployeeDetailsHistory(EmployeeDetailsHistory emp_his) {
		return emp_details_his_repo.save(emp_his);
	}
	
	public EmployeeDetailsHistory employeeDetailsHistoryOnEmpId(Integer emp_id){
		return emp_details_his_repo.employeeDetailsHistoryOnEmpId(emp_id);
	}

	public void delete(Integer id) {
		EmployeeDetailsHistory employeeHistory = emp_details_his_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee Details History Not Found:" + id));
		emp_details_his_repo.delete1(id);
	}

	public void delete1(Integer id) {
		EmployeeDetailsHistory employeeHistory = emp_details_his_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee Details History Not Found:" + id));
		emp_details_his_repo.delete2(id);
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> employeeHistory_filtered_response = emp_details_his_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, employeeHistory_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> employeeHistory_sorted_response = emp_details_his_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, employeeHistory_sorted_response);
	}

	public EmployeeDetailsHistory get(Integer id) {
		return emp_details_his_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee Details History Not Found:" + id));
	}
	
	public List<EmployeeDetailsHistory> listAll1() {
		return emp_details_his_repo.findAll11();
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeywordByEmpId(Pageable pageable, Object keyword, Integer emp_id) {
		Page<Object> employeeHistory_filtered_response = emp_details_his_repo.getAllDataFilteredByKeywordByEmpId(pageable, keyword, emp_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, employeeHistory_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedDataByEmpId(Pageable pageable, Integer emp_id) {
		Page<Object> employeeHistory_sorted_response = emp_details_his_repo.getAllSortedDataByEmpId(pageable, emp_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, employeeHistory_sorted_response);
	}
		
}
