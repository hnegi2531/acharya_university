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
@Table(name = "attachment_sub_category")
public class AttachmentSubCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer attachments_subcategory_id;
	private Integer attachments_category_id;
	
	@NotBlank(message = "attachments_subcategory_name should not be Empty OR Null")
	private String attachments_subcategory_name;
	
	@NotBlank(message = "attachments_subcategory_name_short should not be Empty OR Null")
	private String attachments_subcategory_name_short;
	
	@Column(updatable = false)
	private String created_username;
	private String modified_username;

	
	@Column(name = "created_date",updatable = false)
	@Temporal(TemporalType.TIMESTAMP)	
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)	
	@UpdateTimestamp
	private Date modified_date;
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;
	private Boolean active;
	
	public AttachmentSubCategory() {
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




	public Integer getAttachments_subcategory_id() {
		return attachments_subcategory_id;
	}

	public void setAttachments_subcategory_id(Integer attachments_subcategory_id) {
		this.attachments_subcategory_id = attachments_subcategory_id;
	}

	public Integer getAttachments_category_id() {
		return attachments_category_id;
	}

	public void setAttachments_category_id(Integer attachments_category_id) {
		this.attachments_category_id = attachments_category_id;
	}

	public String getAttachments_subcategory_name() {
		return attachments_subcategory_name;
	}

	public void setAttachments_subcategory_name(String attachments_subcategory_name) {
		this.attachments_subcategory_name = attachments_subcategory_name;
	}

	public String getAttachments_subcategory_name_short() {
		return attachments_subcategory_name_short;
	}

	public void setAttachments_subcategory_name_short(String attachments_subcategory_name_short) {
		this.attachments_subcategory_name_short = attachments_subcategory_name_short;
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
