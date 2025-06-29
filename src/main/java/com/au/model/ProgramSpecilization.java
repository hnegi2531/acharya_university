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
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "program_specialization")
public class ProgramSpecilization {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer program_specialization_id;

	@NotBlank(message = "program_specialization_name should not be Empty OR Null")
	private String program_specialization_name;
	
	@NotBlank(message = "program_specialization_short_name should not be Empty OR Null")
	private String program_specialization_short_name;
	
	@Column(unique = true)
	private String auid_format;
	private Integer program_id;
	private Integer school_id;
	private Integer dept_id;
//	private String program_type;
//	private String min_credit;
//	private String max_credit;
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
	private Integer program_assignment_id;

	public ProgramSpecilization() {
		super();
	}

	public Integer getProgram_specialization_id() {
		return program_specialization_id;
	}

	public void setProgram_specialization_id(Integer program_specialization_id) {
		this.program_specialization_id = program_specialization_id;
	}

	public String getProgram_specialization_name() {
		return program_specialization_name;
	}

	public void setProgram_specialization_name(String program_specialization_name) {
		this.program_specialization_name = program_specialization_name;
	}

	public String getProgram_specialization_short_name() {
		return program_specialization_short_name;
	}

	public void setProgram_specialization_short_name(String program_specialization_short_name) {
		this.program_specialization_short_name = program_specialization_short_name;
	}

	public String getAuid_format() {
		return auid_format;
	}

	public void setAuid_format(String auid_format) {
		this.auid_format = auid_format;
	}

	public Integer getProgram_id() {
		return program_id;
	}

	public void setProgram_id(Integer program_id) {
		this.program_id = program_id;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public Integer getDept_id() {
		return dept_id;
	}

	public void setDept_id(Integer dept_id) {
		this.dept_id = dept_id;
	}

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Integer getModified_by() {
		return modified_by;
	}

	public void setModified_by(Integer modified_by) {
		this.modified_by = modified_by;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public Date getModified_date() {
		return modified_date;
	}

	public void setModified_date(Date modified_date) {
		this.modified_date = modified_date;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getCreated_username() {
		return created_username;
	}

	public void setCreated_username(String created_username) {
		this.created_username = created_username;
	}

	public String getModified_username() {
		return modified_username;
	}

	public void setModified_username(String modified_username) {
		this.modified_username = modified_username;
	}

	public Integer getProgram_assignment_id() {
		return program_assignment_id;
	}

	public void setProgram_assignment_id(Integer program_assignment_id) {
		this.program_assignment_id = program_assignment_id;
	}
	
	
}
