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
@Table(name = "time_interval_types")
public class TimeIntervalTypes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer intervalTypeId;
	@Column(name = "interval_type_name",unique = true)
	private String intervalTypeName;
	@Column(name = "interval_type_short",unique = true)
	private String intervalTypeShort;	
	private String remarks;	
	private String showBatch;	
	private String allowMultipleStaff;	
	private String outside;	
	private String showSubject;	
	private String showAttendance;

	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdDate;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modifiedDate;
	@Column(name = "create_by", updatable = false)
	private Integer createdBy;
	private Integer modifiedBy;
	private Boolean active;
	@Column(name = "created_username", updatable = false)
	private String createdUsername;
	private String modifiedUsername;
	public TimeIntervalTypes() {
		super();
	}
	public Integer getIntervalTypeId() {
		return intervalTypeId;
	}
	public void setIntervalTypeId(Integer intervalTypeId) {
		this.intervalTypeId = intervalTypeId;
	}
	public String getIntervalTypeName() {
		return intervalTypeName;
	}
	public void setIntervalTypeName(String intervalTypeName) {
		this.intervalTypeName = intervalTypeName;
	}
	public String getIntervalTypeShort() {
		return intervalTypeShort;
	}
	public void setIntervalTypeShort(String intervalTypeShort) {
		this.intervalTypeShort = intervalTypeShort;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getShowBatch() {
		return showBatch;
	}
	public void setShowBatch(String showBatch) {
		this.showBatch = showBatch;
	}
	public String getAllowMultipleStaff() {
		return allowMultipleStaff;
	}
	public void setAllowMultipleStaff(String allowMultipleStaff) {
		this.allowMultipleStaff = allowMultipleStaff;
	}
	public String getOutside() {
		return outside;
	}
	public void setOutside(String outside) {
		this.outside = outside;
	}
	public String getShowSubject() {
		return showSubject;
	}
	public void setShowSubject(String showSubject) {
		this.showSubject = showSubject;
	}
	public String getShowAttendance() {
		return showAttendance;
	}
	public void setShowAttendance(String showAttendance) {
		this.showAttendance = showAttendance;
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

	
}
