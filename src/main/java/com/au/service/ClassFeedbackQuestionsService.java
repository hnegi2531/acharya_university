package com.au.service;

import com.au.model.ClassFeedbackQuestions;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.repository.ClassFeedbackQuestionsRepository;
import com.au.response.ResponseHandler;

@Service
public class ClassFeedbackQuestionsService {

	@Autowired
	private ClassFeedbackQuestionsRepository feedback_que_repo;
	
	
	
	public ClassFeedbackQuestions save_clsfeedbackque(ClassFeedbackQuestions feedbackque) throws Exception {
		return	feedback_que_repo.save(feedbackque);
		}
	
	
	public List<ClassFeedbackQuestions> listAll1() {
		return feedback_que_repo.findAll11();
	}
	
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> classFeedbackQuestions_filtered_response = feedback_que_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, classFeedbackQuestions_filtered_response);
	}

	
	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> classFeedbackQuestions_sorted_response = feedback_que_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, classFeedbackQuestions_sorted_response);
	}
	
	
	public ClassFeedbackQuestions get(Integer id) {
		return feedback_que_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("class Feedback Questions Not Found:" + id));
	}
	
	
	public ClassFeedbackQuestions saveClassFeedbackQuestions(ClassFeedbackQuestions feedbackque) {
		return feedback_que_repo.save(feedbackque);
	}
	
	
	public void delete(Integer id) {
		ClassFeedbackQuestions feedbackque = feedback_que_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("class Feedback Questions Not Found:" + id));
		feedback_que_repo.delete1(id);
	}

	public void delete1(Integer id) {
		ClassFeedbackQuestions feedbackque = feedback_que_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("class Feedback Questions Not Found:" + id));
		feedback_que_repo.delete2(id);
	}
	
	public List<ClassFeedbackQuestions> getClassFeedbackQuestions(Integer school_id) {
		return feedback_que_repo.getClassFeedbackQuestions(school_id);
	}
}
