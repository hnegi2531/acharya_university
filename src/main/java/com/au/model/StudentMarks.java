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
@Table(name = "student_marks")
public class StudentMarks {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer marks_id;
	private Integer student_id;
	private Integer course_id;
	private Double marks_obtained_internal;
	private Integer total_marks_internal;
	private Double percentage;
	private String grade;
	private Integer internal_id;
	private Integer batch_id;
	private Integer section_id;
	private Boolean status;
	private Integer exam_room_id;
	private Boolean active;
	private Integer course_assignment_id;
	private Integer current_year_sem;
	
	private Integer ac_year_id;
	private Double external_max_mark;
	private Double external_min_mark;
	private Double marks_obtained_external;
	private String exam_date;
	private Integer program_specialization_id;
	private Integer school_id;
	private Integer current_year;
	private Integer current_sem;
	private Integer internal_session_id;
	
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
	
	private Boolean faculty_status;
	private Boolean hod_status;
	private Boolean hoi_status;
	
	private String faculty_status_date;
	private String hod_status_date;
	private String hoi_status_date;
	
	private Integer faculty_id;
	private Integer hod_id;
	private Integer hoi_id;

}
