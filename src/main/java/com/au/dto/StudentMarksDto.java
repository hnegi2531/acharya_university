package com.au.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import lombok.Data;

@Data
public class StudentMarksDto {

	private Integer marks_id;
	private Integer course_id;
	private Integer internal_id;
	private Integer course_assignment_id;
	private Integer current_year_sem;
	
	private Integer ac_year_id;
	private Double external_max_mark;
	private Double external_min_mark;
	private String exam_date;
	private Integer program_specialization_id;
	private Integer school_id;
	private Integer current_year;
	private Integer current_sem;
	private Integer internal_session_id;
	
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;
	private Boolean active;
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
	
	private Boolean faculty_status;
	private Boolean hod_status;
	private Boolean hoi_status;
	
	private String faculty_status_date;
	private String hod_status_date;
	private String hoi_status_date;
	
	
	 private List<StudentMarksAssignmentDto> studentMarksAssignment;
}
