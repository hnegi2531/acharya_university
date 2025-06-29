package com.au.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.au.repository.StdReportingStudentsHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.InternalSessionCreationDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.ExternalMarks;
import com.au.model.InternalSessionCreation;
import com.au.repository.InternalSessionCreationRepository;
import com.au.response.ResponseHandler;



@Service
public class InternalSessionCreationService {
	
	@Autowired
	private InternalSessionCreationRepository isa_repo;
	
	
//	@Autowired
//	private InternalFacultyRoomAssignment itt_repo;

	@Autowired
	private StdReportingStudentsHistoryRepository reportingStudentsHistoryRepository;
	
   
	public List<InternalSessionCreation> saveInternalSessionCreation(List<InternalSessionCreation>  isas) throws Exception {
    	isas.stream().forEach(intSessCrea -> {
    		
        	if(isa_repo.getCount(intSessCrea.getAc_year_id(),intSessCrea.getProgram_specialization_id(),intSessCrea.getCourse_assignment_id(),
        			intSessCrea.getCurrent_sem(),intSessCrea.getCurrent_year() , intSessCrea.getInternal_master_id()) >= 1) {
    			throw new RuntimeException("Data already present for the given input!");
    		}
    	});
    	
		return isa_repo.saveAll(isas);
    }
	
	public InternalSessionCreation updateExternalMark(InternalSessionCreation con) {
		return isa_repo.save(con);
	}
	
    public ResponseEntity<Object> getAllDataFilteredByKeywordExternal(Pageable pageable, Object keyword) {
    	List<Map<String,Object>> InternalSessionCreation_filtered_response = isa_repo.getAllDataFilteredByKeywordExternal(pageable, keyword);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, InternalSessionCreation_filtered_response);
	}
	
	public ResponseEntity<Object> getAllSortedDataExternal(Pageable pageable) {
		List<Map<String,Object>> InternalSessionCreation_sorted = isa_repo.getAllSortedDataExternal(pageable);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, InternalSessionCreation_sorted);
	}
	
	public List<InternalSessionCreation> saveInternalSessionCreation1(List<InternalSessionCreation>  isas) throws Exception {
//    	isas.stream().forEach(intSessCrea -> {
//    		
//        	if(isa_repo.getCount(intSessCrea.getAc_year_id(),intSessCrea.getProgram_specialization_id(),intSessCrea.getCourse_assignment_id(),
//        			intSessCrea.getCurrent_sem(),intSessCrea.getCurrent_year(), intSessCrea.getInternal_master_id() )>= 1) {
//    			throw new RuntimeException("Data already present for the given input!");
//    		}
//    	});
//    	
		return isa_repo.saveAll(isas);
    }

    public List<InternalSessionCreation> findAll() {
		List<InternalSessionCreation> isas=isa_repo.findAll();
		return isas;
	}
    
    public List<InternalSessionCreation> listAllActiveInternalSessionCreation() {
		List<InternalSessionCreation> isas=isa_repo.listAllActiveInternalSessionCreation();
		return isas;
	}
    
    public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer ac_year_id,
															  Integer school_id, Integer dept_id, Integer program_specialization_id, String internal_short_name) {
    	List<Map<String,Object>> InternalSessionCreation_filtered_response = isa_repo.getAllDataFilteredByKeyword(pageable, keyword, ac_year_id,school_id,dept_id,program_specialization_id,internal_short_name);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, InternalSessionCreation_filtered_response);
	}
	
	public ResponseEntity<Object> getAllSortedData(Pageable pageable1, Integer ac_year_id, Integer school_id,
												   Integer dept_id, Integer program_specialization_id, String internal_short_name) {

		List<Map<String,Object>> InternalSessionCreation_sorted = isa_repo.getAllSortedData(pageable1,
				ac_year_id,school_id,dept_id,program_specialization_id,internal_short_name);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, InternalSessionCreation_sorted);
	}
    
	public InternalSessionCreation get(Integer id) {
		return isa_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Internal Session Assignment Not Found:" + id));
	}
	
	public List<InternalSessionCreation> saveInternalSessionCreations(List<InternalSessionCreation> isc) throws Exception {
    		
//		isc.stream().forEach(isas -> {
//        	if(isa_repo.getInternalSessionCreations(isas.getSchool_id(), isas.getAc_year_id(),isas.getProgram_specialization_id(),isas.getCourse_assignment_id(),isas.getInternal_master_id(),isas.getYear_sem()) >= 1) {
//    			throw new RuntimeException("Data already present for the given input!");
//    		}
//		});			
	  	return isa_repo.saveAll(isc);
		}

	
