package com.au.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.Designation;
import com.au.repository.DesignationRepository;
import com.au.response.ResponseHandler;

@Service
public class DesignationService {

	@Autowired
	private DesignationRepository dr_repo;

	public List<Designation> listAll() {
		return dr_repo.findAll1();
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> des_filtered_response = dr_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, des_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> des_sorted_response = dr_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, des_sorted_response);
	}

	public Designation saveDesignation(Designation designation) throws Exception{
		if(dr_repo.countOfDesignationName(designation.getDesignation_name())>=1) {
			throw new Exception("Designation Name Already Exist");
		}else if(dr_repo.countOfDesignationShortName(designation.getDesignation_short_name())>=1) {
			throw new Exception("Short Name Already Exist");
		}else if(dr_repo.countOfPriority(designation.getPriority())>=1) {
			throw new Exception("Priority Already Exist");
		}else {
			return dr_repo.save(designation);
		} 	
	}
	
	public Designation saveDesignation1(Designation designation) {
		return dr_repo.save(designation);
			
	}

	public Designation get(Integer id) {
		if (id.equals(0)) {
			throw new RuntimeException("Opps Exception raised....");
		}
		return dr_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Designation id Not Found:" + id));
	}

	public void delete(Integer designation_id) {
		Designation ay = dr_repo.findById(designation_id)
				.orElseThrow(() -> new ResourceNotFoundException("Designation id Not Found:" + designation_id));
		dr_repo.update(designation_id);
	}

	public void delete1(Integer designation_id) {
		Designation ay = dr_repo.findById(designation_id)
				.orElseThrow(() -> new ResourceNotFoundException("Designation id Not Found:" + designation_id));
		dr_repo.update1(designation_id);
	}

}
