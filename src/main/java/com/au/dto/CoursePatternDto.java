package com.au.dto;


import java.util.List;


public class CoursePatternDto {

	private Integer ac_year_id;
	private Integer school_id;
	private Float percentage_of_credit;
	private Integer credits;
	private List<Integer> program_id;
	private Boolean active;

	private Integer created_by;
	private Integer modified_by;
	
	private String created_username;
	private String modified_username;
	private Integer course_category_id;
	
	public CoursePatternDto() {
		super();
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

	public List<Integer> getProgram_id() {
		return program_id;
	}

	public void setProgram_id(List<Integer> program_id) {
		this.program_id = program_id;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
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

	public Integer getCourse_category_id() {
		return course_category_id;
	}

	public void setCourse_category_id(Integer course_category_id) {
		this.course_category_id = course_category_id;
	}
	
}
