package com.au.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.SalaryStructureDetails;
import com.au.repository.SalaryStructureDetailsRepository;
import com.au.response.ResponseHandler;

@Service
public class SalaryStructureDetailsService {

	@Autowired
	private SalaryStructureDetailsRepository ssdr_repo;

	public List<SalaryStructureDetails> listAll() {
		return ssdr_repo.findAll1();
	}

//	public List<HashMap<String, Object>> listAll1() {
//		return ssdr_repo.findAlll();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = ssdr_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		
		Page<Object> response = ssdr_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
		
	}

	public SalaryStructureDetails get(Integer id) {
		return ssdr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SalaryStructureDetails Id Not Found:" + id));
	}

	public void delete(Integer id) {
		SalaryStructureDetails ay = ssdr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SalaryStructureDetails Id Not Found:" + id));
		ssdr_repo.update(id);
	}

	public void delete1(Integer id) {
		SalaryStructureDetails ay = ssdr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SalaryStructureDetails Id Not Found:" + id));
		ssdr_repo.update1(id);
	}

	public List<HashMap<String, Object>> getAllDetails(Integer salary_structure_id) {
		return ssdr_repo.getFormulaDetails(salary_structure_id);
	}

	public SalaryStructureDetails saveSalaryStructureDetail(SalaryStructureDetails bs) {
		return ssdr_repo.save(bs);
	}

	public Object getSalaryStructureId(Integer salary_structure_id, Integer salary_structure_head_id) {
		SalaryStructureDetails salaryStructureDetails = ssdr_repo.getAllDeatils(salary_structure_id,salary_structure_head_id);
		if (salaryStructureDetails != null) {
			salaryStructureDetails.setTo_date(LocalDate.now().minusMonths(1).getMonth() + "," + LocalDate.now().getYear());
		      ssdr_repo.updateActive(salary_structure_id,salary_structure_head_id);
		   // ssdr_repo.updateActive1(salary_structure_id,salary_structure_head_id);	    
			// salaryStructureDetails.setTo_date(YearMonth.now().minus(Period.ofMonths(1)));
		}
		return salaryStructureDetails;

	}

	public SalaryStructureDetails saveSalaryStructureDetails(@Valid SalaryStructureDetails bs) {		
			getSalaryStructureId(bs.getSalary_structure_id(), bs.getSalary_structure_head_id());
			return ssdr_repo.save(bs);
		}

	public Object getAllDetailsssssss(Integer salary_structure_id,Integer salary_structure_head_id) {
		System.out.println("++++++++++++++++"+salary_structure_id+"**********"+salary_structure_head_id);
		return ssdr_repo.savesssss(salary_structure_id,salary_structure_head_id);
	}
}
