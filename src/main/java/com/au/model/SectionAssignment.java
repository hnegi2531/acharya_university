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

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;


@Data
@Entity
@Table(name = "section_assignment")
public class SectionAssignment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer section_assignment_id;
	private Integer section_id;
	private Integer school_id; 
	private Integer program_id;
	private Integer program_specialization_id;
	private Integer ac_year_id;
	private Integer current_year_sem;
//	private Integer current_sem;
	@Lob
	@Column(columnDefinition="LONGTEXT")
	private String student_ids;
	private String remarks;
//	private String course_short_name;
//	private String course_branch_short_name;
	@Lob
	@Column(columnDefinition="LONGTEXT")
	private String emp_ids;
	private String contract_emp_ids;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date created_date;
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified_date;
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;
	private boolean active;
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	private Integer program_assignment_id;
	private Integer current_year;
	
}
