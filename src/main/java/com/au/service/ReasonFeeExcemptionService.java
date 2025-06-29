package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.ProgramTranscriptDetails;
import com.au.model.ProgramType;
import com.au.model.ReasonFeeExcemption;
import com.au.repository.ReasonFeeExcemptionRepository;
import com.au.response.ResponseHandler;

@Service
public class ReasonFeeExcemptionService {

	@Autowired
	private ReasonFeeExcemptionRepository r_repo;
	
	
//	public List<ReasonFeeExcemption> listAll(){
//		return r_repo.findAll();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword){
		
		Page<Object> response1 = r_repo.findAll1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable){
		
		Page<Object> response = r_repo.findAll2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public ReasonFeeExcemption save_ReasonFeeExcemption(ReasonFeeExcemption r) throws Exception {
		if(r_repo.countReasonFeeExcemption(r.getReasion_for_fee_exemption()) >= 1) {
			throw new Exception("Reason Fee Excemption Name Already Exist");
		} else {
			return r_repo.save(r);
		}
	}
	
	public ReasonFeeExcemption updateReasonFeeExcemption(ReasonFeeExcemption r) throws Exception {
		if(r_repo.countReasonFeeExcemptionForUpdate(r.getFee_exemption_id(),r.getReasion_for_fee_exemption()) >= 1) {
			throw new Exception("Reason Fee Excemption Name Already Exist");
		} else {
			return r_repo.save(r);
		}
	}
	
	public List<ReasonFeeExcemption> listAllActiveDetails() {
		return r_repo.listAllActiveDetails();
	}
	
	public ReasonFeeExcemption get(Integer id) {
        return r_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("ReasonFeeExcemption Not Found:"+id));
    }
     
    public void delete(Integer id) {
    	ReasonFeeExcemption cc = r_repo.findById(id)
    	.orElseThrow(()-> new ResourceNotFoundException("ReasonFeeExcemption Not Found:"+id));    	
    	r_repo.updateReasonFeeExcemption(id);
    }

	public void delete1(Integer id) {
		ReasonFeeExcemption cc = r_repo.findById(id)
		    	.orElseThrow(()-> new ResourceNotFoundException("ReasonFeeExcemption Not Found:"+id));  
		r_repo.updateReasonFeeExcemption1(id);
		
	}
}
