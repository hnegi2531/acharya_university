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
@Table(name = "alias_name")
public class AliasName {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer alias_id;
	private String alias_name;
	
	@Column(updatable = false)
	private String created_username;
	private String modified_username;


	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	private Boolean active=true;
	
	public AliasName() {
		super();
	}

	

	public AliasName(Integer alias_id, String alias_name, Date created_date, Date modified_date, Boolean active) {
		super();
		this.alias_id = alias_id;
		this.alias_name = alias_name;
		this.created_date = created_date;
		this.modified_date = modified_date;
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



	public Integer getAlias_id() {
		return alias_id;
	}

	public void setAlias_id(Integer alias_id) {
		this.alias_id = alias_id;
	}

	public String getAlias_name() {
		return alias_name;
	}

	public void setAlias_name(String alias_name) {
		this.alias_name = alias_name;
	}
	
	@Override
	public String toString() {
		return "AliasName [alias_id=" + alias_id + ", alias_name=" + alias_name + ", created_date=" + created_date
				+ ", modified_date=" + modified_date + ", active=" + active + "]";
	}
	
	
}
