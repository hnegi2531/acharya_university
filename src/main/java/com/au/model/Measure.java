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
@Table(name="measures")
public class Measure {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer measure_id;
	@Column(unique=true)
	private String measure_name;
	@Column(unique=true)
	private String measure_short_name;
	
	@Column(name = "created_username",updatable = false)
	private String created_username;
	private String modified_username;

	@Column(name = "created_date",updatable = false)
	@Temporal(TemporalType.TIMESTAMP)	
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)	
	@UpdateTimestamp
	private Date modified_date;
	@Column(name = "created_by",updatable = false)
	private Integer created_by;
	private Integer modified_by;
	private Integer active;
	
	
	public Measure() {
		super();
		
	}


	public Integer getMeasure_id() {
		return measure_id;
	}


	public void setMeasure_id(Integer measure_id) {
		this.measure_id = measure_id;
	}


	public String getMeasure_name() {
		return measure_name;
	}


	public void setMeasure_name(String measure_name) {
		this.measure_name = measure_name;
	}


	public String getMeasure_short_name() {
		return measure_short_name;
	}


	public void setMeasure_short_name(String measure_short_name) {
		this.measure_short_name = measure_short_name;
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


	public Integer isActive() {
		return active;
	}


	public void setActive(Integer active) {
		this.active = active;
	}
	
	
	
	
	
	

}
