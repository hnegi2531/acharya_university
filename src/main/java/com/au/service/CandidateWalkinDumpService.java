package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.CandidateWalkinDump;
import com.au.model.Candidate_Walkin;
import com.au.repository.CandidateWalkinDumpRepository;
import com.au.response.ResponseHandler;


@Service
public class CandidateWalkinDumpService {
	
	@Autowired
	private CandidateWalkinDumpRepository cwd_repo;
	
	public CandidateWalkinDump saveCandidateWalkinDump(CandidateWalkinDump p) throws Exception {
		
		return cwd_repo.save(p);

	}
	
	public List<CandidateWalkinDump> listAll() {
		return cwd_repo.findAll1();
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> response1 = cwd_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
        Page<Object> response = cwd_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public CandidateWalkinDump get(Integer id) {
		return cwd_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate_Walkin Not Found:" + id));
	}

	public CandidateWalkinDump updateCandidateWalkinDump(CandidateWalkinDump cwd) {
		return cwd_repo.save(cwd);

	}
	
	public void delete(Integer id) {
		CandidateWalkinDump ay = cwd_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate_Walkin Not Found:" + id));
		cwd_repo.updateCandidateWakin_dump(id);
	}
	
	public void delete1(Integer id) {
		CandidateWalkinDump ay = cwd_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate_Walkin Not Found:" + id));
		cwd_repo.updateCandidateWakin_dump1(id);
	}
}
