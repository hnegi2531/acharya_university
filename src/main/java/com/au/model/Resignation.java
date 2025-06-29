package com.au.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "resignation")
public class Resignation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer resignation_id;
	private Integer emp_id;
	@Lob
	private String comments;
	private Date applied_date;
	private Integer status;
	private Date relieving_date;
	private String reason;
	private Date requested_relieving_date;
	private String employee_reason;
	private String additional_reason;
	
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
	private Boolean resignation_status;
	private Integer nodues_approve_status;
	
	private String relieving_number;
	
	public Resignation() {
		super();
	}

	public Integer getResignation_id() {
		return resignation_id;
	}

	public void setResignation_id(Integer resignation_id) {
		this.resignation_id = resignation_id;
	}

	public Integer getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(Integer emp_id) {
		this.emp_id = emp_id;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getApplied_date() {
		return applied_date;
	}

	public void setApplied_date(Date applied_date) {
		this.applied_date = applied_date;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getRelieving_date() {
		return relieving_date;
	}

	public void setRelieving_date(Date relieving_date) {
		this.relieving_date = relieving_date;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getRequested_relieving_date() {
		return requested_relieving_date;
	}

	public void setRequested_relieving_date(Date requested_relieving_date) {
		this.requested_relieving_date = requested_relieving_date;
	}

	public String getEmployee_reason() {
		return employee_reason;
	}

	public void setEmployee_reason(String employee_reason) {
		this.employee_reason = employee_reason;
	}

	public String getAdditional_reason() {
		return additional_reason;
	}

	public void setAdditional_reason(String additional_reason) {
		this.additional_reason = additional_reason;
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

	public Boolean getResignation_status() {
		return resignation_status;
	}

	public void setResignation_status(Boolean resignation_status) {
		this.resignation_status = resignation_status;
	}

	public Integer getNodues_approve_status() {
		return nodues_approve_status;
	}

	public void setNodues_approve_status(Integer nodues_approve_status) {
		this.nodues_approve_status = nodues_approve_status;
	}

	public String getRelieving_number() {
		return relieving_number;
	}

	public void setRelieving_number(String relieving_number) {
		this.relieving_number = relieving_number;
	}
	

	
	
}
