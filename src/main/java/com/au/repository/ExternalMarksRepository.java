package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.ExternalMarks;

@Transactional
@Repository
public interface ExternalMarksRepository extends JpaRepository<ExternalMarks, Integer>{

	@Query(value = "SELECT em from ExternalMarks em where em.active=true")
	List<ExternalMarks> getAllActiveExternalMarks();
	
	@Modifying
	@Query(value = "update ExternalMarks em set em.active=false where em.external_mark_id=?1")
	public void deactivate(Integer id);

	@Modifying
	@Query(value = "update ExternalMarks em set em.active=true where em.external_mark_id=?1")
	public void activate(Integer id);

	@Query(value = "select new map(em.external_mark_id AS id,"
			+ "em.max_mark AS max_mark,"
			+ "em.min_mark AS min_mark,"
			+ "em.current_year AS current_year,"
			+ "em.current_sem AS current_sem,"
			+ "em.ac_year_id AS ac_year_id,"
			+ "em.course_assignment_id AS course_assignment_id,"
			+ "em.school_id AS school_id,"
			+ "em.program_specialization_id AS program_specialization_id,"
			+ "em.exam_date AS exam_date,"
			+ "ay.ac_year As ac_year,sc.school_name As school_name,sc.school_name_short As school_name_short,"
			+ "ca.course_assignment_coursecode As course_assignment_coursecode,c.course_id As course_id,c.course_name As course_name,"
			+ "c.course_short_name As course_short_name,c.course_code As course_code,"
			+ "ps.program_specialization_name As program_specialization_name,ps.program_specialization_short_name As program_specialization_short_name,"
			+ "Concat(IfNull(ca.course_assignment_coursecode,''),'-',IfNull(c.course_name,'')) as courseName,"
			+ "pa.program_assignment_id As program_assignment_id,pa.number_of_semester As number_of_semester,pa.number_of_years As number_of_years,"
			+ "em.active AS active,"
			+ "em.created_by AS created_by,"
			+ "em.modified_by AS modified_by,"
			+ "em.created_date AS created_date,"
			+ "em.modified_date AS modified_date,"
			+ "em.created_username AS created_username,"
			+ "em.modified_username AS modified_username ) "
			+ "from ExternalMarks em "
	        + "left join Academic_year ay on ay.ac_year_id=em.ac_year_id "
	        + "left join Schools sc on sc.school_id=em.school_id "
	        + "left join CourseAssignment ca on ca.course_assignment_id=em.course_assignment_id "
	        + "left join Course c on c.course_id=ca.course_id "
	        + "left join ProgramSpecilization ps on ps.program_specialization_id=em.program_specialization_id "
	        + "left join ProgramAssigment pa on pa.program_assignment_id=ps.program_assignment_id "
	        + "where CONCAT(IfNull(c.course_short_name,''),'',IfNull(sc.school_name_short,''),'',IfNull(ca.course_assignment_coursecode,''),'',"
	        + "IfNull(ay.ac_year,''),'',IfNull(ps.program_specialization_short_name,''),'',IfNull(em.exam_date,''),'',IfNull(em.current_year,'')) "
	        + "LIKE %?1% ")
	Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);

	@Query(value = "select new map(em.external_mark_id AS id,"
			+ "em.max_mark AS max_mark,"
			+ "em.min_mark AS min_mark,"
			+ "em.current_year AS current_year,"
			+ "em.current_sem AS current_sem,"
			+ "em.ac_year_id AS ac_year_id,"
			+ "em.course_assignment_id AS course_assignment_id,"
			+ "em.school_id AS school_id,"
			+ "em.program_specialization_id AS program_specialization_id,"
			+ "em.exam_date AS exam_date,"
			+ "ay.ac_year As ac_year,sc.school_name As school_name,sc.school_name_short As school_name_short,"
			+ "ca.course_assignment_coursecode As course_assignment_coursecode,c.course_id As course_id,c.course_name As course_name,"
			+ "c.course_short_name As course_short_name,c.course_code As course_code,"
			+ "ps.program_specialization_name As program_specialization_name,ps.program_specialization_short_name As program_specialization_short_name,"
			+ "Concat(IfNull(ca.course_assignment_coursecode,''),'-',IfNull(c.course_name,'')) as courseName,"
			+ "pa.program_assignment_id As program_assignment_id,pa.number_of_semester As number_of_semester,pa.number_of_years As number_of_years,"
			+ "em.active AS active,"
			+ "em.created_by AS created_by,"
			+ "em.modified_by AS modified_by,"
			+ "em.created_date AS created_date,"
			+ "em.modified_date AS modified_date,"
			+ "em.created_username AS created_username,"
			+ "em.modified_username AS modified_username ) "
			+ "from ExternalMarks em "
	        + "left join Academic_year ay on ay.ac_year_id=em.ac_year_id "
	        + "left join Schools sc on sc.school_id=em.school_id "
	        + "left join CourseAssignment ca on ca.course_assignment_id=em.course_assignment_id "
	        + "left join Course c on c.course_id=ca.course_id "
	        + "left join ProgramSpecilization ps on ps.program_specialization_id=em.program_specialization_id "
	        + "left join ProgramAssigment pa on pa.program_assignment_id=ps.program_assignment_id ")
	Page<Object> getAllSortedData(Pageable pageable);

	@Query(value = "SELECT em.course_assignment_id from ExternalMarks em where em.external_mark_id=?1 And em.active=true")
	Integer getcourseAssignment(Integer external_mark_id);



}
