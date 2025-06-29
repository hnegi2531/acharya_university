package com.au.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

public class LeavePatternRequest {

	private Integer leave_pattern_id;
	private Integer leave_id;
	private List<Integer> school_id; //  institute_id
	private List<Integer> emp_type_id; // emp_type_id
	private  List<Integer> job_type_id;
	private Integer leave_days_permit;
	private Integer year;
	private String special_remarks;
	
	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;

	private Integer created_by;
	private Integer modified_by;
	private String created_username;
	private String modified_username;
	
	private Boolean active;
	public LeavePatternRequest() {
		super();
	}
	public Integer getLeave_pattern_id() {
		return leave_pattern_id;
	}
	public void setLeave_pattern_id(Integer leave_pattern_id) {
		this.leave_pattern_id = leave_pattern_id;
	}
	public Integer getLeave_id() {
		return leave_id;
	}
	public void setLeave_id(Integer leave_id) {
		this.leave_id = leave_id;
	}
	public List<Integer> getSchool_id() {
		return school_id;
	}
	public void setSchool_id(List<Integer> school_id) {
		this.school_id = school_id;
	}
	public List<Integer> getEmp_type_id() {
		return emp_type_id;
	}
	public void setEmp_type_id(List<Integer> emp_type_id) {
		this.emp_type_id = emp_type_id;
	}
	public List<Integer> getJob_type_id() {
		return job_type_id;
	}
	public void setJob_type_id(List<Integer> job_type_id) {
		this.job_type_id = job_type_id;
	}
	public Integer getLeave_days_permit() {
		return leave_days_permit;
	}
	public void setLeave_days_permit(Integer leave_days_permit) {
		this.leave_days_permit = leave_days_permit;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public String getSpecial_remarks() {
		return special_remarks;
	}
	public void setSpecial_remarks(String special_remarks) {
		this.special_remarks = special_remarks;
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
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	
	
	
}
