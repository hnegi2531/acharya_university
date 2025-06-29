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
@Table(name = "category_type_creation")
public class CategoryTypeCreation {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer category_type_id;
	@Column(unique = true)
	private String category_name;
	@Column(unique = true)
	private String category_name_sort;
	private String remarks;
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
	private Boolean active;
	private Boolean attendance_status;
	private Boolean outside_campus;
	
	public CategoryTypeCreation() {
		super();
	}

	public Integer getCategory_type_id() {
		return category_type_id;
	}

	public void setCategory_type_id(Integer category_type_id) {
		this.category_type_id = category_type_id;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getCategory_name_sort() {
		return category_name_sort;
	}

	public void setCategory_name_sort(String category_name_sort) {
		this.category_name_sort = category_name_sort;
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

	public Boolean getAttendance_status() {
		return attendance_status;
	}

	public void setAttendance_status(Boolean attendance_status) {
		this.attendance_status = attendance_status;
	}

	public Boolean getOutside_campus() {
		return outside_campus;
	}

	public void setOutside_campus(Boolean outside_campus) {
		this.outside_campus = outside_campus;
	}
	
}
