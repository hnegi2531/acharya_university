package com.au.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.JwtDetails;
import com.au.exception.ResourceNotFoundException;
import com.au.model.CourseObjective;
import com.au.model.Department;
import com.au.model.ServiceTicketMaintenance;
import com.au.model.TransportMaintenance;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.TransportMaintenanceRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.repository.User_Auth_Repository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class TransportMaintenanceService {
	
	@Autowired
	private TransportMaintenanceRepository transport_repo;
	
	@Autowired
	private UserAuthenticationRepository uar_repo;
	
	@Autowired
	private EmployeeDetailsRepository empDetail_repo;
	
	@Autowired
	private JwtTokenService jwt_service;
	
//	** request_status **
	private final String CRITICAL = "CRITICAL";
	private final String NONCRITICAL = "NONCRITICAL";
	private final String UNDERPROCESS= "UNDERPROCESS";
	private final String COMPLETED = "COMPLETED";
	private final String PENDING = "PENDING";
	
	/*             
	 * attend_status 
	 * --------------
	 * 1=attend
	 * 0=not attend
	 *   */
	
	
	/*             
	 * cancelled_status 
	 * --------------
	 * APPROVED
	 * CANCEL
	 *   */
	Date date1;
	Date date2;
	public TransportMaintenance saveTransportMaintenance(@Valid TransportMaintenance tm)throws Exception {
		
		String email = uar_repo.getUserEmail(tm.getUser_id());
		Integer deptId = empDetail_repo.getEmployeeDeptId(email);
		Integer schoolId = empDetail_repo.getEmployeeSchoolId(email); 
		
		tm.setDept_id(deptId);
		tm.setSchool_id(schoolId);
		
		return	transport_repo.save(tm);
	}


	public List<TransportMaintenance> getAllActiveTransportMaintenance() {
		 return transport_repo.getAllActiveTransportMaintenance();
	}
	
	public TransportMaintenance get(Integer id) {
		return transport_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Transport Maintenance Not Found:" + id));
	}


	public TransportMaintenance updateTransportMaintenance(@Valid TransportMaintenance tm) throws JsonParseException, JsonMappingException, IOException {
		
		TransportMaintenance entity = transport_repo.findById(tm.getTransport_maintenance_id())
				.orElseThrow(() -> new ResourceNotFoundException("Transport Maintenance not found"));
		
		entity.setAlloted_person_name(tm.getAlloted_person_name()); 
		entity.setAlloted_person_number(tm.getAlloted_person_number());
		entity.setAttending_remarks(tm.getAttending_remarks());
		entity.setRequest_status(tm.getRequest_status());
		transport_repo.save(entity);
		
		if (entity.getRequest_status().equalsIgnoreCase("Trip Completed")) {
			entity.setActive(false);
			transport_repo.save(entity);
		}
		
		return entity;
	}
	
	public void deactivate(Integer id) {
		TransportMaintenance co = transport_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Course Objective Not Found:" + id));
		transport_repo.deactivate(id);
	}
	
	public void activate(Integer id) {
		TransportMaintenance co = transport_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Course Objective Not Found:" + id));
		transport_repo.activate(id);
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer dept_id, Integer user_id) {
		Page<Object> oc_filtered_response = transport_repo.getAllDataFilteredByKeyword(pageable, keyword ,dept_id ,user_id  );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable, Integer dept_id, Integer user_id) {
		Page<Object> oc_sorted_response = transport_repo.getAllSortedData(pageable ,dept_id ,user_id  );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_sorted_response);
	}


	public Map<String, Object> transportMaintenanceById(Integer id) {
		
		return transport_repo.transportMaintenanceById(id);
	}


	public ResponseEntity<Object> getAllDataFilteredByKeywordDeptId(Pageable pageable, Object keyword,
			Integer dept_id) {
		Page<Object> oc_filtered_response = transport_repo.getAllDataFilteredByKeywordDeptId(pageable, keyword ,dept_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_filtered_response);
	}


	public ResponseEntity<Object> getAllSortedDataDeptId(Pageable pageable1, Integer dept_id) {
		Page<Object> oc_sorted_response = transport_repo.getAllSortedDataDeptId(pageable1 ,dept_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_sorted_response);
	}	
	
	
	public ResponseEntity<Object> getAllDataFilteredByKeywordHistory(Pageable pageable, Object keyword,
			Integer dept_id) {
		Page<Object> oc_filtered_response = transport_repo.getAllDataFilteredByKeywordHistory(pageable, keyword ,dept_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_filtered_response);
	}


	public ResponseEntity<Object> getAllSortedDataHistory(Pageable pageable1, Integer dept_id) {
		Page<Object> oc_sorted_response = transport_repo.getAllSortedDataHistory(pageable1 ,dept_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_sorted_response);
	}	


}
