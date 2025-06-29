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
import com.au.model.ReferenceBooks;
import com.au.model.Roles;
import com.au.repository.ReferenceBooksRepository;
import com.au.response.ResponseHandler;

@Service
public class ReferenceBooksService {

	@Autowired
	private ReferenceBooksRepository r_repo;

	public List<ReferenceBooks> listAll() {
		return r_repo.findAll();
	}

	public ReferenceBooks saveReferenceBooks(ReferenceBooks r) throws Exception {
		if (r_repo.existsBytitleOfBook(r.getTitle_of_book(),r.getIsbn_code()) >= 1) {
			throw new RuntimeException("TitleOfBook and ISBN code already exist!!!");
		}
		return r_repo.save(r);
	}
	
	public ReferenceBooks updateReferenceBooks(ReferenceBooks referencebooks) {
		return r_repo.save(referencebooks);	
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		
		List<Map<String, Object>> reference_books_filtered_response = r_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponse(true, HttpStatus.OK, reference_books_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		List<Map<String, Object>> reference_books_sorted_response = r_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, reference_books_sorted_response);
	}

	public ReferenceBooks get(Integer id) {
		return r_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ReferenceBooks Not Found:" + id));
	}

	public void delete(Integer id) {
		ReferenceBooks cc = r_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ReferenceBooks Not Found:" + id));
		r_repo.updateReferenceBooks(id);
	}

	public void delete1(Integer id) {
		ReferenceBooks cc = r_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ReferenceBooks Not Found:" + id));
		r_repo.updateReferenceBooks1(id);

	}

	public List<HashMap<String, Object>> getReferenceBooks(Integer school_id,Integer program_specialization_id) {
		return r_repo.getAllReferenceBooks(school_id,program_specialization_id);
	}
	
	public List<Map<String, Object>> getReferenceBooksForLessonPlan(Integer school_id,Integer program_specialization_id) {
		return r_repo.getReferenceBooksForLessonPlan(school_id,program_specialization_id);
	}
	
	public List<Map<String, Object>> getDetails(Integer id) {
		return r_repo.getDetails1(id);
	}
	
	public List<HashMap<String, Object>> getReferenceBooks(Integer program_specialization_id) {
		return r_repo.getAllReferenceBooks(program_specialization_id);
	}
	
}
