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
import com.au.model.CourseOutcome;
import com.au.model.Syllabus;
import com.au.repository.CourseOutcomeRepository;
import com.au.response.ResponseHandler;

@Service
public class CourseOutcomeService {

	@Autowired
	private CourseOutcomeRepository coc_repo;
	
	
	
	public List<CourseOutcome> saveCourseOutCome(@Valid List<CourseOutcome> coc) throws Exception {
		return	coc_repo.saveAll(coc);
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> coc_filtered_response = coc_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, coc_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> coc_sorted_response = coc_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, coc_sorted_response);
	}
	
	public List<CourseOutcome> listAll1() {
		return coc_repo.findAll11();
	}
	
	public List<CourseOutcome> get(List<Integer> id) {
		return coc_repo.findAllById(id);
	}
	
	public List<CourseOutcome> saveCourseOutComes(List<CourseOutcome> cocs) {
		return coc_repo.saveAll(cocs);
	}
	
	
	public void delete(Integer id) {
		CourseOutcome coc = coc_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Course Outcome Not Found:" + id));
		coc_repo.updateCourseOutCome(id);
	}
	
	public void delete1(Integer id) {
		CourseOutcome coc = coc_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Course Outcome Not Found:" + id));
		coc_repo.updateCourseOutCome1(id);
	}

	public List<CourseOutcome> getCourseOutCome(Integer course_assignment_id) {
		return coc_repo.getCourseOutCome(course_assignment_id);
	}

	public List<Map<String, Object>> getCourseOutComeDetails(Integer course_assignment_id) {
		return coc_repo.getCourseOutComeDetails(course_assignment_id);
	}

	public String getToxonomyDetails(String toxonomy) {
		String ty=null;
		if (toxonomy.equalsIgnoreCase("Create - Produce new or original work")) {
			ty = "Design, assemble, construct, conjecture, develop, formulate, author, investigate";
		}else if (toxonomy.equalsIgnoreCase("Evaluate - Justify a stand or decision")) {
			ty = "Appraise, argue, defend, judge, select, support, value, critique, weigh";
		}else if (toxonomy.equalsIgnoreCase("Analyse - Draw connections among ideas")) {
			ty = "Differentiate, organise, relate, compare, contrast, distinguish, examine, experiment, question, test";
		}else if (toxonomy.equalsIgnoreCase("Apply - Use information in new situation")) {
			ty = "Execute, implement, solve, use, demonstrate, interpret, operate, schedule, sketch";
		}else if (toxonomy.equalsIgnoreCase("Understand - Explain ideas or concepts")) {
			ty = "Classify, discribe, discuss, explain, identify, locate, recognize, report, select, translate";
		}else if (toxonomy.equalsIgnoreCase("Remember - Recall facts and basic concepts")) {
			ty = "Define duplicate, memorise, list, repeat,state";
		}
		return ty;
	}
	
	
}
