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
@Table(name = "program_fee_asssignment")
public class ProgramFeeAsssignment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer program_fee_asssignment_id;
	
	private Integer program_specialization_id;
	private Integer program_assignment_id;
	private Integer program_id;
	private Integer application_fee_1st_attempt;
	private Integer application_fee_2nd_attempt;
	private Integer ac_year_id;
	
	
	@Column(updatable = false)
	private Integer created_by;
	
	private Integer modified_by;

	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_Date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_Date;

	private Boolean active;
	
	@Column(updatable = false)
	private String created_username;
	
	private String modified_username;

	
	public ProgramFeeAsssignment() {
		super();
		
	}


	public Integer getprogram_fee_asssignment_id() {
		return program_fee_asssignment_id;
	}


	public void setprogram_fee_asssignment_id(Integer programFeeAsssignment_id) {
		this.program_fee_asssignment_id = programFeeAsssignment_id;
	}


	public Integer getProgram_specialization_id() {
		return program_specialization_id;
	}


	public void setProgram_specialization_id(Integer program_specialization_id) {
		this.program_specialization_id = program_specialization_id;
	}


	public Integer getProgram_assignment_id() {
		return program_assignment_id;
	}


	public void setProgram_assignment_id(Integer program_assignment_id) {
		this.program_assignment_id = program_assignment_id;
	}


	public Integer getProgram_id() {
		return program_id;
	}


	public void setProgram_id(Integer program_id) {
		this.program_id = program_id;
	}


	public Integer getApplication_fee_1st_attempt() {
		return application_fee_1st_attempt;
	}


	public void setApplication_fee_1st_attempt(Integer application_fee_1st_attempt) {
		this.application_fee_1st_attempt = application_fee_1st_attempt;
	}


	public Integer getApplication_fee_2nd_attempt() {
		return application_fee_2nd_attempt;
	}


	public void setApplication_fee_2nd_attempt(Integer application_fee_2nd_attempt) {
		this.application_fee_2nd_attempt = application_fee_2nd_attempt;
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


	public Date getCreated_Date() {
		return created_Date;
	}


	public void setCreated_Date(Date created_Date) {
		this.created_Date = created_Date;
	}


	public Date getModified_Date() {
		return modified_Date;
	}


	public void setModified_Date(Date modified_Date) {
		this.modified_Date = modified_Date;
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


	public Integer getAc_year_id() {
		return ac_year_id;
	}


	public void setAc_year_id(Integer ac_year_id) {
		this.ac_year_id = ac_year_id;
	}
	
	
	
}
