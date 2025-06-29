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
@Table(name = "scholarship_vocher_head_wise_amount_details")
public class ScholarshipVocherHeadWiseAmountDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer sch_vocher_head_wise_amount_id;
	private Integer scholarship_id;
	private Integer voucher_head_new_id;
	//voucher head amount for a particular year
	private Integer amount;
	private Integer scholarship_year;
	
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
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	
	public ScholarshipVocherHeadWiseAmountDetails() {
		super();
	}
	
	
	public Integer getSch_vocher_head_wise_amount_id() {
		return sch_vocher_head_wise_amount_id;
	}
	public void setSch_vocher_head_wise_amount_id(Integer sch_vocher_head_wise_amount_id) {
		this.sch_vocher_head_wise_amount_id = sch_vocher_head_wise_amount_id;
	}
	public Integer getScholarship_id() {
		return scholarship_id;
	}
	public void setScholarship_id(Integer scholarship_id) {
		this.scholarship_id = scholarship_id;
	}
	public Integer getVoucher_head_new_id() {
		return voucher_head_new_id;
	}
	public void setVoucher_head_new_id(Integer voucher_head_new_id) {
		this.voucher_head_new_id = voucher_head_new_id;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Integer getScholarship_year() {
		return scholarship_year;
	}
	public void setScholarship_year(Integer scholarship_year) {
		this.scholarship_year = scholarship_year;
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
	
}
