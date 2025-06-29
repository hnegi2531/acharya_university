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
@Table(name = "external_marks_assignment")
public class ExternalMarksAssignment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer external_marks_assignment_id;
	private Integer external_mark_id;
	private Integer student_id;
	private Integer section_id;
	private Double marks_scored;
	private Double percentage;
	
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
