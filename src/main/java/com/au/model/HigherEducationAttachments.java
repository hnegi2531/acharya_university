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
@Table(name = "higher_education_attachment")
public class HigherEducationAttachments {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer he_attachment_id;
	private String he_attachment_path;
	private String he_attachment_file_name;
	private String he_attachement_type;
	private Integer job_id;
	
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
	
	@Column(name = "created_username", updatable = false)
	private String created_username;
	private String modified_username;
	
	public HigherEducationAttachments() {
		super();
	}

	public Integer getHe_attachment_id() {
		return he_attachment_id;
	}

	public void setHe_attachment_id(Integer he_attachment_id) {
		this.he_attachment_id = he_attachment_id;
	}

	public String getHe_attachment_path() {
		return he_attachment_path;
	}

	public void setHe_attachment_path(String he_attachment_path) {
		this.he_attachment_path = he_attachment_path;
	}

	public String getHe_attachment_file_name() {
		return he_attachment_file_name;
	}

	public void setHe_attachment_file_name(String he_attachment_file_name) {
		this.he_attachment_file_name = he_attachment_file_name;
	}

	public String getHe_attachement_type() {
		return he_attachement_type;
	}

	public void setHe_attachement_type(String he_attachement_type) {
		this.he_attachement_type = he_attachement_type;
	}

	public Integer getJob_id() {
		return job_id;
	}

	public void setJob_id(Integer job_id) {
		this.job_id = job_id;
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
	
}
