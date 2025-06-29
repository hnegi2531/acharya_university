package com.au.service;

import java.util.List;

import javax.validation.Valid;

import com.au.response.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.repository.StoreIndentRequestHistoryRepository;

import com.au.model.StoreIndentRequestHistory;

@Service
public class StoreIndentRequestHistoryService {

	@Autowired
	private StoreIndentRequestHistoryRepository sir_history_repo;
	
	
	
	public List<StoreIndentRequestHistory> saveStoreIndentRequest(@Valid List<StoreIndentRequestHistory> sir) {
		return sir_history_repo.saveAll(sir);
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword1(Pageable pageable, Object keyword, Integer requested_by) {
		Page<Object> exam_details_filtered_response = sir_history_repo.getAllDataFilteredByKeyword1(pageable, keyword ,requested_by);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, exam_details_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData1(Pageable pageable ,Integer requested_by) {
		Page<Object> exam_details_response = sir_history_repo.getAllSortedData1(pageable ,requested_by);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, exam_details_response);
	}
}
