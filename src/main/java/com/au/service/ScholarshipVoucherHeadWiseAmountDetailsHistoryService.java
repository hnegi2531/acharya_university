package com.au.service;

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
import com.au.model.ScholarshipVoucherHeadWiseAmountDetailsHistory;
import com.au.repository.ScholarshipVoucherHeadWiseAmountDetailsHistoryRepository;
import com.au.response.ResponseHandler;

@Service
public class ScholarshipVoucherHeadWiseAmountDetailsHistoryService {
	
	@Autowired
	private ScholarshipVoucherHeadWiseAmountDetailsHistoryRepository svhwad_repo;
	
	public List<ScholarshipVoucherHeadWiseAmountDetailsHistory> saveScholarshipVoucherHeadWiseAmountDetailsHistory(@Valid List<ScholarshipVoucherHeadWiseAmountDetailsHistory> svhwad){
		return svhwad_repo.saveAll(svhwad);
	}
	
	public List<HashMap<String,Object>> scholarshipHeadWiseAmountDetailsOnScholarshipId(Integer scholarship_id){
		return svhwad_repo.scholarshipHeadWiseAmountDetailsOnScholarshipId(scholarship_id);
	}
	
	public List<HashMap<String,Object>> scholarshipHeadWiseAmountDetailsOnVocherHeadNewId(Integer scholarship_id,Integer voucher_head_new_id){
		return svhwad_repo.scholarshipHeadWiseAmountDetailsOnVocherHeadNewId(scholarship_id,voucher_head_new_id);
		
	}
	
	public void deactivateScholarshipHeadWiseAmountDetails(Integer id) {
		ScholarshipVoucherHeadWiseAmountDetailsHistory sas = svhwad_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ScholarshipVoucherHeadWiseAmountDetailsHistory Not Found:" + id));
		svhwad_repo.update(id);
	}

	public void activateScholarshipHeadWiseAmountDetails(Integer id) {
		ScholarshipVoucherHeadWiseAmountDetailsHistory sas = svhwad_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ScholarshipVoucherHeadWiseAmountDetailsHistory Not Found:" + id));
		svhwad_repo.update1(id);
	}
	
	public ScholarshipVoucherHeadWiseAmountDetailsHistory get(Integer id) {
        return svhwad_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("ScholarshipVoucherHeadWiseAmountDetailsHistory Not Found:"+id));
    }
	
	public List<ScholarshipVoucherHeadWiseAmountDetailsHistory> saveScholarshipVoucherHeadWiseAmountDetailsHistory1(List<ScholarshipVoucherHeadWiseAmountDetailsHistory> p) {
		return svhwad_repo.saveAll(p);
	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = svhwad_repo.findAll1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}


	public ResponseEntity<Object> listAll2(Pageable pageable1) {
		Page<Object> response = svhwad_repo.findAll2(pageable1);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

}
