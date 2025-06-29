package com.au.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.domain.Sort.Order;

import lombok.Data;


@Data
@Entity
@Table(name = "course_outcome")
public class CourseOutcome {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer course_outcome_id;
	private String course_outcome_code;
//	private Integer course_id;
	
	@Lob
	@NotBlank(message = "Course Objective should not be Empty OR Null")
	private String course_outcome_objective;
	
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

	private Boolean active;
	
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	private Integer course_assignment_id;
	
	@NotBlank(message = "Toxonomy should not be Empty OR Null")
	private String toxonomy;
	
	@Lob
	@NotBlank(message = "Toxonomy Details should not be Empty OR Null")
	private String toxonomy_details;
}
