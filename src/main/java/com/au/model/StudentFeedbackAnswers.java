package com.au.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="student_feedback_answers")
public class StudentFeedbackAnswers {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long studentFeedbackAnswerId;
	
	private Integer questionId;
	
	private Integer courseId;
	
	private Integer instituteId;
	
	private Integer studentId;
	
	private Integer empId;
	
	private Integer ratings;
	

}
