package com.au.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.au.dto.InternalAttendanceDto;
import com.au.dto.JwtDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.InternalAttendance;
import com.au.repository.InternalAttendanceRepository;
import com.au.response.ResponseHandler;

@Service
public class InternalAttendanceService {

	@Autowired
	private InternalAttendanceRepository ia_repo;
	
	
	public List<InternalAttendance> saveInternalAttendance(List<InternalAttendance> ia) throws Exception {
		return ia_repo.saveAll(ia);
	}
	
	private Integer getInternalAttendances(Integer student_id,String exam_date,String exam_time,Integer course_id) {
		return ia_repo.getcountOfInternalAttendances(student_id,exam_date,exam_time,course_id);
	}


	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> ia_filtered_response = ia_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, ia_filtered_response);
	}

	
	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> ia_sorted_response = ia_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, ia_sorted_response);
	}

	
	public List<InternalAttendance> listAll1() {
		return ia_repo.findAll11();
	}
	
	public InternalAttendance get(Integer id) {
		return ia_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Internal Attendance Not Found:" + id));
	}

	public InternalAttendance saveInternalAttendances(InternalAttendance internalAttendance) {
		return ia_repo.save(internalAttendance);
	}
	
	public void delete(Integer id) {
		InternalAttendance ia = ia_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Internal Attendance Not Found:" + id));
		ia_repo.updateInternalAttendance(id);
	}

	public void delete1(Integer id) {
		InternalAttendance ia = ia_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Internal Attendance Not Found:" + id));
		ia_repo.updateInternalAttendance1(id);
	}
	
	public List<HashMap<String, Object>> getInternalAttendanceDetailsOfStudent(Integer internal_session_id){
		 return ia_repo.getInternalAttendanceDetailsOfStudent(internal_session_id);
		
	}

	public List<Map<String, Object>> getInternalAttendanceDetailsOfStudentList(List<Integer> internal_session_id , Integer emp_id){
		 return ia_repo.getInternalAttendanceDetailsOfStudentList(internal_session_id,emp_id);
		
	}
	
	
	public List<InternalAttendance> updateInternalAttendances(List<InternalAttendanceDto> internalAttendanceDto, JwtDetails jwtDetails) {
		List<InternalAttendance> list = new ArrayList<InternalAttendance>();
		internalAttendanceDto.forEach(ia -> {

			InternalAttendance attendance = ia_repo.getOne(ia.getExam_attendance_id());

			attendance.setPresent_status(ia.getPresent_status());
			attendance.setModified_username(jwtDetails.getUserName());
			attendance.setModified_by(jwtDetails.getUserId());

			list.add(attendance);
		});

		return ia_repo.saveAll(list);
	}

}
