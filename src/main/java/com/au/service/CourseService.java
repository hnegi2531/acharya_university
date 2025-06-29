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
import org.springframework.web.bind.annotation.PathVariable;

import com.au.exception.ResourceNotFoundException;
import com.au.model.Course;
import com.au.repository.CategoryTypeDetailsRepository;
import com.au.repository.CourseRepository;
import com.au.response.ResponseHandler;

@Service
public class CourseService {

	@Autowired
	private CourseRepository course_repository;
	
	@Autowired
	private CategoryTypeDetailsRepository ctd_repo;

	public List<Course> listAll() {	
		return course_repository.findAll1();
	}

//	public List<Course> listAll1() {
//		return course_repository.findAll();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = course_repository.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = course_repository.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public Course saveCourse(Course course) throws Exception {
	
		Integer count1 = course.getOrg_id() != null
				? course_repository.countOfCourseNameWithOrgId(course.getOrg_id(), course.getCourse_name())
				: course_repository.countOfCourseName(course.getCourse_name());
		

		Integer count2 = course.getOrg_id() != null
				? course_repository.countOfCourseCodeWithOrgId(course.getOrg_id(), course.getCourse_code())
				: course_repository.countOfCourseCode(course.getCourse_code());

		Integer count3 = course.getOrg_id() != null
				? course_repository.countOfCourseWithOrgnizationId(course.getOrg_id(), course.getCourse_name(),
						course.getCourse_code())
				: course_repository.countOfCourse(course.getCourse_name(), course.getCourse_code());
		
		 if (count1 >= 1) {
		        throw new Exception("Course name already exists.");
		    } else if (count2 >= 1) {
		        throw new Exception("Course code already exists.");
		    } else if (count3 >= 1) {
		        throw new Exception("Course already exists.");
		    } else {
		        return course_repository.save(course);
		    }
	}
	
	public Course saveCourse1(Course course) {
		return course_repository.save(course);
	}

	public Course get(Integer id) {
		return course_repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Course Not Found:" + id));
	}

	public void delete(Integer id) {
		 course_repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Course Not Found:" + id));
		this.course_repository.update(id);
	}

	public void delete1(Integer id) {
		course_repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Course Not Found:" + id));
		this.course_repository.update1(id);
	}

	public List<Map<String, Object>> details() {
		return course_repository.fetchAll();
		
	}
	
	public List<Map<String,Object>> coursesForLessonPlan(Integer school_id, Integer program_id,Integer program_specialization_id, String year_sem) {	
		return course_repository.getCoursesForLessonPlan(school_id,program_id,program_specialization_id,year_sem);
	}
	
	public List<HashMap<String,Object>> categoryTypeDetailsOnCatgoryTypeCreation(){
		return ctd_repo.categoryTypeDetailsOnCatgoryTypeCreation();
	}
	
	public List<Map<String,Object>> getCourseConcat1() {	
		return course_repository.getCourseConcat11();
	}

}
