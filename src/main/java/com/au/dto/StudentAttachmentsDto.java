package com.au.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

public class StudentAttachmentsDto {

	private Integer student_id;
	private String attachments_file_name;
	
	private  String attachments_file_path;
	
	
	private String educational_attach;
	private String personal_attach;
	private String qualification_attach;
	private Integer attachments_subcategory_id;
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
	private Boolean active;
	
	public StudentAttachmentsDto() {
		super();
	}

	public Integer getStudent_id() {
		return student_id;
	}

	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}

	public String getAttachments_file_name() {
		return attachments_file_name;
	}

	public void setAttachments_file_name(String attachments_file_name) {
		this.attachments_file_name = attachments_file_name;
	}



	

	public String getAttachments_file_path() {
		return attachments_file_path;
	}

	public void setAttachments_file_path(String attachments_file_path) {
		this.attachments_file_path = attachments_file_path;
	}

	public String getEducational_attach() {
		return educational_attach;
	}

	public void setEducational_attach(String educational_attach) {
		this.educational_attach = educational_attach;
	}

	public String getPersonal_attach() {
		return personal_attach;
	}

	public void setPersonal_attach(String personal_attach) {
		this.personal_attach = personal_attach;
	}

	public String getQualification_attach() {
		return qualification_attach;
	}

	public void setQualification_attach(String qualification_attach) {
		this.qualification_attach = qualification_attach;
	}

	public Integer getAttachments_subcategory_id() {
		return attachments_subcategory_id;
	}

	public void setAttachments_subcategory_id(Integer attachments_subcategory_id) {
		this.attachments_subcategory_id = attachments_subcategory_id;
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
	
	
}
