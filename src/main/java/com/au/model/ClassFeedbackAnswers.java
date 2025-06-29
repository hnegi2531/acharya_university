package com.au.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.Data;

@Data
@Entity
@Table(name = "class_feedback_answers")
public class ClassFeedbackAnswers {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer class_feedback_answers_id;
	private Integer class_feedback_questions_id;
	private Integer user_id;    // EmployeeId
	private Integer student_id;
	private Integer course_id;
	private Integer course_assignment_id;
	private String remarks;
	private Boolean active;
	private Integer ratings;
	
	private Integer session;
	
	private Integer sectionId;
	private Integer acYearId;
	private Integer year;
	private Integer sem;
	private Integer program_specialization_id;
	private Integer feedback_window_id;
	private Integer window_count;
	@Column(updatable=false)
	private Integer created_by;
	
	private Integer modified_by;
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	
	@Column(updatable=false)
	private String created_username;
	private String modified_username;
}
