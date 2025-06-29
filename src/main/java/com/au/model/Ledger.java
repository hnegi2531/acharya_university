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
@Table(name = "ledger")
public class Ledger {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer ledger_id;

	private Integer group_id; // fk
	@Column(unique = true)
	@NotBlank(message = "ledger_name should not be Empty OR Null")
	private String ledger_name;
	private String remarks;
	private Integer room_status;
	@Column(unique = true)
	@NotBlank(message = "ledger_short_name should not be Empty OR Null")
	private String ledger_short_name;
	
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
	
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	private Integer priority;
	
	private String capex;
	private String opex;
	private String type_of_expenses ;
	
	private String name_in_russia;
	private String name_in_english;
	private String balance_sheet_row_code;
	
	private String financial_report_status;
	private Integer tally_id;
	
	public Ledger() {
		super();
		
	}

	public Integer getLedger_id() {
		return ledger_id;
	}

	public void setLedger_id(Integer ledger_id) {
		this.ledger_id = ledger_id;
	}

	public Integer getGroup_id() {
		return group_id;
	}

	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}

	public String getLedger_name() {
		return ledger_name;
	}

	public void setLedger_name(String ledger_name) {
		this.ledger_name = ledger_name;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getRoom_status() {
		return room_status;
	}

	public void setRoom_status(Integer room_status) {
		this.room_status = room_status;
	}

	public String getLedger_short_name() {
		return ledger_short_name;
	}

	public void setLedger_short_name(String ledger_short_name) {
		this.ledger_short_name = ledger_short_name;
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

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getCapex() {
		return capex;
	}

	public void setCapex(String capex) {
		this.capex = capex;
	}

	public String getOpex() {
		return opex;
	}

	public void setOpex(String opex) {
		this.opex = opex;
	}

	public String getType_of_expenses() {
		return type_of_expenses;
	}

	public void setType_of_expenses(String type_of_expenses) {
		this.type_of_expenses = type_of_expenses;
	}

	public String getName_in_russia() {
		return name_in_russia;
	}

	public void setName_in_russia(String name_in_russia) {
		this.name_in_russia = name_in_russia;
	}

	public String getName_in_english() {
		return name_in_english;
	}

	public void setName_in_english(String name_in_english) {
		this.name_in_english = name_in_english;
	}

	public String getBalance_sheet_row_code() {
		return balance_sheet_row_code;
	}

	public void setBalance_sheet_row_code(String balance_sheet_row_code) {
		this.balance_sheet_row_code = balance_sheet_row_code;
	}

	public String getFinancial_report_status() {
		return financial_report_status;
	}

	public void setFinancial_report_status(String financial_report_status) {
		this.financial_report_status = financial_report_status;
	}

	public Integer getTally_id() {
		return tally_id;
	}

	public void setTally_id(Integer tally_id) {
		this.tally_id = tally_id;
	}

	

}
