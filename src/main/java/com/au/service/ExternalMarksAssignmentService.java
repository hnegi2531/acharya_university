package com.au.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.au.dto.ExternalMarksAssignmentDto;
import com.au.dto.JwtDetails;
import com.au.model.Budget;
import com.au.model.CourseStudentAssignment;
import com.au.model.ExternalMarksAssignment;
import com.au.repository.CourseStudentAssignmentRepository;
import com.au.repository.ExternalMarksAssignmentRepositoty;
import com.au.repository.ExternalMarksRepository;
import com.au.response.ResponseHandler;

@Service
public class ExternalMarksAssignmentService {

	@Autowired
	private ExternalMarksAssignmentRepositoty externalMarksAssignmentRepositoty;
	
	@Autowired
	private ExternalMarksRepository externalMarksRepository;
	
	@Autowired
	private CourseStudentAssignmentRepository courseStudentAssignmentRepository;
	
	
	public List<ExternalMarksAssignment> saveExternalMarksAssignment(@Valid ExternalMarksAssignmentDto marks,JwtDetails jwtDetails) {
		List<ExternalMarksAssignment> listOfExternalMarksAssignment=new ArrayList<ExternalMarksAssignment>();
	Integer courseAssignmentId=externalMarksRepository.getcourseAssignment(marks.getExternal_mark_id());
	List<Integer> studentId	=courseStudentAssignmentRepository.getStudentIdz(courseAssignmentId);
	studentId.stream().forEach(sId ->{
		ExternalMarksAssignment external_marks_assignment=new ExternalMarksAssignment();
		external_marks_assignment.setStudent_id(sId);
		external_marks_assignment.setSection_id(marks.getSection_id());
		external_marks_assignment.setExternal_mark_id(marks.getExternal_mark_id());
		external_marks_assignment.setMarks_scored(marks.getMarks_scored());
		external_marks_assignment.setPercentage(marks.getPercentage());
		external_marks_assignment.setActive(marks.getActive());
		external_marks_assignment.setCreated_by(jwtDetails.getUserId());
		external_marks_assignment.setCreated_username(jwtDetails.getUserName());
		
		externalMarksAssignmentRepositoty.save(external_marks_assignment);
		listOfExternalMarksAssignment.add(external_marks_assignment);
	});
		
		return	listOfExternalMarksAssignment;
	}
	
	
	public List<ExternalMarksAssignment> getAllActiveExternalMarksAssignment() {
		return externalMarksAssignmentRepositoty.getAllActiveExternalMarksAssignment();
	}


	public List<ExternalMarksAssignment> get(List<Integer> id) {
		return externalMarksAssignmentRepositoty.findAllById(id);
	}
	
	
	public List<ExternalMarksAssignment> updateExternalMarksAssignment(List<ExternalMarksAssignment> externalMarksAssignment) {
		return externalMarksAssignmentRepositoty.saveAll(externalMarksAssignment);
	}


	public void deactivate(Integer id) {
		ExternalMarksAssignment con = externalMarksAssignmentRepositoty.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("External Marks Not Found:" + id));
		externalMarksAssignmentRepositoty.deactivate(id);
	}
	
	public void activate(Integer id) {
		ExternalMarksAssignment con = externalMarksAssignmentRepositoty.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("External Marks Not Found:" + id));
		externalMarksAssignmentRepositoty.activate(id);
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> oc_filtered_response = externalMarksAssignmentRepositoty.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> oc_sorted_response = externalMarksAssignmentRepositoty.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_sorted_response);
	}
}
