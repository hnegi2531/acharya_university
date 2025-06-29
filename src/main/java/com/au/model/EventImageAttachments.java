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
@Table(name = "event_image_attachments")
public class EventImageAttachments {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer event_image_attche_id;
	private Integer event_id;
	private String event_image_path;
	private String event_image_type;
	private String image_upload_timing;
	
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
	
	public EventImageAttachments() {
		super();
	}

	public Integer getEvent_image_attche_id() {
		return event_image_attche_id;
	}

	public void setEvent_image_attche_id(Integer event_image_attche_id) {
		this.event_image_attche_id = event_image_attche_id;
	}

	public Integer getEvent_id() {
		return event_id;
	}

	public void setEvent_id(Integer event_id) {
		this.event_id = event_id;
	}

	public String getEvent_image_path() {
		return event_image_path;
	}

	public void setEvent_image_path(String event_image_path) {
		this.event_image_path = event_image_path;
	}

	public String getEvent_image_type() {
		return event_image_type;
	}

	public void setEvent_image_type(String event_image_type) {
		this.event_image_type = event_image_type;
	}

	public String getImage_upload_timing() {
		return image_upload_timing;
	}

	public void setImage_upload_timing(String image_upload_timing) {
		this.image_upload_timing = image_upload_timing;
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
	
	
	
}
