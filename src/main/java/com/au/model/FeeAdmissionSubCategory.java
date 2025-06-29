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
@Table(name = "fee_admission_sub_category")
public class FeeAdmissionSubCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer fee_admission_sub_category_id;


	@NotBlank(message = "fee_admission_sub_category_name should not be Empty OR Null")
	private String fee_admission_sub_category_name;


	@NotBlank(message = "fee_admission_sub_category_short_name should not be Empty OR Null")
	private String fee_admission_sub_category_short_name;

	@Column(name = "created_date", updatable = false)
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
	private Integer fee_admission_category_id; // FK
	
	@Column(updatable = false)
	private String created_username;
	
	private String modified_username;
	private Integer board_unique_id;
	private Boolean approve_intake;

	public FeeAdmissionSubCategory() {
		super();

	}

	public Integer getFee_admission_sub_category_id() {
		return fee_admission_sub_category_id;
	}

	public void setFee_admission_sub_category_id(Integer fee_admission_sub_category_id) {
		this.fee_admission_sub_category_id = fee_admission_sub_category_id;
	}

	public String getFee_admission_sub_category_name() {
		return fee_admission_sub_category_name;
	}

	public void setFee_admission_sub_category_name(String fee_admission_sub_category_name) {
		this.fee_admission_sub_category_name = fee_admission_sub_category_name;
	}

	public String getFee_admission_sub_category_short_name() {
		return fee_admission_sub_category_short_name;
	}

	public void setFee_admission_sub_category_short_name(String fee_admission_sub_category_short_name) {
		this.fee_admission_sub_category_short_name = fee_admission_sub_category_short_name;
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

	public Integer getFee_admission_category_id() {
		return fee_admission_category_id;
	}

	public void setFee_admission_category_id(Integer fee_admission_category_id) {
		this.fee_admission_category_id = fee_admission_category_id;
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

	public Integer getBoard_unique_id() {
		return board_unique_id;
	}

	public void setBoard_unique_id(Integer board_unique_id) {
		this.board_unique_id = board_unique_id;
	}

	public Boolean getApprove_intake() {
		return approve_intake;
	}

	public void setApprove_intake(Boolean approve_intake) {
		this.approve_intake = approve_intake;
	}
	
	

}
