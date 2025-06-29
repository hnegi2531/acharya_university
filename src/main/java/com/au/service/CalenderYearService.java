package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.CalenderYear;
import com.au.repository.CalenderYearRepository;
import com.au.response.ResponseHandler;

@Service
public class CalenderYearService {

	@Autowired
	private CalenderYearRepository calender_year_repo;
	
	public List<CalenderYear> listAll() {
		return calender_year_repo.findAll1();
	}
	
//	public List<CalenderYear> listAll1() {
//		return calender_year_repo.findAll();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = calender_year_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = calender_year_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
		
	}

	public CalenderYear saveCalenderYear(CalenderYear s) {
		
		if(calender_year_repo.countOfCalenderYear(s.getCalender_year())>=1) {
				throw new RuntimeException("Calender Year Already Exist");
		}else {
				return calender_year_repo.save(s);
		}	

	}
	
	public CalenderYear saveCalenderYear1(CalenderYear s) {
		return calender_year_repo.save(s);

	}

	public CalenderYear get(Integer id) {
		return calender_year_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Calender Year Not Found:" + id));
	}

	public void delete(Integer id) {
		CalenderYear cc = calender_year_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Calender Year Not Found:" + id));
		calender_year_repo.updateCalenderYear(id);
	}

	public void delete1(Integer id) {
		CalenderYear cc = calender_year_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Calender Year Not Found:" + id));
		calender_year_repo.updateCalenderYear1(id);

	}
}
