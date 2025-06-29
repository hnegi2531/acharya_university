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
@Table(name = "internal_session_creation")
public class InternalSessionCreation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer internal_session_id;
	private Integer internal_master_id;
	private String internal_name;
	private String internal_short_name;
	private Integer program_id; 
    private String remarks;
	private String exam_time;
	private Integer time_slots_id;
	private String week_day;
	private Integer min_marks;
	private Integer max_marks;
	
	private Integer course_assignment_id;
	private Integer year_sem;
	private Integer ac_year_id;
	private Integer school_id;
	private Integer program_specialization_id;
	private String date_of_exam;
	private Integer external_min_marks;
	private Integer external_max_marks;
	private Double percentage;
	private Integer current_year;
	private Integer current_sem; 
    
    @Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;

	@Column(name = "created_by", updatable = false)
	private Integer created_by;
	private Integer modified_by;
	private Boolean active;
	@Column(name = "created_username", updatable = false)
	private String created_username;
	private String modified_username;


}
