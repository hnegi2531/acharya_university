package com.au.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.SubjectWorkloadCreation;
import com.au.repository.SubjectWorkloadCreationRepository;
import com.au.response.ResponseHandler;

@Service
public class SubjectWorkloadCreationService {

	@Autowired
	private SubjectWorkloadCreationRepository repo;

	public List<SubjectWorkloadCreation> listAll() {
		return repo.findAll1();
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {

		Page<Object> swc_filtered_response = repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, swc_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> swc_sorted_response = repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, swc_sorted_response);
	}

	public SubjectWorkloadCreation saveSubjectWorkloadCreation(SubjectWorkloadCreation s) {
		if(repo.existsBysubWorkloadName(s.getSubWorkloadName())) {
			System.out.println("ALready exist!!!");
			throw new RuntimeException("123");
		}
		return repo.save(s);
	}

	
	public SubjectWorkloadCreation saveSubjectWorkloadCreation1(SubjectWorkloadCreation r) {
		return repo.save(r);
		
	}
	public SubjectWorkloadCreation get(Integer id) {
		return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("SubjectWorkload Not Found:" + id));
	}

	public void delete(Integer id) {
		SubjectWorkloadCreation cc = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SubjectWorkload Not Found:" + id));
		repo.update(id);
	}

	public void delete1(Integer id) {
		SubjectWorkloadCreation cc = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SubjectWorkload Not Found:" + id));
		repo.update1(id);

	}

	

}
