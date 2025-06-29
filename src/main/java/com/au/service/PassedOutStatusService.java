package com.au.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.PassedOutStatus;
import com.au.repository.PassedOutStatusRepository;
import com.au.response.ResponseHandler;

@Service
public class PassedOutStatusService {

	@Autowired
	private PassedOutStatusRepository posr_repo;

	public List<PassedOutStatus> listAll() {
		return posr_repo.findAll1();
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		
		Page<Object> pos_filtered_response = posr_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, pos_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> pos_sorted_response = posr_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, pos_sorted_response);
	}

	public PassedOutStatus savePassedOutStatus(PassedOutStatus p) throws Exception {
		return posr_repo.save(p);
	}

	public PassedOutStatus get(Integer id) {
		return posr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("PassedOutStatus Id Not Found:" + id));
	}

	public void delete(Integer id) {
		PassedOutStatus ay = posr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("PassedOutStatus Id Not Found:" + id));
		posr_repo.update(id);
	}

	public void delete1(Integer id) {
		PassedOutStatus ay = posr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("PassedOutStatus Id Not Found:" + id));
		posr_repo.update1(id);
	}

}