//	public InternalSessionCreation saveInternalSessionCreations(InternalSessionCreation isas) {
//		return isa_repo.save(isas);
//	}

	
	public void delete(Integer id) {
		InternalSessionCreation isa = isa_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Department Not Found:" + id));
		isa_repo.updateDept(id);
	}

	public void delete1(Integer id) {
		InternalSessionCreation isa = isa_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Department Not Found:" + id));
		isa_repo.updateDept1(id);
	}

	public List<Map<String, Object>> internal_session_idbasedOnSessionAssignment11(Integer school_id,Integer program_id,Integer program_specialization_id,Integer ac_year_id,Integer year_sem,Integer course_id) {
		return isa_repo.internal_session_idbasedOnSessionAssignment11(school_id,program_id,program_specialization_id,ac_year_id,year_sem,course_id);
	}
	
	public String deactivateInternalSessionCreation(Integer internal_session_id) {
		isa_repo.deactivateInternalSessionCreation(internal_session_id);
		isa_repo.deactivateInternalFacultyRoomAssignment(internal_session_id);
		isa_repo.deactivateInternalStudentAssignment(internal_session_id);
		
		return "Internal deactivated successfully";
	}

	public List<Map<String, Object>> getProgramSpecializationFromInternalSessionCreation(Integer ac_year_id, Integer internal_master_id,
			Integer year_sem) {
		List<Map<String, Object>> prog_spec = isa_repo.getProgramSpecialization(ac_year_id, internal_master_id, year_sem);
		return prog_spec;
	}
	
	public String activateInternalSessionCreation(Integer internal_session_id) {
		isa_repo.activateInternalSessionCreation(internal_session_id);
		isa_repo.activateInternalFacultyRoomAssignment(internal_session_id);
		isa_repo.activateInternalStudentAssignment(internal_session_id);
		
		return "Internal activated successfully";
	}

	public List<Map<String, Object>> getAllActiveInternalId(Integer ac_year_id, Integer program_specialization_id,
			Integer year_sem) {
		List<Map<String, Object>> internalId = isa_repo.getAllActiveInternalId(ac_year_id, program_specialization_id, year_sem);
		return internalId;
	}

	public List<Map<String, Object>> getAllActivecourseid(Integer internal_session_id, Integer year_sem) {
		List<Map<String, Object>> courseId = isa_repo.getAllActivecourseid(internal_session_id, year_sem);
		return courseId;
	}

	public List<InternalSessionCreation> getAllActiveInternalSessionCreation() {
		List<InternalSessionCreation> isas=isa_repo.getAllActiveInternalSessionCreation();
		return isas;
	}

	public List<Map<String, Object>> getInternalDetailsData(Integer school_id, Integer ac_year_id, Integer internal_master_id,
			Integer year_sem, Integer program_specialization_id) {
		List<Map<String, Object>> isas=isa_repo.getAllActiveInternalSessionCreation1(school_id, ac_year_id, internal_master_id,  year_sem, program_specialization_id);
		return isas;
	}
	
	public List<Map<String, Object>> getCoursesOnDateOfExamAndTimeSlots(String date_of_exam, Integer time_slots_id) {
		List<Map<String, Object>> coursesOnDateOfExamAndTimeSlots = isa_repo.getCoursesOnDateOfExamAndTimeSlots(date_of_exam, time_slots_id);
		return coursesOnDateOfExamAndTimeSlots;
	}
	
	public List<Map<String, Object>> getAssignedCoursesOnDateOfExamAndTimeSlotsIdAndRoomId(String date_of_exam, Integer time_slots_id, Integer room_id) {
		List<Map<String, Object>> coursesOnDateOfExamAndTimeSlots = isa_repo.getAssignedCoursesOnDateOfExamAndTimeSlotsIdAndRoomId(date_of_exam, time_slots_id, room_id);
		return coursesOnDateOfExamAndTimeSlots;
	}

	public ResponseEntity<Object> studentsForExternalMarksAssignment(Integer acYearId, Integer currentYear, Integer currentSem,Integer specializationId) {
		List<Map<String,Object>> students=reportingStudentsHistoryRepository.studentsByAcYearIdAndCurrentYearSem(acYearId,currentYear,currentSem,specializationId);
		List<Map<String, Object>> response = students.stream().collect(
				Collectors.groupingBy(e -> e.get("student_id"), Collectors.collectingAndThen(
								Collectors.maxBy((e1, e2) -> Integer.compare(
										(int) e1.get("reporting_history_id"),
										(int) e2.get("reporting_history_id")
								)),
								optional -> optional.orElse(null)
						)
				))
				.values()
				.stream()
				.collect(Collectors.toList());
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response);
	}
}
