package com.au.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.dto.JwtDetails;
import com.au.dto.ProctorStudentAssignmentDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.ProctorStudentAssignment;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.ProctorHeadRepository;
import com.au.repository.ProctorStudentAssignmentRepository;
import com.au.repository.StudentDetailsRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class ProctorStudentAssignmentService {


	@Autowired
	private ProctorStudentAssignmentRepository psar_repo;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@Autowired
	private StudentDetailsRepository stu_repo;
	
	@Autowired
	private EmployeeDetailsRepository empDetail_repo;
	
	@Autowired
	private UserAuthenticationRepository uar_repo;
	
	@Autowired
	private ProctorHeadRepository phr_repo;
	
	

	public List<ProctorStudentAssignment> listAll() {
		return psar_repo.findAll1();
	}

//	public List<HashMap<String, Object>> listAll1() {
//		return psar_repo.findAlll();
//	}

	public List<ProctorStudentAssignment> saveProctorStudentAssignments(ProctorStudentAssignmentDto proctorStudentAssignmentDto, String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		List<ProctorStudentAssignment> list = new ArrayList<ProctorStudentAssignment>();
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		proctorStudentAssignmentDto.getStudent_id().stream().forEach(p -> {
			ProctorStudentAssignment proctorStudentAssignment = new ProctorStudentAssignment();
			proctorStudentAssignment.setEmp_id(proctorStudentAssignmentDto.getEmp_id());
			proctorStudentAssignment.setSchool_id(proctorStudentAssignmentDto.getSchool_id());
			proctorStudentAssignment.setProctor_status(proctorStudentAssignmentDto.getProctor_status());
			proctorStudentAssignment.setStudent_id(p);
			proctorStudentAssignment.setCreated_by(jwtDetails.getUserId());
			proctorStudentAssignment.setCreated_username(jwtDetails.getUserName());
			proctorStudentAssignment.setActive(proctorStudentAssignmentDto.getActive());
			saveProctorStudentAssignment(proctorStudentAssignment);
			list.add(proctorStudentAssignment);
			stu_repo.update(p);
		});
		return list;
	}
/*
	public ProctorStudentAssignment saveProctorStudentAssignmentss(@Valid ProctorHeadHistoryDto p, String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		ProctorHeadHistory proctorHeadHistory = new ProctorHeadHistory();
		ProctorStudentAssignment proctorStudentAssignment = psar_repo.findByproctor_assign_id(p.getPsa().getProctor_assign_id());
		proctorStudentAssignment.setProctor_id(p.getPsa().getProctor_id());
		proctorStudentAssignment.setStudent_id(p.getPsa().getStudent_id());
		proctorStudentAssignment.setModified_by(jwtDetails.getUserId());
		proctorStudentAssignment.setModified_username(jwtDetails.getUserName());
		proctorStudentAssignment.setActive(p.getPsa().getActive());
		proctorHeadHistory.setProctor_id(p.getPhh().getProctor_id());
		proctorHeadHistory.setStudent_id(p.getPhh().getStudent_id());
		proctorHeadHistory.setStudent_name(p.getPhh().getStudent_name());
		proctorHeadHistory.setModified_by(jwtDetails.getUserId());
		proctorHeadHistory.setModified_username(jwtDetails.getUserName());
		phhr_repo.save(proctorHeadHistory);
		return saveProctorStudentAssignment(p.getPsa());

	}
*/
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer userId, Integer school_id) {

		Page<Object> proc_stu_assignment_filtered_response = psar_repo.getAllDataFilteredByKeyword(pageable, keyword ,userId ,school_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, proc_stu_assignment_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable, Integer userId, Integer school_id) {

		Page<Object> proc_stu_assignment_sorted_response = psar_repo.getAllSortedData(pageable,userId ,school_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, proc_stu_assignment_sorted_response);
	}
	
	private ProctorStudentAssignment saveProctorStudentAssignment(ProctorStudentAssignment proctorStudentAssignment) {
		return psar_repo.save(proctorStudentAssignment);
		
	}

	public ProctorStudentAssignment get(Integer id) {
		return psar_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ProctorStudentAssignment Id Found:" + id));
	}

	public void delete(Integer id) {
		ProctorStudentAssignment ay = psar_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ProctorStudentAssignment Found:" + id));
		psar_repo.update(id);
	}

	public void delete1(Integer id) {
		ProctorStudentAssignment ay = psar_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ProctorStudentAssignment Found:" + id));
		psar_repo.update1(id);
	}

	public List<HashMap<String, Object>> getAllStudentList(Integer emp_id) {
		return psar_repo.getAllList(emp_id);
	}

	public void saveProctorStudentAssignment(List<ProctorStudentAssignment> p) {
		p.stream().forEach(r ->{
			stu_repo.update(r.getStudent_id());
			r.setActive(false);
		});
		psar_repo.saveAll(p);	
	}
	
	public List<HashMap<String, Object>> getProctorStatusAssignedStudentDetailsList(Integer emp_id) {
		
		return psar_repo.getProctorStatusAssignedStudentDetailsList(emp_id);
	}
	
	public List<HashMap<String, Object>> getProctorStatusAssignedStudentDetailsListByUserId(Integer user_id) {
		Integer emp_id = uar_repo.getEmployee_id(user_id);
//		Integer proc_id = phr_repo.getProctorId(emp_id);
		return psar_repo.getProctorStatusAssignedStudentDetailsListByUserId(emp_id);
	}
	
	
	public List<Map<String, Object>> getProctorStatusAssignedStudentsByUserId(Integer user_id) {
		Integer emp_id = uar_repo.getEmployee_id(user_id);
//		Integer proc_id = phr_repo.getProctorId(emp_id);
		return psar_repo.getProctorStatusAssignedStudentsByUserId(emp_id);
	}

	public Map<String, Object> getCountOfStudentBasedOnUserId(Integer user_id) {
		return psar_repo.getCountOfStudentBasedOnUserId(user_id);
	}
}
