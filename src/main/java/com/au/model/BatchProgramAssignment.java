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


@Entity
@Table(name = "batch_program_assignment")
public class BatchProgramAssignment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer batch_program_assignment_id;
	private Integer batch_assignment_id;
	private Integer program_id;
	private Integer ac_year_id;
	private Integer batch_id;
	private Integer school_id;
	private Integer current_year;
	private Integer current_sem;
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
	
	private Integer program_assignment_id;
	private Integer program_specialization_id;
	
	public BatchProgramAssignment() {
		super();

	}
	public Integer getBatch_program_assignment_id() {
		return batch_program_assignment_id;
	}
	public void setBatch_program_assignment_id(Integer batch_program_assignment_id) {
		this.batch_program_assignment_id = batch_program_assignment_id;
	}
	public Integer getBatch_assignment_id() {
		return batch_assignment_id;
	}
	public void setBatch_assignment_id(Integer batch_assignment_id) {
		this.batch_assignment_id = batch_assignment_id;
	}
	public Integer getProgram_id() {
		return program_id;
	}
	public void setProgram_id(Integer program_id) {
		this.program_id = program_id;
	}
	public Integer getAc_year_id() {
		return ac_year_id;
	}
	public void setAc_year_id(Integer ac_year_id) {
		this.ac_year_id = ac_year_id;
	}
	public Integer getBatch_id() {
		return batch_id;
	}
	public void setBatch_id(Integer batch_id) {
		this.batch_id = batch_id;
	}
	public Integer getSchool_id() {
		return school_id;
	}
	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}
	public Integer getCurrent_year() {
		return current_year;
	}
	public void setCurrent_year(Integer current_year) {
		this.current_year = current_year;
	}
	public Integer getCurrent_sem() {
		return current_sem;
	}
	public void setCurrent_sem(Integer current_sem) {
		this.current_sem = current_sem;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
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
	public Integer getProgram_specialization_id() {
		return program_specialization_id;
	}
	public void setProgram_specialization_id(Integer program_specialization_id) {
		this.program_specialization_id = program_specialization_id;
	}
	
	
}
