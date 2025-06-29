package com.au.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.SalaryStructureCategory;
import com.au.repository.SalaryStructureCategoryRepository;
import com.au.response.ResponseHandler;

@Service
public class SalaryStructureCategoryService {

	@Autowired
	private SalaryStructureCategoryRepository sscr_repo;

	public List<SalaryStructureCategory> listAll() {
		return sscr_repo.findAll1();
	}
	
//	public List<SalaryStructureCategory> listAll1() {
//		return sscr_repo.findAll();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		
		Page<Object> response1 = sscr_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		
		Page<Object> response = sscr_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
		
	}

	public SalaryStructureCategory saveSalaryStructureCategory(SalaryStructureCategory salarystructurecategory) {
		return sscr_repo.save(salarystructurecategory);
	}

	public SalaryStructureCategory get(Integer id) {
		return sscr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SalaryStructureCategory Id Not Found:" + id));
	}

	public void delete(Integer id) {
		SalaryStructureCategory ay = sscr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SalaryStructureCategory Id Not Found:" + id));
		sscr_repo.update(id);
	}
	
	public void delete1(Integer id) {
		SalaryStructureCategory ay = sscr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SalaryStructureCategory Id Not Found:" + id));
		sscr_repo.update1(id);
	}

}