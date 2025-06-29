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
@Table(name = "bank_assignment")
public class BankAssignment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bank_assignment_id;
	private Integer bank_id;    // voucher_head_new_id
	private String acc_name;
	private String acc_number;
	private String ifsc_code;
	private String swift_code;
	private String bank_branch_name;
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
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;
	private Integer school_id;
	private Float opening_balance;
	private Float bank_balance;
	private Integer bank_balance_modified_by;
	private Date bank_balance_modified_date;
	//private Integer bank_group_id;
	private Integer ledger_id;
	private Boolean internal_status;

	public BankAssignment() {
		super();
	}

	public Integer getBank_assignment_id() {
		return bank_assignment_id;
	}

	public void setBank_assignment_id(Integer bank_assignment_id) {
		this.bank_assignment_id = bank_assignment_id;
	}

	public Integer getBank_id() {
		return bank_id;
	}

	public void setBank_id(Integer bank_id) {
		this.bank_id = bank_id;
	}

	public String getAcc_name() {
		return acc_name;
	}

	public void setAcc_name(String acc_name) {
		this.acc_name = acc_name;
	}

	public String getAcc_number() {
		return acc_number;
	}

	public void setAcc_number(String acc_number) {
		this.acc_number = acc_number;
	}

	public String getIfsc_code() {
		return ifsc_code;
	}

	public void setIfsc_code(String ifsc_code) {
		this.ifsc_code = ifsc_code;
	}

	public String getSwift_code() {
		return swift_code;
	}

	public void setSwift_code(String swift_code) {
		this.swift_code = swift_code;
	}

	public String getBank_branch_name() {
		return bank_branch_name;
	}

	public void setBank_branch_name(String bank_branch_name) {
		this.bank_branch_name = bank_branch_name;
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

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public Float getOpening_balance() {
		return opening_balance;
	}

	public void setOpening_balance(Float opening_balance) {
		this.opening_balance = opening_balance;
	}

	public Float getBank_balance() {
		return bank_balance;
	}

	public void setBank_balance(Float bank_balance) {
		this.bank_balance = bank_balance;
	}

	public Integer getBank_balance_modified_by() {
		return bank_balance_modified_by;
	}

	public void setBank_balance_modified_by(Integer bank_balance_modified_by) {
		this.bank_balance_modified_by = bank_balance_modified_by;
	}

	public Date getBank_balance_modified_date() {
		return bank_balance_modified_date;
	}

	public void setBank_balance_modified_date(Date bank_balance_modified_date) {
		this.bank_balance_modified_date = bank_balance_modified_date;
	}

	public Integer getLedger_id() {
		return ledger_id;
	}

	public void setLedger_id(Integer ledger_id) {
		this.ledger_id = ledger_id;
	}

	public Boolean getInternal_status() {
		return internal_status;
	}

	public void setInternal_status(Boolean internal_status) {
		this.internal_status = internal_status;
	}

	
}
