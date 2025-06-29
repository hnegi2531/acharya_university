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
@Table(name ="tution_fee_waiver_report")
public class TutionFeeWaiverReport {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer tution_fee_waiver_report_id;
	private Integer tution_fee_waiver_id;
	private Double yearly_waiver_amount;
	private Integer year_sem;
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
	
	public TutionFeeWaiverReport() {
		super();
		
	}

	public Integer getTution_fee_waiver_report_id() {
		return tution_fee_waiver_report_id;
	}

	public void setTution_fee_waiver_report_id(Integer tution_fee_waiver_report_id) {
		this.tution_fee_waiver_report_id = tution_fee_waiver_report_id;
	}

	public Integer getTution_fee_waiver_id() {
		return tution_fee_waiver_id;
	}

	public void setTution_fee_waiver_id(Integer tution_fee_waiver_id) {
		this.tution_fee_waiver_id = tution_fee_waiver_id;
	}

	public Double getYearly_waiver_amount() {
		return yearly_waiver_amount;
	}

	public void setYearly_waiver_amount(Double yearly_waiver_amount) {
		this.yearly_waiver_amount = yearly_waiver_amount;
	}

	public Integer getYear_sem() {
		return year_sem;
	}

	public void setYear_sem(Integer year_sem) {
		this.year_sem = year_sem;
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
