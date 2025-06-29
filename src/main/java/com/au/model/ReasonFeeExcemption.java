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
@Table(name = "reason_fee_excemption")
public class ReasonFeeExcemption {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer fee_exemption_id;
	@Column(unique = true)
	private String  reasion_for_fee_exemption;

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
	private Boolean active;
	
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	private String exemption_status;
	
	public ReasonFeeExcemption() {
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



	public Integer getFee_exemption_id() {
		return fee_exemption_id;
	}
	public void setFee_exemption_id(Integer fee_exemption_id) {
		this.fee_exemption_id = fee_exemption_id;
	}
	public String getReasion_for_fee_exemption() {
		return reasion_for_fee_exemption;
	}
	public void setReasion_for_fee_exemption(String reasion_for_fee_exemption) {
		this.reasion_for_fee_exemption = reasion_for_fee_exemption;
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

	public String getExemption_status() {
		return exemption_status;
	}
	
	public void setExemption_status(String exemption_status) {
		this.exemption_status = exemption_status;
	}
	
	
	
}
