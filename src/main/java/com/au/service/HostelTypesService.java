package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.HostelTypes;
import com.au.repository.HostelTypesRepository;
import com.au.response.ResponseHandler;

@Service
public class HostelTypesService {

	@Autowired
	private HostelTypesRepository repo;
	
	public List<HostelTypes> listAll() {
		return repo.findAll1();
	}
	
//	public List<HostelTypes> listAll1() {
//		return repo.findAll();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		
		Page<Object> response1 = repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		
		Page<Object> response = repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public HostelTypes saveHostelTypes(HostelTypes s) {
		if(repo.existsByhostelType(s.getHostelType())) {
			throw new RuntimeException("Hostel Types is Already exist");
		}
		return repo.save(s);

	}

	public HostelTypes get(Integer id) {
		return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("HostelTypes Not Found:" + id));
	}

	public void delete(Integer id) {
		HostelTypes cc = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("HostelTypes Not Found:" + id));
		repo.update(id);
	}

	public void delete1(Integer id) {
		HostelTypes cc = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("HostelTypes Not Found:" + id));
		repo.update1(id);

	}

	
}
