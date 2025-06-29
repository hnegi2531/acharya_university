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
@Table(name = "subject_workload_creation")
public class SubjectWorkloadCreation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer subWorkloadId;
	@Column(unique = true)
	private String subWorkloadName;
	private Boolean subjectType;
	private Boolean workLoadType;
	
	@Column(name = "createdBy", updatable = false)
	private Integer createdBy;
	private Integer modifiedBy;

	@Column(name = "createdDate", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modifiedDate;
	private Boolean active;
	
	@Column(name = "createdUsername", updatable = false)
	private String createdUsername;
	private String modifiedUsername;
	
	public SubjectWorkloadCreation() {
		super();
	}

	public Integer getSubWorkloadId() {
		return subWorkloadId;
	}

	public void setSubWorkloadId(Integer subWorkloadId) {
		this.subWorkloadId = subWorkloadId;
	}

	public String getSubWorkloadName() {
		return subWorkloadName;
	}

	public void setSubWorkloadName(String subWorkloadName) {
		this.subWorkloadName = subWorkloadName;
	}

	

	public Boolean getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(Boolean subjectType) {
		this.subjectType = subjectType;
	}

	public Boolean getWorkLoadType() {
		return workLoadType;
	}

	public void setWorkLoadType(Boolean workLoadType) {
		this.workLoadType = workLoadType;
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

	
	
}
