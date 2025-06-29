package com.au.repository;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.ClassFeedbackAnswers;

@Transactional
@Repository
public interface ClassFeedbackAnswersRepository extends JpaRepository<ClassFeedbackAnswers, Integer> {
	
	@Query(value ="select count(*) from ClassFeedbackAnswers cfa where cfa.student_id=?1 and cfa.course_id=?2 and cfa.feedback_window_id=?3 and cfa.user_id=?4 and cfa.active=true")
	public Integer getCountOfFeedback(Integer student_id, Integer course_id, Integer feedback_window_id, Integer user_id); 
	
	@Query(value = "SELECT cfa from ClassFeedbackAnswers cfa where cfa.active=true")
	public List<ClassFeedbackAnswers> findAllFeedback();
	
	@Modifying
	@Query(value = "update ClassFeedbackAnswers cfa set cfa.active=false where cfa.class_feedback_answers_id=?1")
	public void updateClassFeedbackAnswers(Integer class_feedback_answers_id);
	
	@Modifying
	@Query(value = "update ClassFeedbackAnswers cfa set cfa.active=true where cfa.class_feedback_answers_id=?1")
	public void updateClassFeedbackAnswers1(Integer class_feedback_answers_id);

	@Query(value = "SELECT s.student_id AS student_id, s.student_name AS student_name, s.auid AS auid,cfa.class_feedback_answers_id As class_feedback_answers_id, "
			+ "cfa.feedback_window_id AS feedback_window_id, fw.from_date AS from_date, fw.to_date AS to_date, "
			+ "GROUP_CONCAT(DISTINCT c.course_id ORDER BY c.course_id) AS course_id, "
			+ "GROUP_CONCAT(DISTINCT c.course_code ORDER BY c.course_id) AS course_code, "
			+ "GROUP_CONCAT(DISTINCT c.course_name ORDER BY c.course_id) AS course_name, "
			+ "GROUP_CONCAT(DISTINCT c.course_short_name ORDER BY c.course_id) AS course_short_name "
			+ "FROM class_feedback_answers cfa "
			+ "LEFT JOIN student_details s ON s.student_id = cfa.student_id "
			+ "LEFT JOIN feedback_window fw ON fw.feedback_window_id = cfa.feedback_window_id "
			+ "LEFT JOIN course c ON c.course_id = cfa.course_id "
			+ "GROUP BY s.student_id", nativeQuery = true)
	List<Map<String, Object>> getClassFeedbackAnswersDetailsData();

	
	@Query(value ="Select new map( cfa.class_feedback_answers_id AS class_feedback_answers_id,cfa.active AS active,cfa.ratings AS ratings,"
			+ "cfa.class_feedback_questions_id AS class_feedback_questions_id,cfa.course_id AS course_id,cfa.remarks AS remarks,"
			+ "cfa.student_id AS student_id,cfa.user_id AS emp_id,cfa.course_assignment_id AS course_assignment_id,cfa.acYearId AS ac_year_id,"
			+ "cfa.sectionId AS section_id,cfa.sem AS sem,cfa.session AS session,cfa.year AS year,cfa.program_specialization_id AS program_specialization_id,"
			+ "ac.ac_year As ac_year,ed.employee_name As employee_name,ed.empcode As empcode,ed.date_of_joining As date_of_joining,"
			+ "c.course_code As course_code,c.course_name As course_name,c.course_short_name As course_short_name,c.course_id As course_id,"
			+ "d.dept_name_short As dept_name_short,d.dept_name As dept_name,de.designation_name As designation_name,d.dept_id As dept_id,"
			+ "de.designation_short_name As designation_short_name,s.school_name As school_name,s.school_name_short As school_name_short,ed.school_id As school_id,"
			+ "fw.fromDate As fromDate,fw.toDate As toDate,"
			+ "CONCAT(IFNULL(DATE_FORMAT(fw.fromDate, '%d-%m-%Y'), ''),'/',IFNULL(DATE_FORMAT(fw.toDate, '%d-%m-%Y'), '')) AS concateFeedbackWindow,"
			+ " cfa.feedback_window_id AS feedback_window_id,cfa.window_count AS window_count,de.designation_id As designation_id) From ClassFeedbackAnswers cfa "
			+ "Left Join Student_Details sd On sd.student_id=cfa.student_id "
			+ "left join Academic_year ac on ac.ac_year_id=cfa.acYearId "
			+ "Left Join EmployeeDetails ed On ed.emp_id=cfa.user_id "
			+ "Left Join UserAuthentication ud On ed.email=ud.email "
			+ "left join Department d on ed.dept_id=d.dept_id "
			+ "left join Designation de on ed.designation_id=de.designation_id "
			+ "left join Schools s on ed.school_id=s.school_id "
			+ "Left Join Course c On c.course_id=cfa.course_id "
			+ "Left Join CourseAssignment ca On ca.course_id=c.course_id "
			+ "Left Join FeedbackWindow fw On fw.feedbackWindowId=cfa.feedback_window_id "
			+ "Where (:ac_year_id is null or cfa.acYearId = :ac_year_id) And "
			+ "(:school_id is null or ed.school_id = :school_id) And "
			+ "(:dept_id is null or ed.dept_id = :dept_id) And "
			+ "(:emp_id is null or ed.emp_id = :emp_id) And "
			+ "CONCAT(IfNull(cfa.class_feedback_answers_id,''),'',IfNull(ed.employee_name,''),'',IfNull(ed.empcode,''),'',"
			+ "IfNull(ed.date_of_joining,''),'',IfNull(cfa.created_date,''),'',IfNull(cfa.created_username,''),IfNull(ac.ac_year,'')) LIKE %:keyword% group by cfa.user_id")
	public Page<Object> classFeedbackAnswersEmployeeDetailsKeyword(Pageable pageable,Integer ac_year_id, Integer school_id,Integer dept_id,Integer emp_id, Object keyword);

	
	@Query(value ="Select new map( cfa.class_feedback_answers_id AS class_feedback_answers_id,cfa.active AS active,cfa.ratings AS ratings,"
			+ "cfa.class_feedback_questions_id AS class_feedback_questions_id,cfa.course_id AS course_id,cfa.remarks AS remarks,"
			+ "cfa.student_id AS student_id,cfa.user_id AS emp_id,cfa.course_assignment_id AS course_assignment_id,cfa.acYearId AS ac_year_id,"
			+ "cfa.sectionId AS section_id,cfa.sem AS sem,cfa.session AS session,cfa.year AS year,cfa.program_specialization_id AS program_specialization_id,"
			+ "ac.ac_year As ac_year,ed.employee_name As employee_name,ed.empcode As empcode,ed.date_of_joining As date_of_joining,"
			+ "c.course_code As course_code,c.course_name As course_name,c.course_short_name As course_short_name,c.course_id As course_id,"
			+ "d.dept_name_short As dept_name_short,d.dept_name As dept_name,de.designation_name As designation_name,d.dept_id As dept_id,"
			+ "de.designation_short_name As designation_short_name,s.school_name As school_name,s.school_name_short As school_name_short,ed.school_id As school_id,"
			+ "fw.fromDate As fromDate,fw.toDate As toDate,"
			+ "CONCAT(IFNULL(DATE_FORMAT(fw.fromDate, '%d-%m-%Y'), ''),'/',IFNULL(DATE_FORMAT(fw.toDate, '%d-%m-%Y'), '')) AS concateFeedbackWindow,"
			+ " cfa.feedback_window_id AS feedback_window_id,cfa.window_count AS window_count,de.designation_id As designation_id) From ClassFeedbackAnswers cfa "
			+ "Left Join Student_Details sd On sd.student_id=cfa.student_id "
			+ "left join Academic_year ac on ac.ac_year_id=cfa.acYearId "
			+ "Left Join EmployeeDetails ed On ed.emp_id=cfa.user_id "
			+ "Left Join UserAuthentication ud On ed.email=ud.email "
			+ "left join Department d on ed.dept_id=d.dept_id "
			+ "left join Designation de on ed.designation_id=de.designation_id "
			+ "left join Schools s on ed.school_id=s.school_id "
			+ "Left Join Course c On c.course_id=cfa.course_id "
			+ "Left Join CourseAssignment ca On ca.course_id=c.course_id "
			+ "Left Join FeedbackWindow fw On fw.feedbackWindowId=cfa.feedback_window_id "
			+ "Where (:ac_year_id is null or cfa.acYearId = :ac_year_id) And "
			+ "(:school_id is null or ed.school_id = :school_id) And "
			+ "(:dept_id is null or ed.dept_id = :dept_id) And "
			+ "(:emp_id is null or ed.emp_id = :emp_id) group by cfa.user_id")
	public Page<Object> classFeedbackAnswersEmployeeDetailsWithoutKeyword(Pageable pageable1,Integer ac_year_id, Integer school_id,Integer dept_id,Integer emp_id);

	
	@Query(value = "SELECT c.class_feedback_questions_id, fq.feedback_questions,SUM(c.ratings) AS total_ratings,"
			+ "ROUND(AVG(c.window_count)) AS avg_window_count,COUNT(c.student_id) AS total_students, "
			+ "ROUND(((SUM(c.ratings) - (COUNT(c.student_id) * 1)) / (COUNT(c.student_id) * (5 - 1))) * 100, 2) AS ratings_percentage  "
			+ "FROM class_feedback_answers c  "
			+ "LEFT JOIN feedback_questions fq ON fq.feedback_id = c.class_feedback_questions_id  "
			+ "WHERE c.user_id = ?1  "
			+ "GROUP BY c.class_feedback_questions_id", nativeQuery = true)
	public List<Map<String, Object>> getQuestionWithRating(Integer employeeId);
	
	
	@Query(value = "SELECT c.class_feedback_questions_id As class_feedback_questions_id, c.section_id As section_id,s.section_name As section_name,fq.feedback_questions As feedback_questions,"
			+ "co.course_name As course_name,co.course_short_name As course_short_name,co.course_code As course_code,"
			+ "fw.feedback_window_id As feedback_window_id,fw.from_date As from_date,fw.to_date As to_date,"
			+ "CONCAT(IFNULL(DATE_FORMAT(fw.from_date, '%d-%m-%Y'), ''),'-',IFNULL(DATE_FORMAT(fw.to_date, '%d-%m-%Y'), '')) AS concateFeedbackWindow,"
			+ "c.window_count AS avg_window_count, count(fq.feedback_id) AS total_students,sum(c.ratings) AS total_ratings,"
			+ "round(sum(c.ratings)/(count(fq.feedback_id)*5)*100 ,2) AS ratings_percentage "
			+ "FROM class_feedback_answers c "
			+ "LEFT JOIN feedback_questions fq ON fq.feedback_id = c.class_feedback_questions_id "
			+ "LEFT JOIN feedback_window fw ON fw.feedback_window_id = c.feedback_window_id "
			+ "LEFT JOIN section s ON s.section_id = c.section_id "
			+ "LEFT JOIN course co ON co.course_id = c.course_id "
			+ "WHERE (:employeeId IS NULL OR c.user_id = :employeeId) And (:course_id IS NULL OR c.course_id = :course_id) "
			+ "group by fq.feedback_id,c.window_count,c.course_id,c.section_id,c.feedback_window_id", nativeQuery = true)
	public List<Map<String, Object>> getQuestionWithRatingSection(Integer employeeId, Integer course_id);
	
	
	@Query(value = "SELECT cfa.course_id As course_id,c.course_name As course_name,c.course_short_name As course_short_name,c.course_code As course_code "
			+ "FROM class_feedback_answers cfa "
			+ "left join course c on c.course_id = cfa.course_id "
			+ "where cfa.user_id =?1 group by cfa.course_id ", nativeQuery = true)
	public List<Map<String, Object>> getCourseDetailsDataFromFeedBack(Integer employeeId);

	
	@Query(value = "SELECT c.class_feedback_questions_id As class_feedback_questions_id, c.section_id As section_id,"
			+ "s.section_name As section_name,co.course_name As course_name,co.course_short_name As course_short_name,co.course_code As course_code,"
			+ "fw.feedback_window_id As feedback_window_id,ac.ac_year As ac_year,c.ratings As ratings,c.window_count As window_count,count(fq.feedback_id) AS total_students,"
			+ "CONCAT(IFNULL(DATE_FORMAT(fw.from_date, '%d-%m-%Y'), ''),'-',IFNULL(DATE_FORMAT(fw.to_date, '%d-%m-%Y'), '')) AS concateFeedbackWindow,"
			+ "round(sum(c.ratings)/(count(fq.feedback_id)*5)*100 ,2) AS avg_ratings_percentage "
			+ "FROM class_feedback_answers c  "
			+ "LEFT JOIN feedback_questions fq ON fq.feedback_id = c.class_feedback_questions_id  "
			+ "LEFT JOIN feedback_window fw ON fw.feedback_window_id = c.feedback_window_id  "
			+ "LEFT JOIN section s ON s.section_id = c.section_id   "
			+ "LEFT JOIN course co ON co.course_id = c.course_id  "
			+ "LEFT JOIN academic_year ac ON ac.ac_year_id = c.ac_year_id  "
			+ "WHERE (:employeeId IS NULL OR c.user_id = :employeeId) And (:course_id IS NULL OR c.course_id = :course_id) "
			+ "group by c.window_count,co.course_id,c.section_id,c.feedback_window_id ", nativeQuery = true)
	public List<Map<String, Object>> getCourseWithRatingSection(Integer employeeId, Integer course_id);

}
