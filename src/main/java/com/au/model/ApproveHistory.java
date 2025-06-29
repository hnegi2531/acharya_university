package com.au.model;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "approve_history")
public class ApproveHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer app_his_id;
	private Integer fee_template_id;
	private Integer approved_by;
	private Boolean approved_status;
	private LocalDate approved_date;
	
	public ApproveHistory() {
		super();
	}

	public Integer getApp_his_id() {
		return app_his_id;
	}

	public void setApp_his_id(Integer app_his_id) {
		this.app_his_id = app_his_id;
	}

	public Integer getFee_template_id() {
		return fee_template_id;
	}

	public void setFee_template_id(Integer fee_template_id) {
		this.fee_template_id = fee_template_id;
	}

	public Integer getApproved_by() {
		return approved_by;
	}

	public void setApproved_by(Integer approved_by) {
		this.approved_by = approved_by;
	}

	public Boolean getApproved_status() {
		return approved_status;
	}

	public void setApproved_status(Boolean approved_status) {
		this.approved_status = approved_status;
	}

	public LocalDate getApproved_date() {
		return approved_date;
	}

	public void setApproved_date(LocalDate approved_date) {
		this.approved_date = approved_date;
	}
}
