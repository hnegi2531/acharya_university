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

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name ="court_cases_history")
public class CourtCasesHistory {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer court_cases_history_id;
	private Integer court_cases_id;
	private String advocate_or_firm_name;
	private String advocate_or_firm_contact_no;
	private String case_no;
	private String appeal_ref_no;
	private String plaintiffs;
	private String defendants;
	private Integer court_id;
	@JsonFormat(pattern ="dd-mm-yyyy")
	private String last_hearing_date;
	@JsonFormat(pattern ="dd-mm-yyyy")
	private String next_hearing_date;
	private String stage_of_the_case;
	private String case_content;
	private String case_status;
	private String case_type;
	private String remarks;
	private Boolean active;
	
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;
	
	@Column(updatable = false)
	private Integer created_by;
	
	@Column(updatable = false)
	private String created_username;
	private Date fronted_use_next_hearing_date;
	private Date fronted_use_last_hearing_date;
	

	public CourtCasesHistory() {
		super();
	}


	public Integer getCourt_cases_history_id() {
		return court_cases_history_id;
	}


	public void setCourt_cases_history_id(Integer court_cases_history_id) {
		this.court_cases_history_id = court_cases_history_id;
	}


	public Integer getCourt_cases_id() {
		return court_cases_id;
	}


	public void setCourt_cases_id(Integer court_cases_id) {
		this.court_cases_id = court_cases_id;
	}


	public String getAdvocate_or_firm_name() {
		return advocate_or_firm_name;
	}


	public void setAdvocate_or_firm_name(String advocate_or_firm_name) {
		this.advocate_or_firm_name = advocate_or_firm_name;
	}


	public String getAdvocate_or_firm_contact_no() {
		return advocate_or_firm_contact_no;
	}


	public void setAdvocate_or_firm_contact_no(String advocate_or_firm_contact_no) {
		this.advocate_or_firm_contact_no = advocate_or_firm_contact_no;
	}


	public String getCase_no() {
		return case_no;
	}


	public void setCase_no(String case_no) {
		this.case_no = case_no;
	}


	public String getAppeal_ref_no() {
		return appeal_ref_no;
	}


	public void setAppeal_ref_no(String appeal_ref_no) {
		this.appeal_ref_no = appeal_ref_no;
	}


	public String getPlaintiffs() {
		return plaintiffs;
	}


	public void setPlaintiffs(String plaintiffs) {
		this.plaintiffs = plaintiffs;
	}


	public String getDefendants() {
		return defendants;
	}


	public void setDefendants(String defendants) {
		this.defendants = defendants;
	}


	public Integer getCourt_id() {
		return court_id;
	}


	public void setCourt_id(Integer court_id) {
		this.court_id = court_id;
	}


	public String getLast_hearing_date() {
		return last_hearing_date;
	}


	public void setLast_hearing_date(String last_hearing_date) {
		this.last_hearing_date = last_hearing_date;
	}


	public String getNext_hearing_date() {
		return next_hearing_date;
	}


	public void setNext_hearing_date(String next_hearing_date) {
		this.next_hearing_date = next_hearing_date;
	}


	public String getStage_of_the_case() {
		return stage_of_the_case;
	}


	public void setStage_of_the_case(String stage_of_the_case) {
		this.stage_of_the_case = stage_of_the_case;
	}


	public String getCase_content() {
		return case_content;
	}


	public void setCase_content(String case_content) {
		this.case_content = case_content;
	}


	public String getCase_status() {
		return case_status;
	}


	public void setCase_status(String case_status) {
		this.case_status = case_status;
	}


	public String getCase_type() {
		return case_type;
	}


	public void setCase_type(String case_type) {
		this.case_type = case_type;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public Boolean getActive() {
		return active;
	}


	public void setActive(Boolean active) {
		this.active = active;
	}


	public Date getCreated_date() {
		return created_date;
	}


	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}


	public Integer getCreated_by() {
		return created_by;
	}


	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}


	public String getCreated_username() {
		return created_username;
	}


	public void setCreated_username(String created_username) {
		this.created_username = created_username;
	}


	public Date getFronted_use_next_hearing_date() {
		return fronted_use_next_hearing_date;
	}


	public void setFronted_use_next_hearing_date(Date fronted_use_next_hearing_date) {
		this.fronted_use_next_hearing_date = fronted_use_next_hearing_date;
	}


	public Date getFronted_use_last_hearing_date() {
		return fronted_use_last_hearing_date;
	}


	public void setFronted_use_last_hearing_date(Date fronted_use_last_hearing_date) {
		this.fronted_use_last_hearing_date = fronted_use_last_hearing_date;
	}
	
	
	
}
