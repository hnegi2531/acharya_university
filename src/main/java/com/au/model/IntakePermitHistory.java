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
@Table(name = "intake_permit_history")
public class IntakePermitHistory {

	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer intake_permit_history_id;
	private Integer intake_permit_id;
	private Integer intake_id;
	private Integer fee_admission_sub_category_id;
	private Integer intake_permit;
	
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
	private Integer intake_history_id;

	public IntakePermitHistory() {
		super();
	}

	public Integer getIntake_permit_history_id() {
		return intake_permit_history_id;
	}

	public void setIntake_permit_history_id(Integer intake_permit_history_id) {
		this.intake_permit_history_id = intake_permit_history_id;
	}

	public Integer getIntake_permit_id() {
		return intake_permit_id;
	}

	public void setIntake_permit_id(Integer intake_permit_id) {
		this.intake_permit_id = intake_permit_id;
	}

	public Integer getIntake_id() {
		return intake_id;
	}

	public void setIntake_id(Integer intake_id) {
		this.intake_id = intake_id;
	}

	public Integer getFee_admission_sub_category_id() {
		return fee_admission_sub_category_id;
	}

	public void setFee_admission_sub_category_id(Integer fee_admission_sub_category_id) {
		this.fee_admission_sub_category_id = fee_admission_sub_category_id;
	}

	public Integer getIntake_permit() {
		return intake_permit;
	}

	public void setIntake_permit(Integer intake_permit) {
		this.intake_permit = intake_permit;
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

	public Integer getIntake_history_id() {
		return intake_history_id;
	}

	public void setIntake_history_id(Integer intake_history_id) {
		this.intake_history_id = intake_history_id;
	}

	
}
