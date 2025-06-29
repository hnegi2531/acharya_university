package com.au.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.AcademicSchoolVision;
import com.au.model.Department;
import com.au.repository.AcademicSchoolVisionRepository;
import com.au.response.ResponseHandler;

@Service
public class AcademicSchoolVisionService {

	@Autowired
	private AcademicSchoolVisionRepository asv_repo;
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		
		Page<Object> response1 = asv_repo.findAll1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		
		Page<Object> response = asv_repo.findAll2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public AcademicSchoolVision saveAcademicSchoolVision(AcademicSchoolVision s) {
		return asv_repo.save(s);
	}

	public AcademicSchoolVision get(Integer id) {
		return asv_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("AcademicSchoolVision Not Found:" + id));
	}

	public ResponseEntity<Object> delete(Integer id) {
		AcademicSchoolVision cc = asv_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("AcademicSchoolVision Not Found:" + id));
		asv_repo.updateStd_subjects(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}

	public ResponseEntity<Object> delete1(Integer id) {
		AcademicSchoolVision cc = asv_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("AcademicSchoolVision Not Found:" + id));
		asv_repo.updateStd_subjects1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;

	}

	
	public List<AcademicSchoolVision> listAll1() {
		return asv_repo.findAll11();
	}
	
}
