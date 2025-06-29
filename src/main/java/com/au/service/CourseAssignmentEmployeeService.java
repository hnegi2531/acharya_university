package com.au.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.CourseAssignmentEmployeeDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.Course;
import com.au.model.CourseAssignmentEmployee;
import com.au.repository.CourseAssignmentEmployeeRepository;
import com.au.repository.CourseRepository;
import com.au.response.ResponseHandler;

@Service
public class CourseAssignmentEmployeeService {

	
	@Autowired
	private CourseAssignmentEmployeeRepository courseAssignmentEmployeeRepository;

	public List<CourseAssignmentEmployee> saveCourseAssignmentEmployees(@Valid CourseAssignmentEmployeeDto courseEmployee) throws Exception {
	    List<CourseAssignmentEmployee> savedAssignments = new ArrayList<>();
	    
	    for (Integer courseId : courseEmployee.getCourse_id()) {
	        if (courseAssignmentEmployeeRepository.getCountOfCourseEmployee(courseEmployee.getUser_id(), courseId) >= 1) {
	            throw new Exception("Employee already assigned to course ID " + courseId + "!!!");
	        } else {
	            // Create a new assignment instance for saving
	            CourseAssignmentEmployee assignment = new CourseAssignmentEmployee();
	            assignment.setUser_id(courseEmployee.getUser_id());
	            assignment.setCourse_id(courseId);  // Set the individual course_id
	            assignment.setRemark(courseEmployee.getRemark());
	            assignment.setCreated_by(courseEmployee.getCreated_by());
	            assignment.setModified_by(courseEmployee.getModified_by());
	            assignment.setCreated_date(courseEmployee.getCreated_date());
	            assignment.setModified_date(courseEmployee.getModified_date());
	            assignment.setActive(courseEmployee.getActive());
	            assignment.setCreated_username(courseEmployee.getCreated_username());
	            assignment.setModified_username(courseEmployee.getModified_username());

	            // Save and add to the list of saved assignments
	            savedAssignments.add(courseAssignmentEmployeeRepository.save(assignment));
	        }
	    }
	    return savedAssignments; // Return the list of saved assignments
	}

	public List<CourseAssignmentEmployee> getAllActiveCourseAssignmentEmployee() {
		return courseAssignmentEmployeeRepository.getAllActiveCourseAssignmentEmployee();
	}
	
	
	public CourseAssignmentEmployee saveCourse1(CourseAssignmentEmployee course) {
		return courseAssignmentEmployeeRepository.save(course);
	}

	public CourseAssignmentEmployee get(Integer id) {
		return courseAssignmentEmployeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("CourseAssignmentEmployee Not Found:" + id));
	}
	
	
	public void deactivate(Integer id) {
		courseAssignmentEmployeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Course Not Found:" + id));
		this.courseAssignmentEmployeeRepository.deactivate(id);
	}

	public void activate(Integer id) {
		courseAssignmentEmployeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Course Not Found:" + id));
		this.courseAssignmentEmployeeRepository.activate(id);
	}
	
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword, Integer user_id) {
		Page<Object> response1 = courseAssignmentEmployeeRepository.findAll1(pageable, keyword, user_id );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable, Integer user_id) {
		Page<Object> response = courseAssignmentEmployeeRepository.findAll2(pageable,user_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public List<Map<String, Object>> getCourseAssignmentEmployeeBasedOnUserId(Integer user_id) {
		return courseAssignmentEmployeeRepository.getCourseAssignmentEmployeeBasedOnUserId(user_id);
	}
	
	public Map<String, Object> getCountOfCourseBasedOnUserId(Integer user_id) {
		return courseAssignmentEmployeeRepository.getCountOfCourseBasedOnUserId(user_id);
	}
	
}
