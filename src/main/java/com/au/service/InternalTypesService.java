package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;

import com.au.model.InternalTypes;

import com.au.repository.InternalTypesRepository;

import com.au.response.ResponseHandler;

@Service
public class InternalTypesService {

	@Autowired
	private InternalTypesRepository it_repo;
	
	public InternalTypes saveInternalTypes(InternalTypes its) throws Exception {
		
		if(it_repo.getInternalName(its.getInternal_name()) >= 1) {
			throw new Exception("Internal Name Already Present");
	
		} else if(it_repo.getCountInternalShortName(its.getInternal_short_name()) >= 1) {
			throw new Exception("Internal Short Name Already Exist");
		
		} else {
			InternalTypes it=it_repo.save(its);
		
			return it;
		}
	}
	
	public List<InternalTypes> listAllActiveInternalTypes() {
		List<InternalTypes> its=it_repo.findAll();
		return its;
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> internaltypes_filtered_response = it_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, internaltypes_filtered_response);
	}
	
	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> internaltypes_sorted = it_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, internaltypes_sorted);
	}
	

	public InternalTypes get(Integer id) {
		return it_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Internal Type Not Found:" + id));
	}
	
	public InternalTypes saveInternalType(InternalTypes its) {
		return it_repo.save(its);
	}

	
	public void delete(Integer id) {
		InternalTypes it = it_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Department Not Found:" + id));
		it_repo.updateDept(id);
	}

	public void delete1(Integer id) {
		InternalTypes it = it_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Department Not Found:" + id));
		it_repo.updateDept1(id);
	}


}
