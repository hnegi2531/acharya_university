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
@Table(name = "salary_structure_category")
public class SalaryStructureCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer salary_structure_category_id;
	private String salary_structure_category_name;
	private String salary_structure_category_short_name;
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;
	@Column(updatable = false)
	@Temporal(TemporalType.DATE)
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	private Boolean active;
	@Column(updatable = false)
	private String created_username;
	private String modified_username;

	public SalaryStructureCategory() {
		super();
	}

	public Integer getSalary_structure_category_id() {
		return salary_structure_category_id;
	}

	public void setSalary_structure_category_id(Integer salary_structure_category_id) {
		this.salary_structure_category_id = salary_structure_category_id;
	}

	public String getSalary_structure_category_name() {
		return salary_structure_category_name;
	}

	public void setSalary_structure_category_name(String salary_structure_category_name) {
		this.salary_structure_category_name = salary_structure_category_name;
	}

	public String getSalary_structure_category_short_name() {
		return salary_structure_category_short_name;
	}

	public void setSalary_structure_category_short_name(String salary_structure_category_short_name) {
		this.salary_structure_category_short_name = salary_structure_category_short_name;
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
