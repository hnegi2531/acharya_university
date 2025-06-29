package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.Court;
import com.au.repository.CourtRepository;
import com.au.response.ResponseHandler;

@Service
public class CourtService {
	
	
	@Autowired
	private CourtRepository court_repo;
	
	public Court saveCourt(Court court) {
		if(court_repo.countOfCourtName(court.getCourt_name())>=1) {
			throw new RuntimeException("Court Name Already Exist");
		}else if(court_repo.countOfCourtShortName(court.getCourt_short_name())>=1) {
			throw new RuntimeException("Short Name Already Exist");
		}else {
			return court_repo.save(court);
		}
	}
	
	public List<Court> getActiveDetails() {
		return court_repo.findAll1();
	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = court_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = court_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public Court getDetailById(Integer court_id) {
		return court_repo.findById(court_id).orElseThrow(() -> new ResourceNotFoundException("Court Not Found:" + court_id));
	}
	
	public Court updateDetailById(Court court) {
		return court_repo.save(court);

	}
	
	public void deactivate(Integer court_id) {
		court_repo.findById(court_id)
				.orElseThrow(() -> new ResourceNotFoundException("Court Not Found:" + court_id));
		court_repo.deactivate(court_id);
	}
	
	public void activate(Integer court_id) {
		court_repo.findById(court_id)
				.orElseThrow(() -> new ResourceNotFoundException("Court Not Found:" + court_id));
		court_repo.activate(court_id);
	}

}
