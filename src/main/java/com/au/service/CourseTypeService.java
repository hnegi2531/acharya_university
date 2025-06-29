package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.CourseType;
import com.au.repository.CourseTypeRepository;
import com.au.response.ResponseHandler;

@Service
public class CourseTypeService {

	@Autowired
	private CourseTypeRepository c_repo;

	public List<CourseType> listAll() {
		return c_repo.findAll1();
	}

//	public List<CourseType> listAll1() {
//		return c_repo.findAll();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = c_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = c_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
		
	}

	public CourseType saveCourseType(CourseType s) throws Exception{
		if(c_repo.countOfCourseTypeName(s.getCourse_type_name())>=1) {
			throw new Exception("Course Type Name Already Exist");
		}else if(c_repo.countOfCourseShortName(s.getCourse_type_code())>=1) {
			throw new Exception("Short Name Already Exist");
		}else {
			return c_repo.save(s);
		}	

	}
	
	public CourseType saveUpdatedCourseType(CourseType s) {
		return c_repo.save(s);
	}

	public CourseType get(Integer id) {
		return c_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("CourseType Not Found:" + id));
	}

	public void delete(Integer id) {
		CourseType cc = c_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("CourseType Not Found:" + id));
		c_repo.updateCourseType(id);
	}

	public void delete1(Integer id) {
		CourseType cc = c_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("CourseType Not Found:" + id));
		c_repo.updateCourseType1(id);

	}

}
