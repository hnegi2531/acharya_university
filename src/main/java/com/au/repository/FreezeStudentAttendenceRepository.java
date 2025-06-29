package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.au.dto.FreezeStudentAttendenceDTO;
import com.au.model.FreezeStudentAttendence;

@Repository
public interface FreezeStudentAttendenceRepository extends JpaRepository<FreezeStudentAttendence, Long>{

	@Query(value="select new com.au.dto.FreezeStudentAttendenceDTO(s.freezeStudentAttendenceId ,s.academicYear, s.percentage, sc.school_name, s.created_date, s.createdBy , sc.school_id ,s.active)  "
			+ "from FreezeStudentAttendence s "
			+ " left join Schools sc on sc.school_id=s.instituteId ")
	List<FreezeStudentAttendenceDTO> getAllFreezeStudentList();

	@Query(value="select new com.au.dto.FreezeStudentAttendenceDTO(s.freezeStudentAttendenceId, s.academicYear, s.percentage, sc.school_name, s.created_date, s.createdBy, sc.school_id , s.active) "
			+ " from FreezeStudentAttendence s "
			+ " left join Schools sc on sc.school_id=s.instituteId  where s.active=true and s.freezeStudentAttendenceId=:freezeId ")
	FreezeStudentAttendenceDTO getfreezeStudentAttendenceById(@Param("freezeId") Long freezeId);

	FreezeStudentAttendence findByFreezeStudentAttendenceId(Long freezeId);

	FreezeStudentAttendence findByInstituteIdAndAcademicYear(Integer instituteId, String academicYear);

	FreezeStudentAttendence findByInstituteId(Integer schoolId);

	
	@Query(value="select s.auid as auid,s.student_id As studentId , s.student_name as studentName,"
			+ "     CAST(CAST(ROUND((SUM(CASE WHEN sa.present_status = 1 THEN 1 ELSE 0 END) / COUNT(sa.student_attendance_id)) * 100) AS SIGNED) AS CHAR) AS actualPercentage,"
			+ " sa.student_attendance_id  as studentAttendenceId,sa.section_id AS section_id,sa.course_id AS course_id,"
			+ "fsa.percentage AS assignPercentage , rs.current_sem As current_sem,rs.current_year As current_year from student_details s "
			+ " left join student_attendance sa on sa.student_id=s.student_id "
			+ " left join freeze_student_attendence fsa on fsa.institute_id=s.school_id "
			+ " left join reporting_students rs on rs.student_id=s.student_id "
			+ " where s.student_id =?1 And course_id =?2 "
			+ " group by  s.auid,s.student_name,sa.course_id ", nativeQuery = true)
	public Map<String, Object> getAllPercentage(Integer studentId, Integer courseId);



}
