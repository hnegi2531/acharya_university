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
@Table(name = "peo")
public class Peo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer peoId;
	
	private String peoName;
	
	private Integer programMissionId;                //fk
	private Integer programMissionComponentId;       //fk    
	
	private Integer mapValues;
	
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
	
	public Peo() {
		super();
	}

	public Integer getPeoId() {
		return peoId;
	}

	public void setPeoId(Integer peoId) {
		this.peoId = peoId;
	}

	public String getPeoName() {
		return peoName;
	}

	public void setPeoName(String peoName) {
		this.peoName = peoName;
	}

	public Integer getProgramMissionId() {
		return programMissionId;
	}

	public void setProgramMissionId(Integer programMissionId) {
		this.programMissionId = programMissionId;
	}

	public Integer getProgramMissionComponentId() {
		return programMissionComponentId;
	}

	public void setProgramMissionComponentId(Integer programMissionComponentId) {
		this.programMissionComponentId = programMissionComponentId;
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

	public Integer getMapValues() {
		return mapValues;
	}

	public void setMapValues(Integer mapValues) {
		this.mapValues = mapValues;
	}
	
	
	
}
