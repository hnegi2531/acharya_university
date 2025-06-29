package com.au.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.InternalSessionAssignment;


@Transactional
@Repository
public interface InternalSessionAssignmentRepository extends JpaRepository<InternalSessionAssignment, Integer> {

	@Query(value = "select count(*) from InternalSessionCreation isa where isa.school_id=?1 and isa.ac_year_id=?2 and isa.program_specialization_id=?3 and isa.course_assignment_id=?4 and "
			+ "isa.internal_master_id=?5 and isa.year_sem=?6 and active=true")
	public Integer getInternalSessionCreations(Integer schoolId, Integer ac_year_id,Integer program_specialization_id, Integer course_assignment_id ,Integer internal_master_id, Integer year_sem);
	
	@Query(value = "select sd.student_id as student_id,sd.auid as auid,sd.student_name as student_name "
			+ "from student_details sd "
			+ "where sd.student_id =?1 and sd.active=true",nativeQuery = true)
	Map<Object, Object> getStudentDetailsData(Integer st);

	
	
	@Query(value = "select itt.max_marks as max_marks,itt.min_marks as min_marks,itt.internal_id as internal_id "
			+ "from internal_time_table itt "
			+"left join internal_session_assignment isa on itt.internal_id=isa.internal_id "
			+ "where isa.internal_id=?2 and isa.ac_year_id=?1 And isa.year_sem=?3 And isa.program_specialization_id=?4 And "
			+ "itt.course_assignment_id=?5 group by itt.internal_id",nativeQuery = true)
	Map<Object, Object> getInternalDetailsData(Integer ac_year_id, Integer internal_id, Integer year_sem,
			Integer program_specialization_id, Integer course_assignment_id);

	
	
	
	@Query(value = "select count(*) from InternalSessionAssignment isa where isa.program_specialization_id=?1 and isa.ac_year_id=?2 "
			+ "and isa.school_id=?3 and active=true")
	public Integer getInternalSessionAssignments(Integer program_specialization_id,Integer ac_year_id,Integer school_id);
//	
//	
////	@Query(value = "select count(*) from InternalSessionAssignment isa where isa.ac_year_id=?1 and active=true")
////	public Integer getCountOfAcademicYear(Integer ac_year_id);
//	
////	@Query(value = "select count(*) from InternalSessionAssignment isa where isa.school_id=?1 and active=true")
////	public Integer getCountOfIsaSchool(Integer school_id);
//	
////	@Query(value = "select count(*) from InternalSessionAssignment isa where isa.year_sem=?1 and active=true")
////	public Integer getCountOfIsaYearSem(Integer year_sem);  
//	
////	@Query(value = "select count(*) from InternalSessionAssignment isa where isa.from_date=?1 and active=true")
////	public Integer getCountOfFrom_Date(Date from_date);  
////	
////	@Query(value = "select count(*) from InternalSessionAssignment isa where isa.to_date=?1 and active=true")
////	public Integer getCountOfTo_Date(Date to_date);
//	
//	@Query(value = "select isa.internal_id as id,isa.internal_master_id as internal_master_id,isa.internal_name as internal_name,"
//			+ "isa.from_date as from_date,isa.to_date as to_date,isa.ac_year_id as ac_year_id,isa.internal_short_name as internal_short_name,"
//			+ "isa.school_id as school_id,isa.program_id as program_id,isa.program_specialization_id as program_specialization_id,"
//			+ "ps.program_specialization_short_name as program_specialization_short_name,p.program_short_name as program_short_name,"
//			+ "isa.year_sem as year_sem,ay.ac_year as ac_year,isa.created_by as created_by,isa.created_username as created_username,isa.created_date as created_date,"
//			+ "isa.modified_date as modified_date,isa.modified_by as modified_by,isa.modified_username as modified_username,isa.active as active,"
//			+ "(select ((LENGTH(group_concat(itta2.student_ids))) - ((LENGTH(REPLACE((group_concat(itta2.student_ids)),\",\",\"\")))-1)) as count From internal_timetable_assignment itta2 "
//			+ "left join internal_session_assignment isa on itta2.internal_id=isa.internal_id) as countOfStudent,"
//			+ "sc.school_name as school_name,sc.school_name_short as school_name_short from internal_session_assignment isa "
//			+ "left join program p on isa.program_id=p.program_id "
//			+ "left join academic_year ay on isa.ac_year_id=ay.ac_year_id "
//			+ "left join schools sc on isa.school_id=sc.school_id "
//			+ "left join program_specialization ps on isa.program_specialization_id=ps.program_specialization_id "
//			+ "left join internal_types it on isa.internal_master_id=it.internal_master_id "
//			+ "where CONCAT(IfNull(isa.created_username,''),'',IfNull(isa.internal_name,''),'',IfNull(isa.internal_short_name,''),'',IfNull(isa.remarks,''),"
//			+ "'',IfNull(isa.created_by,''),'',IfNull(isa.created_date,'')) LIKE %?1% and isa.active=true",nativeQuery=true)
//	public List<Map<String,Object>> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);
//	
//	
//	
//	@Query(value = "select isa.internal_id as id,isa.internal_master_id as internal_master_id,isa.internal_name as internal_name,"
//			+ "isa.from_date as from_date,isa.to_date as to_date,isa.ac_year_id as ac_year_id,isa.internal_short_name as internal_short_name,"
//			+ "isa.school_id as school_id,isa.program_id as program_id,isa.program_specialization_id as program_specialization_id,"
//			+ "ps.program_specialization_short_name as program_specialization_short_name,p.program_short_name as program_short_name,"
//			+ "isa.year_sem as year_sem,ay.ac_year as ac_year,isa.created_by as created_by,isa.created_username as created_username,isa.created_date as created_date,"
//			+ "isa.modified_date as modified_date,isa.modified_by as modified_by,isa.modified_username as modified_username,isa.active as active,"
//			+ "(select ((LENGTH(group_concat(itta2.student_ids))) - ((LENGTH(REPLACE((group_concat(itta2.student_ids)),\",\",\"\")))-1)) as count "
//			+ "From internal_timetable_assignment itta2 "
//			+ "left join internal_session_assignment isa on itta2.internal_id=isa.internal_id) as countOfStudent,"
//		    + "sc.school_name as school_name,sc.school_name_short as school_name_short from internal_session_assignment isa "
//			+ "left join program p on isa.program_id=p.program_id "
//			+ "left join academic_year ay on isa.ac_year_id=ay.ac_year_id "
//			+ "left join schools sc on isa.school_id=sc.school_id "
//		    + "left join program_specialization ps on isa.program_specialization_id=ps.program_specialization_id "
//		    + "left join internal_types it on isa.internal_master_id=it.internal_master_id and isa.active=true",nativeQuery=true)
//	public List<Map<String,Object>> getAllSortedData(Pageable pageable);
//	
//	
//	@Modifying
//	@Query(value = "update InternalSessionAssignment isa set isa.active=false where isa.internal_id=?1")
//	public void updateDept(Integer id);
//	
//	
//	@Modifying
//	@Query(value = "update InternalSessionAssignment isa set isa.active=true where isa.internal_id=?1")
//	public void updateDept1(Integer id);
//
//	
//	
//	@Query(value = "Select isa.internal_name,isa.internal_short_name "
//			+ "from  internal_session_assignment isa "
//			+ "left join internal_time_table itt on itt.internal_id=isa.internal_id "
//			+ "left join course c on c.course_id=itt.course_id "
//			+ "where isa.school_id=?1 and isa.program_id=?2 and isa.program_specialization_id=?3 and isa.ac_year_id=?4 and isa.year_sem=?5 and c.course_id=?6 and isa.active=true",nativeQuery = true)
//	public List<Map<String, Object>> internal_idbasedOnSessionAssignment11(Integer school_id,Integer program_id,Integer program_specialization_id,Integer ac_year_id,Integer year_sem,Integer course_id);
//	



	
}
