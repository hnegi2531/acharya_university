package com.au.model;

import java.sql.Time;
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

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "shift")
public class Shift {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "shift_category_id")
	private Integer shiftCategoryId;
	
	@Column(name = "shift_name")
	private String shiftName;

	@Column(name = "shift_start_time")
	private String shiftStartTime;

	private String fhPunchIn;
	private String fhPunchOut;
	private String shPunchIn;
	private String shPunchOut;

	@Column(name = "shift_end_time")
	private String shiftEndTime;
	@Column(name = "created_by", updatable = false)
	private Integer createdBy;
	@Column(name = "modified_by")
	private Integer modifiedBy;

	@Column(name = "created_Date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdDate;

	@Column(name = "modified_Date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modifiedDate;

	@Column(name = "active")
	private Boolean active;
	
	@Column(name = "created_username", updatable = false)
	private String createdUsername;
	@Column(name = "modified_username")
	private String modifiedUsername;
	private String frontend_use_start_time;
	private String frontend_use_end_time;
	
	private Boolean is_saturday;
	private String school_id;
	

	@Column(name = "grace_time")
	private String grace_time;
	
	@Column(name = "actual_start_time")
	private String actual_start_time;

	public Shift() {
		super();
	}

	public Integer getShiftCategoryId() {
		return shiftCategoryId;
	}

	public void setShiftCategoryId(Integer shiftCategoryId) {
		this.shiftCategoryId = shiftCategoryId;
	}

	public String getShiftName() {
		return shiftName;
	}

	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}

	public String getShiftStartTime() {
		return shiftStartTime;
	}

	public void setShiftStartTime(String shiftStartTime) {
		this.shiftStartTime = shiftStartTime;
	}

	public String getShiftEndTime() {
		return shiftEndTime;
	}

	public void setShiftEndTime(String shiftEndTime) {
		this.shiftEndTime = shiftEndTime;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getCreatedUsername() {
		return createdUsername;
	}

	public void setCreatedUsername(String createdUsername) {
		this.createdUsername = createdUsername;
	}

	public String getModifiedUsername() {
		return modifiedUsername;
	}

	public void setModifiedUsername(String modifiedUsername) {
		this.modifiedUsername = modifiedUsername;
	}

	public String getFrontend_use_start_time() {
		return frontend_use_start_time;
	}

	public void setFrontend_use_start_time(String frontend_use_start_time) {
		this.frontend_use_start_time = frontend_use_start_time;
	}

	public String getFrontend_use_end_time() {
		return frontend_use_end_time;
	}

	public void setFrontend_use_end_time(String frontend_use_end_time) {
		this.frontend_use_end_time = frontend_use_end_time;
	}

	public Boolean getIs_saturday() {
		return is_saturday;
	}

	public void setIs_saturday(Boolean is_saturday) {
		this.is_saturday = is_saturday;
	}

	public String getSchool_id() {
		return school_id;
	}

	public void setSchool_id(String school_id) {
		this.school_id = school_id;
	}

	public String getFhPunchIn() {
		return fhPunchIn;
	}

	public void setFhPunchIn(String fhPunchIn) {
		this.fhPunchIn = fhPunchIn;
	}

	public String getFhPunchOut() {
		return fhPunchOut;
	}

	public void setFhPunchOut(String fhPunchOut) {
		this.fhPunchOut = fhPunchOut;
	}

	public String getShPunchIn() {
		return shPunchIn;
	}

	public void setShPunchIn(String shPunchIn) {
		this.shPunchIn = shPunchIn;
	}

	public String getShPunchOut() {
		return shPunchOut;
	}

	public void setShPunchOut(String shPunchOut) {
		this.shPunchOut = shPunchOut;
	}

	public String getGrace_time() {
		return grace_time;
	}

	public void setGrace_time(String grace_time) {
		this.grace_time = grace_time;
	}

	public String getActual_start_time() {
		return actual_start_time;
	}

	public void setActual_start_time(String actual_start_time) {
		this.actual_start_time = actual_start_time;
	}



}
