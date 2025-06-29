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
@Table(name = "academic_program_vision")
public class AcademicsProgramVision {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer academicsProgramVisionId;
	
	@Size(max = 1000 ,message = "Program vision should not exceed 1000 letters")
	private String apvVision;
	
	@Size(max = 1000 ,message = "Program mission should not exceed 1000 letters")
	private String apvMission;
	
	private String apvDescription;

	private Integer schoolId;
	private Integer dept_id;
	
	
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
	
	@Column(updatable = false)
	private String createdUsername;
	
	private String modifiedUsername;
	private Boolean active;

	public AcademicsProgramVision() {
		super();
	}

	public Integer getAcademicsProgramVisionId() {
		return academicsProgramVisionId;
	}

	public void setAcademicsProgramVisionId(Integer academicsProgramVisionId) {
		this.academicsProgramVisionId = academicsProgramVisionId;
	}

	public String getApvVision() {
		return apvVision;
	}

	public void setApvVision(String apvVision) {
		this.apvVision = apvVision;
	}

	public String getApvMission() {
		return apvMission;
	}

	public void setApvMission(String apvMission) {
		this.apvMission = apvMission;
	}

	public String getApvDescription() {
		return apvDescription;
	}

	public void setApvDescription(String apvDescription) {
		this.apvDescription = apvDescription;
	}

	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}

	
	public Integer getDept_id() {
		return dept_id;
	}

	public void setDept_id(Integer dept_id) {
		this.dept_id = dept_id;
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
