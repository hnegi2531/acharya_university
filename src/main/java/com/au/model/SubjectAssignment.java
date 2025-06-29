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
@Table(name = "subject_assignment")
public class SubjectAssignment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer subjetAssignId;

	private Integer course_id;  // on the basis of subject table
	private Integer user_id;
	private String remarks;
	
//	private Integer credits;
//	private Float subjectUniversityMaxHours;
//	private Float subjectInstituteMaxHours;
//	private Integer subjectTypeId;          //core /elective /others
//
//	private Integer maxCieMarks;
//	private Integer maxSemYearMarks;
//	private Integer minCieMarks;
//	private Integer minSemYearMarks;
//	
//	private Integer schoolId;
//	private Integer programId;
//	private String programSpecializationId;
//	private String programSpecializationShortName;

	@Column(updatable = false)
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
	@Column(updatable = false)
	private String createdUsername;
	private String modifiedUsername;
	
	private Integer course_assignment_id;
	
	public SubjectAssignment() {
		super();
	}

	public Integer getSubjetAssignId() {
		return subjetAssignId;
	}

	public void setSubjetAssignId(Integer subjetAssignId) {
		this.subjetAssignId = subjetAssignId;
	}
	
	
	public Integer getCourse_id() {
		return course_id;
	}

	public void setCourse_id(Integer course_id) {
		this.course_id = course_id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getCourse_assignment_id() {
		return course_assignment_id;
	}

	public void setCourse_assignment_id(Integer course_assignment_id) {
		this.course_assignment_id = course_assignment_id;
	}

	
	
}
