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
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Entity
@Table(name = "course_assignment")
public class CourseAssignment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer course_assignment_id;
	private Integer ac_year_id;                    // Fk
	private Integer school_id;                    // Fk
	private Integer program_id;                  // Fk
	private Integer dept_id;                    // FK
	private Integer program_specialization_id; // Fk
	private Integer course_id;                // Fk
	private Integer course_type_id;          // FK
	private Integer course_category_id; 
	// not in use
	private Integer syllabus_id;           // Fk
	private Integer emp_id;                // Fk
	@Size(max=100)
	private String email;
	private Integer year_sem;
	private String total_credit;
	private Float lecture;
	private Float tutorial;
	private Float practical;
	private Integer duration;
	private Integer cie_marks;
	private Integer see_marks;
	// not in use
	private Float course_price;
	// not in use
	private Float course_price_usd;
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
	private Boolean active;
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	private String remarks;
	private Integer program_assignment_id;
	private String course_assignment_coursecode;
	
	private Integer user_id;


	
}
