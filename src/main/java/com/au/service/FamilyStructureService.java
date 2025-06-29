package com.au.service;

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
import com.au.model.FamilyStructure;
import com.au.repository.FamilyStructureRepository;
import com.au.response.ResponseHandler;

@Service
public class FamilyStructureService {
	


	@Autowired
	private FamilyStructureRepository familyStructureRepository;
	
	public List<FamilyStructure> saveFamily(@Valid List<FamilyStructure> family)  {
		return	familyStructureRepository.saveAll(family);
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> familyStructoreFilteredResponse = familyStructureRepository.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, familyStructoreFilteredResponse);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> familyStructoreSortedResponse = familyStructureRepository.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, familyStructoreSortedResponse);
	}

	public List<FamilyStructure> listAll1() {
		return familyStructureRepository.findAll11();
	}
	
	
	public FamilyStructure get(Integer id) {
		return familyStructureRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Family Details  Not Found:" + id));
	}
	
	public List<FamilyStructure> updateFamilystructure(List<FamilyStructure> FamilyStructures) {
		return familyStructureRepository.saveAll(FamilyStructures);
	}
	
	public void delete(Integer id) {
		 familyStructureRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Family Details Not Found:" + id));
		familyStructureRepository.updateDept(id);
	}
	
	public void delete1(Integer id) {
		familyStructureRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Family Details Not Found:" + id));
		familyStructureRepository.updateDept1(id);
	}
	
	public List<Map<String, Object>> getFamilyStructureDetailsData(Integer empId) {
		return familyStructureRepository.getFamilyStructureDetailsData(empId);
	}
	
}
	
	
	
	
	
	
	
	
	
	

