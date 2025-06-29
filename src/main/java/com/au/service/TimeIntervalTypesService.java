package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.TimeIntervalTypes;
import com.au.repository.TimeIntervalTypesRepository;
import com.au.response.ResponseHandler;

@Service
public class TimeIntervalTypesService {

	@Autowired
	private TimeIntervalTypesRepository s_repo;

	public List<TimeIntervalTypes> listAll() {
		return s_repo.findAll1();
	}
	
//	public List<TimeIntervalTypes> listAll1() {
//		return s_repo.findAll();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = s_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = s_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public TimeIntervalTypes saveTimeIntervalTypes(TimeIntervalTypes s) {

		if (s_repo.existsByIntervalTypeName(s.getIntervalTypeName())) {
			throw new RuntimeException("Name is already taken!!");
		}
		if (s_repo.existsByIntervalTypeShort(s.getIntervalTypeShort())) {
			throw new RuntimeException("Interval Short Name is already taken!!");
		}
		return s_repo.save(s);

	}
	
	public TimeIntervalTypes updateTimeIntervalTypes(TimeIntervalTypes s) {
		return s_repo.save(s);

	}

	public TimeIntervalTypes get(Integer id) {
		return s_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("TimeIntervalTypes Not Found:" + id));
	}

	public void delete(Integer id) {
		TimeIntervalTypes cc = s_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("TimeIntervalTypes Not Found:" + id));
		s_repo.updateStd_subjects(id);
	}

	public void delete1(Integer id) {
		TimeIntervalTypes cc = s_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("TimeIntervalTypes Not Found:" + id));
		s_repo.updateStd_subjects1(id);

	}
	
	public List<TimeIntervalTypes> getTimeIntervalTypesInSectionDropdown() {
		return s_repo.getTimeIntervalTypesInSectionDropdown();
	}
	
	public List<TimeIntervalTypes> getTimeIntervalTypesInBatchDropdown() {
		List<TimeIntervalTypes> intervalTypes = s_repo.getTimeIntervalTypesInBatchDropdown();
		return intervalTypes;
	}

}
