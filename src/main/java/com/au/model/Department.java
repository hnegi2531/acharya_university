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
@Table(name = "department")
public class Department {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer dept_id;
	@Column(unique = true)
	private String dept_name;

	@Column(unique = true)
	private String dept_name_short;
	private String web_status;

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
	
	private Boolean common_service;
	private Boolean no_dues_status;
	
	private String dept_icon;
	private Integer hod_id;
	private String comments;
	
	public Department() {
		super();
	}

	public Integer getDept_id() {
		return dept_id;
	}

	public void setDept_id(Integer dept_id) {
		this.dept_id = dept_id;
	}

	public String getDept_name() {
		return dept_name;
	}

	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}

	public String getDept_name_short() {
		return dept_name_short;
	}

	public void setDept_name_short(String dept_name_short) {
		this.dept_name_short = dept_name_short;
	}

	public String getWeb_status() {
		return web_status;
	}

	public void setWeb_status(String web_status) {
		this.web_status = web_status;
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

	public Boolean getCommon_service() {
		return common_service;
	}

	public void setCommon_service(Boolean common_service) {
		this.common_service = common_service;
	}

	public Boolean getNo_dues_status() {
		return no_dues_status;
	}

	public void setNo_dues_status(Boolean no_dues_status) {
		this.no_dues_status = no_dues_status;
	}

	public String getDept_icon() {
		return dept_icon;
	}

	public void setDept_icon(String dept_icon) {
		this.dept_icon = dept_icon;
	}

	public Integer getHod_id() {
		return hod_id;
	}

	public void setHod_id(Integer hod_id) {
		this.hod_id = hod_id;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	
	
}