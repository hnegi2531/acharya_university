package com.au.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.JwtDetails;
import com.au.dto.NoDuesAssignmentDto;
import com.au.dto.NoDuesAssignmentRequest;
import com.au.dto.NoDuesAssignmentResponse;
import com.au.exception.ResourceNotFoundException;
import com.au.model.Department;
import com.au.model.EmployeeDetails;
import com.au.model.NoDuesAssignment;
import com.au.model.Resignation;
import com.au.repository.DepartmentRepository;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.NoDuesAssignmentRepository;
import com.au.repository.ResignationRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class NoDuesAssignmentService {
	
private final ModelMapper modelMapper = new ModelMapper();
	
	@Autowired
	private JwtTokenService jwtService;
	
	@Autowired
	private NoDuesAssignmentRepository noDuesAssignmentRepository;
	
	@Autowired
	private EmployeeDetailsRepository employeeDetailsRepository;
	
	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private ResignationRepository resignationRepository;
	
	public List<NoDuesAssignmentResponse> saveNoDuesAssignment(List<NoDuesAssignmentRequest> noDuesAssignmentRequest,String jwtToken) throws JsonParseException, JsonMappingException, IOException{

		List<NoDuesAssignmentResponse> noDuesAssignmentResponses=new ArrayList<>();
		JwtDetails jwtDetails = jwtService.callJwtToken(jwtToken);

		noDuesAssignmentRequest.parallelStream().forEach(nda -> {
			NoDuesAssignment noDuesAssignment=new NoDuesAssignment();
			noDuesAssignment.setActive(nda.getActive());
			noDuesAssignment.setNo_due_status(nda.getNo_due_status());
			noDuesAssignment.setComments(nda.getComments());
			EmployeeDetails employeeDetails=employeeDetailsRepository.findById(nda.getEmployee_Id()).orElseThrow(() -> new ResourceNotFoundException("Employee Details Not Found:" + nda.getEmployee_Id()));
			noDuesAssignment.setEmployee_details(employeeDetails);
			Department department=departmentRepository.findById(nda.getDepartment_id()).orElseThrow(() -> new ResourceNotFoundException("Department Not Found:" + nda.getDepartment_id()));
			noDuesAssignment.setDepartment(department);
			Resignation resignation=resignationRepository.findById(nda.getResignation_id()).orElseThrow(() -> new ResourceNotFoundException("Resignation Not Found:" + nda.getDepartment_id()));
			noDuesAssignment.setResignation(resignation);
			noDuesAssignment.setCreated_by(jwtDetails.getUserId());
			noDuesAssignment.setCreated_username(jwtDetails.getUserName());
			noDuesAssignment.setIp_address(nda.getIp_address());
			noDuesAssignment.setApprover_id(nda.getApprover_id());
			noDuesAssignment.setApprover_date(nda.getApprover_date());
			noDuesAssignmentRepository.save(noDuesAssignment);
			

			NoDuesAssignmentResponse noDuesAssignmentResponse=modelMapper.map(nda,NoDuesAssignmentResponse.class);
			noDuesAssignmentResponse.setEmployee_Id(employeeDetails.getEmp_id());
			noDuesAssignmentResponse.setDepartment_id(department.getDept_id());
			noDuesAssignmentResponse.setResignation_id(resignation.getResignation_id());
			noDuesAssignmentResponse.setCreated_by(jwtDetails.getUserId());
			noDuesAssignmentResponse.setCreated_username(jwtDetails.getUserName());
			noDuesAssignmentResponses.add(noDuesAssignmentResponse);
		});

		return noDuesAssignmentResponses;
	}

	public ResponseEntity<Object> listAllWithKeyword(Pageable pageable, Object keyword) {
		Page<Object> response1 = noDuesAssignmentRepository.listAllWithKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}

	public ResponseEntity<Object> listAllWithOutKeyword(Pageable pageable1) {
		Page<Object> response = noDuesAssignmentRepository.listAllWithOutKeyword(pageable1);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	
	public ResponseEntity<Object> listAllWithLeaveApprKeyword(Pageable pageable, Object keyword, Integer userId) {
		Page<Object> response1 = noDuesAssignmentRepository.listAllWithLeaveApprKeyword(pageable, keyword,userId );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}

	public ResponseEntity<Object> listAllLeaveApprWithOutKeyword(Pageable pageable1, Integer userId) {
		Page<Object> response = noDuesAssignmentRepository.listAllLeaveApprWithOutKeyword(pageable1,userId);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}	

	public List<HashMap<String, Object>> getNoDueAssignmentData(Integer resignation_id) {
		return noDuesAssignmentRepository.getNoDueAssignmentData(resignation_id);
	}

	
	public List<NoDuesAssignmentDto> updateNoDuesAssignment(List<NoDuesAssignmentDto> res,String jwtToken) throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwtService.callJwtToken(jwtToken);
		
		res.stream().forEach(reg ->{
			
		NoDuesAssignment nodue = noDuesAssignmentRepository.findById(reg.getNo_dues_assignment_id())
		.orElseThrow(()-> new ResourceNotFoundException("NoDuesAssignment not found"));
		nodue.setModified_by(jwtDetails.getUserId());
		nodue.setModified_username(jwtDetails.getUserName());
		
		nodue.setNo_due_status(ObjectUtils.isNotEmpty(reg.getNo_due_status()) ? reg.getNo_due_status() : nodue.getNo_due_status());
		nodue.setComments(ObjectUtils.isNotEmpty(reg.getComments()) ? reg.getComments() : nodue.getComments());
		nodue.setApprover_date(ObjectUtils.isNotEmpty(reg.getApprover_date()) ? reg.getApprover_date() : nodue.getApprover_date());
		nodue.setIp_address(ObjectUtils.isNotEmpty(reg.getIp_address()) ? reg.getIp_address() : nodue.getIp_address());
		 noDuesAssignmentRepository.save(nodue);
		});
		return res;
	}

	public List<Map<String, Object>> getAllNoDueAssignmentData(Integer resignation_id) {
		return noDuesAssignmentRepository.getAllNoDueAssignmentData(resignation_id);
	}

}
