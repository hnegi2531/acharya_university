package com.au.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.HodComments;
import com.au.repository.HodCommentsRepository;
import com.au.response.ResponseHandler;

@Service
public class HodCommentsService {

	@Autowired
	private HodCommentsRepository hcr_repo;

	public List<HodComments> listAll() {
		return hcr_repo.findAll();
	}
	
	public List<HashMap<String, Object>> listAll1() {
		return hcr_repo.findAll1();
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		
		Page<Object> hod_filtered_response = hcr_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, hod_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> hod_sorted_response = hcr_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, hod_sorted_response);
	}

	public List<HodComments> saveHodComments(List<HodComments> hc) {
		return hcr_repo.saveAll(hc);
	}
	
	public HodComments saveHodComment(@Valid HodComments hc) {
		return hcr_repo.save(hc);
	}

	public HodComments get(Integer id) {
		return hcr_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("HodComments ID Not Found:" + id));
	}

	public void delete(Integer id) {
		HodComments ms = hcr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("HodComments ID Not Found:" + id));
		hcr_repo.delete(ms);
	}
	
	public List<Map<String, Object>> getallDeatils(Integer job_id) {
		return hcr_repo.fetchAllHodComment(job_id);
	}
}
