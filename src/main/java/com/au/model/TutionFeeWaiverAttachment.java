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
@Table(name="tution_fee_waiver_attachment")
public class TutionFeeWaiverAttachment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer tution_fee_waiver_attachment_id;
	private Integer tution_fee_waiver_id;
	private String tution_fee_waiver_attachment_path;
	private String tution_fee_waiver_attachment_file_name;
	private String tution_fee_waiver_attachement_type;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date created_date;
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified_date;
	
	public TutionFeeWaiverAttachment() {
		super();
		
	}

	public Integer getTution_fee_waiver_attachment_id() {
		return tution_fee_waiver_attachment_id;
	}

	public void setTution_fee_waiver_attachment_id(Integer tution_fee_waiver_attachment_id) {
		this.tution_fee_waiver_attachment_id = tution_fee_waiver_attachment_id;
	}

	public Integer getTution_fee_waiver_id() {
		return tution_fee_waiver_id;
	}

	public void setTution_fee_waiver_id(Integer tution_fee_waiver_id) {
		this.tution_fee_waiver_id = tution_fee_waiver_id;
	}

	public String getTution_fee_waiver_attachment_path() {
		return tution_fee_waiver_attachment_path;
	}

	public void setTution_fee_waiver_attachment_path(String tution_fee_waiver_attachment_path) {
		this.tution_fee_waiver_attachment_path = tution_fee_waiver_attachment_path;
	}

	public String getTution_fee_waiver_attachment_file_name() {
		return tution_fee_waiver_attachment_file_name;
	}

	public void setTution_fee_waiver_attachment_file_name(String tution_fee_waiver_attachment_file_name) {
		this.tution_fee_waiver_attachment_file_name = tution_fee_waiver_attachment_file_name;
	}

	public String getTution_fee_waiver_attachement_type() {
		return tution_fee_waiver_attachement_type;
	}

	public void setTution_fee_waiver_attachement_type(String tution_fee_waiver_attachement_type) {
		this.tution_fee_waiver_attachement_type = tution_fee_waiver_attachement_type;
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
