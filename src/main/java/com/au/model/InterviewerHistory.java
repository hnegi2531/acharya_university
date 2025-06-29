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
@Table(name = "interviewerHistory")
public class InterviewerHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer ir_history_id;
	public Integer interviewer_id;
	public Integer emp_id;
	public String interviewer_name;
	public Integer job_id;
	public String interviewer_comments;
	public String email;
	public Integer interview_id;
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	public String hr_remarks;
	public String hr_name;
	public Integer hr_id;
	public String hr_date;

	public InterviewerHistory() {
		super();
	}

	public Integer getInterviewer_id() {
		return interviewer_id;
	}

	public void setInterviewer_id(Integer interviewer_id) {
		this.interviewer_id = interviewer_id;
	}

	public Integer getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(Integer emp_id) {
		this.emp_id = emp_id;
	}

	public String getInterviewer_name() {
		return interviewer_name;
	}

	public void setInterviewer_name(String interviewer_name) {
		this.interviewer_name = interviewer_name;
	}

	public Integer getJob_id() {
		return job_id;
	}

	public void setJob_id(Integer job_id) {
		this.job_id = job_id;
	}

	public String getInterviewer_comments() {
		return interviewer_comments;
	}

	public void setInterviewer_comments(String interviewer_comments) {
		this.interviewer_comments = interviewer_comments;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getInterview_id() {
		return interview_id;
	}

	public void setInterview_id(Integer interview_id) {
		this.interview_id = interview_id;
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

	public String getHr_remarks() {
		return hr_remarks;
	}

	public void setHr_remarks(String hr_remarks) {
		this.hr_remarks = hr_remarks;
	}

	public String getHr_name() {
		return hr_name;
	}

	public void setHr_name(String hr_name) {
		this.hr_name = hr_name;
	}

	public Integer getHr_id() {
		return hr_id;
	}

	public void setHr_id(Integer hr_id) {
		this.hr_id = hr_id;
	}

	public String getHr_date() {
		return hr_date;
	}

	public void setHr_date(String hr_date) {
		this.hr_date = hr_date;
	}


}
