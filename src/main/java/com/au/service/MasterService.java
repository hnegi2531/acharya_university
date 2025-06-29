package com.au.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.Master;
import com.au.repository.MasterRepository;
import com.au.response.ResponseHandler;

@Service
public class MasterService {

	@Autowired
	private MasterRepository master_repo;

	public List<Master> listAll() {
		return master_repo.findAll1();
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {

		Page<Object> mtr_filtered_response = master_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, mtr_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> mtr_sorted_response = master_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, mtr_sorted_response);
	}

	public Master saveMaster(Master master) {
		return master_repo.save(master);
	}

	public Master get(Integer id) {
		return master_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Master Not Found:" + id));
	}

	public void delete(Integer id) {
		Master existingCourse = master_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Master Not Found:" + id));
		this.master_repo.update(id);
	}

	public void delete1(Integer id) {
		Master existingCourse = master_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Master Not Found:" + id));
		this.master_repo.update1(id);
	}

}
