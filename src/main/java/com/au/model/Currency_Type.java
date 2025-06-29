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
@Table(name = "currency_type")
public class Currency_Type {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer currency_type_id;
	
	@Column(unique = true)
	@NotBlank(message = "currency_type_name should not be Empty OR Null")
	private String currency_type_name;
	
	@Column(unique = true)
	@NotBlank(message = "currency_type_short_name should not be Empty OR Null")
	private String currency_type_short_name;

	@Column(name = "created_date", updatable = false)
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
	
	@Column(updatable = false)
	private String created_username;
	
	private String modified_username;
	private Boolean active;

	public Currency_Type() {
		super();
	}

	public Integer getCurrency_type_id() {
		return currency_type_id;
	}

	public void setCurrency_type_id(Integer currency_type_id) {
		this.currency_type_id = currency_type_id;
	}

	public String getCurrency_type_name() {
		return currency_type_name;
	}

	public void setCurrency_type_name(String currency_type_name) {
		this.currency_type_name = currency_type_name;
	}

	public String getCurrency_type_short_name() {
		return currency_type_short_name;
	}

	public void setCurrency_type_short_name(String currency_type_short_name) {
		this.currency_type_short_name = currency_type_short_name;
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	

}
