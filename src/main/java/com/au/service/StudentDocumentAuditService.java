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
import com.au.model.StudentDocumentAudit;
import com.au.repository.StudentDocumentAuditRepository;
import com.au.response.ResponseHandler;

@Service
public class StudentDocumentAuditService {

	@Autowired
	private StudentDocumentAuditRepository studentDocumentAuditRepository;

	public StudentDocumentAudit saveStudentDocumentAudit(@Valid StudentDocumentAudit dp) throws Exception {
		return studentDocumentAuditRepository.save(dp);
	}

	public List<StudentDocumentAudit> getAllActiveStudentDocumentAudit() {
		return studentDocumentAuditRepository.getAllActiveStudentDocumentAudit();
	}

	public StudentDocumentAudit get(Integer id) {
		return studentDocumentAuditRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Student Document Audit Not Found:" + id));
	}

	public StudentDocumentAudit updateStudentDocumentAudit(@Valid StudentDocumentAudit tm) {
		return studentDocumentAuditRepository.save(tm);
	}

	public void deactivate(Integer id) {
		StudentDocumentAudit co = studentDocumentAuditRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Student Document Audit Not Found:" + id));
		studentDocumentAuditRepository.deactivate(id);
	}

	public void activate(Integer id) {
		StudentDocumentAudit co = studentDocumentAuditRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Student Document Audit Not Found:" + id));
		studentDocumentAuditRepository.activate(id);
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> oc_filtered_response = studentDocumentAuditRepository.getAllDataFilteredByKeyword(pageable,
				keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> oc_sorted_response = studentDocumentAuditRepository.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_sorted_response);
	}
}
