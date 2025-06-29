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
@Table(name = "scholarship_attachment")
public class ScholarshipAttachment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer scholarship_attachment_id;
	private Integer student_id;
	private String scholarship_attachment_path;
	private String scholarship_attachment_file_name;
	private String scholarship_attachement_type;
	private Integer candidate_id;
	
	
	@Column(name = "created_date",updatable = false)
	@Temporal(TemporalType.TIMESTAMP)	
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)	
	@UpdateTimestamp
	private Date modified_date;

	private Integer created_by;
	private Integer modified_by;
	private boolean active;
	
	private String created_username;
	private String modified_username;

	
	public ScholarshipAttachment() {
		super();
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


	public Integer getScholarship_attachment_id() {
		return scholarship_attachment_id;
	}
	public void setScholarship_attachment_id(Integer scholarship_attachment_id) {
		this.scholarship_attachment_id = scholarship_attachment_id;
	}
	public Integer getStudent_id() {
		return student_id;
	}
	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}
	
	
	
	public String getScholarship_attachment_path() {
		return scholarship_attachment_path;
	}
	public void setScholarship_attachment_path(String scholarship_attachment_path) {
		this.scholarship_attachment_path = scholarship_attachment_path;
	}
	public String getScholarship_attachment_file_name() {
		return scholarship_attachment_file_name;
	}
	public void setScholarship_attachment_file_name(String scholarship_attachment_file_name) {
		this.scholarship_attachment_file_name = scholarship_attachment_file_name;
	}
	public Integer getCandidate_id() {
		return candidate_id;
	}
	public void setCandidate_id(Integer candidate_id) {
		this.candidate_id = candidate_id;
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
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}


	public String getScholarship_attachement_type() {
		return scholarship_attachement_type;
	}


	public void setScholarship_attachement_type(String scholarship_attachement_type) {
		this.scholarship_attachement_type = scholarship_attachement_type;
	}

	
	
}
