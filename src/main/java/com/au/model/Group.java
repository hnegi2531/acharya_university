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
@Table(name = "group_table")
public class Group {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer group_id;
	
	@Column(unique = true)
	@NotBlank(message = "group_name should not be Empty OR Null")
	private String group_name;
	
	@Column(unique = true)
	@NotBlank(message = "group_short_name should not be Empty OR Null")
	private String group_short_name;

	@Column(updatable = false)
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
	private Boolean active;
	private String remarks;
	private Integer group_priority;
	private String financials;
	private String balance_sheet_group;
	
	@Column(updatable = false)
	private String created_username;
	
	private String modified_username;

	public Group() {
		super();

	}

	public Integer getGroup_id() {
		return group_id;
	}

	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}

	public String getFinancials() {
		return financials;
	}

	public void setFinancials(String financials) {
		this.financials = financials;
	}

	public String getBalance_sheet_group() {
		return balance_sheet_group;
	}

	public void setBalance_sheet_group(String balance_sheet_group) {
		this.balance_sheet_group = balance_sheet_group;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getGroup_short_name() {
		return group_short_name;
	}

	public void setGroup_short_name(String group_short_name) {
		this.group_short_name = group_short_name;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getGroup_priority() {
		return group_priority;
	}

	public void setGroup_priority(Integer group_priority) {
		this.group_priority = group_priority;
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
