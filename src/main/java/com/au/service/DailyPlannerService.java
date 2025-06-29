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
import com.au.model.DailyPlanner;
import com.au.repository.DailyPlannerRepository;
import com.au.response.ResponseHandler;

@Service
public class DailyPlannerService {
	
	@Autowired
	private DailyPlannerRepository planner_repo;
	
	
	
	
	public DailyPlanner saveDailyPlanner(@Valid DailyPlanner dp)throws Exception {
		return	planner_repo.save(dp);
	}

	public List<DailyPlanner> getAllActiveDailyPlanner() {
		 return planner_repo.getAllActiveDailyPlanner();
	}
	
	public DailyPlanner get(Integer id) {
		return planner_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Daily planner Not Found:" + id));
	}
	
	
	public DailyPlanner updateDailyPlannere(@Valid DailyPlanner tm) {
		return planner_repo.save(tm);
	}
	
	public void deactivate(Integer id) {
		DailyPlanner co = planner_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Daily planner Not Found:" + id));
		planner_repo.deactivate(id);
	}
	
	public void activate(Integer id) {
		DailyPlanner co = planner_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Daily planner Not Found:" + id));
		planner_repo.activate(id);
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer emp_id) {
		Page<Object> oc_filtered_response = planner_repo.getAllDataFilteredByKeyword(pageable, keyword,emp_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable, Integer emp_id) {
		Page<Object> oc_sorted_response = planner_repo.getAllSortedData(pageable,emp_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_sorted_response);
	}
	
	
	public ResponseEntity<Object> fetchAllDailyPlannerBasedOnUserKeyword(Pageable pageable, Object keyword, Integer userId) {
		Page<Object> oc_filtered_response = planner_repo.fetchAllDailyPlannerBasedOnUserKeyword(pageable, keyword,userId);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_filtered_response);
	}

	public ResponseEntity<Object> fetchAllDailyPlannerBasedOnUserSortedData(Pageable pageable, Integer userId) {
		Page<Object> oc_sorted_response = planner_repo.fetchAllDailyPlannerBasedOnUserSortedData(pageable,userId);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_sorted_response);
	}

	public List<Map<String, Object>> getAllActiveDailyPlannerBasedOnEmpId(Integer emp_id) {
		return planner_repo.getAllActiveDailyPlannerBasedOnEmpId(emp_id);
	}	
}
