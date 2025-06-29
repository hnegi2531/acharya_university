package com.au.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "interview")
public class Interview {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer interview_id;
//  private String interview_name;
	private String subject;
//	private String position;
	private Integer designation_id;
	@Lob
	private String body;
	private String comments;
	private String interview_date;
	private Integer job_id;
	private Boolean approve;
//  private Integer template_id;
//  private Integer template_type_id;
//  private Integer interview_count;
	
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
	private Boolean schedule;
	// private String hr_remarks;
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	private String frontend_use_datetime;
	private Boolean ictStatus;

	public Interview() {
		super();
	}

	public Integer getInterview_id() {
		return interview_id;
	}

	public void setInterview_id(Integer interview_id) {
		this.interview_id = interview_id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Integer getDesignation_id() {
		return designation_id;
	}

	public void setDesignation_id(Integer designation_id) {
		this.designation_id = designation_id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getInterview_date() {
		return interview_date;
	}

	public void setInterview_date(String interview_date) {
		this.interview_date = interview_date;
	}

	public Integer getJob_id() {
		return job_id;
	}

	public void setJob_id(Integer job_id) {
		this.job_id = job_id;
	}

	public Boolean getApprove() {
		return approve;
	}

	public void setApprove(Boolean approve) {
		this.approve = approve;
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

	public Boolean getSchedule() {
		return schedule;
	}

	public void setSchedule(Boolean schedule) {
		this.schedule = schedule;
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

	public String getFrontend_use_datetime() {
		return frontend_use_datetime;
	}

	public void setFrontend_use_datetime(String frontend_use_datetime) {
		this.frontend_use_datetime = frontend_use_datetime;
	}

	public Boolean getIctStatus() {
		return ictStatus;
	}

	public void setIctStatus(Boolean ictStatus) {
		this.ictStatus = ictStatus;
	}
	
	
	
}
