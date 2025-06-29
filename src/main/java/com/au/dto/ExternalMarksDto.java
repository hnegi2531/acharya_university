package com.au.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.au.model.StudentMarks;

import lombok.Data;

@Data
public class ExternalMarksDto {

	private Integer marks_id;
	private Integer ac_year_id;
	private Double external_max_mark;
	private Double external_min_mark;
	private Double marks_obtained_external;
	private String exam_date;
	private Integer program_specialization_id;
	private Integer school_id;
	private Integer course_assignment_id;
	private Integer current_year_sem;
	
	
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
