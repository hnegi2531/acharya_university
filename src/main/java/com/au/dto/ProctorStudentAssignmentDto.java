package com.au.dto;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

public class ProctorStudentAssignmentDto {

//	private Integer proctor_id;
	private Integer school_id;
	private List<Integer> student_id;
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	private Integer created_by;
	private Integer modified_by;
	private Boolean active;
	private Integer proctor_status;
	private String created_username;
	private String modified_username;
	private Integer emp_id;
	
	public ProctorStudentAssignmentDto() {
		super();
	}

//	public Integer getProctor_id() {
//		return proctor_id;
//	}
//
//	public void setProctor_id(Integer proctor_id) {
//		this.proctor_id = proctor_id;
//	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public List<Integer> getStudent_id() {
		return student_id;
	}

	public void setStudent_id(List<Integer> student_id) {
		this.student_id = student_id;
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

	public Integer getProctor_status() {
		return proctor_status;
	}

	public void setProctor_status(Integer proctor_status) {
		this.proctor_status = proctor_status;
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

	public Integer getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(Integer emp_id) {
		this.emp_id = emp_id;
	}
}
