package com.au.service;

import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.ProgramMaster;
import com.au.repository.ProgramMasterRepository;
import com.au.response.ResponseHandler;

@Service
public class ProgramMasterService {

	@Autowired
	private ProgramMasterRepository pr_repo;

	public List<ProgramMaster> listAll() {
		return pr_repo.findAll1();
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> roles_filtered_response = pr_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> roles_sorted_response = pr_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
	}
	

	public ProgramMaster saveProgramMaster(ProgramMaster programMaster) {
		return pr_repo.save(programMaster);
	}

	public ProgramMaster get(Integer id) {
		return pr_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Program Master Not Found:" + id));
	}

	public void delete(Integer id) {
		ProgramMaster pm = pr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Program Master Not Found:" + id));
		pr_repo.update(id);
	}

	public void delete1(Integer id) {
		ProgramMaster pm = pr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Program Master Not Found:" + id));
		pr_repo.update1(id);
	}
}
