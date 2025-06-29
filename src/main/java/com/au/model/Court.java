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
@Table(name ="court")
public class Court {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer court_id;
	@Column(unique = true)
	@NotBlank(message = "Court Name should not be Empty OR Null")
	private String court_name;
	@Column(unique = true)
	@NotBlank(message = "Court Short Name should not be Empty OR Null")
	private String court_short_name;
	private Boolean active;
	
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	
	@Column(updatable = false)
	private Integer created_by;
	
	private Integer modified_by;
	
	@Column(updatable = false)
	private String created_username;
	
	private String modified_username;
	


	public Court() {
		super();
	}

	public Integer getCourt_id() {
		return court_id;
	}

	public void setCourt_id(Integer court_id) {
		this.court_id = court_id;
	}

	public String getCourt_name() {
		return court_name;
	}

	public void setCourt_name(String court_name) {
		this.court_name = court_name;
	}

	public String getCourt_short_name() {
		return court_short_name;
	}

	public void setCourt_short_name(String court_short_name) {
		this.court_short_name = court_short_name;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
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
