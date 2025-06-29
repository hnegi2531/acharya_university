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
@Table(name = "scholarship")
public class Scholarship {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer scholarship_id;

	private Integer student_id;
	private Float parent_income;
	private String residence;
	private String exemption_received;
	private String exemption_type;
	private String award;
	private String award_details;
	private String reason;
	private Integer requested_scholarship;
	private Integer candidate_id;

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
	private String occupation;
	private Integer pre_admission_id;
	private String adjStatus;

	public Scholarship() {
		super();
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public Integer getScholarship_id() {
		return scholarship_id;
	}

	public void setScholarship_id(Integer scholarship_id) {
		this.scholarship_id = scholarship_id;
	}

	public Integer getStudent_id() {
		return student_id;
	}

	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}

	public Float getParent_income() {
		return parent_income;
	}

	public void setParent_income(Float parent_income) {
		this.parent_income = parent_income;
	}

	public String getResidence() {
		return residence;
	}

	public void setResidence(String residence) {
		this.residence = residence;
	}

	public String getExemption_received() {
		return exemption_received;
	}

	public void setExemption_received(String exemption_received) {
		this.exemption_received = exemption_received;
	}

	public String getExemption_type() {
		return exemption_type;
	}

	public void setExemption_type(String exemption_type) {
		this.exemption_type = exemption_type;
	}

	public String getAward() {
		return award;
	}

	public void setAward(String award) {
		this.award = award;
	}

	public String getAward_details() {
		return award_details;
	}

	public void setAward_details(String award_details) {
		this.award_details = award_details;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Integer getRequested_scholarship() {
		return requested_scholarship;
	}

	public void setRequested_scholarship(Integer requested_scholarship) {
		this.requested_scholarship = requested_scholarship;
	}

	public Integer getCandidate_id() {
		return candidate_id;
	}

	public void setCandidate_id(Integer candidate_id) {
		this.candidate_id = candidate_id;
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

	public Integer getPre_admission_id() {
		return pre_admission_id;
	}

	public void setPre_admission_id(Integer pre_admission_id) {
		this.pre_admission_id = pre_admission_id;
	}

	public String getAdjStatus() {
		return adjStatus;
	}

	public void setAdjStatus(String adjStatus) {
		this.adjStatus = adjStatus;
	}

}
