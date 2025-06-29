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
@Table(name="vendor_attachments")
public class VendorAttachment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer vendor_attachment_id;
	private Integer vendor_id;
	private String vendor_attachment_path;
	private String vendor_attachment_file_name;
	private String vendor_attachement_type;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date created_date;
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified_date;
	
	
	public VendorAttachment() {
		super();
		
	}


	public Integer getVendor_attachment_id() {
		return vendor_attachment_id;
	}


	public void setVendor_attachment_id(Integer vendor_attachment_id) {
		this.vendor_attachment_id = vendor_attachment_id;
	}


	public Integer getVendor_id() {
		return vendor_id;
	}


	public void setVendor_id(Integer vendor_id) {
		this.vendor_id = vendor_id;
	}


	public String getVendor_attachment_path() {
		return vendor_attachment_path;
	}


	public void setVendor_attachment_path(String vendor_attachment_path) {
		this.vendor_attachment_path = vendor_attachment_path;
	}


	public String getVendor_attachment_file_name() {
		return vendor_attachment_file_name;
	}


	public void setVendor_attachment_file_name(String vendor_attachment_file_name) {
		this.vendor_attachment_file_name = vendor_attachment_file_name;
	}


	public String getVendor_attachement_type() {
		return vendor_attachement_type;
	}


	public void setVendor_attachement_type(String vendor_attachement_type) {
		this.vendor_attachement_type = vendor_attachement_type;
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
	
	
	
	
	
	

}
