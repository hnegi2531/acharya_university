package com.au.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

public class VoucherHeadRequest {
	private Integer voucher_head_id;
	//private String voucher_head;
	//private HashMap<Integer, Double> school_id;//Double is for opening balance amount school wise
	private List<Integer> school_id;
	private Integer voucher_head_new_id;
	private Integer tally_id; // fk
	private String school_name;

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
	private Integer ledger_id;
	private String voucher_type;
	private String budget_head;
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	private Integer voucher_priority;
	private Boolean salaries;
	private Integer salary_structure_head_id;
	private Boolean cash_or_bank;
	private Double opening_balance;

	public VoucherHeadRequest() {
		super();
	}

	public Integer getVoucher_head_id() {
		return voucher_head_id;
	}

	public void setVoucher_head_id(Integer voucher_head_id) {
		this.voucher_head_id = voucher_head_id;
	}

	/*public String getVoucher_head() {
		return voucher_head;
	}

	public void setVoucher_head(String voucher_head) {
		this.voucher_head = voucher_head;
	}
	*/

	public List<Integer> getSchool_id() {
		return school_id;
	}

	public void setSchool_id(List<Integer> school_id) {
		this.school_id = school_id;
	}

	public Integer getVoucher_head_new_id() {
		return voucher_head_new_id;
	}

	public void setVoucher_head_new_id(Integer voucher_head_new_id) {
		this.voucher_head_new_id = voucher_head_new_id;
	}

	public Integer getTally_id() {
		return tally_id;
	}

	public void setTally_id(Integer tally_id) {
		this.tally_id = tally_id;
	}

	public String getSchool_name() {
		return school_name;
	}

	public void setSchool_name(String school_name) {
		this.school_name = school_name;
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

	public String getBudget_head() {
		return budget_head;
	}

	public void setBudget_head(String budget_head) {
		this.budget_head = budget_head;
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

	public Integer getVoucher_priority() {
		return voucher_priority;
	}

	public void setVoucher_priority(Integer voucher_priority) {
		this.voucher_priority = voucher_priority;
	}

	public Boolean getSalaries() {
		return salaries;
	}

	public void setSalaries(Boolean salaries) {
		this.salaries = salaries;
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

}
