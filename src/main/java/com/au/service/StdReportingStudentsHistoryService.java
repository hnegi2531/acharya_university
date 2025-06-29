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
import org.springframework.transaction.annotation.Transactional;
import com.au.exception.ResourceNotFoundException;
import com.au.model.ProgramAssigment;
import com.au.model.StdReportingStudentsHistory;
import com.au.repository.ProgramAssigmentRepository;
import com.au.repository.StdReportingStudentsHistoryRepository;
import com.au.repository.StudentDetailsRepository;
import com.au.response.ResponseHandler;

@Service
public class StdReportingStudentsHistoryService {

	@Autowired
	private StdReportingStudentsHistoryRepository r_repo;
	
	@Autowired
	private StudentDetailsRepository stu_repo;
	
	@Autowired
	private ProgramAssigmentRepository progass_repo;
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		
		Page<Object> reportingStudentsHistory_filtered_response = r_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, reportingStudentsHistory_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> reportingStudentsHistory_sorted_response = r_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, reportingStudentsHistory_sorted_response);
	}
	
	public StdReportingStudentsHistory save_StdReportingStudentsHistory(StdReportingStudentsHistory r) {
		return r_repo.save(r);
	}
	
	public List<StdReportingStudentsHistory> saveMultipleStdReportingStudentsHistory(List<StdReportingStudentsHistory> r) {
		return r_repo.saveAll(r);
	}
	
	public StdReportingStudentsHistory get(Integer id) {
        return r_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("StdReportingStudentsHistory Not Found:"+id));
    }
     
    public void delete(Integer id) {
    	StdReportingStudentsHistory cc = r_repo.findById(id)
    	.orElseThrow(()-> new ResourceNotFoundException("StdReportingStudentsHistory Not Found:"+id));    	
    	r_repo.delete(cc);
    }

    public List<Map<String, Object>> fetchAllStudentDetailsForHistoryIndexOnSem(Integer school_id,Integer program_id,Integer program_specialization_id, Integer current_sem) {
		return stu_repo.fetchAllStudentDetailssWithEligibleStatusOnSem(school_id, program_id,program_specialization_id,current_sem);
	}
	
	public List<Map<String, Object>> fetchAllStudentDetailsForHistoryIndexOnYear(Integer school_id, Integer program_id,Integer program_specialization_id, Integer current_year) {
		return stu_repo.fetchAllStudentDetailssWithEligibleStatusOnYear( school_id, program_id,program_specialization_id,current_year);
	}
	
	public List<Map<String, Object>> fetchReportingStudentsHistoryByStudentId(Integer student_id) {
		return r_repo.fetchReportingStudentsHistoryByStudentId(student_id);
	}
	
	public List<HashMap<String, Object>> fetchProgramWithSpecializationBySchoolId(Integer school_id) {
		return progass_repo.fetchProgramWithSpecializationBySchoolId(school_id);
	}
	
	
	public List<ProgramAssigment> getNumOfSemAndYearByProgram_IdAndSchool_Id(Integer program_id,Integer school_id) {
		return progass_repo.getNumOfSemAndYearByProgram_IdAndSchool_Id(program_id,school_id);
	}

}
