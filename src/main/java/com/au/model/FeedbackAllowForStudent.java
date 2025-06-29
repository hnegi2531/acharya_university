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
@Table(name = "feedback_allowfor_student")
public class FeedbackAllowForStudent {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer feedback_allowfor_student_id;
	
	private Integer student_id;
	private Integer course_id;
	private Integer feedback_window_id;
	private Integer ac_year_id;
	private Integer school_id;
	
	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;

	@Column(name = "created_username", updatable = false)
	private String created_username;
	private String modified_username;

	@Column(name = "created_by", updatable = false)
	private Integer created_by;
	private Integer modified_by;

	private Boolean active;
}
