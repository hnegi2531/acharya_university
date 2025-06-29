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
@Table(name = "sms_template_format")
public class SmsTemplateFormat {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer sms_template_format_id;
	
	@Column(unique =true)
	private String template_name;
	
	@Column(unique =true)
	private String template_type;
	
	@Column(columnDefinition="LONGTEXT")
	private String english_content;
	
	@Column(columnDefinition="LONGTEXT")
	private String uzbek_content;
	
	private String lead_satge;
	private Integer role_id;
	private String user_id;
	private String program_id;
	private String program_assignment_id;
	private String template_for;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date created_date;
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified_date;
	
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;
	private boolean active;
	
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	
	public SmsTemplateFormat() {
		super();

	}

	public Integer getSms_template_format_id() {
		return sms_template_format_id;
	}

	public void setSms_template_format_id(Integer sms_template_format_id) {
		this.sms_template_format_id = sms_template_format_id;
	}

	public String getTemplate_name() {
		return template_name;
	}

	public void setTemplate_name(String template_name) {
		this.template_name = template_name;
	}

	public String getTemplate_type() {
		return template_type;
	}

	public void setTemplate_type(String template_type) {
		this.template_type = template_type;
	}

	public String getEnglish_content() {
		return english_content;
	}

	public void setEnglish_content(String english_content) {
		this.english_content = english_content;
	}

	public String getLead_satge() {
		return lead_satge;
	}

	public void setLead_satge(String lead_satge) {
		this.lead_satge = lead_satge;
	}

	public Integer getRole_id() {
		return role_id;
	}

	public void setRole_id(Integer role_id) {
		this.role_id = role_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
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

	public String getUzbek_content() {
		return uzbek_content;
	}

	public void setUzbek_content(String uzbek_content) {
		this.uzbek_content = uzbek_content;
	}

	public String getProgram_id() {
		return program_id;
	}

	public void setProgram_id(String program_id) {
		this.program_id = program_id;
	}

	public String getTemplate_for() {
		return template_for;
	}

	public void setTemplate_for(String template_for) {
		this.template_for = template_for;
	}

	public String getProgram_assignment_id() {
		return program_assignment_id;
	}

	public void setProgram_assignment_id(String program_assignment_id) {
		this.program_assignment_id = program_assignment_id;
	}
	
	
	
	

}