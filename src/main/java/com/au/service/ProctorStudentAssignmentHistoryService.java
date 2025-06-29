package com.au.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.model.EmployeeDetails;
import com.au.model.ProctorStudentAssignmentHistory;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.ProctorStudentAssignmentHistoryRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class ProctorStudentAssignmentHistoryService {
	
	
	@Autowired
	private ProctorStudentAssignmentHistoryRepository psar_repo;
	
	@Autowired
	private EmployeeDetailsRepository employeeDetailsRepository;

	
	public List<ProctorStudentAssignmentHistory> saveProctorStudentAssignments(List<ProctorStudentAssignmentHistory> prochistory, String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {

		return psar_repo.saveAll(prochistory);
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer userId, Integer school_id) {

		Page<Object> proc_stu_assignment_filtered_response = psar_repo.getAllDataFilteredByKeyword(pageable, keyword ,userId ,school_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, proc_stu_assignment_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable, Integer userId, Integer school_id) {

		Page<Object> proc_stu_assignment_sorted_response = psar_repo.getAllSortedData(pageable,userId ,school_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, proc_stu_assignment_sorted_response);
	}
	
	public List<HashMap<String, Object>> proctorStudentAssignmentHistoryDetailByUserId(Integer userId) {
		EmployeeDetails employee=employeeDetailsRepository.getEmployeeDataByUserID(userId);
		return psar_repo.proctorStudentAssignmentHistoryDetailByEmployeeId(employee.getEmp_id());
	}
	


}
