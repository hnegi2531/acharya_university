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

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "slab_details")
public class SlabDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer slab_details_id;
	private String salary_structure_head_ids;
	private String slab_details_name;
	private String slab_details_short_name;
	private String description;
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	private Boolean active;
	@Column(updatable = false)
	private String created_username;
	private String modified_username;

	public SlabDetails() {
		super();
	}

	public Integer getSlab_details_id() {
		return slab_details_id;
	}

	public void setSlab_details_id(Integer slab_details_id) {
		this.slab_details_id = slab_details_id;
	}

	public String getSalary_structure_head_ids() {
		return salary_structure_head_ids;
	}

	public void setSalary_structure_head_ids(String salary_structure_head_ids) {
		this.salary_structure_head_ids = salary_structure_head_ids;
	}

	public String getSlab_details_name() {
		return slab_details_name;
	}

	public void setSlab_details_name(String slab_details_name) {
		this.slab_details_name = slab_details_name;
	}

	public String getSlab_details_short_name() {
		return slab_details_short_name;
	}

	public void setSlab_details_short_name(String slab_details_short_name) {
		this.slab_details_short_name = slab_details_short_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
