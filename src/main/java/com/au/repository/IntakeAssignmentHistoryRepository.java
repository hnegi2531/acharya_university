package com.au.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.au.model.IntakeAssignmentHistory;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Repository
public interface IntakeAssignmentHistoryRepository extends JpaRepository<IntakeAssignmentHistory, Integer>{

	
	@Query(value = "Select new map(iah.intake_history_id as intake_history_id,iah.intake_id as intake_id,iah.school_id as school_id,iah.program_id as program_id,iah.program_specialization_id as program_specialization_id,"
			+ "iah.ac_year_id as ac_year_id,iah.maximum_intake as maximum_intake,iah.actual_intake as actual_intake,iah.remarks as remarks,"
			+ "iah.created_date as created_date,iah.created_by as created_by,iah.active as active,iah.created_username as created_username,"
			+ "sc.school_name_short as school_name_short,p.program_short_name as program_short_name,gr.graduation_name_short as graduation_name_short,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,ay.ac_year as ac_year) From IntakeAssignmentHistory iah "
			+ "Left join Schools sc On sc.school_id=iah.school_id "
			+ "Left join Program p On p.program_id=iah.program_id "
			+ "Left join ProgramSpecilization ps On ps.program_specialization_id=iah.program_specialization_id "
			+ "Left join Academic_year ay  On ay.ac_year_id=iah.ac_year_id "
			+ "Left join ProgramAssigment pa On pa.program_assignment_id=iah.program_assignment_id "
			+ "Left join Graduation gr On gr.graduation_id=pa.graduation_id Where iah.intake_id=?1")
	public List<HashMap<String,Object>> intakeAssignmentHistoryDetails(Integer intake_id);
	
}
