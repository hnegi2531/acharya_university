package com.au.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.dto.EmployeeFeedbackReportList;
import com.au.model.ClassFeedbackAnswersWeb;



@Transactional
@Repository
public interface ClassFeedbackAnswersRepositoryWeb extends JpaRepository<ClassFeedbackAnswersWeb, Integer> {

	
	@Query(value ="select count(*) from ClassFeedbackAnswers cfa where cfa.student_id=?1 and cfa.course_id=?2 and cfa.user_id=?3 and cfa.active=true")
	public Integer getCountOfFeedback(Integer student_id, Integer course_id, Integer user_id); 
	
	@Query(value = "SELECT cfa from ClassFeedbackAnswers cfa where cfa.active=true")
	public List<ClassFeedbackAnswersWeb> findAllFeedback();
	
	@Modifying
	@Query(value = "update ClassFeedbackAnswers cfa set cfa.active=false where cfa.class_feedback_answers_id=?1")
	public void updateClassFeedbackAnswers(Integer class_feedback_answers_id);
	
	@Modifying
	@Query(value = "update ClassFeedbackAnswers cfa set cfa.active=true where cfa.class_feedback_answers_id=?1")
	public void updateClassFeedbackAnswers1(Integer class_feedback_answers_id);

	@Query(value=" select * from class_feedback_answers cfa  where  cfa.user_id=:empId and cfa.course_id=:courseId ",nativeQuery = true)
	public List<ClassFeedbackAnswersWeb> getAnswersByEmpIdAndCourseId(Integer empId, Integer courseId);

	
	
	@Query(value=" select new com.au.dto.EmployeeFeedbackReportList( cfa.sectionId, s.section_name, c.course_code, c.course_name, ac.ac_year,"
			+ " cfa.year, cfa.sem, count(cfa.class_feedback_answers_id), sum(cfa.ratings) ) from ClassFeedbackAnswers cfa "
			+ " left join Course c on c.course_id=cfa.course_id "
			+ " left join Section s on s.section_id=cfa.sectionId"
			+ " left join Academic_year ac on ac.ac_year_id=cfa.acYearId "
			+ " where cfa.user_id=:empId group by c.course_id" )
	public List<EmployeeFeedbackReportList> getEmployeeForFeedbackReport(Integer empId);

	@Query(value ="select count(*) from ClassFeedbackAnswers cfa where cfa.student_id=?1 And cfa.course_id=?2 And cfa.active=true")
	public Integer getFeedbackAnswerCount(Integer studentId, Integer courseId);

}
