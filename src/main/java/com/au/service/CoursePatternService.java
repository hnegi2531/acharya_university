package com.au.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.CoursePatternDto;
import com.au.dto.JwtDetails;
import com.au.exception.ResourceNotFoundException;
import com.au.model.CoursePattern;
import com.au.model.Department;
import com.au.repository.CourseAssignmentRepository;
import com.au.repository.CoursePatternRepository;
import com.au.repository.ProgramAssigmentRepository;
import com.au.response.ResponseHandler;

@Service
public class CoursePatternService {
	
	@Autowired
	private CoursePatternRepository course_repo;
	
	@Autowired
	private ProgramAssigmentRepository r_repo;
	
	@Autowired
	private CourseAssignmentRepository repo_carepo;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	public List<CoursePattern> saveCoursePattern(CoursePatternDto course_pattern_dto,String jwtToken) throws Exception {
		List<CoursePattern> course_pattern_list=new ArrayList<CoursePattern>();
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		course_pattern_dto.getProgram_id().stream().forEach(pro_id ->{
		if(course_repo.getCountOfCoursePattern(course_pattern_dto.getAc_year_id(),course_pattern_dto.getSchool_id(),course_pattern_dto.getCourse_category_id(),pro_id) >= 1)
		{
			throw new RuntimeException("Course Pattern with Academic Year,School,Course Category and Program combination already exist");
		} else {
			CoursePattern course_pattern=new CoursePattern();
			course_pattern.setAc_year_id(course_pattern_dto.getAc_year_id());
			course_pattern.setSchool_id(course_pattern_dto.getSchool_id());
			course_pattern.setPercentage_of_credit(course_pattern_dto.getPercentage_of_credit());
			course_pattern.setCredits(course_pattern_dto.getCredits());
			course_pattern.setProgram_id(pro_id);
			course_pattern.setActive(course_pattern_dto.getActive());
			course_pattern.setCreated_by(jwtDetails.getUserId());
			course_pattern.setCreated_username(jwtDetails.getUserName());
			course_pattern.setCourse_category_id(course_pattern_dto.getCourse_category_id());
			course_repo.save(course_pattern);
			course_pattern_list.add(course_pattern);
		}	
		});
		return course_pattern_list;
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> coursepattern_filtered_response = course_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, coursepattern_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> coursepattern_sorted_response = course_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, coursepattern_sorted_response);
	}

	public List<CoursePattern> listAll1() {
		return course_repo.findAll11();
	}
	
	public CoursePattern get(Integer id) {
		return course_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("CoursePattern Not Found:" + id));
	}
	
	public CoursePattern updateCoursePattern(CoursePattern coursepattern) throws Exception {
		return course_repo.save(coursepattern);

	}
	
	public void delete(Integer id) {
		CoursePattern dept = course_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Department Not Found:" + id));
		course_repo.deactivateCoursePattern(id);
	}

	public void delete1(Integer id) {
		CoursePattern dept = course_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Department Not Found:" + id));
		course_repo.activateCoursePattern(id);
	}
	
	public List<Map<String, Object>> fetchProgram(Integer ac_year_id, Integer school_id) {
		return r_repo.fetchProgramIds(ac_year_id, school_id );
	}
	
	
}
