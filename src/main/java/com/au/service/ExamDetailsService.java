package com.au.service;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.au.model.ExamDetails;
import com.au.repository.ExamDetailsRepository;
import com.au.response.ResponseHandler;

@Service
public class ExamDetailsService {

	@Autowired
	private ExamDetailsRepository exam_details_repo;
	
	
	public ExamDetails saveExamDetails(ExamDetails ed) throws Exception {
		return	exam_details_repo.save(ed);
	}
	
	
	public List<ExamDetails> listAll1() {
		return exam_details_repo.findAll11();
	}

	
	public ExamDetails get(Integer id) {
		return exam_details_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Exam Details Not Found:" + id));
	}
	
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> exam_details_filtered_response = exam_details_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, exam_details_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> exam_details_response = exam_details_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, exam_details_response);
	}
	
	public ExamDetails updateExamDetails(ExamDetails ed) {
		return exam_details_repo.save(ed);
	}

	
	public void delete(Integer id) {
		ExamDetails ed = exam_details_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Exam Details Not Found:" + id));
		exam_details_repo.updateExamDetail(id);
	}

	public void delete1(Integer id) {
		ExamDetails ed = exam_details_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Exam Details Not Found:" + id));
		exam_details_repo.updateExamDetail1(id);
	}
	
	public List<Map<String,Object>> getExamCenterWithDate() {	
		return exam_details_repo.getExamCenterWithDate();
	}
	
	public List<Map<String,Object>> getExamDetailsForHallTicketGeneration(){
		return exam_details_repo.getExamDetailsForHallTicketGeneration();
	}
}
