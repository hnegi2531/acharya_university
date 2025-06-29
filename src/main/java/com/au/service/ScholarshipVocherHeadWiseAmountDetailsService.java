package com.au.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.PreAdmissionProcess;
import com.au.model.Scholarship;
import com.au.model.ScholarshipVocherHeadWiseAmountDetails;
import com.au.repository.ScholarshipVocherHeadWiseAmountDetailsRepository;

@Service
public class ScholarshipVocherHeadWiseAmountDetailsService {

	
	@Autowired
	private ScholarshipVocherHeadWiseAmountDetailsRepository svhwad_repo;
	
	public List<ScholarshipVocherHeadWiseAmountDetails> saveScholarshipVocherHeadWiseAmountDetails(List<ScholarshipVocherHeadWiseAmountDetails> svhwad){
		return svhwad_repo.saveAll(svhwad);
	}
	
	public List<HashMap<String,Object>> scholarshipHeadWiseAmountDetailsOnScholarshipId(Integer scholarship_id){
		return svhwad_repo.scholarshipHeadWiseAmountDetailsOnScholarshipId(scholarship_id);
	}
	
	public List<HashMap<String,Object>> scholarshipHeadWiseAmountDetailsOnVocherHeadNewId(Integer scholarship_id,Integer voucher_head_new_id){
		return svhwad_repo.scholarshipHeadWiseAmountDetailsOnVocherHeadNewId(scholarship_id,voucher_head_new_id);
		
	}
	
	public void deactivateScholarshipHeadWiseAmountDetails(Integer id) {
		ScholarshipVocherHeadWiseAmountDetails sas = svhwad_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ScholarshipVocherHeadWiseAmountDetails Not Found:" + id));
		svhwad_repo.update(id);
	}

	public void activateScholarshipHeadWiseAmountDetails(Integer id) {
		ScholarshipVocherHeadWiseAmountDetails sas = svhwad_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ScholarshipVocherHeadWiseAmountDetails Not Found:" + id));
		svhwad_repo.update1(id);
	}
	
	public ScholarshipVocherHeadWiseAmountDetails get(Integer id) {
        return svhwad_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("ScholarshipVocherHeadWiseAmountDetails Not Found:"+id));
    }
	
	public List<ScholarshipVocherHeadWiseAmountDetails> saveScholarshipVocherHeadWiseAmountDetails1(List<ScholarshipVocherHeadWiseAmountDetails> p) {
		return svhwad_repo.saveAll(p);
	}

	public List<HashMap<String, Object>> scholarshipHeadWiseAmountDetailsOnStudentId(Integer student_id) {
		return svhwad_repo.scholarshipHeadWiseAmountDetailsOnStudentId(student_id);
	}
	
}
