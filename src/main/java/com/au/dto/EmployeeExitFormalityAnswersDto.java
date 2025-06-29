package com.au.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

public class EmployeeExitFormalityAnswersDto {

	
	private Integer eefaid;
	private Integer emp_id;
	
	private List<Integer> eefqid;
	
	private String answers;
	
	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	
	@Column(name = "created_by", updatable = false)
	private Integer created_by;
	
	private Integer modified_by;
	private Boolean active;
	
	@Column(name = "created_username", updatable = false)
	private String created_username;
	private String modified_username;
	
	
	public Integer getEefaid() {
		return eefaid;
	}
	
	public void setEefaid(Integer eefaid) {
		this.eefaid = eefaid;
	}
	
	
	public Integer getEmp_id() {
		return emp_id;
	}
	
	public void setEmp_id(Integer emp_id) {
		this.emp_id = emp_id;
	}
	
	
	
	public List<Integer> getEefqid() {
		return eefqid;
	}
	
	public void setEefqid(List<Integer> eefqid) {
		this.eefqid = eefqid;
	}
	
	
	public String getAnswers() {
		return answers;
	}
	
	public void setAnswers(String answers) {
		this.answers = answers;
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
	
	
}
