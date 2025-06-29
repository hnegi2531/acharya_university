package com.au.dto;

import java.util.Date;
import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

public class SubjectAssignLoadDto {

	private Integer subjectId;

	private Integer credits;
 
	private HashMap<Integer,Integer> sw_hours;
	private String subjectCode;
	private Integer schoolId;
	private Integer programId;
	private String programSpecializationId;
	private String programSpecializationShortName;
	private Float subjectUniversityMaxHours;
	private Float subjectInstituteMaxHours;

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
	private Integer subjectTypeId;          //core /elective /others

	private Integer maxCieMarks;
	private Integer maxSemYearMarks;
	private Integer minCieMarks;
	private Integer minSemYearMarks;
	
	

	public SubjectAssignLoadDto() {
		super();
	}

	
	public Integer getSchoolId() {
		return schoolId;
	}


	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}


	public Integer getProgramId() {
		return programId;
	}


	public void setProgramId(Integer programId) {
		this.programId = programId;
	}


	public String getProgramSpecializationId() {
		return programSpecializationId;
	}


	public void setProgramSpecializationId(String programSpecializationId) {
		this.programSpecializationId = programSpecializationId;
	}


	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	

	

	public Integer getCredits() {
		return credits;
	}

	public void setCredits(Integer credits) {
		this.credits = credits;
	}

	public HashMap<Integer, Integer> getSw_hours() {
		return sw_hours;
	}

	public void setSw_hours(HashMap<Integer, Integer> sw_hours) {
		this.sw_hours = sw_hours;
	}

	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public Float getSubjectUniversityMaxHours() {
		return subjectUniversityMaxHours;
	}

	public void setSubjectUniversityMaxHours(Float subjectUniversityMaxHours) {
		this.subjectUniversityMaxHours = subjectUniversityMaxHours;
	}

	public Float getSubjectInstituteMaxHours() {
		return subjectInstituteMaxHours;
	}

	public void setSubjectInstituteMaxHours(Float subjectInstituteMaxHours) {
		this.subjectInstituteMaxHours = subjectInstituteMaxHours;
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

	public Integer getSubjectTypeId() {
		return subjectTypeId;
	}

	public void setSubjectTypeId(Integer subjectTypeId) {
		this.subjectTypeId = subjectTypeId;
	}

	public Integer getMaxCieMarks() {
		return maxCieMarks;
	}

	public void setMaxCieMarks(Integer maxCieMarks) {
		this.maxCieMarks = maxCieMarks;
	}

	public Integer getMaxSemYearMarks() {
		return maxSemYearMarks;
	}

	public void setMaxSemYearMarks(Integer maxSemYearMarks) {
		this.maxSemYearMarks = maxSemYearMarks;
	}

	public Integer getMinCieMarks() {
		return minCieMarks;
	}

	public void setMinCieMarks(Integer minCieMarks) {
		this.minCieMarks = minCieMarks;
	}

	public Integer getMinSemYearMarks() {
		return minSemYearMarks;
	}

	public void setMinSemYearMarks(Integer minSemYearMarks) {
		this.minSemYearMarks = minSemYearMarks;
	}


	public String getProgramSpecializationShortName() {
		return programSpecializationShortName;
	}


	public void setProgramSpecializationShortName(String programSpecializationShortName) {
		this.programSpecializationShortName = programSpecializationShortName;
	}

	
}
