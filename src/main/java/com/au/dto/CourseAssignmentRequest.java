package com.au.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.Data;

@Data
public class CourseAssignmentRequest {
	
	private Integer ac_year_id;                    // Fk
	private Integer school_id;                    // Fk
	private Integer program_id;                  // Fk
	private Integer dept_id;                    // FK
	private Integer program_specialization_id; // Fk
	private Integer course_id;                // Fk
	private Integer course_type_id;          // FK
	private Integer course_category_id;     // FK
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
	private Float course_price;
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
	private String ac_year;
	private String school_name_short;
	private String program_short_name;
	private String program_specialization_short_name;
	private String course_type_name;
	private String course_category_code;
	private Integer program_assignment_id;
	
	private Integer user_id;
	
	
}
