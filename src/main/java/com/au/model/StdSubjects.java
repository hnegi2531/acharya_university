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
@Table(name = "subjects")
public class StdSubjects {
	
	//dummy table not in use we are using course table instead of this.
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "subject_id")
	private Integer subjectId;

	@Column(name = "subject_name", nullable = false)
	private String subjectName;

	@Column(name = "subject_name_short",  nullable = false)
	private String subjectNameShort;

	@Column(name = "subject_code", unique = true, nullable = false)
	private String subjectCode;

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
	

	public StdSubjects() {
		super();
	}


	public Integer getSubjectId() {
		return subjectId;
	}


	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}


	public String getSubjectName() {
		return subjectName;
	}


	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}


	public String getSubjectNameShort() {
		return subjectNameShort;
	}


	public void setSubjectNameShort(String subjectNameShort) {
		this.subjectNameShort = subjectNameShort;
	}


	public String getSubjectCode() {
		return subjectCode;
	}


	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
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