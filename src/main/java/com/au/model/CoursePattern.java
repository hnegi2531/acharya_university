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
@Table(name = "course_pattern")
public class CoursePattern {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer course_pattern_id;
	private Integer ac_year_id;
	private Integer school_id;
//	private Integer course_type_id;
	private Float percentage_of_credit;
	private Integer credits;
	private Integer program_id;
	private Boolean active;
	
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
	
	@Column(name = "created_username", updatable = false)
	private String created_username;
	private String modified_username;
	private Integer course_category_id;
	
	public CoursePattern() {
		super();
	}

	public Integer getCourse_pattern_id() {
		return course_pattern_id;
	}

	public void setCourse_pattern_id(Integer course_pattern_id) {
		this.course_pattern_id = course_pattern_id;
	}
	
	public Integer getAc_year_id() {
		return ac_year_id;
	}

	public void setAc_year_id(Integer ac_year_id) {
		this.ac_year_id = ac_year_id;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public Integer getCourse_category_id() {
		return course_category_id;
	}

	public void setCourse_category_id(Integer course_category_id) {
		this.course_category_id = course_category_id;
	}

	public Float getPercentage_of_credit() {
		return percentage_of_credit;
	}

	public void setPercentage_of_credit(Float percentage_of_credit) {
		this.percentage_of_credit = percentage_of_credit;
	}

	public Integer getCredits() {
		return credits;
	}

	public void setCredits(Integer credits) {
		this.credits = credits;
	}

	public Integer getProgram_id() {
		return program_id;
	}

	public void setProgram_id(Integer program_id) {
		this.program_id = program_id;
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
