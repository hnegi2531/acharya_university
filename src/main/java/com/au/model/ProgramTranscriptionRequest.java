package com.au.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

public class ProgramTranscriptionRequest {

	private Integer trans_id;
	// private String transcript_details;
	// private String transcript_short_name;
	private List<Integer> program_id;
	private Integer created_by;
	private Integer modified_by;

	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;

	private Boolean active;

	private Integer foriegn_status;
	private String created_username;
	private String modified_username;
	
	private Boolean is_submitted;
	private String fee_admission_sub_category_id;

	public Integer getTrans_id() {
		return trans_id;
	}

	public void setTrans_id(Integer trans_id) {
		this.trans_id = trans_id;
	}

	public List<Integer> getProgram_id() {
		return program_id;
	}

	public void setProgram_id(List<Integer> program_id) {
		this.program_id = program_id;
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Integer getForiegn_status() {
		return foriegn_status;
	}

	public void setForiegn_status(Integer foriegn_status) {
		this.foriegn_status = foriegn_status;
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

	public Boolean getIs_submitted() {
		return is_submitted;
	}

	public void setIs_submitted(Boolean is_submitted) {
		this.is_submitted = is_submitted;
	}

	public String getFee_admission_sub_category_id() {
		return fee_admission_sub_category_id;
	}

	public void setFee_admission_sub_category_id(String fee_admission_sub_category_id) {
		this.fee_admission_sub_category_id = fee_admission_sub_category_id;
	}


	

}