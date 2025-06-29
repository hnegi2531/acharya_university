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

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "employee_type")
public class EmployeeType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer empTypeId;
	@Column(unique = true)
	@NotBlank(message = "Emp Type should not be Empty OR Null")
	private String empType;
	@Column(unique = true)
	@NotBlank(message = "Emp Type Short Name should not be Empty OR Null")
	private String empTypeShortName;
	
	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdDate;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modifiedDate;

	@Column(updatable = false)
	private Integer createdBy;
	private Integer modifiedBy;
	
	@Column(updatable = false)
	private String createdUsername;
	private String modifiedUsername;
	private Boolean active;
	public EmployeeType() {
		super();
	}
	public Integer getEmpTypeId() {
		return empTypeId;
	}
	public void setEmpTypeId(Integer empTypeId) {
		this.empTypeId = empTypeId;
	}
	public String getEmpType() {
		return empType;
	}
	public void setEmpType(String empType) {
		this.empType = empType;
	}
	public String getEmpTypeShortName() {
		return empTypeShortName;
	}
	public void setEmpTypeShortName(String empTypeShortName) {
		this.empTypeShortName = empTypeShortName;
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
