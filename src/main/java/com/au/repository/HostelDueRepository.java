package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.HostelDue;

@Transactional
@Repository
public interface HostelDueRepository extends JpaRepository<HostelDue, Integer> {
	
	@Query(value = "SELECT hd from HostelDue hd ")
	public List<HostelDue> findAll11();
	
	@Modifying
	@Query(value = "CALL hostel_due_by_student_and_academic_year(:student_id,:ac_year_id) ",nativeQuery=true)
	public void hostelDueProcedureByStudentIdAndAcYearId(@Param("student_id")Integer student_id,@Param("ac_year_id") Integer ac_year_id);
	
	@Modifying
	@Query(value = "CALL hostel_due() ", nativeQuery = true)
	public void hostelDueProcedure();
	
	@Query(value = "SELECT hd from HostelDue hd Where hd.ac_year_id=?1 And hd.student_id=?2 ")
	public HostelDue hostelDueByAcademicYearIdAndStudentId(Integer acYearID,Integer studentId);
	
	@Query(value = "SELECT new map(hd.total_amount as total_amount,hd.paid as paid,hd.due as due,ay.ac_year_id as ac_year_id,ay.ac_year as ac_year) from HostelDue hd "
			+ "left Join Academic_year ay On hd.ac_year_id=ay.ac_year_id Where hd.student_id=?1 ")
	public List<HashMap<String, Object>> hostelDueByStudentId(Integer studentId);


	@Query(value = "select SUM(hd.due) from HostelDue hd where hd.school_id=:schoolId group by hd.school_id")
	public Double getSumOfHostelDue(Integer schoolId);

	@Query(value = "select SUM(hd.due) from HostelDue hd where hd.program_specialization_id=:programSpecializationId group by hd.program_specialization_id")
	Double getSumOfHostelDueByProgramSpecializationId(Integer programSpecializationId);

	@Query(value = " select sum(due) from hostel_due hd " +
			" join student_details sd " +
			" on hd.student_id = sd.student_id " +
			" where sd.auid = ?1 ", nativeQuery = true)
	public Double getSumOfHostelDueByStudent(String auid);
}
