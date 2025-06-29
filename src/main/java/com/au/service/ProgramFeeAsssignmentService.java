package com.au.service;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.au.model.ProgramFeeAsssignment;
import com.au.repository.ProgramFeeAsssignmentRepository;
import com.au.response.ResponseHandler;

@Service
public class ProgramFeeAsssignmentService {

	
	@Autowired
	private ProgramFeeAsssignmentRepository pfa_repo;
	
	
   public ProgramFeeAsssignment saveProgramFeeAsssignment(ProgramFeeAsssignment pfa) throws Exception {
		
	   if (getCountOfProgramAndAc_year(pfa.getAc_year_id(),pfa.getProgram_id()) >= 1)
			throw new Exception("Combination of Ac_year_id and Program_id is already exist");
		 else {
			pfa_repo.save(pfa);
		}
		return pfa;
	}
	   
    private Integer getCountOfProgramAndAc_year(Integer ac_year_id,Integer program_id) {
		return pfa_repo.getCountOfProgramAndAc_year(ac_year_id,program_id);
	}
    
  
	   
	  
   public List<ProgramFeeAsssignment> listAll1() {
		return pfa_repo.findAll11();
	}
	
   


	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		
		Page<Object> pfa_filtered_response = pfa_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, pfa_filtered_response);
		}

	
	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> pfa_sorted_response = pfa_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, pfa_sorted_response);
	}
	
	public ProgramFeeAsssignment updateProgramFeeAssignment(ProgramFeeAsssignment pfa) {
		
		if ((pfa_repo.getCountOfProgramAndAc_year1(pfa.getprogram_fee_asssignment_id(),pfa.getAc_year_id(),pfa.getProgram_id()) >= 1)){
			throw new RuntimeException("Combination of Ac_year_id and Program_id is already exist !!!");
		}
		
		else {
			return pfa_repo.save(pfa);
		}
	}
	
	
	public ProgramFeeAsssignment get(Integer id) {
	    return pfa_repo.findById(id)
	    		.orElseThrow(()-> new ResourceNotFoundException("Program Fee Asssignment Id Not Found:"+id));
		}
	
	
	
	public void deactivate(Integer id) {
		ProgramFeeAsssignment pfa = pfa_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Program Fee Asssignment Id Not Found:" + id));
		pfa_repo.updateToDeactive(id);
	
		
	}

	public void activate(Integer id) {
		ProgramFeeAsssignment pfa = pfa_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Program Fee Asssignment Id Not Found:" + id));
		pfa_repo.updateToActive(id);
		
	}
	
	public List<Map<String,Object>> getProgramConcat() {	
		return pfa_repo.getProgramConcat();
	}
}
