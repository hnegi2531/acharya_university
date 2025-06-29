package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.ExternalMarksAssignment;

@Transactional
@Repository
public interface ExternalMarksAssignmentRepositoty extends JpaRepository<ExternalMarksAssignment, Integer>{

	@Query(value = "SELECT ema from ExternalMarksAssignment ema where ema.active=true")
	List<ExternalMarksAssignment> getAllActiveExternalMarksAssignment();
	
	
	@Modifying
	@Query(value = "update ExternalMarksAssignment ema set ema.active=false where ema.external_marks_assignment_id=?1")
	public void deactivate(Integer id);

	@Modifying
	@Query(value = "update ExternalMarksAssignment ema set ema.active=true where ema.external_marks_assignment_id=?1")
	public void activate(Integer id);
	
	
	@Query(value = "select new map(ema.external_marks_assignment_id AS id,"
			+ "ema.section_id As section_id,"
			+ "ema.marks_scored As marks_scored,"
			+ "ema.percentage As percentage,"
			+ "ema.external_mark_id AS external_mark_id,"
			+ "ema.student_id AS student_id,"
			+ "em.max_mark AS max_mark,"
			+ "em.min_mark AS min_mark,"
			+ "em.current_year AS current_year,"
			+ "em.current_sem AS current_sem,"
			+ "em.ac_year_id AS ac_year_id,"
			+ "em.course_assignment_id AS course_assignment_id,"
			+ "em.school_id AS school_id,"
			+ "em.program_specialization_id AS program_specialization_id,"
			+ "em.exam_date AS exam_date,"
			+ "sd.auid As auid,sd.usn As usn,sd.student_name As student_name,sd.acharya_email As acharya_email,"
			+ "ay.ac_year As ac_year,sc.school_name As school_name,sc.school_name_short As school_name_short,"
			+ "ca.course_assignment_coursecode As course_assignment_coursecode,c.course_id As course_id,c.course_name As course_name,"
			+ "c.course_short_name As course_short_name,c.course_code As course_code,pr.program_name As program_name,pr.program_short_name As program_short_name,"
			+ "ps.program_specialization_name As program_specialization_name,ps.program_specialization_short_name As program_specialization_short_name,"
			+ "Concat(IfNull(ca.course_assignment_coursecode,''),'-',IfNull(c.course_name,'')) as courseName,pr.program_id As program_id,"
			+ "pa.program_assignment_id As program_assignment_id,pa.number_of_semester As number_of_semester,pa.number_of_years As number_of_years,"
			+ "s.section_id As section_id,s.section_name As section_name,s.remarks As remarks,"
			+ "ema.active AS active,"
			+ "ema.created_by AS created_by,"
			+ "ema.modified_by AS modified_by,"
			+ "ema.created_date AS created_date,"
			+ "ema.modified_date AS modified_date,"
			+ "ema.created_username AS created_username,"
			+ "ema.modified_username AS modified_username ) "
			+ "from ExternalMarksAssignment ema "
			+ "left join ExternalMarks em on em.external_mark_id=em.external_mark_id "
	        + "left join Student_Details sd on sd.student_id=ema.student_id "
	        + "left join Academic_year ay on ay.ac_year_id=em.ac_year_id "
	        + "left join Schools sc on sc.school_id=em.school_id "
	        + "left join CourseAssignment ca on ca.course_assignment_id=em.course_assignment_id "
	        + "left join Course c on c.course_id=ca.course_id "
	        + "left join ProgramSpecilization ps on ps.program_specialization_id=em.program_specialization_id "
	        + "left join Program pr on pr.program_id=ps.program_id "
	        + "left join ProgramAssigment pa on pa.program_assignment_id=ps.program_assignment_id "
	        + "left join SectionAssignment sa on ps.program_specialization_id=sa.program_specialization_id "
	        + "left join Section s on s.section_id=sa.section_id "
	        + "where CONCAT(IfNull(sd.student_name,''),'',IfNull(sc.school_name_short,''),'',IfNull(ca.course_assignment_coursecode,''),'',"
	        + "IfNull(pr.program_short_name,''),'',IfNull(ps.program_specialization_short_name,''),'',IfNull(em.exam_date,''),'',IfNull(sd.auid,'')) "
	        + "LIKE %?1% group by ema.external_marks_assignment_id")
	Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);

	@Query(value = "select new map(ema.external_marks_assignment_id AS id,"
			+ "ema.section_id As section_id,"
			+ "ema.marks_scored As marks_scored,"
			+ "ema.percentage As percentage,"
			+ "ema.external_mark_id AS external_mark_id,"
			+ "ema.student_id AS student_id,"
			+ "em.max_mark AS max_mark,"
			+ "em.min_mark AS min_mark,"
			+ "em.current_year AS current_year,"
			+ "em.current_sem AS current_sem,"
			+ "em.ac_year_id AS ac_year_id,"
			+ "em.course_assignment_id AS course_assignment_id,"
			+ "em.school_id AS school_id,"
			+ "em.program_specialization_id AS program_specialization_id,"
			+ "em.exam_date AS exam_date,"
			+ "sd.auid As auid,sd.usn As usn,sd.student_name As student_name,sd.acharya_email As acharya_email,"
			+ "ay.ac_year As ac_year,sc.school_name As school_name,sc.school_name_short As school_name_short,"
			+ "ca.course_assignment_coursecode As course_assignment_coursecode,c.course_id As course_id,c.course_name As course_name,"
			+ "c.course_short_name As course_short_name,c.course_code As course_code,pr.program_name As program_name,pr.program_short_name As program_short_name,"
			+ "ps.program_specialization_name As program_specialization_name,ps.program_specialization_short_name As program_specialization_short_name,"
			+ "Concat(IfNull(ca.course_assignment_coursecode,''),'-',IfNull(c.course_name,'')) as courseName,pr.program_id As program_id,"
			+ "pa.program_assignment_id As program_assignment_id,pa.number_of_semester As number_of_semester,pa.number_of_years As number_of_years,"
			+ "s.section_id As section_id,s.section_name As section_name,s.remarks As remarks,"
			+ "ema.active AS active,"
			+ "ema.created_by AS created_by,"
			+ "ema.modified_by AS modified_by,"
			+ "ema.created_date AS created_date,"
			+ "ema.modified_date AS modified_date,"
			+ "ema.created_username AS created_username,"
			+ "ema.modified_username AS modified_username ) "
			+ "from ExternalMarksAssignment ema "
			+ "left join ExternalMarks em on em.external_mark_id=em.external_mark_id "
	        + "left join Student_Details sd on sd.student_id=ema.student_id "
	        + "left join Academic_year ay on ay.ac_year_id=em.ac_year_id "
	        + "left join Schools sc on sc.school_id=em.school_id "
	        + "left join CourseAssignment ca on ca.course_assignment_id=em.course_assignment_id "
	        + "left join Course c on c.course_id=ca.course_id "
	        + "left join ProgramSpecilization ps on ps.program_specialization_id=em.program_specialization_id "
	        + "left join Program pr on pr.program_id=ps.program_id "
	        + "left join ProgramAssigment pa on pa.program_assignment_id=ps.program_assignment_id "
	        + "left join SectionAssignment sa on ps.program_specialization_id=sa.program_specialization_id "
	        + "left join Section s on s.section_id=sa.section_id group by ema.external_marks_assignment_id")
	Page<Object> getAllSortedData(Pageable pageable);
}
