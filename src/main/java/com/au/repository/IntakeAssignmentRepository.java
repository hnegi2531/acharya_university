package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.IntakeAssignment;

@Transactional
@Repository
public interface IntakeAssignmentRepository extends JpaRepository<IntakeAssignment , Integer> {
	
	@Query(value ="Select count(*) From IntakeAssignment ia "
			+ "Where ia.ac_year_id=?1 and ia.school_id=?2 and ia.program_id =?3 and ia.program_specialization_id=?4 and ia.active=true")
	public Integer countIntakeAssignmentForCreation(Integer ac_year_id, Integer school_id, Integer program_id, Integer program_specialization_id);
	
	@Query(value = "SELECT ia FROM IntakeAssignment ia where ia.active=true")
	public List<IntakeAssignment> findAll1();
	
	@Query(value ="Select new map(ia.intake_id as id,ia.school_id as school_id,ia.program_id as program_id,ia.program_specialization_id as program_specialization_id,"
			+ "ia.ac_year_id as ac_year_id,ia.maximum_intake as maximum_intake,ia.actual_intake as actual_intake,ia.remarks as remarks,"
			+ "ia.created_date as created_date,ia.created_by as created_by,ia.active as active,ia.created_username as created_username,"
			+ "sc.school_name_short as school_name_short,p.program_short_name as program_short_name,ia.program_assignment_id as program_assignment_id,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,ay.ac_year as ac_year,ay.current_year as current_year) From IntakeAssignment ia "
			+ "Left join Schools sc On sc.school_id=ia.school_id "
			+ "Left join Program p On p.program_id=ia.program_id "
			+ "Left join ProgramSpecilization ps On ps.program_specialization_id=ia.program_specialization_id "
			+ "Left join Academic_year ay  On ay.ac_year_id=ia.ac_year_id "
			+ "Where CONCAT(IfNull(ia.intake_id,''),'',IfNull(ia.maximum_intake,''),'',IfNull(ia.actual_intake,''),'',"
			+ "IfNull(ia.created_date,''),'',IfNull(ia.created_username,''),'',IfNull(sc.school_name_short,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(ia.intake_id as id,ia.school_id as school_id,ia.program_id as program_id,ia.program_specialization_id as program_specialization_id,"
			+ "ia.ac_year_id as ac_year_id,ia.maximum_intake as maximum_intake,ia.actual_intake as actual_intake,ia.remarks as remarks,"
			+ "ia.created_date as created_date,ia.created_by as created_by,ia.active as active,ia.created_username as created_username,"
			+ "sc.school_name_short as school_name_short,p.program_short_name as program_short_name,ia.program_assignment_id as program_assignment_id,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,ay.ac_year as ac_year,ay.current_year as current_year) From IntakeAssignment ia "
			+ "Left join Schools sc On sc.school_id=ia.school_id "
			+ "Left join Program p On p.program_id=ia.program_id "
			+ "Left join ProgramSpecilization ps On ps.program_specialization_id=ia.program_specialization_id "
			+ "Left join Academic_year ay  On ay.ac_year_id=ia.ac_year_id ")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value ="Select count(*) From IntakeAssignment ia "
			+ "Where ia.intake_id !=?1 and ia.ac_year_id=?2 and ia.school_id=?3 and ia.program_id =?4 and ia.program_specialization_id=?5 and ia.active=true")
	public Integer countIntakeAssignmentForUpdate(Integer intake_id,Integer ac_year_id, Integer school_id, Integer program_id, Integer program_specialization_id);
	
	
	@Modifying
	@Query(value = "Update IntakeAssignment ia set ia.active=false where ia.intake_id=?1")
	public void update(Integer id);
	
	@Modifying
	@Query(value = "Update IntakeAssignment ia set ia.active=true where ia.intake_id=?1")
	public void update1(Integer id);
	
	@Query(value ="Select new map(ia.intake_id as intake_id,ia.school_id as school_id,ia.program_id as program_id,ia.program_specialization_id as program_specialization_id,"
			+ "ia.ac_year_id as ac_year_id,ia.maximum_intake as maximum_intake,ia.actual_intake as actual_intake,ia.remarks as remarks,"
			+ "ia.created_date as created_date,ia.created_by as created_by,ia.active as active,ia.created_username as created_username,"
			+ "sc.school_name_short as school_name_short,p.program_short_name as program_short_name,ip.intake_permit as intake_permit,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,ay.ac_year as ac_year,ip.intake_permit_id as id,"
			+ "fasc.fee_admission_sub_category_short_name as fee_admission_sub_category_short_name,"
			+ "ip.fee_admission_sub_category_id as fee_admission_sub_category_id,fac.fee_admission_category_type as fee_admission_category_type,"
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,fac.fee_admission_category_id as fee_admission_category_id) From IntakeAssignment ia "
			+ "Left join Schools sc On sc.school_id=ia.school_id "
			+ "Left join Program p On p.program_id=ia.program_id "
			+ "Left join ProgramSpecilization ps On ps.program_specialization_id=ia.program_specialization_id "
			+ "Left join Academic_year ay  On ay.ac_year_id=ia.ac_year_id "
			+ "Inner join IntakePermit ip  On ip.intake_id=ia.intake_id "
			+ "Left Join FeeAdmissionCategory fac on fac.fee_admission_category_id=ip.fee_admission_category_id "
			+ "Left Join FeeAdmissionSubCategory fasc on fasc.fee_admission_sub_category_id=ip.fee_admission_sub_category_id Where ia.ac_year_id=?1 and ia.active=true")
	public List<HashMap<String,Object>> intakeAssignmentAndPermitDetailsOnAcademicYear(Integer ac_year_id);
	
	@Query(value ="Select new map(ia.intake_id as intake_id,ia.school_id as school_id,ia.program_id as program_id,ia.program_specialization_id as program_specialization_id,"
			+ "ia.ac_year_id as ac_year_id,ia.maximum_intake as maximum_intake,ia.actual_intake as actual_intake,ia.remarks as remarks,"
			+ "ia.created_date as created_date,ia.created_by as created_by,ia.active as active,ia.created_username as created_username,"
			+ "sc.school_name_short as school_name_short,p.program_short_name as program_short_name,ip.intake_permit as intake_permit,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,ay.ac_year as ac_year,ip.intake_permit_id as id,"
			+ "fasc.fee_admission_sub_category_short_name as fee_admission_sub_category_short_name,"
			+ "ip.fee_admission_sub_category_id as fee_admission_sub_category_id,fac.fee_admission_category_type as fee_admission_category_type,"
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,fac.fee_admission_category_id as fee_admission_category_id) From IntakeAssignment ia "
			+ "Left join Schools sc On sc.school_id=ia.school_id "
			+ "Left join Program p On p.program_id=ia.program_id "
			+ "Left join ProgramSpecilization ps On ps.program_specialization_id=ia.program_specialization_id "
			+ "Left join Academic_year ay  On ay.ac_year_id=ia.ac_year_id "
			+ "Inner join IntakePermit ip  On ip.intake_id=ia.intake_id "
			+ "Left Join FeeAdmissionCategory fac on fac.fee_admission_category_id=ip.fee_admission_category_id "
			+ "Left Join FeeAdmissionSubCategory fasc on fasc.fee_admission_sub_category_id=ip.fee_admission_sub_category_id Where ia.intake_id=?1 and ia.active=true")
	public List<HashMap<String,Object>> intakeAssignmentAndPermitDetails(Integer intake_id);
	
	@Query(value ="Select new map(ia.intake_id as intake_id,ia.school_id as school_id,ia.program_id as program_id,ia.program_specialization_id as program_specialization_id,"
			+ "ia.ac_year_id as ac_year_id,ia.maximum_intake as maximum_intake,ia.actual_intake as actual_intake,ia.remarks as remarks,"
			+ "ia.created_date as created_date,ia.created_by as created_by,ia.active as active,ia.created_username as created_username,"
			+ "sc.school_name_short as school_name_short,p.program_short_name as program_short_name,gr.graduation_name_short as graduation_name_short,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,ay.ac_year as ac_year,ia.program_assignment_id as program_assignment_id,"
			+ "ps.program_specialization_name as program_specialization_name) From IntakeAssignment ia "
			+ "Left join Schools sc On sc.school_id=ia.school_id "
			+ "Left join Program p On p.program_id=ia.program_id "
			+ "Left join ProgramSpecilization ps On ps.program_specialization_id=ia.program_specialization_id "
			+ "Left join Academic_year ay On ay.ac_year_id=ia.ac_year_id "
			+ "Left join ProgramAssigment pa On pa.program_assignment_id=ia.program_assignment_id "
			+ "Left join Graduation gr On gr.graduation_id=pa.graduation_id Where ia.ac_year_id=?1 And ia.school_id=?2 And gr.graduation_id =?3 And ia.active=true")
	public List<HashMap<String,Object>> intakeAssignmentDetailsForGridView(Integer ac_year_id,Integer school_id,Integer graduation_id);
	
	@Query(value ="Select new map(ia.intake_id as intake_id,ia.school_id as school_id,ia.program_id as program_id,ia.program_specialization_id as program_specialization_id,"
			+ "ia.ac_year_id as ac_year_id,ia.maximum_intake as maximum_intake,ia.actual_intake as actual_intake,ia.remarks as remarks,"
			+ "ia.created_date as created_date,ia.created_by as created_by,ia.active as active,ia.created_username as created_username,"
			+ "sc.school_name_short as school_name_short,p.program_short_name as program_short_name,ia.program_assignment_id as program_assignment_id,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,ay.ac_year as ac_year,gr.graduation_name_short as graduation_name_short) From IntakeAssignment ia "
			+ "Left join Schools sc On sc.school_id=ia.school_id "
			+ "Left join Program p On p.program_id=ia.program_id "
			+ "Left join ProgramSpecilization ps On ps.program_specialization_id=ia.program_specialization_id "
			+ "Left join Academic_year ay  On ay.ac_year_id=ia.ac_year_id "
			+ "Inner join ProgramAssigment pa On pa.program_assignment_id=ia.program_assignment_id "
			+ "Left join Graduation gr On gr.graduation_id=pa.graduation_id Where ia.intake_id=?1")
	public List<HashMap<String,Object>> intakeAssignmentDetails(Integer intake_id);
	
	@Query(value ="Select ia.program_specialization_id From IntakeAssignment ia Where ia.ac_year_id=?1 And ia.school_id=?2 And ia.program_id=?3 And ia.active=true")
	public List<Integer> intakeAssignmentProgramSpecializationDetails(Integer ac_year_id,Integer school_id,Integer program_id);
	
	
	
	@Query(value ="Select new map(fac.fee_admission_category_id as fee_admission_category_id,fac.year_sem as year_sem,"
			+ "fac.fee_admission_category_type as fee_admission_category_type,fac.is_regular as is_regular,"
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,"
			+ "fac.created_date as created_date,fac.created_by as created_by,fac.active as active,fac.is_check as is_check,"
			+ "fac.is_sub_category_applicable as is_sub_category_applicable,fac.created_username as created_username) From FeeAdmissionCategory fac "
			+ "Where fac.fee_admission_category_id not in (Select ip.fee_admission_category_id From IntakeAssignment ia "
			+ "Inner join IntakePermit ip  On ip.intake_id=ia.intake_id Where ia.ac_year_id=?1 And ia.school_id=?2 And ia.program_specialization_id=?3 And ia.active=true ) And fac.active=true ")
	public List<HashMap<String,Object>> intakeNotAssignedfeeAdmissionCategory(Integer acYearId,Integer schoolId,Integer programSpecializationId);

}
