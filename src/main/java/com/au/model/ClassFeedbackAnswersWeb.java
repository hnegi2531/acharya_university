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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="class_feedback_answers")
public class ClassFeedbackAnswersWeb {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer class_feedback_answers_id;
	private Integer class_feedback_questions_id;
	private Integer user_id;   // EmployeeId
	private Integer student_id;
	private Integer course_id;
	private String remarks;
	private Boolean active;
	private Integer ratings;
	private Integer session;
	
	private Integer sectionId;
	private Integer acYearId;
	private Integer year;
	private Integer sem;
	private Integer Program_specialization_id;
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

