package com.au.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Entity
@Table(name = "batch_assignment")
public class BatchAssignment 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer batch_assignment_id;
	private Integer batch_id;
	private Integer school_id;  	// institute_id
	private Integer program_id; // course_assignment_id;  now it not required here as we are storing program id in separate table i.e. BatchProgramAssignment
	private Integer ac_year_id;
//	private Integer section_id; 	//Not required
	private Integer current_year;
	private Integer current_sem;
	@Lob
	@Column(columnDefinition="LONGTEXT")
	private String student_ids;
	private Integer batch_type; 	// from time interval type table where show batch is 'yes'
	private String remarks;
	private Integer batch_master_id;

	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	private Boolean active;
	@Lob
	@Column(columnDefinition="LONGTEXT")
	private String guest_uesr_ids; //Coming From User Details Table
	private Integer interval_type_id;
	private Integer program_assignment_id;
	private Integer program_specialization_id;
	
	
}
