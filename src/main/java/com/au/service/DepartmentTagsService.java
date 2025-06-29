package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.DepartmentTags;
import com.au.repository.DepartmentTagsRepository;
import com.au.response.ResponseHandler;

@Service
public class DepartmentTagsService {

	@Autowired
	private DepartmentTagsRepository depttag_repo;

	public List<DepartmentTags> listAll() {
		return depttag_repo.findAll1();
	}

	public List<DepartmentTags> listAll1() {
		return depttag_repo.findAll();
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> roles_filtered_response = depttag_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> roles_sorted_response = depttag_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
	}

	public DepartmentTags saveDepartmentTags(DepartmentTags tags) throws Exception {

		List<DepartmentTags> list = depttag_repo.checkexist(tags.getTag_name(), tags.getTag_short_name());
		if (list != null && list.size() != 0) {
			throw new Exception("Service  already exist");
		} else {
			if(depttag_repo.countOfServiceTagName(tags.getTag_name())>=1) {
				throw new Exception("Sevice Name Already Exist");
			}else if(depttag_repo.countOfServiceTagShortName(tags.getTag_short_name())>=1) {
				throw new Exception("Short Name Already Exist");
			}else {
				return depttag_repo.save(tags);
			}
		}	
		 
	}

	public DepartmentTags saveDepartmentTag(DepartmentTags departmentTags) {
		return depttag_repo.save(departmentTags);
	}

	public DepartmentTags get(Integer id) {
		return depttag_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("DepartmentTags Id Found:" + id));
	}

	public void delete(Integer id) {
		DepartmentTags ay = depttag_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("DepartmentTags Id Found:" + id));
		depttag_repo.update(id);
	}

	public void delete1(Integer id) {
		DepartmentTags ay = depttag_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("DepartmentTags Id Found:" + id));
		depttag_repo.update1(id);
	}

}
