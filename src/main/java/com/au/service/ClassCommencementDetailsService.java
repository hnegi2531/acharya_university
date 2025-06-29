package com.au.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.ClassCommencementDetailsDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.ClassCommencementDetails;
import com.au.repository.ClassCommencementDetailsRepository;
import com.au.repository.ProgramAssigmentRepository;
import com.au.repository.ProgramSpecilizationRepository;
import com.au.repository.StudentDetailsRepository;
import com.au.response.ResponseHandler;

@Service
public class ClassCommencementDetailsService {
	
	@Autowired
	public ClassCommencementDetailsRepository classCommenceDetRepository;
	
	@Autowired
	private StudentDetailsRepository studentDetailsRepository;
	
	@Autowired
	private ProgramAssigmentRepository programAssigmentRepository;
	
	@Autowired
	private ProgramSpecilizationRepository programSpecilizationRepository;
	
	private final Integer  DEFAULT_COUNT=1;
	
	public ResponseEntity<Object> saveClassCommencementDetails(ClassCommencementDetailsDto ccd) throws Exception {
		
		
		List<Integer> savedSpecializtionIds=new ArrayList<>();
		ccd.getProgram_specialization_id().entrySet().stream().forEach(id -> {
			String programType=programAssigmentRepository.getProgramTypeOnProgramAssignmentId(id.getValue());
			if(programType.equalsIgnoreCase("Semester")) {
				if(classCommenceDetRepository.countOfCommenceDetailsOnSem(ccd.getCommencement_id(),ccd.getSchool_id(),id.getKey(),id.getValue(),ccd.getAc_year_id(),ccd.getYear_sem()) >=DEFAULT_COUNT) {
					savedSpecializtionIds.add(id.getKey());
				}
			} else {
				if(classCommenceDetRepository.countOfCommenceDetailsOnYear(ccd.getCommencement_id(),ccd.getSchool_id(),id.getKey(),id.getValue(),ccd.getAc_year_id(),ccd.getCurrent_year()) >= DEFAULT_COUNT) {
					savedSpecializtionIds.add(id.getKey());
				}
			}
		});
		
		if(ObjectUtils.isNotEmpty(savedSpecializtionIds)) {
			String specializationsName=programSpecilizationRepository.specializationNamesWithCommaSeperated(savedSpecializtionIds);
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR,
					"Commencement is already created for " + specializationsName
							+ " in the selected academic year and year or sem");
			
			
		}
		List<ClassCommencementDetails> savedDetails=createClassCommencementDetails(ccd);
		return ResponseHandler.generateResponse(true, HttpStatus.CREATED,savedDetails);
	}
	
	private List<ClassCommencementDetails> createClassCommencementDetails(ClassCommencementDetailsDto ccd) {
		List<ClassCommencementDetails> createdDetails = new ArrayList<ClassCommencementDetails>();
		ccd.getProgram_specialization_id().entrySet().stream().forEach(p -> {
			ClassCommencementDetails ccdetails = new ClassCommencementDetails();
			
			ccdetails.setProgram_specialization_id(p.getKey());
			ccdetails.setProgram_assignment_id(p.getValue());
			ccdetails.setAc_year_id(ccd.getAc_year_id());
			ccdetails.setActive(ccd.getActive());
			ccdetails.setCommencement_id(ccd.getCommencement_id());
			ccdetails.setCreated_by(ccd.getAc_year_id());
			ccdetails.setCreated_date(ccd.getCreated_date());
			ccdetails.setCreated_username(ccd.getCreated_username());
			ccdetails.setFrom_date(ccd.getFrom_date());
			ccdetails.setModified_by(ccd.getModified_by());
			ccdetails.setModified_date(ccd.getModified_date());
			ccdetails.setModified_username(ccd.getModified_username());
			ccdetails.setRemarks(ccd.getRemarks());
			ccdetails.setSchool_id(ccd.getSchool_id());
			ccdetails.setTo_date(ccd.getTo_date());
			ccdetails.setYear_sem(ccd.getYear_sem());
			ccdetails.setFromDate_for_fronted_use(ccd.getFromDate_for_fronted_use());
			ccdetails.setToDate_for_fronted_use(ccd.getToDate_for_fronted_use());
			ccdetails.setCurrent_year(ccd.getCurrent_year());
			classCommenceDetRepository.save(ccdetails);
			
			createdDetails.add(ccdetails);
			
		});
		return createdDetails;
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer ac_year_id, Integer school_id,
															  Integer program_assignment_id, Integer program_specialization_id,
															  Integer year_sem) {

		Page<Object> roles_filtered_response = classCommenceDetRepository.getAllDataFilteredByKeyword(pageable, keyword, ac_year_id, school_id,
				program_assignment_id, program_specialization_id, year_sem);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable, Integer ac_year_id, Integer school_id,
												   Integer program_assignment_id, Integer program_specialization_id, Integer year_sem) {

		Page<Object> roles_sorted_response = classCommenceDetRepository.getAllSortedData(pageable, ac_year_id, school_id,
				program_assignment_id, program_specialization_id, year_sem);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
	}

	public List<ClassCommencementDetails> listAll1() {
		return classCommenceDetRepository.findAll11();
	}
	
	public ClassCommencementDetails get(Integer id) {
		return classCommenceDetRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ClassCommencementDetails Not Found:" + id));
	}
	
	public ClassCommencementDetails saveClassCommencementDetails(ClassCommencementDetails ccd) {
		return classCommenceDetRepository.save(ccd);
	}
	
	public void delete(Integer id) {
		ClassCommencementDetails ccd = classCommenceDetRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ClassCommencementDetails Not Found:" + id));
		classCommenceDetRepository.updateClassCommencementDetails(id);
	}

	public void delete1(Integer id) {
		ClassCommencementDetails ccd = classCommenceDetRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ClassCommencementDetails Not Found:" + id));
		classCommenceDetRepository.updateClassCommencementDetails1(id);
	}

	public List<Map<String ,Object>> getClassCommencementDetails(Integer school_id, Integer student_id, Integer year_sem) {
	     Integer program_specialization_id = studentDetailsRepository.getProgramSpecializationId(student_id);
	     List<Map<String ,Object>> data = classCommenceDetRepository.getClassCommencementDetails(school_id,  program_specialization_id,  year_sem);
		return data;
	}

	public ClassCommencementDetails getClassCommencementDetailsForValidatingTimeTable(Integer ac_year,
			Integer school_id, Integer year_sem, Integer commencement_id, Integer program_specialization_id) {

		return classCommenceDetRepository.getClassCommencementDetailsForValidatingTimeTable( ac_year,
				 school_id,  year_sem,  commencement_id,  program_specialization_id);
	}
	
}
