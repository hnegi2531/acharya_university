package com.au.service;

import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.Offer;
import com.au.model.SalaryStructure;
import com.au.repository.OfferRepository;
import com.au.repository.SalaryStructureRepository;
import com.au.response.ResponseHandler;

@Service
public class SalaryStructureService {
	
	@Autowired
	private SalaryStructureRepository ssr_repo;
	
	@Autowired
	private OfferRepository or_repo;

	public List<SalaryStructure> listAll() {
		return ssr_repo.findAll1();
	}
	
//	public List<SalaryStructure> listAll1() {
//		return ssr_repo.findAll();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		
		Page<Object> response1 = ssr_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		
		Page<Object> response = ssr_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public SalaryStructure saveSalaryStructure(SalaryStructure salarystructure) {
		if(ssr_repo.countOfSalaryStructure(salarystructure.getSalary_structure())>=1) {
			throw new RuntimeException("Salary Structure Name Already Exist");
		}else if(ssr_repo.countOfPrintName(salarystructure.getPrint_name())>=1) {
			throw new RuntimeException("Print Name Already Exist");
		}else {
			return ssr_repo.save(salarystructure);
		}
	}
	
	public SalaryStructure updateSalaryStructure(SalaryStructure salarystructure) {
		if(ssr_repo.countOfSalaryStructureForUpdate(salarystructure.getSalary_structure_id(),salarystructure.getSalary_structure())>=1) {
			throw new RuntimeException("Salary Structure Name Already Exist");
		}else if(ssr_repo.countOfPrintNameForUpdate(salarystructure.getSalary_structure_id(),salarystructure.getPrint_name())>=1) {
			throw new RuntimeException("Print Name Already Exist");
		}else {
			return ssr_repo.save(salarystructure);
		}
	}

	public SalaryStructure get(Integer id) {
		if (id.equals(0)) {
			throw new RuntimeException("Opps Exception raised....");
		}
		return ssr_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("SalaryStructure id Not Found:" + id));
	}

	public void delete(Integer salary_structure_id) {
		SalaryStructure ay = ssr_repo.findById(salary_structure_id).orElseThrow(() -> new ResourceNotFoundException("SalaryStructure id Not Found:" + salary_structure_id));
		ssr_repo.update(salary_structure_id);
	}
	
	public void delete1(Integer salary_structure_id) {
		SalaryStructure ay = ssr_repo.findById(salary_structure_id).orElseThrow(() -> new ResourceNotFoundException("SalaryStructure id Not Found:" + salary_structure_id));
		ssr_repo.update1(salary_structure_id);
	}
	
	public Object checkSalaryStructureIdInOffer(Integer salary_structure_id) {
		Predicate<Integer> p = i -> (or_repo.getCountOfSalaryStructureId(salary_structure_id) >= 1);
		if(p.test(salary_structure_id))
			return "Id Already Assigned. Don't Allow to Edit.";
		else
			 throw new ResourceNotFoundException("SalaryStructure id Not Found:" + salary_structure_id);

		
//		if(or_repo.getCountOfSalaryStructureId(salary_structure_id) >= 1)
//			throw new RuntimeException("Opps!!! Updation Not Allowed.");
//		else
//		 return ssr_repo.findById(salary_structure_id).
//				 orElseThrow(() -> new ResourceNotFoundException("SalaryStructure id Not Found:" + salary_structure_id));
	}

}