package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.FeedbackQuestions;
import com.au.model.StudentFeedbackQuestions;

@Repository
public interface StudentFeedbackQuestionsRepository extends JpaRepository<StudentFeedbackQuestions, Long> {

	StudentFeedbackQuestions findByInstituteIdAndCourseIdAndQuestionIdAndActive(Integer instituteId, Integer courseId,
			Long questionId, Boolean true1);


	List<FeedbackQuestions> findByInstituteIdAndCourseIdAndActive(Integer instituteId, Integer courseId, Boolean true1);



	@Query(value = "SELECT fq FROM FeedbackQuestions fq WHERE fq.school_id = ?1 AND fq.active = true", nativeQuery = false)
	public List<FeedbackQuestions> getFeedbackQuestions(Integer schoolId);



}
