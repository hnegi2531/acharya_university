package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.CourseCategory;
import com.au.repository.CourseCategoryRepository;
import com.au.response.ResponseHandler;

@Service
public class CourseCategoryService {

	@Autowired
	private CourseCategoryRepository cc_repo;

	public List<CourseCategory> listAll() {
		return cc_repo.findAll1();
	}

//	public List<CourseCategory> listAll1() {
//		return cc_repo.findAll();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = cc_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = cc_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
		
	}
	public CourseCategory saveCourseCategory(CourseCategory c) {
		if (cc_repo.countCourseCategoryName(c.getCourse_category_name()) >= 1) {
			throw new RuntimeException("Course Category Name Already Exist");
		}else if (cc_repo.countCourseCategoryCode(c.getCourse_category_code()) >= 1) {
			throw new RuntimeException("Short Name Already Exist");
		}else {
			return cc_repo.save(c);
		}
	}
	
	public CourseCategory updateCourseCategory(CourseCategory c) {
		if (cc_repo.countCourseCategoryNameForUpdate(c.getCourse_category_id(),c.getCourse_category_name()) >= 1) {
			throw new RuntimeException("Course Category Name Already Exist");
		}else if (cc_repo.countCourseCategoryCodeForUpdate(c.getCourse_category_id(),c.getCourse_category_code()) >= 1) {
			throw new RuntimeException("Short Name Already Exist");
		}else {
			return cc_repo.save(c);
		}
	}

	public CourseCategory get(Integer id) {
		return cc_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("CourseCategory id Not Found:" + id));
	}

	public void delete(Integer designation_id) {
		CourseCategory ay = cc_repo.findById(designation_id)
				.orElseThrow(() -> new ResourceNotFoundException("CourseCategory id Not Found:" + designation_id));
		cc_repo.updateCourseCategory(designation_id);
	}

	public void delete1(Integer designation_id) {
		CourseCategory ay = cc_repo.findById(designation_id)
				.orElseThrow(() -> new ResourceNotFoundException("CourseCategory id Not Found:" + designation_id));
		cc_repo.updateCourseCategory1(designation_id);
	}
}
