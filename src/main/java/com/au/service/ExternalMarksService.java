package com.au.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.au.model.ExternalMarks;
import com.au.repository.CourseAssignmentRepository;
import com.au.repository.ExternalMarksRepository;
import com.au.response.ResponseHandler;

@Service
public class ExternalMarksService {

	@Autowired
	private ExternalMarksRepository externalMarksRepository;
	
	@Autowired
	private CourseAssignmentRepository courseAssignmentRepo;
	
	
	public List<ExternalMarks> saveExternalMarks(@Valid List<ExternalMarks> ademail) {
		return	externalMarksRepository.saveAll(ademail);
	}
	
	public List<ExternalMarks> getAllActiveExternalMarks() {
		return externalMarksRepository.getAllActiveExternalMarks();
	}


	public ExternalMarks get(Integer id) {
	return externalMarksRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("External Marks Not Found:" + id));
	}
	
	
	public ExternalMarks updateExternalMarks(ExternalMarks con) {
		return externalMarksRepository.save(con);
	}


	public void deactivate(Integer id) {
		ExternalMarks con = externalMarksRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("External Marks Not Found:" + id));
		externalMarksRepository.deactivate(id);
	}
	
	public void activate(Integer id) {
		ExternalMarks con = externalMarksRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("External Marks Not Found:" + id));
		externalMarksRepository.activate(id);
	}
	
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> oc_filtered_response = externalMarksRepository.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> oc_sorted_response = externalMarksRepository.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_sorted_response);
	}

	public List<Map<String, Object>> courseConcateWithName(Integer ac_year_id) {
		return courseAssignmentRepo.courseConcateWithName(ac_year_id);
	}
}
