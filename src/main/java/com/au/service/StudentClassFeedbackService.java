package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.au.exception.ResourceNotFoundException;
import com.au.model.Department;
import com.au.model.StudentClassFeedback;
import com.au.repository.StudentClassFeedbackRepository;
import com.au.response.ResponseHandler;

@Service
public class StudentClassFeedbackService {
	
	@Autowired
	private StudentClassFeedbackRepository scf_repo;
	
	public StudentClassFeedback saveStudentClassFeedback(StudentClassFeedback scf) throws Exception {

		if (scf_repo.getCountOfFeedback(scf.getStudent_id(), scf.getTime_table_employee_id()) >= 1)
			throw new Exception("Feedback already submitted!");
	
		return scf_repo.save(scf);
		
	}
	
//	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
//		Page<Object> feedback_filtered_response = scf_repo.getAllDataFilteredByKeyword(pageable, keyword);
//		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, feedback_filtered_response);
//	}
//
//	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
//		Page<Object> feedback_sorted_response = scf_repo.getAllSortedData(pageable);
//		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, feedback_sorted_response);
//	}
	
	public List<StudentClassFeedback> listAllFeedback() {
		return scf_repo.findAllFeedback();
	}
	
	public StudentClassFeedback getStudentClassFeedback(Integer student_class_feedback_id) {
		return scf_repo.findById(student_class_feedback_id).orElseThrow(() -> new ResourceNotFoundException("StudentClassFeedback Not Found:" + student_class_feedback_id));
	}
	
	public StudentClassFeedback updateStudentClassFeedback(StudentClassFeedback feedback) {
		return scf_repo.save(feedback);
	}
	
	public void delete(Integer student_class_feedback_id) {
		StudentClassFeedback feedback = scf_repo.findById(student_class_feedback_id)
				.orElseThrow(() -> new ResourceNotFoundException("StudentClassFeedback Not Found:" + student_class_feedback_id));
		scf_repo.updateStudentClassFeedback(student_class_feedback_id);
	}

	public void delete1(Integer student_class_feedback_id) {
		StudentClassFeedback feedback = scf_repo.findById(student_class_feedback_id)
				.orElseThrow(() -> new ResourceNotFoundException("StudentClassFeedback Not Found:" + student_class_feedback_id));
		scf_repo.updateStudentClassFeedback1(student_class_feedback_id);
	}
	
	public Boolean checkStudentClassFeedback(Integer student_id, Integer time_table_employee_id) {
		if(scf_repo.getCountOfFeedback(student_id, time_table_employee_id) >=1)
			return true;
		
		else
			return false;
	}
	
	
}
