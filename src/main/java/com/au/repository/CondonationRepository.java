package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.au.model.Condonation.Status;
import com.au.dto.CondonationListDTO;
import com.au.model.Condonation;



@Repository
public interface CondonationRepository extends JpaRepository<Condonation, Long> {

	@Query("SELECT new com.au.dto.CondonationListDTO("
			+ "c.condonationId, s.auid, s.usn, rs.current_year, rs.current_sem, co.course_name, "
			+ "c.condonationType, c.remarks, c.additionClass)" + " FROM Condonation c "
			+ " LEFT JOIN Student_Details s ON s.student_id=c.studentId "
			+ " LEFT JOIN ReportingStudents rs ON rs.student_id=s.student_id "
			+ " LEFT JOIN Course co ON co.course_id = c.courseId")
	List<CondonationListDTO> getAllCondonation();

	Condonation findByCondonationId(Long condotionId);
	@Query("SELECT new com.au.dto.CondonationListDTO("
			+ "c.condonationId, s.auid, s.usn, rs.current_year, rs.current_sem, co.course_name, "
			+ "c.condonationType, c.remarks, c.additionClass)" + " FROM Condonation c "
			+ " LEFT JOIN Student_Details s ON s.student_id=c.studentId "
			+ " LEFT JOIN ReportingStudents rs ON rs.student_id=s.student_id "
			+ " LEFT JOIN Course co ON co.course_id = c.courseId where c.princpalStatus=true and c.condonationId=:condonationId")
	List<CondonationListDTO> getCondonationListForPrincipalScreen(@Param("condonationId") Long condonationId);

	Condonation findByStudentIdAndCourseIdAndStatus(Integer studentId, Integer courseId,Status statusApproved);

}
