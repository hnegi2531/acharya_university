package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.SubjectType;
import com.au.repository.SubjectTypeRepository;
import com.au.response.ResponseHandler;

@Service
public class SubjectTypeService {

	@Autowired
	private SubjectTypeRepository s_repo;
	
	public List<SubjectType> listAll1() {
		return s_repo.findAll1();
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> subtype_filtered_response = s_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, subtype_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> subtype_sorted_response = s_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, subtype_sorted_response);
	}
	
	public SubjectType saveSubjectType(SubjectType s) {

		if (s_repo.existsBySubjectTypeName(s.getSubjectTypeName())) {
			throw new RuntimeException("Subject Name Already Exist!!");
		}

		return s_repo.save(s);

	}

	public SubjectType get(Integer id) {
		return s_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("SubjectType Not Found:" + id));
	}

	public void delete(Integer id) {
		SubjectType cc = s_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SubjectType Not Found:" + id));
		s_repo.updateStd_subjects(id);
	}

	public void delete1(Integer id) {
		SubjectType cc = s_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SubjectType Not Found:" + id));
		s_repo.updateStd_subjects1(id);

	}

	
	
}
