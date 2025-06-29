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
@Table(name = "fee_head")
public class FeeHead {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
 	private Integer fee_head_id;
	@Column(unique = true)
	@NotBlank(message = "fee_head should not be Empty OR Null")
	private String fee_head;
	private String school_id; // institute_id
	private Integer charged_type_id;
	private Boolean repeat_fee_head;
	private String fee_head_category;
	private Integer tally_id;
	
	private Boolean vendor_id;

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
	
	public FeeHead() {
		super();
	}

	public Integer getFee_head_id() {
		return fee_head_id;
	}

	public void setFee_head_id(Integer fee_head_id) {
		this.fee_head_id = fee_head_id;
	}

	public String getFee_head() {
		return fee_head;
	}

	public void setFee_head(String fee_head) {
		this.fee_head = fee_head;
	}

	public String getSchool_id() {
		return school_id;
	}

	public void setSchool_id(String school_id) {
		this.school_id = school_id;
	}

	public Integer getCharged_type_id() {
		return charged_type_id;
	}

	public void setCharged_type_id(Integer charged_type_id) {
		this.charged_type_id = charged_type_id;
	}

	public Boolean getRepeat_fee_head() {
		return repeat_fee_head;
	}

	public void setRepeat_fee_head(Boolean repeat_fee_head) {
		this.repeat_fee_head = repeat_fee_head;
	}

	public String getFee_head_category() {
		return fee_head_category;
	}

	public void setFee_head_category(String fee_head_category) {
		this.fee_head_category = fee_head_category;
	}

	public Integer getTally_id() {
		return tally_id;
	}

	public void setTally_id(Integer tally_id) {
		this.tally_id = tally_id;
	}

	public Boolean getVendor_id() {
		return vendor_id;
	}

	public void setVendor_id(Boolean vendor_id) {
		this.vendor_id = vendor_id;
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
