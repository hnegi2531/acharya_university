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
@Table(name = "internal_session_assignment")
public class InternalSessionAssignment {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer internal_id;
	private Integer internal_master_id;
	private String internal_name;
	private Date from_date;
	private Date to_date;
	private Integer ac_year_id;
	private String internal_short_name;
	private Integer school_id;
	private Integer program_id; 
	private Integer program_specialization_id;
    private String remarks;
   
    private Integer year_sem;
    
    
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
	
	
	public InternalSessionAssignment() {
		super();
	}


	public Integer getInternal_id() {
		return internal_id;
	}


	public void setInternal_id(Integer internal_id) {
		this.internal_id = internal_id;
	}


	public Integer getInternal_master_id() {
		return internal_master_id;
	}


	public void setInternal_master_id(Integer internal_master_id) {
		this.internal_master_id = internal_master_id;
	}


	public String getInternal_name() {
		return internal_name;
	}


	public void setInternal_name(String internal_name) {
		this.internal_name = internal_name;
	}


	public Date getFrom_date() {
		return from_date;
	}


	public void setFrom_date(Date from_date) {
		this.from_date = from_date;
	}


	public Date getTo_date() {
		return to_date;
	}


	public void setTo_date(Date to_date) {
		this.to_date = to_date;
	}


	public Integer getAc_year_id() {
		return ac_year_id;
	}


	public void setAc_year_id(Integer ac_year_id) {
		this.ac_year_id = ac_year_id;
	}


	public String getInternal_short_name() {
		return internal_short_name;
	}


	public void setInternal_short_name(String internal_short_name) {
		this.internal_short_name = internal_short_name;
	}


	public Integer getSchool_id() {
		return school_id;
	}


	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}


	

	public Integer getProgram_id() {
		return program_id;
	}


	public void setProgram_id(Integer program_id) {
		this.program_id = program_id;
	}


	public Integer getProgram_specialization_id() {
		return program_specialization_id;
	}


	public void setProgram_specialization_id(Integer program_specialization_id) {
		this.program_specialization_id = program_specialization_id;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public Integer getYear_sem() {
		return year_sem;
	}


	public void setYear_sem(Integer year_sem) {
		this.year_sem = year_sem;
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
	
	
	
	
	
	







}
