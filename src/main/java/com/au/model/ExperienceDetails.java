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
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "experience_details")
public class ExperienceDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer exp_id;
	private Integer job_id;
	private String employer_name;
	private String designation;
	private String skills;
	
//  private String job_profile
//  private String skills;
//	@Temporal(TemporalType.DATE)
//	@JsonFormat(pattern = "dd-MM-yyyy")
//	private Date exp_doj;
//	@Temporal(TemporalType.DATE)
//	@JsonFormat(pattern = "dd-MM-yyyy")
//	private Date exp_dol;
	
	private Integer exp_in_years;
	private Integer exp_in_months;
	private Integer annual_salary_lakhs; // last_ctc
	
//	@CreationTimestamp
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(updatable = false)
//	private Date created_date;
//	@UpdateTimestamp
//	@Temporal(TemporalType.TIMESTAMP)
//	private Date modified_date; 
//	private Boolean active;
	
	
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date exp_doj;
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date exp_dol;

	public ExperienceDetails() {
		super();
	}

	public Integer getExp_id() {
		return exp_id;
	}

	public void setExp_id(Integer exp_id) {
		this.exp_id = exp_id;
	}

	public Integer getJob_id() {
		return job_id;
	}

	public void setJob_id(Integer job_id) {
		this.job_id = job_id;
	}

	public String getEmployer_name() {
		return employer_name;
	}

	public void setEmployer_name(String employer_name) {
		this.employer_name = employer_name;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public Integer getExp_in_years() {
		return exp_in_years;
	}

	public void setExp_in_years(Integer exp_in_years) {
		this.exp_in_years = exp_in_years;
	}

	public Integer getExp_in_months() {
		return exp_in_months;
	}

	public void setExp_in_months(Integer exp_in_months) {
		this.exp_in_months = exp_in_months;
	}

	public Integer getAnnual_salary_lakhs() {
		return annual_salary_lakhs;
	}

	public void setAnnual_salary_lakhs(Integer annual_salary_lakhs) {
		this.annual_salary_lakhs = annual_salary_lakhs;
	}
//
//	public Date getCreated_date() {
//		return created_date;
//	}
//
//	public void setCreated_date(Date created_date) {
//		this.created_date = created_date;
//	}
//
//	public Date getModified_date() {
//		return modified_date;
//	}
//
//	public void setModified_date(Date modified_date) {
//		this.modified_date = modified_date;
//	}
//
//	public Boolean getActive() {
//		return active;
//	}
//
//	public void setActive(Boolean active) {
//		this.active = active;
//	}
}
