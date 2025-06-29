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
@Table(name = "internal_types")
public class InternalTypes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer internal_master_id;
	private String internal_name;
	private String internal_short_name;
	private String remarks;
	
	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;

	@Column(name = "created_by", updatable = false)
	private Integer created_by;
	private Integer modified_by;
	private Boolean active;
	@Column(name = "created_username", updatable = false)
	private String created_username;
	private String modified_username;
	
	
	
	public InternalTypes() {
		super();
		}



	public Integer getInternal_master_id() {
		return internal_master_id;
	}



	public void setInternal_master_id(Integer internal_master_id) {
		this.internal_master_id = internal_master_id;
	}



	public String getInternal_name() {
		return internal_name;
	}



	public void setInternal_name(String internal_name) {
		this.internal_name = internal_name;
	}



	public String getInternal_short_name() {
		return internal_short_name;
	}



	public void setInternal_short_name(String internal_short_name) {
		this.internal_short_name = internal_short_name;
	}



	public String getRemarks() {
		return remarks;
	}



	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


   public Date getCreated_date() {
		return created_date;
	}



	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}



	public Date getModified_date() {
		return modified_date;
	}



	public void setModified_date(Date modified_date) {
		this.modified_date = modified_date;
	}



	public Integer getCreated_by() {
		return created_by;
	}



	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}



	public Integer getModified_by() {
		return modified_by;
	}



	public void setModified_by(Integer modified_by) {
		this.modified_by = modified_by;
	}



	public Boolean getActive() {
		return active;
	}



	public void setActive(Boolean active) {
		this.active = active;
	}



	public String getCreated_username() {
		return created_username;
	}



	public void setCreated_username(String created_username) {
		this.created_username = created_username;
	}



	public String getModified_username() {
		return modified_username;
	}



	public void setModified_username(String modified_username) {
		this.modified_username = modified_username;
	}
	
	
	
	
	
	
	
	
	



	
 






}
