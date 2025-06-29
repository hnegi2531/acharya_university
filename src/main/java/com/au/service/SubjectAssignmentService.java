package com.au.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.dto.JwtDetails;
import com.au.dto.SubjectAssignmentDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.SubjectAssignment;
import com.au.repository.SubjectAssignmentRepository;
import com.au.repository.SubjectWorkLoadTypeRepository;
import com.au.response.ResponseHandler;

@Service
public class SubjectAssignmentService {

	@Autowired
	private SubjectAssignmentRepository s_repo;
	
	@Autowired
	private SubjectWorkLoadTypeRepository sw_repo;
	
	
	@Autowired
	private JwtTokenService jwt_service;
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer school_id, Integer dept_id, Integer userId) {

		Page<Object> sa_filtered_response = s_repo.getAllDataFilteredByKeyword(pageable, keyword, school_id ,dept_id ,userId);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, sa_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable, Integer school_id, Integer dept_id, Integer userId) {

		Page<Object> sa_sorted_response = s_repo.getAllSortedData(pageable,school_id ,dept_id ,userId);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, sa_sorted_response);
	}
	
	
	public ResponseEntity<Object> fetchAllSubjectAssignmentBasedOnSchoolIdAndCreatedByByKeyword(Pageable pageable,
			Object keyword, Integer school_id, Integer createdBy, Integer user_id) {
		Page<Object> sa_filtered_response = s_repo.fetchAllSubjectAssignmentBasedOnSchoolIdAndCreatedByByKeyword(pageable, keyword, school_id, createdBy,user_id );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, sa_filtered_response);
	}

	public ResponseEntity<Object> fetchAllSubjectAssignmentBasedOnSchoolIdAndCreatedByData(Pageable pageable1,
			Integer school_id, Integer createdBy, Integer user_id) {
		Page<Object> sa_sorted_response = s_repo.fetchAllSubjectAssignmentBasedOnSchoolIdAndCreatedByData(pageable1,school_id, createdBy,user_id );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, sa_sorted_response);
	}
	

	public List<SubjectAssignment> saveSubjectAssignment(SubjectAssignmentDto s,String jwtToken) throws Exception {
		List<SubjectAssignment> subject_assign_list=new ArrayList<SubjectAssignment>();
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		s.getCourse_assignment_id().stream().forEach(courseAssId ->{
			
			if(s_repo.countCourseAssignedToUser(courseAssId,s.getUser_id())>=1) {
				throw new RuntimeException("Subject Is Already Assigned");
			}else {
				SubjectAssignment subject_assign=new SubjectAssignment();
				subject_assign.setCourse_id(s.getCourse_id());
				subject_assign.setUser_id(s.getUser_id());
				subject_assign.setRemarks(s.getRemarks());
				subject_assign.setActive(s.getActive());
				subject_assign.setCreatedBy(jwtDetails.getUserId());
				subject_assign.setCreatedUsername(jwtDetails.getUserName());
				subject_assign.setCourse_assignment_id(courseAssId);
				s_repo.save(subject_assign);
				subject_assign_list.add(subject_assign);
			}
		});
		return subject_assign_list;
	}

	public SubjectAssignment get(Integer id) {
		return s_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("SubjectAssignment Not Found:" + id));
	}

	public void delete(Integer id) {
		SubjectAssignment cc = s_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SubjectAssignment Not Found:" + id));
		s_repo.updateAcademicWorkLoad(id);
	}

	public void delete1(Integer id) {
		SubjectAssignment cc = s_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SubjectAssignment Not Found:" + id));
		s_repo.updateAcademicWorkLoad1(id);

	}

	public void saveSubjectAssignment1(@Valid SubjectAssignment r) throws Exception{
		if(s_repo.countCourseAssignedToUser(r.getCourse_assignment_id(),r.getUser_id())>=1) {
			throw new RuntimeException("Subject Is Already Assigned");
		} else {
			s_repo.save(r);
		}

	}

	public List<Map<String, Object>> fetchAllDetails() {
		return s_repo.fetchAllDetails();
	}
/*
	public Integer findByprogramSpecializationIdContaining(String id1) {
		return s_repo.countByProgramSpecializationIdLike(id1);
	}
*/
	
	public List<Map<String, Object>> getSubjectAssignIndex(){
		return s_repo.getSubjectAssignIndex();
	}
	
	public List<Map<String,Object>> fetchSubWorkLoadBySubtype(){
		return s_repo.fetchSubWorkLoadBySubtype();
	}


	public List<Map<String,Object>> fetchSubWorkLoadByWorkloadtype(){
		return s_repo.fetchSubWorkLoadByWorkloadtype();
	}

	public List<Map<String, Object>> subjectAssignIndexDetails1() {
		return s_repo.subjectAssignIndexDetails1();	
	}
	
	public List<Map<String, Object>> subjectUnassignedDetails(Integer user_id) {
		return s_repo.subjectUnassignedDetails(user_id);	
	}
	
	public List<Map<String, Object>> getAssignedCoursesDetails(Integer user_id) {
		return s_repo.getAssignedCoursesDetails(user_id);	
	}
	
	public Map<String,Object> getCourseNameBySubjectAssignmentId(Integer subject_assignment_id){
		return s_repo.getCourseNameBySubjectAssignmentId(subject_assignment_id);
	}

	public List<Map<String, Object>> getSubjectAssignmentDetailsData(Integer user_id) {
		return s_repo.getSubjectAssignmentDetailsData(user_id);	
	}

	public List<Map<String, Object>> getCourseAssignmentEmployeeBasedOnUserIdAndYearSem(Integer user_id, Integer year_sem) {
		return s_repo.getCourseAssignmentEmployeeBasedOnUserIdAndYearSem(user_id, year_sem);
	}
	
}
