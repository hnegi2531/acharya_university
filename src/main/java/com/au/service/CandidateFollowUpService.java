package com.au.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.au.model.CandidateFollowUp;
import com.au.repository.CandidateFollowUpRepository;
import com.au.response.ResponseHandler;



@Service
public class CandidateFollowUpService {
	
	@Autowired
	private CandidateFollowUpRepository can_repo;
	
	
	public CandidateFollowUp saveCandidateFollowUp(CandidateFollowUp cfu) throws Exception {
		
		return	can_repo.save(cfu);
		
	}
	
	public List<CandidateFollowUp> listAll() {
		return can_repo.findAll1();
	}
	
	public CandidateFollowUp get(Integer candidate_followup_id) {
		return can_repo.findById(candidate_followup_id).orElseThrow(() -> new ResourceNotFoundException("CandidateFollowUp Not Found:" + candidate_followup_id));
	}
	
	public void delete(Integer candidate_followup_id) {
		CandidateFollowUp cc = can_repo.findById(candidate_followup_id)
				.orElseThrow(() -> new ResourceNotFoundException("CandidateFollowUp Not Found:" + candidate_followup_id));
		can_repo.updateCandidateFollowUp(candidate_followup_id);
	}

	public void delete1(Integer candidate_followup_id) {
		CandidateFollowUp cc = can_repo.findById(candidate_followup_id)
				.orElseThrow(() -> new ResourceNotFoundException("CandidateFollowUp Not Found:" + candidate_followup_id));
		can_repo.updateCandidateFollowUp1(candidate_followup_id);

	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> response1 = can_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
        Page<Object> response = can_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public List<CandidateFollowUp> getData(Integer candidate_id) {
		return can_repo.getData(candidate_id);
	}
	
//	public List<CandidateFollowUp> getDataByFollowUpDate(String follow_up_date) {
//		System.out.println("((((((((((((((((((((())))))))))))))))))))))) : "+follow_up_date);
//		return can_repo.getDataByFollowUpDate(follow_up_date);
//	}
	
	public List<Map<String, Object>> fetchCandidateFollowUpData(Integer created_by, Date from_date, Date to_date) {
		List<Map<String, Object>> response1=null;
		if(created_by == null) {
			response1 = can_repo.fetchCandidateFollowUpData( from_date,to_date );
			
		}
		else {
			response1 = can_repo.fetchCandidateFollowUpData1(created_by, from_date,to_date );
		
		}
		return response1;
	}

	
	
//	public List<Map<String, Object>> fetchCandidateFollowUpData(Integer id, Date from_date, Date to_date) {
//		if(id==null) 
//			return can_repo.fetchCandidateFollowUpData(from_date,to_date);
//		
//		else
//			return can_repo.fetchCandidateFollowUpData1(from_date,to_date);
//	}

}

