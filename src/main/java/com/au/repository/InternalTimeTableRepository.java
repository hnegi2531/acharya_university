package com.au.repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.InternalTimeTable;

@Transactional
@Repository
public interface InternalTimeTableRepository extends JpaRepository<InternalTimeTable, Integer>{

	
	
//	@Query(value = "select count(*) from InternalTimeTable itt where itt.date_of_exam=?1 and itt.time_slots_id=?2 and active=true")
//	public Integer getDateOfExamWithTimeSlotId(Date date_of_exam,Integer time_slots_id);
//	
//	
//	@Query(value = "select count(*) from InternalTimeTable itt where itt.course_id=?1 and active=true")
//	public Integer getCountInternalTimeTableCourse(Integer course_id);
//	
//	
//	
//	@Query(value = "select new map(itt.internal_time_table_id as id,itt.course_id as course_id,itt.exam_time as exam_time,"
//			+ "itt.date_of_exam as date_of_exam,itt.internal_id as internal_id,itt.time_slots_id as time_slots_id,"
//			+ "itt.active as active,itt.week_day as week_day,ca.program_specialization_id as program_specialization_id,"
//			+ "itt.max_marks as max_marks,itt.min_marks as min_marks,"
//			+ "ca.school_id as school_id,ca.year_sem as year_sem,ca.program_id as program_id,ca.ac_year_id as ac_year_id,"
//			+ "itt.created_by as created_by,itt.modified_by as modified_by,itt.created_date as created_date,itt.modified_date as modified_date,"
//			+ "itt.created_username as created_username,itt.modified_username as modified_username) from InternalTimeTable itt "
//			+ "left join CourseAssignment ca on itt.course_id=ca.course_id "
//			+"left join InternalSessionAssignment isa on itt.internal_id=isa.internal_id "
//			+ "where CONCAT(IfNull(itt.created_username,''),'',IfNull(itt.course_id,''),'',IfNull(itt.time_slots_id,''),'',IfNull(itt.date_of_exam,''),"
//			+ "'',IfNull(itt.created_by,''),'',IfNull(itt.created_date,'')) LIKE %?1%")
//	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);
//	
//	
//	@Query(value = "select new map(itt.internal_time_table_id as id,itt.course_id as course_id,itt.exam_time as exam_time,"
//			+ "itt.date_of_exam as date_of_exam,itt.internal_id as internal_id,itt.time_slots_id as time_slots_id,"
//			+ "itt.active as active,itt.week_day as week_day,ca.program_specialization_id as program_specialization_id,"
//			+ "itt.max_marks as max_marks,itt.min_marks as min_marks,"
//			+ "ca.school_id as school_id,ca.year_sem as year_sem,ca.program_id as program_id,ca.ac_year_id as ac_year_id,"
//			+ "itt.created_by as created_by,itt.modified_by as modified_by,itt.created_date as created_date,itt.modified_date as modified_date,"
//			+ "itt.created_username as created_username,itt.modified_username as modified_username) from InternalTimeTable itt "
//			+ "left join CourseAssignment ca on itt.course_id=ca.course_id "
//			+"left join InternalSessionAssignment isa on itt.internal_id=isa.internal_id")
//	public Page<Object> getAllSortedData(Pageable pageable);
//	
//	
//	
//	
//	@Modifying
//	@Query(value = "update InternalTimeTable itt set itt.active=false where itt.internal_time_table_id=?1")
//	public void updateDept(Integer id);
//	
//	
//	@Modifying
//	@Query(value = "update InternalTimeTable itt set itt.active=true where itt.internal_time_table_id=?1")
//	public void updateDept1(Integer id);
//	
//	
//	
//	@Query(value = "Select new map(c.course_id as course_id,c.course_name as course_name,"
//			+ "concat(c.course_name,'-',c.course_code) as course,c.course_short_name as course_short_name) from Course c "
//			+ "where c.course_id in (select itt.course_id from InternalTimeTable itt "
//			 + "where itt.internal_id=?1 and itt.date_of_exam=?2)")
//	public List<HashMap<String, Object>> listIttaCourseBasedOnDate11(Integer internal_id,Date date_of_exam);
//	
//	
//	
//	@Query(value = "select itta.student_ids from InternalTimeTableAssignment itta left join "
//			+ "InternalTimeTable itt on itt.internal_id=itta.internal_id where itt.course_id=1 and itta.internal_id=1")
//	public List<String> getStudentIds(Integer course_id, Integer internal_id);
//	
//	
//	@Query(value = "Select itta1.student_ids as student_ids,sd.student_name as student_name,sd.auid as auid,sd.usn as usn,"
//			+ "itta1.internal_id as internal_id from internal_timetable_assignment itta1 "
//			+"left join student_details sd on sd.student_id=itta1.student_ids "
//			+ "where itta1.internal_id not in (select itt1.internal_id from internal_time_table itt1 where itt1.course_id=?1 and itt1.internal_id=?2)",nativeQuery = true)
//	public List<Map<String, Object>> listOfStudentDetails11(Integer course_id,Integer internal_id);
//	
//	

	

}
