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
@Table(name = "department_tags")
public class DepartmentTags {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer tag_id;
	@Column(unique = true)
	@NotBlank(message = "service name should not be Empty OR Null")
	private String tag_name;
	@Column(unique = true)
	@NotBlank(message = "service short name should not be Empty OR Null")
	private String tag_short_name;
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	private Integer created_by;
	private Integer modified_by;
	private Boolean active;
	private Integer service_oriented;
	private Boolean show_in_event;
	private String created_username;
	private String modified_username;

	public DepartmentTags() {
		super();
	}

	public Integer getTag_id() {
		return tag_id;
	}

	public void setTag_id(Integer tag_id) {
		this.tag_id = tag_id;
	}

	public String getTag_name() {
		return tag_name;
	}

	public void setTag_name(String tag_name) {
		this.tag_name = tag_name;
	}

	public String getTag_short_name() {
		return tag_short_name;
	}

	public void setTag_short_name(String tag_short_name) {
		this.tag_short_name = tag_short_name;
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

	public Integer getService_oriented() {
		return service_oriented;
	}

	public void setService_oriented(Integer service_oriented) {
		this.service_oriented = service_oriented;
	}

	public Boolean getShow_in_event() {
		return show_in_event;
	}

	public void setShow_in_event(Boolean show_in_event) {
		this.show_in_event = show_in_event;
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
