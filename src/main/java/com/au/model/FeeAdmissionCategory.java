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
@Table(name = "fee_admission_category")
public class FeeAdmissionCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer fee_admission_category_id;
	
	@Column(unique = true)
	@NotBlank(message = "fee_admission_category_type should not be Empty OR Null")
	private String fee_admission_category_type;
	
	@Column(unique = true)
	@NotBlank(message = "fee_admission_category_short_name should not be Empty OR Null")
	private String fee_admission_category_short_name;

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
	private boolean active;
	private boolean is_check;

	private String is_sub_category_applicable;
	
	@Column(updatable = false)
	private String created_username;
	
	private String modified_username;
	private boolean year_sem;
	
	private boolean is_regular;

	public FeeAdmissionCategory() {
		super();
	}

	public FeeAdmissionCategory(Integer fee_admission_category_id, String fee_admission_category_type,
			String fee_admission_category_short_name, Date created_date, Date modified_date, Integer created_by,
			Integer modified_by, boolean active, boolean is_check, String is_sub_category_applicable) {
		super();
		this.fee_admission_category_id = fee_admission_category_id;
		this.fee_admission_category_type = fee_admission_category_type;
		this.fee_admission_category_short_name = fee_admission_category_short_name;
		this.created_date = created_date;
		this.modified_date = modified_date;
		this.created_by = created_by;
		this.modified_by = modified_by;
		this.active = active;
		this.is_check = is_check;
		this.is_sub_category_applicable = is_sub_category_applicable;
	}

	public Integer getFee_admission_category_id() {
		return fee_admission_category_id;
	}

	public void setFee_admission_category_id(Integer fee_admission_category_id) {
		this.fee_admission_category_id = fee_admission_category_id;
	}

	public String getFee_admission_category_type() {
		return fee_admission_category_type;
	}

	public void setFee_admission_category_type(String fee_admission_category_type) {
		this.fee_admission_category_type = fee_admission_category_type;
	}

	public String getFee_admission_category_short_name() {
		return fee_admission_category_short_name;
	}

	public void setFee_admission_category_short_name(String fee_admission_category_short_name) {
		this.fee_admission_category_short_name = fee_admission_category_short_name;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isIs_check() {
		return is_check;
	}

	public void setIs_check(boolean is_check) {
		this.is_check = is_check;
	}

	public String getIs_sub_category_applicable() {
		return is_sub_category_applicable;
	}

	public void setIs_sub_category_applicable(String is_sub_category_applicable) {
		this.is_sub_category_applicable = is_sub_category_applicable;
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

	public boolean isYear_sem() {
		return year_sem;
	}

	public void setYear_sem(boolean year_sem) {
		this.year_sem = year_sem;
	}

	public boolean isIs_regular() {
		return is_regular;
	}

	public void setIs_regular(boolean is_regular) {
		this.is_regular = is_regular;
	}
	
	

}