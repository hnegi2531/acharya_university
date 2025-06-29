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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "voucher_head_new")
public class VoucherHeadNew {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer voucher_head_new_id;
	
	@Column(unique = true)
	@NotBlank(message = "voucher_head should not be Empty OR Null")
	private String voucher_head;
	
	@Column(unique = true)
	@NotBlank(message = "voucher_head_short_name should not be Empty OR Null")
	private String voucher_head_short_name;
	
	@Column(updatable = false)
	private Integer created_by;
	
	private Integer modified_by;
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;

	private Boolean active;
	
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	private Boolean is_common;
	private Boolean is_salaries;
	private Boolean hostel_status;
	
	private Boolean is_vendor;
	
	private Integer school_id;
	private Integer tally_id;
	private Integer ledger_id;
	private String voucher_type;
	private Boolean budget_head;
	private Integer voucher_priority;
	private Integer salary_structure_head_id;
	private Boolean cash_or_bank;
	private Double opening_balance;
	private Integer priority;
	private Boolean is_exam;

	public VoucherHeadNew() {
		super();
	}

	public Integer getVoucher_head_new_id() {
		return voucher_head_new_id;
	}

	public void setVoucher_head_new_id(Integer voucher_head_new_id) {
		this.voucher_head_new_id = voucher_head_new_id;
	}

	public String getVoucher_head() {
		return voucher_head;
	}

	public void setVoucher_head(String voucher_head) {
		this.voucher_head = voucher_head;
	}

	public String getVoucher_head_short_name() {
		return voucher_head_short_name;
	}

	public void setVoucher_head_short_name(String voucher_head_short_name) {
		this.voucher_head_short_name = voucher_head_short_name;
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

	public Boolean getIs_common() {
		return is_common;
	}

	public void setIs_common(Boolean is_common) {
		this.is_common = is_common;
	}

	public Boolean getIs_salaries() {
		return is_salaries;
	}

	public void setIs_salaries(Boolean is_salaries) {
		this.is_salaries = is_salaries;
	}

	public Boolean getHostel_status() {
		return hostel_status;
	}

	public void setHostel_status(Boolean hostel_status) {
		this.hostel_status = hostel_status;
	}

	public Boolean getIs_vendor() {
		return is_vendor;
	}

	public void setIs_vendor(Boolean is_vendor) {
		this.is_vendor = is_vendor;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public Integer getTally_id() {
		return tally_id;
	}

	public void setTally_id(Integer tally_id) {
		this.tally_id = tally_id;
	}

	public Integer getLedger_id() {
		return ledger_id;
	}

	public void setLedger_id(Integer ledger_id) {
		this.ledger_id = ledger_id;
	}

	public String getVoucher_type() {
		return voucher_type;
	}

	public void setVoucher_type(String voucher_type) {
		this.voucher_type = voucher_type;
	}

	public Boolean getBudget_head() {
		return budget_head;
	}

	public void setBudget_head(Boolean budget_head) {
		this.budget_head = budget_head;
	}

	public Integer getVoucher_priority() {
		return voucher_priority;
	}

	public void setVoucher_priority(Integer voucher_priority) {
		this.voucher_priority = voucher_priority;
	}

	public Integer getSalary_structure_head_id() {
		return salary_structure_head_id;
	}

	public void setSalary_structure_head_id(Integer salary_structure_head_id) {
		this.salary_structure_head_id = salary_structure_head_id;
	}

	public Boolean getCash_or_bank() {
		return cash_or_bank;
	}

	public void setCash_or_bank(Boolean cash_or_bank) {
		this.cash_or_bank = cash_or_bank;
	}

	public Double getOpening_balance() {
		return opening_balance;
	}

	public void setOpening_balance(Double opening_balance) {
		this.opening_balance = opening_balance;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Boolean getIs_exam() {
		return is_exam;
	}

	public void setIs_exam(Boolean is_exam) {
		this.is_exam = is_exam;
	}
	
	

}
