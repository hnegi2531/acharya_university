package com.au.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.au.model.StudentFeedbackAnswers;

@Repository
public interface StudentFeedbackAnswersRepository extends JpaRepository<StudentFeedbackAnswers, Long>{
	
	

}
