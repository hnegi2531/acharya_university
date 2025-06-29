package com.au.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.ClassFeedbackAnswersDTO;
import com.au.exception.ResourceNotFoundException;
import com.au.model.ClassFeedbackAnswers;
import com.au.repository.ClassFeedbackAnswersRepository;
import com.au.repository.EmployeeDetailsRepository;
import com.au.response.ResponseHandler;

@Service
public class ClassFeedbackAnswersService {
	
	@Autowired
	private ClassFeedbackAnswersRepository cfa_repo;
	
	@Autowired
	private EmployeeDetailsRepository empDetail_repo;
	
	public List<ClassFeedbackAnswers> saveClassFeedbackAnswers(ClassFeedbackAnswersDTO cfa) throws Exception {
		
		List<ClassFeedbackAnswers> feedback_list = new ArrayList<ClassFeedbackAnswers>();

		if (cfa_repo.getCountOfFeedback(cfa.getStudent_id(), cfa.getCourse_id(), cfa.getFeedback_window_id(),cfa.getUser_id()) >= 1) {
			throw new Exception("Feedback already given for the selected faculty and course!");
		} else {
			cfa.getRatings().entrySet().forEach(rate -> {
				ClassFeedbackAnswers feedback = new ClassFeedbackAnswers();
				feedback.setActive(cfa.getActive());
				feedback.setClass_feedback_questions_id(rate.getKey());
				feedback.setUser_id(cfa.getUser_id());
				feedback.setRatings(rate.getValue());
				feedback.setCourse_id(cfa.getCourse_id());
				feedback.setCreated_by(cfa.getCreated_by());
				feedback.setCreated_username(cfa.getCreated_username());
				feedback.setRemarks(cfa.getRemarks());
				feedback.setStudent_id(cfa.getStudent_id());
				feedback.setFeedback_window_id(cfa.getFeedback_window_id());
				feedback.setWindow_count(cfa.getWindow_count());
				cfa_repo.save(feedback);
				feedback_list.add(feedback);
				
			});
		return feedback_list;
		}
	}
	
	public List<ClassFeedbackAnswers> listAllFeedback() {
		return cfa_repo.findAllFeedback();
	}
	
	public ClassFeedbackAnswers getClassFeedbackAnswers(Integer class_feedback_answers_id) {
		return cfa_repo.findById(class_feedback_answers_id).orElseThrow(() -> new ResourceNotFoundException("ClassFeedbackAnswers Not Found:" + class_feedback_answers_id));
	}
	
	public ClassFeedbackAnswers updateClassFeedbackAnswers(ClassFeedbackAnswers feedback) {
		return cfa_repo.save(feedback);
	}
	
	public void delete(Integer class_feedback_answers_id) {
		ClassFeedbackAnswers feedback = cfa_repo.findById(class_feedback_answers_id)
				.orElseThrow(() -> new ResourceNotFoundException("StudentClassFeedback Not Found:" + class_feedback_answers_id));
		cfa_repo.updateClassFeedbackAnswers(class_feedback_answers_id);
	}

	public void delete1(Integer class_feedback_answers_id) {
		ClassFeedbackAnswers feedback = cfa_repo.findById(class_feedback_answers_id)
				.orElseThrow(() -> new ResourceNotFoundException("StudentClassFeedback Not Found:" + class_feedback_answers_id));
		cfa_repo.updateClassFeedbackAnswers1(class_feedback_answers_id);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getClassFeedbackAnswersDetailsData() {
	    List<Map<String, Object>> rawData = cfa_repo.getClassFeedbackAnswersDetailsData();

	    List<Map<String, Object>> result = new ArrayList<>();

	    for (Map<String, Object> row : rawData) {
	        Map<String, Object> studentData = new HashMap<>();
	        studentData.put("class_feedback_answers_id", row.get("class_feedback_answers_id"));
	        studentData.put("student_id", row.get("student_id"));
	        studentData.put("student_name", row.get("student_name"));
	        studentData.put("auid", row.get("auid"));
	        studentData.put("feedback_window_id", row.get("feedback_window_id"));
	        studentData.put("from_date", row.get("from_date"));
	        studentData.put("to_date", row.get("to_date"));
	        studentData.put("course_id", row.get("course_id"));
	        studentData.put("course_code", row.get("course_code"));
	        studentData.put("course_name", row.get("course_name"));
	        studentData.put("course_short_name", row.get("course_short_name"));

	        result.add(studentData);
	    }

	    return result;
	}

	public ResponseEntity<Object> classFeedbackAnswersEmployeeDetailsKeyword(Pageable pageable,Integer ac_year_id, Integer school_id,Integer dept_id,Integer emp_id, Object keyword) {
		Page<Object> response1 = cfa_repo.classFeedbackAnswersEmployeeDetailsKeyword(pageable,ac_year_id,school_id,dept_id,emp_id, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}

	public ResponseEntity<Object> classFeedbackAnswersEmployeeDetailsWithoutKeyword(Pageable pageable1,Integer ac_year_id, Integer school_id,Integer dept_id,Integer emp_id) {
		Page<Object> response = cfa_repo.classFeedbackAnswersEmployeeDetailsWithoutKeyword(pageable1,ac_year_id,school_id,dept_id,emp_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public Map<String, Object> getFeedbackRatingReport(Integer employeeId) {

		Map<String, Object> employeeDetails = empDetail_repo.getemployeeDetailsBasedOnEmployeeId(employeeId);
		List<Map<String, Object>> questionWithRating = cfa_repo.getQuestionWithRating(employeeId);

		Map<String, Object> response = new HashMap<>();
		response.put("employeeDetails", employeeDetails);
		response.put("questionWithRating", questionWithRating);

		return response;
	}

	public Map<String, Object> getFeedbackRatingReportSectionWise(Integer employeeId, Integer course_id) {
		Map<String, Object> employeeDetails = empDetail_repo.getemployeeDetailsBasedOnEmployeeId(employeeId);
		List<Map<String, Object>> questionWithRatingSection = cfa_repo.getQuestionWithRatingSection(employeeId,course_id);
		
		Map<String, Object> response = new HashMap<>();
		response.put("employeeDetails", employeeDetails);
		response.put("questionWithRatingSection", questionWithRatingSection);

		return response;
	}

	public List<Map<String, Object>> getCourseDetailsDataFromFeedBack(Integer employeeId) {
		return cfa_repo.getCourseDetailsDataFromFeedBack(employeeId);
	}

	public Map<String, Object> getFeedbackRatingReportForEmployee(Integer employeeId, Integer course_id) {
		Map<String, Object> employeeDetails = empDetail_repo.getemployeeDetailsBasedOnEmployeeId(employeeId);
		List<Map<String, Object>> courseWithRatingSection = cfa_repo.getCourseWithRatingSection(employeeId,course_id);
		
		Map<String, Object> response = new HashMap<>();
		response.put("employeeDetails", employeeDetails);
		response.put("courseWithRatingSection", courseWithRatingSection);

		return response;
	}


}
