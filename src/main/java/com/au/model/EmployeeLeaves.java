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
@Table(name = "employee_leaves")
public class EmployeeLeaves {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer employee_leave_id ;
	private Integer leave_pattern_id;
	private Integer emp_id;
	private Integer initial_days_count;
	private Integer updated_days_count;
	private Double accumulated_count=0.0 ;
	private Date accumulated_date; 
	private Boolean  carry_status;
	
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
	
	
	public EmployeeLeaves() {
		super();
		}


	public Integer getEmployee_leave_id() {
		return employee_leave_id;
	}


	public void setEmployee_leave_id(Integer employee_leave_id) {
		this.employee_leave_id = employee_leave_id;
	}


	public Integer getLeave_pattern_id() {
		return leave_pattern_id;
	}


	public void setLeave_pattern_id(Integer leave_pattern_id) {
		this.leave_pattern_id = leave_pattern_id;
	}


	public Integer getEmp_id() {
		return emp_id;
	}


	public void setEmp_id(Integer emp_id) {
		this.emp_id = emp_id;
	}


	public Integer getInitial_days_count() {
		return initial_days_count;
	}


	public void setInitial_days_count(Integer initial_days_count) {
		this.initial_days_count = initial_days_count;
	}


	public Integer getUpdated_days_count() {
		return updated_days_count;
	}


	public void setUpdated_days_count(Integer updated_days_count) {
		this.updated_days_count = updated_days_count;
	}


	public Double getAccumulated_count() {
		return accumulated_count;
	}


	public void setAccumulated_count(Double accumulated_count) {
		this.accumulated_count = accumulated_count;
	}


	public Date getAccumulated_date() {
		return accumulated_date;
	}


	public void setAccumulated_date(Date accumulated_date) {
		this.accumulated_date = accumulated_date;
	}


	public Boolean getCarry_status() {
		return carry_status;
	}


	public void setCarry_status(Boolean carry_status) {
		this.carry_status = carry_status;
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
