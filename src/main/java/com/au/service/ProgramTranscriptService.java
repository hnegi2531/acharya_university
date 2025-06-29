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
import com.au.model.ProgramTranscript;
import com.au.repository.ProgramTranscriptDetailsRepository;
import com.au.repository.ProgramTranscriptRepository;
import com.au.response.ResponseHandler;

@Service
public class ProgramTranscriptService {
	
	@Autowired
	private ProgramTranscriptRepository trans_repo;
	
	@Autowired
	private ProgramTranscriptDetailsRepository p_repo;
	
	
	public ProgramTranscript saveProgramTranscript(ProgramTranscript prog) throws Exception {
		if(trans_repo.getCountTranscript(prog.getTranscript()) >= 1) {
			throw new Exception("Transcript Already Exist");
		} else if(trans_repo.getCountTranscriptShortName(prog.getTranscript_short_name()) >= 1) {
			throw new Exception("Short Name Already Exist");
		} else if(trans_repo.getCountPriority(prog.getPriority()) >= 1) {
			throw new Exception("Priority Already Exist");
		} else {
			 trans_repo.save(prog);
		}
		return prog;
	}

		public List<ProgramTranscript> listAll1() {
			return trans_repo.findAll1();
		}
		
//		public List<ProgramTranscript> listAll(){
//			return trans_repo.findAll();
//		}
		
		public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
			Page<Object> roles_filtered_response = trans_repo.getAllDataFilteredByKeyword(pageable, keyword);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_filtered_response);
		}
		
		public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
			Page<Object> roles_sorted_response = trans_repo.getAllSortedData(pageable);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
		}
		
		
		public ProgramTranscript get(Integer trans_id) {
			return trans_repo.findById(trans_id)
					.orElseThrow(() -> new ResourceNotFoundException("Program Transcript Not Found:" + trans_id));
		}
		
		public ProgramTranscript saveUpdateProgramTranscript(ProgramTranscript prog) {
			
			return trans_repo.save(prog);
		}
		
		public void delete(Integer trans_id) {
			trans_repo.findById(trans_id)
					.orElseThrow(() -> new ResourceNotFoundException("Program Transcript Not Found:" + trans_id));
			trans_repo.update(trans_id);
		}
		
		public void delete1(Integer trans_id) {
			
			trans_repo.findById(trans_id)
			.orElseThrow(() -> new ResourceNotFoundException("Pragram Transcript Not Found:" + trans_id));
			trans_repo.update1(trans_id);
		}
		
		public List<Map<String, Object>> fetchUnassignedProgramDetail(Integer trans_id) {
			return p_repo.fetchUnassignedProgramDetail(trans_id);
		}


}
