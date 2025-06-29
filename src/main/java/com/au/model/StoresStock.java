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
@Table(name="stores_stock")
public class StoresStock {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer stock_type_id;
	@Column(unique=true)
	@NotBlank(message = "stock_type_name should not be Empty OR Null")
	private String stock_type_name;
	@Column(unique=true)
	@NotBlank(message = "stock_type_short_name should not be Empty OR Null")
	private String stock_type_short_name;
	
	@Column(updatable = false)
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
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;
	private boolean active;
	
	
	public StoresStock() {
		super();
		
	}


	public Integer getStock_type_id() {
		return stock_type_id;
	}


	public void setStock_type_id(Integer stock_type_id) {
		this.stock_type_id = stock_type_id;
	}


	public String getStock_type_name() {
		return stock_type_name;
	}


	public void setStock_type_name(String stock_type_name) {
		this.stock_type_name = stock_type_name;
	}


	public String getStock_type_short_name() {
		return stock_type_short_name;
	}


	public void setStock_type_short_name(String stock_type_short_name) {
		this.stock_type_short_name = stock_type_short_name;
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


	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}
	
	
	
	
	

}
