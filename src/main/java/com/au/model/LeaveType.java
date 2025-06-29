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
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "leave_type")
public class LeaveType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer leave_id;

	@Column(unique = true)
	@NotBlank(message = "leave_type should not be Empty OR Null")
	private String leave_type;

	@Column(unique = true)
	@NotBlank(message = "leave_type_short should not be Empty OR Null")
	private String leave_type_short;
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;

	private String type;
	private String unit;

	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	private Boolean active;
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	private Boolean holiday;
	private String leave_type_path;
	private String remarks;
	private Boolean is_attendance;
	
	private Boolean leave_type_attachment_required;
	private Boolean hr_initialization_status;

	public LeaveType() {
		super();
	}

	public Integer getLeave_id() {
		return leave_id;
	}

	public void setLeave_id(Integer leave_id) {
		this.leave_id = leave_id;
	}

	public String getLeave_type() {
		return leave_type;
	}

	public void setLeave_type(String leave_type) {
		this.leave_type = leave_type;
	}

	public String getLeave_type_short() {
		return leave_type_short;
	}

	public void setLeave_type_short(String leave_type_short) {
		this.leave_type_short = leave_type_short;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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

	public Boolean getHoliday() {
		return holiday;
	}

	public void setHoliday(Boolean holiday) {
		this.holiday = holiday;
	}

	public String getLeave_type_path() {
		return leave_type_path;
	}

	public String setLeave_type_path(String leave_type_path) {
		return this.leave_type_path = leave_type_path;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Boolean getIs_attendance() {
		return is_attendance;
	}

	public void setIs_attendance(Boolean is_attendance) {
		this.is_attendance = is_attendance;
	}

	public Boolean getLeave_type_attachment_required() {
		return leave_type_attachment_required;
	}

	public void setLeave_type_attachment_required(Boolean leave_type_attachment_required) {
		this.leave_type_attachment_required = leave_type_attachment_required;
	}

	public Boolean getHr_initialization_status() {
		return hr_initialization_status;
	}

	public void setHr_initialization_status(Boolean hr_initialization_status) {
		this.hr_initialization_status = hr_initialization_status;
	}

	
	
}
