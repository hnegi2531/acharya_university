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
@Table(name = "documents")
public class Documents {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer documents_id;
	private String group_type;
	private String staff_student_reference;
	private String contract_number;
	private String category;
	private String document_attachment_path;
	
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
	
	private Integer school_id;
	
	public Documents() {
		super();
	}


	public Integer getDocuments_id() {
		return documents_id;
	}


	public void setDocuments_id(Integer documents_id) {
		this.documents_id = documents_id;
	}


	public String getGroup_type() {
		return group_type;
	}


	public void setGroup_type(String group_type) {
		this.group_type = group_type;
	}


	public String getStaff_student_reference() {
		return staff_student_reference;
	}


	public void setStaff_student_reference(String staff_student_reference) {
		this.staff_student_reference = staff_student_reference;
	}


	public String getContract_number() {
		return contract_number;
	}


	public void setContract_number(String contract_number) {
		this.contract_number = contract_number;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}

	public String getDocument_attachment_path() {
		return document_attachment_path;
	}


	public String setDocument_attachment_path(String document_attachment_path) {
		return this.document_attachment_path = document_attachment_path;
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


	public Integer getSchool_id() {
		return school_id;
	}


	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}
	
	
}
