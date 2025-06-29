package com.au.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

public class SectionDto {

	private Integer sectionId;

	@Column(unique = true)
	private String sectionName;
	@Column(unique = true)
	private String sectionShortName;
	
	private List<Integer> school_id;
	private Integer volume;
	
	private String remarks;
	
	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdDate;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modifiedDate;

	private Integer createdBy;
	private Integer modifiedBy;
	private String createdUsername;
	private String modifiedUsername;
	private Boolean active;
	public SectionDto() {
		super();
	}
	public Integer getSectionId() {
		return sectionId;
	}
	public void setSectionId(Integer sectionId) {
		this.sectionId = sectionId;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public String getSectionShortName() {
		return sectionShortName;
	}
	public void setSectionShortName(String sectionShortName) {
		this.sectionShortName = sectionShortName;
	}
	public List<Integer> getSchool_id() {
		return school_id;
	}
	public void setSchool_id(List<Integer> school_id) {
		this.school_id = school_id;
	}
	public Integer getVolume() {
		return volume;
	}
	public void setVolume(Integer volume) {
		this.volume = volume;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
