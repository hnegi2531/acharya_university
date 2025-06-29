package com.au.model;

import java.time.LocalDateTime;
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
@Table(name = "legal_department_users")
public class LegalDepartmentUsers {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer legal_department_user_id;
	@Column(unique = true)
	@NotBlank(message = "User Name Should Not Be Empty OR Null")
	private String legal_department_user_name;
	@Column(unique = true)
	@NotBlank(message = "Password Should Not Be Empty OR Null")
	private String legal_department_user_password;
	private String legal_validation_token;
	private LocalDateTime time_of_validation_token;
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	
	private Boolean active;
	

	public LegalDepartmentUsers() {
		super();
	}

	public Integer getLegal_department_user_id() {
		return legal_department_user_id;
	}

	public void setLegal_department_user_id(Integer legal_department_user_id) {
		this.legal_department_user_id = legal_department_user_id;
	}

	public String getLegal_department_user_name() {
		return legal_department_user_name;
	}

	public void setLegal_department_user_name(String legal_department_user_name) {
		this.legal_department_user_name = legal_department_user_name;
	}

	public String getLegal_department_user_password() {
		return legal_department_user_password;
	}

	public void setLegal_department_user_password(String legal_department_user_password) {
		this.legal_department_user_password = legal_department_user_password;
	}

	public String getLegal_validation_token() {
		return legal_validation_token;
	}

	public void setLegal_validation_token(String legal_validation_token) {
		this.legal_validation_token = legal_validation_token;
	}

	public LocalDateTime getTime_of_validation_token() {
		return time_of_validation_token;
	}

	public void setTime_of_validation_token(LocalDateTime time_of_validation_token) {
		this.time_of_validation_token = time_of_validation_token;
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
