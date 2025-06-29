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
@Table(name = "program_mission_component")
public class ProgramMissionComponent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer programMissionComponentId;
	
	private String programMissionComponentName;
	private Integer programMissionId;         //fk
	
	
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
	
	public ProgramMissionComponent() {
		super();
	}

	public Integer getProgramMissionComponentId() {
		return programMissionComponentId;
	}

	public void setProgramMissionComponentId(Integer programMissionComponentId) {
		this.programMissionComponentId = programMissionComponentId;
	}

	public String getProgramMissionComponentName() {
		return programMissionComponentName;
	}

	public void setProgramMissionComponentName(String programMissionComponentName) {
		this.programMissionComponentName = programMissionComponentName;
	}

	public Integer getProgramMissionId() {
		return programMissionId;
	}

	public void setProgramMissionId(Integer programMissionId) {
		this.programMissionId = programMissionId;
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
