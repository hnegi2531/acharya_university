package com.au.dto;

import java.util.List;
import com.au.model.Interview;

public class InterviewRequest {
	
	Interview interview;
	List<String> emails;
	
	public Integer job_id;
	public String interviewer_comments;
	public List<String> email;
	public String hr_remarks;
	public Boolean mail_sent;
	private Boolean ictStatus;
	
	public InterviewRequest() {
		super();
	}

	public Interview getInterview() {
		return interview;
	}

	public void setInterview(Interview interview) {
		this.interview = interview;
	}

	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
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

	public List<String> getEmail() {
		return email;
	}

	public void setEmail(List<String> email) {
		this.email = email;
	}

	public String getHr_remarks() {
		return hr_remarks;
	}

	public void setHr_remarks(String hr_remarks) {
		this.hr_remarks = hr_remarks;
	}

	public Boolean getMail_sent() {
		return mail_sent;
	}

	public void setMail_sent(Boolean mail_sent) {
		this.mail_sent = mail_sent;
	}

	public Boolean getIctStatus() {
		return ictStatus;
	}

	public void setIctStatus(Boolean ictStatus) {
		this.ictStatus = ictStatus;
	}

	
	
}
