package com.au.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.SalaryStructureHead;
import com.au.repository.SalaryStructureHeadRepository;
import com.au.response.ResponseHandler;

@Service
public class SalaryStructureHeadService {

	@Autowired
	private SalaryStructureHeadRepository sshr_repo;

	public List<HashMap<String, Object>> listAll() {
		return sshr_repo.findAll1();
	}

//	public List<SalaryStructureHead> listAll1() {
//		return sshr_repo.findAll();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		
		Page<Object> response1 = sshr_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = sshr_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
		
	}

	public SalaryStructureHead saveSalaryStructureHead(SalaryStructureHead salarystructurehead) throws Exception {
		if(sshr_repo.getCountSalaryStructureHead(salarystructurehead.getVoucher_head_new_id()) >= 1) {
			throw new Exception("Salary Structure Head or Vocher Head Already Exist");
		} else if(sshr_repo.getCountPriority(salarystructurehead.getPriority()) >= 1) {
			throw new Exception("Priority Already Exist");
		} else {
		return sshr_repo.save(salarystructurehead);
		}
	}
	
	public SalaryStructureHead saveSalaryStructureHead1(SalaryStructureHead salarystructurehead) {
		return sshr_repo.save(salarystructurehead);
	}

	public SalaryStructureHead get(Integer id) {
		return sshr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SalaryStructureHead Id Not Found:" + id));
	}

	public void delete(Integer id) {
		SalaryStructureHead ay = sshr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SalaryStructureHead Id Not Found:" + id));
		sshr_repo.update(id);
	}

	public void delete1(Integer id) {
		SalaryStructureHead ay = sshr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SalaryStructureHead Id Not Found:" + id));
		sshr_repo.update1(id);
	}

	public List<HashMap<String, Object>> getPrintName(Integer salary_structure_head_id) {
		return sshr_repo.getPrintNames(salary_structure_head_id);
	}
	
	public List<HashMap<String, Object>> categoryNameType(Integer voucher_head_new_id) {
		return sshr_repo.categoryNameTypes(voucher_head_new_id);
	}
	
	public List<HashMap<String, Object>> allSalaryStructureHeadAndVocherHeadDetails() {
		return sshr_repo.allSalaryStructureHeadAndVocherHeadDetails();
	}
	
	public List<HashMap<String, Object>> listAll4(Integer salary_structure_id) {
		return sshr_repo.findAll4(salary_structure_id);
	}

}
