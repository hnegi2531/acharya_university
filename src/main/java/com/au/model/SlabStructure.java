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
@Table(name = "slab_structure")
public class SlabStructure {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer slab_structure_id;
	private Integer slab_details_id;
	private Integer min_value;
	private Integer max_value;
	private Float head_value;
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

	public SlabStructure() {
		super();
	}

	public Integer getSlab_structure_id() {
		return slab_structure_id;
	}

	public void setSlab_structure_id(Integer slab_structure_id) {
		this.slab_structure_id = slab_structure_id;
	}

	public Integer getSlab_details_id() {
		return slab_details_id;
	}

	public void setSlab_details_id(Integer slab_details_id) {
		this.slab_details_id = slab_details_id;
	}

	public Integer getMin_value() {
		return min_value;
	}

	public void setMin_value(Integer min_value) {
		this.min_value = min_value;
	}

	public Integer getMax_value() {
		return max_value;
	}

	public void setMax_value(Integer max_value) {
		this.max_value = max_value;
	}

	public Float getHead_value() {
		return head_value;
	}

	public void setHead_value(Float head_value) {
		this.head_value = head_value;
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
