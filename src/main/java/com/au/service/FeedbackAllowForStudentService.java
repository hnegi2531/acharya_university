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

import com.au.model.AcademicsProgramVision;
import com.au.model.FeedbackAllowForStudent;
import com.au.model.MealBill;
import com.au.repository.FeedbackAllowForStudentRepository;
import com.au.repository.MealBillRepository;
import com.au.response.ResponseHandler;

@Service
public class FeedbackAllowForStudentService {
	
	@Autowired
	private FeedbackAllowForStudentRepository feedbackAllowForStudentRepository;


	public List<FeedbackAllowForStudent> createFeedbackAllowForStudent(@Valid List<FeedbackAllowForStudent> feedbackAllowForStudent) throws Exception {
		return	feedbackAllowForStudentRepository.saveAll(feedbackAllowForStudent);
	}


	public List<FeedbackAllowForStudent> getAllActiveFeedbackAllowForStudent() {
		return feedbackAllowForStudentRepository.getAllActiveFeedbackAllowForStudent();
	}
	
	
	public ResponseEntity<Object> fetchAllFeedbackAllowForStudentDetails(Pageable pageable, Object keyword) {
		Page<Map<String, Object>> response1 = feedbackAllowForStudentRepository.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex1(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> fetchAllFeedbackAllowForStudentDetails1(Pageable pageable) {
		Page<Map<String, Object>> response = feedbackAllowForStudentRepository.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex1(true, HttpStatus.OK, response);
	}
	
}