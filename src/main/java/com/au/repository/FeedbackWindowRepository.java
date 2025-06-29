package com.au.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.au.model.FeedbackQuestions;
import com.au.model.FeedbackWindow;

@Repository
public interface FeedbackWindowRepository extends JpaRepository<FeedbackWindow, Long> {

	@Query(value=" select new com.au.model.FeedbackWindow( fw.feedbackWindowId, fw.academicYear, sc.school_name,"
			+ " fw.courseAndBranch, fw.semester, fw.fromDate, fw.toDate, fw.instituteId, fw.created_date , fw.active , fw.program_specialization_id)  from FeedbackWindow  "
			+ "fw left join Schools sc on sc.school_id=fw.instituteId  where fw.feedbackWindowId=:feedbackWindowId  ")
	FeedbackWindow findByFeedbackWindowId(@Param("feedbackWindowId") Long feedbackWindowId);

	@Query(value = "SELECT new map(fw.year AS year,fw.feedbackWindowId AS feedbackWindowId,fw.academicYear AS academicYear,"
			+ "fw.institute AS institute,fw.program_specialization_id AS program_specialization_id,fw.courseAndBranch AS courseAndBranch,fw.branch AS branch,"
			+ "fw.sem AS sem,fw.semester AS semester,fw.fromDate AS fromDate,fw.toDate AS toDate,"
			+ "fw.active AS active,fw.instituteId AS instituteId,fw.createdBy AS createdBy,fw.modifiedBy AS modifiedBy,"
			+ "fw.created_date AS created_date,fw.modified_date AS modified_date,sc.school_name As school_name,sc.school_name_short As school_name_short,"
			+ "CONCAT(IfNull(ps.program_specialization_short_name,''),'-',IfNull(p.program_short_name,'')) as specialization_with_program,"
			+ "ps.program_specialization_name As program_specialization_name,ps.program_specialization_short_name As program_specialization_short_name) "
			+ "From FeedbackWindow fw "
			+ "left join Schools sc on sc.school_id=fw.instituteId  "
			+ "left join ProgramSpecilization ps on ps.program_specialization_id=fw.program_specialization_id "
			+ "Inner join Program p on ps.program_id=p.program_id ")
	public List<Map<String, Object>> getAllActiveFeedbackWindow();

	
//	Order by fw.feedback_window_id desc limit 1 
	@Query(value=" select  * from feedback_window fw  where fw.institute_id=?1 and fw.semester=?2 and fw.program_specialization_id=?3  "
			+ "and  ( ?4 between date(fw.from_date) AND  date(fw.to_date)) And fw.academic_year=?5 ",nativeQuery=true)
	FeedbackWindow checkSessionExistsOrNotForInstitute(Integer instituteId,  String sem, Integer program_specialization_id, String formattedDate, String academicYear);

	@Query(value = "Select fw.feedback_window_id As feedbackWindowId,fw.academic_year As academicYear,fw.course_and_branch As course_and_branch,"
			+ "fw.institute_id As institute_id,fw.from_date As from_date,fw.to_date As to_date,ac.ac_year As ac_year ,"
			+ "CONCAT(IfNull(ps.program_specialization_short_name,''),'-',IfNull(p.program_short_name,'')) as specialization_with_program,"
			+ "CONCAT(IFNULL(ac.ac_year, ''), ' ',IFNULL(DATE_FORMAT(fw.from_date, '%d-%m-%Y'), ''),'/',IFNULL(DATE_FORMAT(fw.to_date, '%d-%m-%Y'), '')) AS concateFeedbackWindow "
			+ "from feedback_window fw "
			+ "left join academic_year ac on ac.ac_year_id=fw.academic_year "
			+ "left join program_specialization ps on ps.program_specialization_id=fw.program_specialization_id "
			+ "Inner join program p on ps.program_id=p.program_id "
			+ "WHERE fw.academic_year = :academicYear " 
	        + "AND fw.program_specialization_id = :program_specialization_id " 
	        + "AND fw.semester = :semester " 
	        + "AND fw.institute_id = :instituteId " 
	        + "AND fw.active = TRUE ",nativeQuery=true)
	public List<Map<String, Object>> feedbackWindowListForDropDown(String academicYear, Integer program_specialization_id, String semester, Integer instituteId);

	   @Query("SELECT f FROM FeedbackWindow f "
	   		+ "WHERE f.academicYear = :academicYear " 
	        + "AND f.program_specialization_id = :program_specialization_id " 
	        + "AND f.semester = :semester " 
	        + "AND f.instituteId = :instituteId " 
	        + "AND f.active = TRUE")
	    List<FeedbackWindow> findByAcademicYearAndCourseAndSemesterAndInstituteIdAndActiveTrue(
	            String academicYear, 
	            Integer program_specialization_id, 
	            String semester, 
	            Integer instituteId);

		@Query(value = "Select CONCAT(IFNULL(ac.ac_year, ''), ' ',IFNULL(DATE_FORMAT(fw.from_date, '%d-%m-%Y'), ''),'/',IFNULL(DATE_FORMAT(fw.to_date, '%d-%m-%Y'), '')) AS concateFeedbackWindow "
				+ "from feedback_window fw "
				+ "left join academic_year ac on ac.ac_year_id=fw.academic_year "
				+ "WHERE fw.academic_year = ?3 " 
		        + "AND fw.program_specialization_id = ?1 " 
		        + "AND fw.institute_id = ?2 " 
		        + "AND fw.active = TRUE Order by feedback_window_id desc limit 1",nativeQuery=true)
	public String getConcateFeedbackWindow(Integer program_specialization_id, Integer schoolId, String academicYear);

	@Query(value = "Select fw.feedback_window_id AS concateFeedbackWindow "
			+ "from feedback_window fw "
			+ "WHERE fw.academic_year = ?3 " 
	        + "AND fw.program_specialization_id = ?1 " 
	        + "AND fw.institute_id = ?2 " 
	        + "AND fw.active = TRUE Order by feedback_window_id desc limit 1",nativeQuery=true)
	public Integer getFeedbackWindowId(Integer program_specialization_id, Integer schoolId, String string);

	@Query(value = "Select count(fw.feedback_window_id) from feedback_window fw "
			+ "WHERE fw.academic_year = ?3 AND fw.program_specialization_id = ?1 AND fw.institute_id = ?2 And fw.semester=?4 AND fw.active = TRUE",nativeQuery=true)
	public Integer getCountFeedbackWindowId(Integer program_specialization_id, Integer schoolId, String academic_year, String semester);
	

	@Query(value=" select  count(*) from feedback_window fw  where fw.institute_id=?1 and fw.program_specialization_id=?2 And fw.active=true And "
			+ "(?3 between date(fw.from_date) AND  date(fw.to_date))",nativeQuery=true)
	Integer checkCount(Integer schoolId,Integer programSpecializationId, String formattedDate);

	@Query(value = "Select fw.semester AS semester from feedback_window fw WHERE fw.feedback_window_id = ?1 ",nativeQuery=true)
	String getFeedBackWindowSemester(Integer feedbackWindowId);
	
}
