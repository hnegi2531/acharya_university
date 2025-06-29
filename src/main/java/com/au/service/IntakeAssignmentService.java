package com.au.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.IntakeAssignmentDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.IntakeAssignment;
import com.au.model.IntakePermit;
import com.au.repository.IntakeAssignmentRepository;
import com.au.repository.IntakePermitRepository;
import com.au.response.ResponseHandler;

@Service
public class IntakeAssignmentService {
	
	@Autowired
	private IntakeAssignmentRepository intake_assign_repo;
	
	@Autowired
	private IntakePermitRepository intake_permit_repo;
	
	private int count=0;
	
	public List<IntakeAssignment> saveIntakeAssignment(IntakeAssignmentDto iad){
		
		iad.getIntake_assignment().stream().forEach(ia -> {
			if (intake_assign_repo.countIntakeAssignmentForCreation(ia.getAc_year_id(), ia.getSchool_id(),
					ia.getProgram_id(), ia.getProgram_specialization_id()) >= 1) {
				
				throw new RuntimeException("Intake Assignment is Already Exist For this Academic Year, School, Program and Specialization");
				
			} else {
				intake_assign_repo.save(ia);
				
			}
		});
		 
		iad.getIntake_assignment().stream().forEach(ia -> {
			iad.getFee_admission_category_id().get(count).entrySet().stream().forEach(ip1 -> {
					IntakePermit ip2 = new IntakePermit();
					ip2.setIntake_id(ia.getIntake_id());
					ip2.setFee_admission_category_id(ip1.getKey());
					ip2.setIntake_permit(ip1.getValue());
					ip2.setCreated_by(ia.getCreated_by());
					ip2.setCreated_username(ia.getCreated_username());
					ip2.setActive(iad.getActive());
					intake_permit_repo.save(ip2);
				});
			count++;
		});
		count=0;
		return iad.getIntake_assignment();
	}
	
	public IntakeAssignment copiedIntakeAssignmentCopied(IntakeAssignment ia){
		if (intake_assign_repo.countIntakeAssignmentForCreation(ia.getAc_year_id(), ia.getSchool_id(),
				ia.getProgram_id(), ia.getProgram_specialization_id()) >= 1) {
			throw new RuntimeException("Intake Assignment is Already Exist For this Academic Year, School, Program and Specialization");

		} else {
			return intake_assign_repo.save(ia);

		}
	}
	
	public List<IntakeAssignment> listAll() {
		return intake_assign_repo.findAll1();
	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = intake_assign_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = intake_assign_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public IntakeAssignment get(Integer id) {
		return intake_assign_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Intake Assignment Is Not Found" + id));
	}
	
	public IntakeAssignment updateIntakeAssignment(IntakeAssignment ia) {
		if (intake_assign_repo.countIntakeAssignmentForUpdate(ia.getIntake_id(),ia.getAc_year_id(), ia.getSchool_id(),
				ia.getProgram_id(), ia.getProgram_specialization_id()) >= 1) {
			
			throw new RuntimeException("Intake Assignment is Already Exist For this Academic Year, School, Program and Specialization");
			
		} else {
			return intake_assign_repo.save(ia);
		}
		
	}
	
	public void delete(Integer id) {
		intake_assign_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Intake Assignment Not Found:" + id));
		intake_assign_repo.update(id);
	}
	
	public void delete1(Integer id) {
		intake_assign_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ProgramType Not Found:" + id));
		intake_assign_repo.update1(id);
	}
	
	public List<HashMap<String,Object>> intakeAssignmentAndPermitDetailsOnAcademicYear(Integer ac_year_id) {
		return intake_assign_repo.intakeAssignmentAndPermitDetailsOnAcademicYear(ac_year_id);
	}
	
	public List<HashMap<String,Object>> intakeAssignmentAndPermitDetails(Integer intake_id) {
		return intake_assign_repo.intakeAssignmentAndPermitDetails(intake_id);
	}
	
	public List<HashMap<String,Object>> intakeAssignmentDetailsForGridView(Integer ac_year_id,Integer school_id,Integer graduation_id) {
		return intake_assign_repo.intakeAssignmentDetailsForGridView(ac_year_id,school_id,graduation_id);
	}
	
	public List<HashMap<String,Object>> intakeAssignmentDetails(Integer intake_id) {
		return intake_assign_repo.intakeAssignmentDetails(intake_id);
	}
	
	public List<Integer> intakeAssignmentProgramSpecializationDetails(Integer ac_year_id,Integer school_id,Integer program_id) {
		return intake_assign_repo.intakeAssignmentProgramSpecializationDetails(ac_year_id,school_id,program_id);
	}
	
	public List<HashMap<String,Object>> intakeNotAssignedfeeAdmissionCategory(Integer acYearId,Integer schoolId,Integer programSpecializationId) {
		return intake_assign_repo.intakeNotAssignedfeeAdmissionCategory(acYearId,schoolId,programSpecializationId);
	}
}
