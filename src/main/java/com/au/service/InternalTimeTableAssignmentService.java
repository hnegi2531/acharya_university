package com.au.service;

import javax.validation.Valid;
import com.au.model.InternalTimeTableAssignment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.repository.CourseAssignmentRepository;
import com.au.repository.CourseStudentAssignmentRepository;
import com.au.repository.InternalTimeTableAssignmentRepository;
import com.au.repository.InternalTimeTableRepository;
import com.au.response.ResponseHandler;

import com.au.repository.EmployeeDetailsRepository;

import com.au.repository.UserAuthenticationRepository;

import com.au.model.UserAuthentication;

@Service
public class InternalTimeTableAssignmentService {

	@Autowired
	private InternalTimeTableAssignmentRepository internalTimeTableAssignmentRepository;

	@Autowired
	private InternalTimeTableRepository internalTimeTableRepository;

	@Autowired
	private CourseAssignmentRepository courseAssignmentRepository;

	@Autowired
	private CourseStudentAssignmentRepository courseStudentAssignmentRepository;

	@Autowired
	private UserAuthenticationRepository userAuthenticationRepository;

	@Autowired
	private EmployeeDetailsRepository employeeDetailsRepository;

//	public InternalTimeTableAssignment saveInternalTimeTableAssignments(@Valid InternalTimeTableAssignment itta) {
//
//		if (internalTimeTableAssignmentRepository.getCountTimeTableEmployee(itta.getRoom_id(), itta.getEmp_ids(),
//				itta.getTime_slots_id(), itta.getSelected_date()) >= 1) {
//			throw new RuntimeException(
//					"Data  Already Exist with Combination Of employee id and time slot and selected Date");
//		}
//
//		else {
//			internalTimeTableAssignmentRepository.save(itta);
//		}
//
//		return itta;
//
//	}
//
//	public List<InternalTimeTableAssignment> listAll() {
//		return internalTimeTableAssignmentRepository.findAll1();
//	}
//
//	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
//		List<Map<String, Object>> response1 = internalTimeTableAssignmentRepository.findAll1(pageable, keyword);
//		return ResponseHandler.generateResponse(true, HttpStatus.OK, response1);
//	}
//
//	public ResponseEntity<Object> listAll2(Pageable pageable) {
//		List<Map<String, Object>> response = internalTimeTableAssignmentRepository.findAll2(pageable);
//		return ResponseHandler.generateResponse(true, HttpStatus.OK, response);
//	}
//
//	public InternalTimeTableAssignment get(Integer id) {
//		return internalTimeTableAssignmentRepository.findById(id)
//				.orElseThrow(() -> new ResourceNotFoundException("InternalTimeTableAssignment Not Found:" + id));
//	}
//
//	public InternalTimeTableAssignment saveInternalTimeTableAssignments1(@Valid InternalTimeTableAssignment itta) {
//
//		List<Integer> list_std = ResponseHandler.toConvertCommaSeperatedIdsAsList(itta.getStudent_ids());
//		if (internalTimeTableAssignmentRepository.getCountInternalTimeTableAssignments(itta.getRoom_id(), list_std,
//				itta.getEmp_ids(), itta.getSelected_date(), itta.getTime_slots_id()) >= 1) {
//			throw new RuntimeException("room id  Already Exist with Combination Of room id and emp id and student id");
//		} else {
//			internalTimeTableAssignmentRepository.save(itta);
//		}
//		return itta;
//	}
//
//	public void delete(Integer id) {
//		internalTimeTableAssignmentRepository.findById(id)
//				.orElseThrow(() -> new ResourceNotFoundException("Internal TimeTable Assignment Not Found:" + id));
//		internalTimeTableAssignmentRepository.update(id);
//	}
//
//	public void delete1(Integer id) {
//		internalTimeTableAssignmentRepository.findById(id)
//				.orElseThrow(() -> new ResourceNotFoundException("Internal TimeTable Assignment Not Found:" + id));
//		internalTimeTableAssignmentRepository.update1(id);
//	}
//
//	public List<Map<String, Object>> listAllIttaEmpBasedOnTimeAndDate1(Integer time_slots_id, String selected_date,
//			Integer course_id) {
//		return internalTimeTableAssignmentRepository.listAllIttaEmpBasedOnTimeAndDate11(time_slots_id, selected_date,
//				course_id);
//	}
//
//	public List<Map<String, Object>> listAllIttaRoomBasedOnTimeAndDate1(Integer program_specialization_id,
//			Integer course_id, Date date) {
//		List<Integer> assigned_room_id = internalTimeTableAssignmentRepository
//				.getAssignedRooms(program_specialization_id, course_id, date);
//		if (assigned_room_id.isEmpty()) {
//			return internalTimeTableAssignmentRepository.listAllIttaRoomBasedOnTimeAndDate11();
//		} else {
//			return internalTimeTableAssignmentRepository
//					.listAllUnassignedIttaRoomBasedOnTimeAndDate11(assigned_room_id);
//		}
//
//	}
//
//	public List<HashMap<String, Object>> listIttaCourseBasedOnDate1(Integer internal_id, Date date_of_exam) {
//		return internalTimeTableRepository.listIttaCourseBasedOnDate11(internal_id, date_of_exam);
//	}
//
//	public List<HashMap<String, Object>> listOfStudentDetails1(Integer internal_id, Integer course_id,
//			Integer school_id, Integer program_specialization_id, Integer ac_year_id, Integer year_sem) {
//		String course_type = courseAssignmentRepository.getCourseCategoryType11(course_id, school_id,
//				program_specialization_id, ac_year_id, year_sem);
//
//		if (course_type.equalsIgnoreCase("optional")) {
//			List<Integer> std_list_from_course_student_assignment = courseStudentAssignmentRepository
//					.getStudentIds(course_id);
//
//			String s = internalTimeTableAssignmentRepository.getStudentIds(course_id, internal_id);
//			if (s != null) {
//				List<Integer> std_list_from_itta_assignment = ResponseHandler.toConvertCommaSeperatedIdsAsList(
//						internalTimeTableAssignmentRepository.getStudentIds(course_id, internal_id));
//
//				ArrayList<Integer> duplicateList = new ArrayList<Integer>();
//				ArrayList<Integer> uniqueList = new ArrayList<Integer>();
//
//				for (Integer item : std_list_from_course_student_assignment) {
//					if (std_list_from_itta_assignment.contains(item)) {
//
//						duplicateList.add(item);
//					} else {
//
//						uniqueList.add(item);
//					}
//				}
//
//				List<HashMap<String, Object>> data = courseStudentAssignmentRepository.getStudentData1(uniqueList);
//				return data;
//			} else {
//				return courseStudentAssignmentRepository.getStudentData(course_id);
//			}
//		} else {
//			return courseStudentAssignmentRepository.getAllStudentData(school_id, program_specialization_id,
//					ac_year_id);
//		}
//
//	}
//
//	public List<Map<String, Object>> internalTimeTableAssignmentDetailsByUserId(Integer userId) {
//		UserAuthentication userDetails = userAuthenticationRepository.findById(userId)
//				.orElseThrow(() -> new ResourceNotFoundException("User Not Found: " + userId));
//		Integer employeeId = employeeDetailsRepository.getEmpId(userDetails.getEmail());
//		return internalTimeTableAssignmentRepository.internalTimeTableAssignmentDetailsByEmployeeId(employeeId);
//	}
	
}
