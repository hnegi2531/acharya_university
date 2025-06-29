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
@Table(name = "external_marks")
public class ExternalMarks {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer external_mark_id;
	private Integer max_mark;
	private Integer min_mark;
	private Integer current_year;
	private Integer current_sem;
	private Integer ac_year_id;
	private Integer course_assignment_id;
	private Integer school_id;
	private Integer program_specialization_id;
	private String exam_date;
	
	private Boolean active;
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;

	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
}
