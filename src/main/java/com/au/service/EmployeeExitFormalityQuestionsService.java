package com.au.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.EmployeeExitFormalityQuestions;
import com.au.repository.EmployeeExitFormalityQuestionsRepository;
import com.au.response.ResponseHandler;

@Service
public class EmployeeExitFormalityQuestionsService {

	@Autowired
	private EmployeeExitFormalityQuestionsRepository eefqrepo;
	
	public EmployeeExitFormalityQuestions save_EmployeeExitFormalityQuestions(EmployeeExitFormalityQuestions eefq) throws Exception {

		if (getQuestions(eefq.getQuestion()) >= 1)
			throw new Exception("Employee Exit Formality Question already exist");
		
		 else {
			 eefqrepo.save(eefq);
		}
		return eefq;
	}
	
	private Integer getQuestions(String question) {
		return eefqrepo.getcountOfquestion(question);
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> roles_filtered_response = eefqrepo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> roles_sorted_response = eefqrepo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
	}
	
	public List<EmployeeExitFormalityQuestions> listAll1() {
		return eefqrepo.findAll1();
	}
	
	public EmployeeExitFormalityQuestions get(Integer id) {
		return eefqrepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee Exit Formality Question  Not Found:" + id));
	}

	public EmployeeExitFormalityQuestions saveEmployeeExitFormalityQuestions(EmployeeExitFormalityQuestions eEFQ) {
		return eefqrepo.save(eEFQ);
	}
	
	public void delete(Integer id) {
		EmployeeExitFormalityQuestions eefq = eefqrepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Department Not Found:" + id));
		eefqrepo.updateDept(id);
	}

	public void delete1(Integer id) {
		EmployeeExitFormalityQuestions eefq = eefqrepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Department Not Found:" + id));
		eefqrepo.updateDept1(id);
	}

	
	public List<Map<String,Object>> listAllData() {
		return eefqrepo.listAllData1();
	}
}
