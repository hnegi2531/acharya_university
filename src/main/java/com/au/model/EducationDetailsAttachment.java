package com.au.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "education_details_attachments")
public class EducationDetailsAttachment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer education_details_attachment_id;
	private Integer empId;
	private Integer graduationId;
	private String graduationName;
	private String attachment_path;
	private String attachment_file_name;
	private String attachment_type;
	private String attachment_purpose;
	private Boolean active;
	public Integer getEducation_details_attachment_id() {
		return education_details_attachment_id;
	}
	public void setEducation_details_attachment_id(Integer education_details_attachment_id) {
		this.education_details_attachment_id = education_details_attachment_id;
	}
	public Integer getEmpId() {
		return empId;
	}
	public void setEmpId(Integer eduId) {
		this.empId = eduId;
	}
	
	public String getAttachment_path() {
		return attachment_path;
	}
	public void setAttachment_path(String attachment_path) {
		this.attachment_path = attachment_path;
	}
	public String getAttachment_file_name() {
		return attachment_file_name;
	}
	public void setAttachment_file_name(String attachment_file_name) {
		this.attachment_file_name = attachment_file_name;
	}
	public String getAttachment_type() {
		return attachment_type;
	}
	public void setAttachment_type(String attachment_type) {
		this.attachment_type = attachment_type;
	}
	public String getAttachment_purpose() {
		return attachment_purpose;
	}
	public void setAttachment_purpose(String attachment_purpose) {
		this.attachment_purpose = attachment_purpose;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public Integer getGraduationId() {
		return graduationId;
	}
	public void setGraduationId(Integer graduationId) {
		this.graduationId = graduationId;
	}
	public String getGraduationName() {
		return graduationName;
	}
	public void setGraduationName(String graduationName) {
		this.graduationName = graduationName;
	}
	
	
}

	