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
@Table(name = "roles")
public class Roles {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer role_id;
	@Column(unique = true)
	@NotBlank(message = "role_name should not be Empty OR Null")
	private String role_name;
	private String role_desc;
	@Column(unique = true)
	@NotBlank(message = "role_short_name should not be Empty OR Null")
	private String role_short_name;
	
	@Column(updatable = false)
	private Integer created_by;
	
	private Integer modified_by;

	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_Date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_Date;

	private Boolean active;
	private Boolean access;
	private Boolean back_date;
	
	@Column(updatable = false)
	private String created_username;
	
	private String modified_username;
	
	private String lms_role;
	
	private String lms_status;
	private Boolean adp_status;

	public Roles() {
		super();
	}

	public Integer getRole_id() {
		return role_id;
	}

	public void setRole_id(Integer role_id) {
		this.role_id = role_id;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	public String getRole_desc() {
		return role_desc;
	}

	public void setRole_desc(String role_desc) {
		this.role_desc = role_desc;
	}

	public String getRole_short_name() {
		return role_short_name;
	}

	public void setRole_short_name(String role_short_name) {
		this.role_short_name = role_short_name;
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

	public Date getCreated_Date() {
		return created_Date;
	}

	public void setCreated_Date(Date created_Date) {
		this.created_Date = created_Date;
	}

	public Date getModified_Date() {
		return modified_Date;
	}

	public void setModified_Date(Date modified_Date) {
		this.modified_Date = modified_Date;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getAccess() {
		return access;
	}

	public void setAccess(Boolean access) {
		this.access = access;
	}

	public Boolean getBack_date() {
		return back_date;
	}

	public void setBack_date(Boolean back_date) {
		this.back_date = back_date;
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

	public String getLms_role() {
		return lms_role;
	}

	public void setLms_role(String lms_role) {
		this.lms_role = lms_role;
	}

	public String getLms_status() {
		return lms_status;
	}

	public void setLms_status(String lms_status) {
		this.lms_status = lms_status;
	}

	public Boolean getAdp_status() {
		return adp_status;
	}

	public void setAdp_status(Boolean adp_status) {
		this.adp_status = adp_status;
	}
	
	

}
