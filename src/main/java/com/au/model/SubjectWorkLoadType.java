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
@Table(name = "subject_workload_type")
public class SubjectWorkLoadType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer subjectWorkLoadTypeId;
	
	private  Integer subjectId;
	private Integer academicWorkLoadTypeId;
	private Integer subjetAssignId;
	
	private Integer hours;
	private Integer createdBy;
	private Integer modifiedBy;

	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modifiedDate;
	private Boolean active;

	private String createdUsername;
	private String modifiedUsername;

	public SubjectWorkLoadType() {
		super();
	}

	
	public Integer getSubjetAssignId() {
		return subjetAssignId;
	}


	public void setSubjetAssignId(Integer subjetAssignId) {
		this.subjetAssignId = subjetAssignId;
	}


	public Integer getSubjectWorkLoadTypeId() {
		return subjectWorkLoadTypeId;
	}

	public void setSubjectWorkLoadTypeId(Integer subjectWorkLoadTypeId) {
		this.subjectWorkLoadTypeId = subjectWorkLoadTypeId;
	}

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	

	public Integer getAcademicWorkLoadTypeId() {
		return academicWorkLoadTypeId;
	}


	public void setAcademicWorkLoadTypeId(Integer academicWorkLoadTypeId) {
		this.academicWorkLoadTypeId = academicWorkLoadTypeId;
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

	public Integer getHours() {
		return hours;
	}

	public void setHours(Integer hours) {
		this.hours = hours;
	}


	@Override
	public String toString() {
		return "SubjectWorkLoadType [subjectWorkLoadTypeId=" + subjectWorkLoadTypeId + ", subjectId=" + subjectId
				+ ", academicWorkLoadTypeId=" + academicWorkLoadTypeId + ", subjetAssignId=" + subjetAssignId
				+ ", hours=" + hours + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy + ", createdDate="
				+ createdDate + ", modifiedDate=" + modifiedDate + ", active=" + active + ", createdUsername="
				+ createdUsername + ", modifiedUsername=" + modifiedUsername + "]";
	}


}
