package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.FeedbackAllowForStudent;

@Transactional
@Repository
public interface FeedbackAllowForStudentRepository extends JpaRepository<FeedbackAllowForStudent, Integer>  {

	@Query(value = "SELECT mb from FeedbackAllowForStudent mb where mb.active=true")
	List<FeedbackAllowForStudent> getAllActiveFeedbackAllowForStudent();
	
	
	@Query(value ="SELECT fa.feedback_allowfor_student_id AS id, "
			+ "       fa.student_id AS student_id, "
			+ "       fa.course_id AS course_id, "
			+ "       fa.feedback_window_id AS feedback_window_id, "
			+ "       fa.ac_year_id AS ac_year_id, "
			+ "       fa.school_id AS school_id, "
			+ "       fa.created_date AS created_date, "
			+ "       fa.modified_date AS modified_date, "
			+ "       fa.active AS active, "
			+ "       fa.created_username AS created_username, "
			+ "       fa.modified_username AS modified_username, "
			+ "       fa.created_by AS created_by, "
			+ "       fa.modified_by AS modified_by, "
			+ "       sd.student_name AS student_name, "
			+ "       sd.auid AS auid, "
			+ "       sd.acharya_email AS acharya_email, "
			+ "       sd.usn AS usn, "
			+ "       c.course_name AS course_name, "
			+ "       ac.ac_year AS ac_year, "
			+ "       ac.current_year AS current_year, "
			+ "       sc.school_name AS school_name, "
			+ "       sc.school_name_short AS school_name_short, "
			+ "       CONCAT(IFNULL(DATE_FORMAT(fw.from_date, '%d-%m-%Y'), ''), '/', IFNULL(DATE_FORMAT(fw.to_date, '%d-%m-%Y'), '')) AS windowPeriod, "
			+ "       fw.to_date AS toDate,fw.from_date AS fromDate, "
			+ "       CONCAT(IFNULL(c.course_name, ''), '-', IFNULL(c.course_code, '')) AS courseConcate, "
			+ "       CONCAT(IFNULL(c.course_short_name, ''), '-', IFNULL(c.course_code, '')) AS courseShortConcate, "
			+ "       sa.total_attendance AS total_attendance,sa.present_count AS present_count, "
			+ "       ROUND((sa.present_count / sa.total_attendance) * 100, 2) AS actualPercentage, "
			+ "       c.course_short_name AS course_short_name,c.course_code AS course_code "
			+ "FROM feedback_allowfor_student fa "
			+ "LEFT JOIN student_details sd ON sd.student_id = fa.student_id "
			+ "LEFT JOIN course c ON c.course_id = fa.course_id "
			+ "LEFT JOIN academic_year ac ON ac.ac_year_id = fa.ac_year_id "
			+ "LEFT JOIN schools sc ON sc.school_id = fa.school_id "
			+ "LEFT JOIN feedback_window fw ON fw.feedback_window_id = fa.feedback_window_id "
			+ "LEFT JOIN ( "
			+ "    SELECT student_id, course_id, "
			+ "           COUNT(student_attendance_id) AS total_attendance, "
			+ "           SUM(CASE WHEN present_status = 1 THEN 1 ELSE 0 END) AS present_count "
			+ "    FROM student_attendance "
			+ "    GROUP BY student_id, course_id "
			+ ") sa ON sa.student_id = fa.student_id AND sa.course_id = fa.course_id "
			+ "where CONCAT(IfNull(fa.feedback_allowfor_student_id,''),'',IfNull(sd.auid,''),'',IfNull(c.course_short_name,''),"
			+ "'',IfNull(sc.school_name_short,''),'',IfNull(fa.created_date,'')) LIKE %?1% "
			+ "GROUP BY fa.feedback_allowfor_student_id, fa.student_id, fa.course_id, fa.feedback_window_id, "
			+ "         fa.ac_year_id, fa.school_id, fa.created_date, fa.modified_date, fa.active, "
			+ "         fa.created_username, fa.modified_username, fa.created_by, fa.modified_by, "
			+ "         sd.student_name, sd.auid, sd.acharya_email, sd.usn, c.course_name, "
			+ "         ac.ac_year, ac.current_year, sc.school_name, sc.school_name_short, "
			+ "         fw.to_date, fw.from_date, c.course_short_name, c.course_code", nativeQuery = true)
	public Page<Map<String, Object>> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="SELECT fa.feedback_allowfor_student_id AS id, "
			+ "       fa.student_id AS student_id, "
			+ "       fa.course_id AS course_id, "
			+ "       fa.feedback_window_id AS feedback_window_id, "
			+ "       fa.ac_year_id AS ac_year_id, "
			+ "       fa.school_id AS school_id, "
			+ "       fa.created_date AS created_date, "
			+ "       fa.modified_date AS modified_date, "
			+ "       fa.active AS active, "
			+ "       fa.created_username AS created_username, "
			+ "       fa.modified_username AS modified_username, "
			+ "       fa.created_by AS created_by, "
			+ "       fa.modified_by AS modified_by, "
			+ "       sd.student_name AS student_name, "
			+ "       sd.auid AS auid, "
			+ "       sd.acharya_email AS acharya_email, "
			+ "       sd.usn AS usn, "
			+ "       c.course_name AS course_name, "
			+ "       ac.ac_year AS ac_year, "
			+ "       ac.current_year AS current_year, "
			+ "       sc.school_name AS school_name, "
			+ "       sc.school_name_short AS school_name_short, "
			+ "       CONCAT(IFNULL(DATE_FORMAT(fw.from_date, '%d-%m-%Y'), ''), '/', IFNULL(DATE_FORMAT(fw.to_date, '%d-%m-%Y'), '')) AS windowPeriod, "
			+ "       fw.to_date AS toDate,fw.from_date AS fromDate, "
			+ "       CONCAT(IFNULL(c.course_name, ''), '-', IFNULL(c.course_code, '')) AS courseConcate, "
			+ "       CONCAT(IFNULL(c.course_short_name, ''), '-', IFNULL(c.course_code, '')) AS courseShortConcate, "
			+ "       sa.total_attendance AS total_attendance,sa.present_count AS present_count, "
			+ "       ROUND((sa.present_count / sa.total_attendance) * 100, 2) AS actualPercentage, "
			+ "       c.course_short_name AS course_short_name,c.course_code AS course_code "
			+ "FROM feedback_allowfor_student fa "
			+ "LEFT JOIN student_details sd ON sd.student_id = fa.student_id "
			+ "LEFT JOIN course c ON c.course_id = fa.course_id "
			+ "LEFT JOIN academic_year ac ON ac.ac_year_id = fa.ac_year_id "
			+ "LEFT JOIN schools sc ON sc.school_id = fa.school_id "
			+ "LEFT JOIN feedback_window fw ON fw.feedback_window_id = fa.feedback_window_id "
			+ "LEFT JOIN ( "
			+ "    SELECT student_id, course_id, "
			+ "           COUNT(student_attendance_id) AS total_attendance, "
			+ "           SUM(CASE WHEN present_status = 1 THEN 1 ELSE 0 END) AS present_count "
			+ "    FROM student_attendance "
			+ "    GROUP BY student_id, course_id "
			+ ") sa ON sa.student_id = fa.student_id AND sa.course_id = fa.course_id "
			+ "GROUP BY fa.feedback_allowfor_student_id, fa.student_id, fa.course_id, fa.feedback_window_id, "
			+ "         fa.ac_year_id, fa.school_id, fa.created_date, fa.modified_date, fa.active, "
			+ "         fa.created_username, fa.modified_username, fa.created_by, fa.modified_by, "
			+ "         sd.student_name, sd.auid, sd.acharya_email, sd.usn, c.course_name, "
			+ "         ac.ac_year, ac.current_year, sc.school_name, sc.school_name_short, "
			+ "         fw.to_date, fw.from_date, c.course_short_name, c.course_code", nativeQuery = true)
	public Page<Map<String, Object>> findAll3(Pageable pageable);

	@Query(value = "SELECT mb.feedback_allowfor_student_id from feedback_allowfor_student mb "
			+ "where mb.active=true And mb.feedback_window_id=?1 And mb.student_id=?2 And mb.course_id=?3 "
			+ "Order by mb.feedback_allowfor_student_id desc limit 1", nativeQuery = true)
	Integer getCountFeedbackAllowForStudent(Integer feedbackWindowId, Integer studentId, Integer courseId);




}
