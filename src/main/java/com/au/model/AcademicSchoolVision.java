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
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "academic_school_vision")
public class AcademicSchoolVision {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer academicSchoolVisionId;	
	
	@Size(max = 500 ,message = "School vision should not exceed 500 letters")
	private String asvVision; 
	
	@Size(max = 500 ,message = "School mission should not exceed 500 letters")
	private String asvMission;
	
	@Size(max = 500 ,message = "School mission should not exceed 500 letters")
	private String asvDescription;
	private Integer schoolId;
	
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
	
	public AcademicSchoolVision() {
		super();
	}

	public Integer getAcademicSchoolVisionId() {
		return academicSchoolVisionId;
	}

	public void setAcademicSchoolVisionId(Integer academicSchoolVisionId) {
		this.academicSchoolVisionId = academicSchoolVisionId;
	}

	public String getAsvMission() {
		return asvMission;
	}

	public void setAsvMission(String asvMission) {
		this.asvMission = asvMission;
	}

	public String getAsvVision() {
		return asvVision;
	}

	public void setAsvVision(String asvVision) {
		this.asvVision = asvVision;
	}

	public String getAsvDescription() {
		return asvDescription;
	}

	public void setAsvDescription(String asvDescription) {
		this.asvDescription = asvDescription;
	}

	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
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
