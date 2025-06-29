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

import com.au.model.ProgramType;
import com.au.model.StdReportingStudentsHistory;

@Repository
@Transactional

public interface StdReportingStudentsHistoryRepository extends JpaRepository<StdReportingStudentsHistory, Integer>{
/*
	@Query(value = "SELECT * FROM std_reporting_students_history where active=true", nativeQuery = true)
	public List<ProgramType> findAll1();

	@Modifying
	@Query(value = "update StdReportingStudentsHistory pt set pt.active=false where pt.reporting_history_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update StdReportingStudentsHistory pt set pt.active=true where pt.reporting_history_id=?1")
	public void update1(Integer id);
	*/
	
	@Query(value = "select new map(srsh.reporting_history_id as id,srsh.student_id as student_id,srsh.current_year as current_year,"
			+ "srsh.current_sem as current_sem,srsh.remarks as remarks,srsh.reporting_date as reporting_date,"
			+ "srsh.distinct_status as distinct_status,srsh.previous_sem as previous_sem,srsh.previous_year as previous_year,"
			+ "srsh.eligible_reported_status as eligible_reported_status,srsh.program_type_id as program_type_id,"
			+ "srsh.program_specialization_id as program_specialization_id,srsh.school_id as school_id,srsh.created_by as created_by,"
			+ "srsh.modified_by as modified_by,srsh.created_date as created_date,srsh.modified_date as modified_date,srsh.active as active,"
			+ "srsh.created_username as created_username,srsh.modified_username as modified_username,srsh.reported_ac_year_id as reported_ac_year_id) "
			+ "from StdReportingStudentsHistory srsh "
			+ "where CONCAT(IfNull(srsh.reporting_history_id,''),'',IfNull(srsh.student_id,''),'',IfNull(srsh.current_year,''),'',IfNull(srsh.current_sem,''),"
			+ "'',IfNull(srsh.reporting_date,''),'',IfNull(srsh.eligible_reported_status,''),'',IfNull(srsh.created_by,''),'',IfNull(srsh.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(srsh.reporting_history_id as id,srsh.student_id as student_id,srsh.current_year as current_year,"
			+ "srsh.current_sem as current_sem,srsh.remarks as remarks,srsh.reporting_date as reporting_date,"
			+ "srsh.distinct_status as distinct_status,srsh.previous_sem as previous_sem,srsh.previous_year as previous_year,"
			+ "srsh.eligible_reported_status as eligible_reported_status,srsh.program_type_id as program_type_id,"
			+ "srsh.program_specialization_id as program_specialization_id,srsh.school_id as school_id,srsh.created_by as created_by,"
			+ "srsh.modified_by as modified_by,srsh.created_date as created_date,srsh.modified_date as modified_date,srsh.active as active,"
			+ "srsh.created_username as created_username,srsh.modified_username as modified_username,srsh.reported_ac_year_id as reported_ac_year_id) "
			+ "from StdReportingStudentsHistory srsh")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Query(value = "SELECT rsh.reporting_history_id as id,rsh.student_id,sd.auid,sd.usn,sd.student_name,rsh.eligible_reported_status,"
			+ "rsh.remarks,rsh.current_year,rsh.current_sem,rsh.created_by,rsh.created_date,rsh.created_username,"
			+ "rsh.distinct_status,rsh.previous_sem,rsh.previous_year,rsh.reported_ac_year_id,"
			+ "rsh.reporting_date,rsh.section_id,rsh.year_back_status "
			+ "FROM std_reporting_students_history rsh Inner join student_details sd on rsh.student_id=sd.student_id " 
			+ "where rsh.student_id=?1", nativeQuery = true)
	public List<Map<String, Object>> fetchReportingStudentsHistoryByStudentId(Integer student_id);

	@Query(value = "SELECT rsh.reporting_history_id as reporting_history_id,rsh.student_id,sd.auid,sd.usn,sd.student_name,rsh.eligible_reported_status,"
			+ "rsh.remarks,rsh.current_year,rsh.current_sem,rsh.created_by,rsh.created_date,rsh.created_username,"
			+ "rsh.distinct_status,rsh.previous_sem,rsh.previous_year,rsh.reported_ac_year_id,"
			+ "rsh.reporting_date,rsh.section_id,rsh.year_back_status,sc.section_name "
			+ "FROM std_reporting_students_history rsh "
			+"Inner join student_details sd on rsh.student_id=sd.student_id "
			+"Left join section sc on rsh.section_id=sc.section_id "
			+"where rsh.reported_ac_year_id=:acYearId And (:currentYear is Null OR  rsh.current_year=:currentYear) "
			+"And (:currentSem is NUll Or rsh.current_sem=:currentSem) "
			+"And sd.program_specialization_id=:specializationId And rsh.active=true", nativeQuery = true)
	List<Map<String,Object>> studentsByAcYearIdAndCurrentYearSem(Integer acYearId, Integer currentYear, Integer currentSem,Integer specializationId);
}
