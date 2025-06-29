package com.au.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.AcademicSchoolVision;
import com.au.model.AcademicsProgramVision;
import com.au.model.CourseObjective;
import com.au.repository.AcademicsProgramVisionRepository;
import com.au.response.ResponseHandler;

@Service
public class AcademicsProgramVisionService {

	@Autowired
	private AcademicsProgramVisionRepository apv_repo;
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = apv_repo.findAll1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = apv_repo.findAll2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
		
	}

	public List<AcademicsProgramVision> saveAcademicsProgramVision1(@Valid List<AcademicsProgramVision> apv) throws Exception {
		return	apv_repo.saveAll(apv);
	}
	

	public AcademicsProgramVision get(Integer id) {
		return apv_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("AcademicsProgramVision Not Found:" + id));
	}
	
	public AcademicsProgramVision saveAcademicsProgramVision2(AcademicsProgramVision apvs) {
		return apv_repo.save(apvs);
	}
	

	public ResponseEntity<Object> delete(Integer id) {
		AcademicsProgramVision cc = apv_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("AcademicsProgramVision Not Found:" + id));
		apv_repo.updateStd_subjects(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}

	public ResponseEntity<Object> delete1(Integer id) {
		AcademicsProgramVision cc = apv_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("AcademicsProgramVision Not Found:" + id));
		apv_repo.updateStd_subjects1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;

	}
	
	
	public List<AcademicsProgramVision> listAll1() {
		return apv_repo.findAll11();
	}
	

	
}
