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
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "program_master")
public class ProgramMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer master_id;

	@NotNull
	private Integer max_duration_in_years;

	@NotNull
	private Integer credits_completion_first_year;

	@NotNull
	private Integer credits_completion_second_year;

	@NotNull
	private Integer credits_completion_third_year;

	@NotNull
	private Integer credits_completion_fourth_year;

	@NotNull
	private Integer total_min_credits;

	@NotNull
	private Integer total_max_credits;

	@NotNull
	private Integer program_id; // FK

	@NotNull
	private Integer school_id; // FK

	@NotNull
	private Integer major_id; // FK

	@NotNull
	private Integer dept_id; // FK

	@NotNull
	private Integer syllabus_id;// FK

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
	private String created_username;
	private String modified_username;

	public ProgramMaster() {
		super();
	}

	public Integer getMaster_id() {
		return master_id;
	}

	public void setMaster_id(Integer master_id) {
		this.master_id = master_id;
	}

	public Integer getMax_duration_in_years() {
		return max_duration_in_years;
	}

	public void setMax_duration_in_years(Integer max_duration_in_years) {
		this.max_duration_in_years = max_duration_in_years;
	}

	public Integer getCredits_completion_first_year() {
		return credits_completion_first_year;
	}

	public void setCredits_completion_first_year(Integer credits_completion_first_year) {
		this.credits_completion_first_year = credits_completion_first_year;
	}

	public Integer getCredits_completion_second_year() {
		return credits_completion_second_year;
	}

	public void setCredits_completion_second_year(Integer credits_completion_second_year) {
		this.credits_completion_second_year = credits_completion_second_year;
	}

	public Integer getCredits_completion_third_year() {
		return credits_completion_third_year;
	}

	public void setCredits_completion_third_year(Integer credits_completion_third_year) {
		this.credits_completion_third_year = credits_completion_third_year;
	}

	public Integer getCredits_completion_fourth_year() {
		return credits_completion_fourth_year;
	}

	public void setCredits_completion_fourth_year(Integer credits_completion_fourth_year) {
		this.credits_completion_fourth_year = credits_completion_fourth_year;
	}

	public Integer getTotal_min_credits() {
		return total_min_credits;
	}

	public void setTotal_min_credits(Integer total_min_credits) {
		this.total_min_credits = total_min_credits;
	}

	public Integer getTotal_max_credits() {
		return total_max_credits;
	}

	public void setTotal_max_credits(Integer total_max_credits) {
		this.total_max_credits = total_max_credits;
	}

	public Integer getProgram_id() {
		return program_id;
	}

	public void setProgram_id(Integer program_id) {
		this.program_id = program_id;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public Integer getMajor_id() {
		return major_id;
	}

	public void setMajor_id(Integer major_id) {
		this.major_id = major_id;
	}

	public Integer getDept_id() {
		return dept_id;
	}

	public void setDept_id(Integer dept_id) {
		this.dept_id = dept_id;
	}

	public Integer getSyllabus_id() {
		return syllabus_id;
	}

	public void setSyllabus_id(Integer syllabus_id) {
		this.syllabus_id = syllabus_id;
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
