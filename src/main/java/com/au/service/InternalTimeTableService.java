package com.au.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.InternalTimeTable;
import com.au.repository.CourseRepository;
import com.au.repository.InternalTimeTableRepository;
import com.au.response.ResponseHandler;

@Service
public class InternalTimeTableService {

	
	@Autowired
	private InternalTimeTableRepository itt_repo;
	
	@Autowired
	private CourseRepository course_repository;

	
//	public InternalTimeTable saveInternalTimeTable(InternalTimeTable itts) throws Exception {
//			
//			if(itt_repo.getDateOfExamWithTimeSlotId(itts.getDate_of_exam(),itts.getTime_slots_id()) >= 1) {
//				System.out.println("((((((((((((((((((()))))))))))))))))))) "+itt_repo.getDateOfExamWithTimeSlotId(itts.getDate_of_exam(),itts.getTime_slots_id()));
//				throw new Exception("Combination of DateOfExam and TimeSlotId is Already Assigned");
//		
//			} else if(itt_repo.getCountInternalTimeTableCourse(itts.getCourse_id()) >= 1) {
//				throw new Exception("course id Already Exist");
//			
//			} else {
//				InternalTimeTable itt=itt_repo.save(itts);
//			
//				return itt;
//			}
//		}
//	
//	
//	 public List<InternalTimeTable> listAllActiveInternalTimeTable() {
//			List<InternalTimeTable> itts=itt_repo.findAll();
//			return itts;
//		}
//	    
//	 
//	 public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
//			Page<Object> internalTimeTable_filtered_response = itt_repo.getAllDataFilteredByKeyword(pageable, keyword);
//			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, internalTimeTable_filtered_response);
//		}
//		
//	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
//
//			Page<Object> internalTimeTable_sorted = itt_repo.getAllSortedData(pageable);
//			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, internalTimeTable_sorted);
//		}
//	    
//		
//	public InternalTimeTable get(Integer id) {
//			return itt_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Internal Time Table Not Found:" + id));
//		}
//		
//	public InternalTimeTable saveInternalTimeTables(InternalTimeTable itts) {
//		return itt_repo.save(itts);
//	}
//
//	
//	public void delete(Integer id) {
//		InternalTimeTable itt = itt_repo.findById(id)
//				.orElseThrow(() -> new ResourceNotFoundException("Department Not Found:" + id));
//		itt_repo.updateDept(id);
//	}
//
//	public void delete1(Integer id) {
//		InternalTimeTable itt = itt_repo.findById(id)
//				.orElseThrow(() -> new ResourceNotFoundException("Department Not Found:" + id));
//		itt_repo.updateDept1(id);
//	}
//	
//
//	public List<Map<String, Object>> listAllActiveInternalTimeTableData(Integer school_id,Integer program_id,Integer program_specialization_id,Integer ac_year_id,Integer year_sem) {
//		return course_repository.listActivecourseDatas(school_id,program_id,program_specialization_id,ac_year_id,year_sem);
//	}
//	
//	
//	public List<Map<String, Object>> listAllActiveInternalTimeTableDataBasisOfDOE(Integer internal_id) {
//		return course_repository.listAllActiveInternalTimeTableDataBasisOfDOE(internal_id);
//	}
//	
}
