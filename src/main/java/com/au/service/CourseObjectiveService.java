package com.au.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import com.au.repository.CourseObjectiveRepository;
import com.au.repository.CourseRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.JwtDetails;
import com.au.dto.ProgramSpecilizationDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.CourseCredit;
import com.au.model.CourseObjective;
import com.au.model.InternalTimeTableAssignment;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class CourseObjectiveService {


	@Autowired
	private CourseObjectiveRepository co_repo;
	
	@Autowired
	private CourseRepository course_repository;

	
	public List<CourseObjective> saveCourseObjective(@Valid List<CourseObjective> co) throws Exception {
		return	co_repo.saveAll(co);
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> oc_filtered_response = co_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> oc_sorted_response = co_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_sorted_response);
	}
	
	public List<CourseObjective> listAll1() {
		return co_repo.findAll11();
	}
	
	public List<CourseObjective> get(List<Integer> id) {
		return co_repo.findAllById(id);
	}
	
	public List<CourseObjective> saveCourseObjectives(List<CourseObjective> cos) {
		return co_repo.saveAll(cos);
	}
	
	
	public void delete(Integer id) {
		CourseObjective co = co_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Course Objective Not Found:" + id));
		co_repo.updateCourseObjective(id);
	}
	
	public void delete1(Integer id) {
		CourseObjective co = co_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Course Objective Not Found:" + id));
		co_repo.updateCourseObjective1(id);
	}
	
	
	public List<Map<String,Object>> getCoursesConcate() {	
		return course_repository.getCoursesConcate();
	}

	public List<Map<String,Object>> getCourseObjectiveAndOutcome(Integer course_assignment_id) {
		return co_repo.getCourseObjectiveAndOutcome(course_assignment_id);
	}

	public List<Map<String, Object>> getCourseObjectiveDetails(Integer course_assignment_id) {
		return co_repo.getCourseObjectiveDetails(course_assignment_id);
	}
}
