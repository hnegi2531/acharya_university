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
@Table(name ="leave_pattern")
public class LeavePattern {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer leave_pattern_id;
	private Integer leave_id;
	private Integer school_id; //  institute_id
	private Integer emp_type_id; // emp_type_id
	private Integer job_type_id;
	private Integer leave_days_permit;
	private Integer year;
	private String specal_remarks;
	
	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	private Boolean active;
	public LeavePattern() {
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



	public Integer getSchool_id() {
		return school_id;
	}



	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}



	public Integer getEmp_type_id() {
		return emp_type_id;
	}



	public void setEmp_type_id(Integer emp_type_id) {
		this.emp_type_id = emp_type_id;
	}



	public Integer getJob_type_id() {
		return job_type_id;
	}



	public void setJob_type_id(Integer job_type_id) {
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



	public String getSpecal_remarks() {
		return specal_remarks;
	}



	public void setSpecal_remarks(String specal_remarks) {
		this.specal_remarks = specal_remarks;
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



	@Override
	public String toString() {
		return "LeavePattern [leavePatternId=" + leave_pattern_id + ", leaveId=" + leave_id + ", schoolId=" + school_id
				+ ", empTypeId=" + emp_type_id + ", job_type_id=" + job_type_id + ", leave_days_permit="
				+ leave_days_permit + ", year=" + year + ", specal_remarks=" + specal_remarks + ", createdDate="
				+ created_date + ", modifiedDate=" + modified_date + ", createdBy=" + created_by + ", modifiedBy="
				+ modified_by + ", createdUsername=" + created_username + ", modifiedUsername=" + modified_username
				+ ", active=" + active + "]";
	}
	
	
}
