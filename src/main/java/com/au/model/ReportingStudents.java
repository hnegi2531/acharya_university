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
@Table(name = "reporting_students")
public class ReportingStudents {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer reporting_id;
	private Integer student_id;
	private Integer current_year;
	private Integer current_sem;
	private Integer reported_ac_year_id;
	private String remarks;

	private Date reporting_date;
	private Boolean distinct_status;
	private Integer previous_sem;
	private Integer previous_year;

	/**
		*	1: "No status",
		* 	2: "Not Eligible",
		* 	3: "Eligible",    
		*	4: "Not Reported",
		*	5: "Pass Out",
		*	6: promoted
	 **/
	private Integer eligible_reported_status;
	private Integer year_back_status;
	private Integer section_id;

	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	@Column(updatable = false)
	private int created_by;
	private Integer modified_by;
	private Boolean active;
	@Column(updatable = false)
	private String created_username;
	private String modified_username;

	public ReportingStudents() {
		super();
	}

	public Integer getReporting_id() {
		return reporting_id;
	}

	public void setReporting_id(Integer reporting_id) {
		this.reporting_id = reporting_id;
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

	public Integer getYear_back_status() {
		return year_back_status;
	}

	public void setYear_back_status(Integer year_back_status) {
		this.year_back_status = year_back_status;
	}

	public Integer getSection_id() {
		return section_id;
	}

	public void setSection_id(Integer section_id) {
		this.section_id = section_id;
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

	public int getCreated_by() {
		return created_by;
	}

	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}

	public int getModified_by() {
		return modified_by;
	}

	public void setModified_by(int modified_by) {
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
