package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.HostelWarden;
import com.au.repository.HostelWardenRepository;
import com.au.response.ResponseHandler;

@Service
public class HostelWardenService {
	
	@Autowired
	private HostelWardenRepository repo;
	
	public List<HostelWarden> listAll() {
		return repo.findAll1();
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> hw_filtered_response = repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, hw_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> hw_sorted_response = repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, hw_sorted_response);
	}
	
	public HostelWarden saveHostelWarden(HostelWarden s) {
		return repo.save(s);
	}

	public HostelWarden get(Integer id) {
		return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("HostelWarden Not Found:" + id));
	}

	public void delete(Integer id) {
		HostelWarden cc = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("HostelWarden Not Found:" + id));
		repo.update(id);
	}

	public void delete1(Integer id) {
		HostelWarden cc = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("HostelWarden Not Found:" + id));
		repo.update1(id);

	}
}
