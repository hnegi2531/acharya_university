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
@Table(name = "std_reporting_students_history")
public class StdReportingStudentsHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer reporting_history_id;
	private Integer  student_id;
	private Integer current_year;
	private Integer current_sem;
	private Integer program_specialization_id; //course_branch_id
	private Integer school_id;
	private Integer	reported_ac_year_id;
	private String remarks;

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
	
	private Date reporting_date;
	private Boolean distinct_status;
	private Integer previous_sem;
	private Integer previous_year;
	private Integer eligible_reported_status;
	private Integer program_type_id;//course_type_id
	private Integer section_id;
	private Integer year_back_status;

	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	
	public StdReportingStudentsHistory() {
		super();
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


	public Integer getReporting_history_id() {
		return reporting_history_id;
	}
	public void setReporting_history_id(Integer reporting_history_id) {
		this.reporting_history_id = reporting_history_id;
	}
	public Integer getStudent_id() {
		return student_id;
	}
	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
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
	public Integer getProgram_specialization_id() {
		return program_specialization_id;
	}
	public void setProgram_specialization_id(Integer program_specialization_id) {
		this.program_specialization_id = program_specialization_id;
	}
	public Integer getSchool_id() {
		return school_id;
	}
	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}
	public Integer getReported_ac_year_id() {
		return reported_ac_year_id;
	}
	public void setReported_ac_year_id(Integer reported_ac_year_id) {
		this.reported_ac_year_id = reported_ac_year_id;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
	public Date getReporting_date() {
		return reporting_date;
	}
	public void setReporting_date(Date reporting_date) {
		this.reporting_date = reporting_date;
	}
	public Boolean getDistinct_status() {
		return distinct_status;
	}
	public void setDistinct_status(Boolean distinct_status) {
		this.distinct_status = distinct_status;
	}
	public Integer getPrevious_sem() {
		return previous_sem;
	}
	public void setPrevious_sem(Integer previous_sem) {
		this.previous_sem = previous_sem;
	}
	public Integer getPrevious_year() {
		return previous_year;
	}
	public void setPrevious_year(Integer previous_year) {
		this.previous_year = previous_year;
	}

	public Integer getEligible_reported_status() {
		return eligible_reported_status;
	}


	public void setEligible_reported_status(Integer eligible_reported_status) {
		this.eligible_reported_status = eligible_reported_status;
	}


	public Integer getProgram_type_id() {
		return program_type_id;
	}
	public void setProgram_type_id(Integer program_type_id) {
		this.program_type_id = program_type_id;
	}
	
	public Integer getSection_id() {
		return section_id;
	}


	public void setSection_id(Integer section_id) {
		this.section_id = section_id;
	}


	public Integer getYear_back_status() {
		return year_back_status;
	}


	public void setYear_back_status(Integer year_back_status) {
		this.year_back_status = year_back_status;
	}

	
	
}
