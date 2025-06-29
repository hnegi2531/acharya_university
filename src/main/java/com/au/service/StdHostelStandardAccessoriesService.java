package com.au.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.StdHostelStandardAccessories;
import com.au.repository.StdHostelStandardAccessoriesRepository;
import com.au.response.ResponseHandler;

@Service
public class StdHostelStandardAccessoriesService {

	@Autowired
	private StdHostelStandardAccessoriesRepository repo;

	public List<StdHostelStandardAccessories> listAll() {
		return repo.findAll1();
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {

		Page<Object> shsa_filtered_response = repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, shsa_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> shsa_sorted_response = repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, shsa_sorted_response);
	}

	public StdHostelStandardAccessories saveStdHostelStandardAccessories(StdHostelStandardAccessories s) {
		
		return repo.save(s);
	}
/*
	public StdHostelStandardAccessories saveHostelFloor1(StdHostelStandardAccessories r) {
		return repo.save(r);
	}
*/	
	public StdHostelStandardAccessories get(Integer id) {
		return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("StdHostelStandardAccessories Not Found:" + id));
	}

	public void delete(Integer id) {
		StdHostelStandardAccessories cc = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("StdHostelStandardAccessories Not Found:" + id));
		repo.update(id);
	}

	public void delete1(Integer id) {
		StdHostelStandardAccessories cc = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("StdHostelStandardAccessories Not Found:" + id));
		repo.update1(id);

	}

	
}
