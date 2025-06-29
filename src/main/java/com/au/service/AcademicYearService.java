package com.au.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.Academic_year;
import com.au.repository.Academic_year_repository;
import com.au.response.ResponseHandler;

@Service
public class AcademicYearService {

	@Autowired
	private Academic_year_repository ac_repo;

	public List<Academic_year> listAll() {
		return ac_repo.findAll1();
	}
	
	public List<Academic_year> academicYearGT() {
		return ac_repo.academicYearGT();
	}

	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		//return ac_repo.findAll11();
		Page<Object> response1 = ac_repo.findAll11(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		//return ac_repo.findAll12();
		Page<Object> response = ac_repo.findAll12(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public Academic_year save_Academic_Year(Academic_year academic) throws Exception {
		if(ac_repo.countOfAcYear(academic.getAc_year())>=1) {
			throw new Exception("Academic year Already Exist");
		}else if(ac_repo.countOfCurrentYear(academic.getCurrent_year())>=1) {
			throw new Exception("Current year Already Exist");
		}else{
			return ac_repo.save(academic);
		}
	}

	public Academic_year get(Integer id) {
		return ac_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Academic Year Not Found:" + id));
	}

	public ResponseEntity<Object> delete(Integer id) {
		Academic_year ay = ac_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Academic Year Not Found:" + id));
		ac_repo.update(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}

	public ResponseEntity<Object> delete1(Integer id) {
		Academic_year ay = ac_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Academic Year Not Found:" + id));
		ac_repo.update1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}

	public List<Academic_year> getAcademicYearByACYearId() {
		return ac_repo.findByAcYearId();
	}

	public List<Academic_year> findByAcYearId1() {
		return ac_repo.findByAcYearId1();
	}

	public Integer countRecords() {
		// TODO Auto-generated method stub
		return ac_repo.countRecords();
	}

	public Academic_year save_Academic_Year1(Academic_year ac_year) {
		return ac_repo.save(ac_year);

	}
}
