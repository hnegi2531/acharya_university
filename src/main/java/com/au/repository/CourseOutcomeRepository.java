package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.CourseOutcome;
import com.au.model.Syllabus;

@Transactional
@Repository
public interface CourseOutcomeRepository extends JpaRepository<CourseOutcome, Integer> {

	@Query(value = "select new map(coc.course_outcome_id as id,coc.course_outcome_code as course_outcome_code,"
			+ "c.course_id as course_id,c.course_name as course_name,ca.course_assignment_id as course_assignment_id,"
			+ "coc.course_outcome_objective as course_outcome_objective,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "concat(coc.course_outcome_objective,'-',c.course_name,'-',ca.course_assignment_coursecode,'-',ca.year_sem,'-',ps.program_specialization_short_name) as courseOutcome_objectiveConcate,"
			+ "coc.created_username as created_username,coc.modified_username as modified_username,c.course_code as course_code,"
			+ "coc.created_date as created_date,coc.modified_date as modified_date,coc.created_by as created_by,"
			+ "coc.toxonomy As toxonomy,coc.toxonomy_details As toxonomy_details,"
			+ "coc.modified_by as modified_by,coc.active as active) from CourseOutcome coc "
			+ "Left join CourseAssignment ca on ca.course_assignment_id=coc.course_assignment_id "
			+ "left join Course c on c.course_id=ca.course_id "
			+ "Left join ProgramSpecilization ps on ps.program_specialization_id=ca.program_specialization_id "
			+ "where CONCAT(IfNull(coc.created_username,''),'',IfNull(coc.course_outcome_code,''),'',IfNull(coc.course_outcome_objective,''),'',IfNull(c.course_name,''),"
			+ "'',IfNull(coc.created_by,''),'',IfNull(coc.created_date,'',IfNull(c.course_id,''),'')) LIKE %?1% Group by coc.course_assignment_id")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	
	@Query(value = "select new map(coc.course_outcome_id as id,coc.course_outcome_code as course_outcome_code,"
			+ "c.course_id as course_id,c.course_name as course_name,ca.course_assignment_id as course_assignment_id,"
			+ "coc.course_outcome_objective as course_outcome_objective,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "concat(coc.course_outcome_objective,'-',c.course_name,'-',ca.course_assignment_coursecode,'-',ca.year_sem,'-',ps.program_specialization_short_name) as courseOutcome_objectiveConcate,"
			+ "coc.created_username as created_username,coc.modified_username as modified_username,c.course_code as course_code,"
			+ "coc.created_date as created_date,coc.modified_date as modified_date,coc.created_by as created_by,"
			+ "coc.toxonomy As toxonomy,coc.toxonomy_details As toxonomy_details,"
			+ "coc.modified_by as modified_by,coc.active as active) from CourseOutcome coc "
			+ "Left join CourseAssignment ca on ca.course_assignment_id=coc.course_assignment_id "
			+ "left join Course c on c.course_id=ca.course_id "
			+ "Left join ProgramSpecilization ps on ps.program_specialization_id=ca.program_specialization_id Group by coc.course_assignment_id")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	

	@Query(value = "SELECT coc from CourseOutcome coc where coc.active=true")
	public List<CourseOutcome> findAll11();
	
	
	@Modifying
	@Query(value = "update CourseOutcome coc set coc.active=false where coc.course_outcome_id=?1")
	public void updateCourseOutCome(Integer id);

	@Modifying
	@Query(value = "update CourseOutcome coc set coc.active=true where coc.course_outcome_id=?1")
	public void updateCourseOutCome1(Integer id);


	@Query(value = "select coc from CourseOutcome coc where coc.course_assignment_id=?1 and coc.active=true")
	public List<CourseOutcome> getCourseOutCome(Integer course_assignment_id);


	@Query(value = "select new map(coc.course_outcome_id as id,coc.course_outcome_code as course_outcome_code,"
			+ "c.course_id as course_id,c.course_name as course_name,ca.course_assignment_id as course_assignment_id,"
			+ "coc.course_outcome_objective as course_outcome_objective,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "concat(coc.course_outcome_objective,'-',c.course_name,'-',ca.course_assignment_coursecode,'-',ca.year_sem,'-',ps.program_specialization_short_name) as courseOutcome_objectiveConcate,"
			+ "coc.created_username as created_username,coc.modified_username as modified_username,c.course_code as course_code,"
			+ "coc.created_date as created_date,coc.modified_date as modified_date,coc.created_by as created_by,"
			+ "coc.toxonomy As toxonomy,coc.toxonomy_details As toxonomy_details,"
			+ "coc.modified_by as modified_by,coc.active as active) from CourseOutcome coc "
			+ "Left join CourseAssignment ca on ca.course_assignment_id=coc.course_assignment_id "
			+ "left join Course c on c.course_id=ca.course_id "
			+ "Left join ProgramSpecilization ps on ps.program_specialization_id=ca.program_specialization_id where coc.course_assignment_id=?1 And coc.active=true")
	public List<Map<String, Object>> getCourseOutComeDetails(Integer course_assignment_id);
	

}
