package com.au.dto;

public class SectionByAcademicYear {

	private Integer section_assignment_id;
	private Integer section_id;
	private Integer course_assignment_id;
	private Integer course_branch_assignment_id;
	private String course_branch_short_name;
	private String course_short_name;
	private Integer ac_year_id;
	private String section_short_name;
	private Integer current_year;
	private Integer current_sem;
	
	public SectionByAcademicYear() {
		
	}

	public SectionByAcademicYear(Integer section_assignment_id, Integer section_id, Integer course_assignment_id,
			Integer course_branch_assignment_id, String course_branch_short_name, String course_short_name,
			Integer ac_year_id, String section_short_name, Integer current_year, Integer current_sem) {
		
		this.section_assignment_id = section_assignment_id;
		this.section_id = section_id;
		this.course_assignment_id = course_assignment_id;
		this.course_branch_assignment_id = course_branch_assignment_id;
		this.course_branch_short_name = course_branch_short_name;
		this.course_short_name = course_short_name;
		this.ac_year_id = ac_year_id;
		this.section_short_name = section_short_name;
		this.current_year = current_year;
		this.current_sem = current_sem;
	}

	public Integer getSection_assignment_id() {
		return section_assignment_id;
	}

	public void setSection_assignment_id(Integer section_assignment_id) {
		this.section_assignment_id = section_assignment_id;
	}

	public Integer getSection_id() {
		return section_id;
	}

	public void setSection_id(Integer section_id) {
		this.section_id = section_id;
	}

	public Integer getCourse_assignment_id() {
		return course_assignment_id;
	}

	public void setCourse_assignment_id(Integer course_assignment_id) {
		this.course_assignment_id = course_assignment_id;
	}

	public Integer getCourse_branch_assignment_id() {
		return course_branch_assignment_id;
	}

	public void setCourse_branch_assignment_id(Integer course_branch_assignment_id) {
		this.course_branch_assignment_id = course_branch_assignment_id;
	}

	public String getCourse_branch_short_name() {
		return course_branch_short_name;
	}

	public void setCourse_branch_short_name(String course_branch_short_name) {
		this.course_branch_short_name = course_branch_short_name;
	}

	public String getCourse_short_name() {
		return course_short_name;
	}

	public void setCourse_short_name(String course_short_name) {
		this.course_short_name = course_short_name;
	}

	public Integer getAc_year_id() {
		return ac_year_id;
	}

	public void setAc_year_id(Integer ac_year_id) {
		this.ac_year_id = ac_year_id;
	}

	public String getSection_short_name() {
		return section_short_name;
	}

	public void setSection_short_name(String section_short_name) {
		this.section_short_name = section_short_name;
	}

	public Integer getCurrent_year() {
		return current_year;
	}

	public void setCurrent_year(Integer current_year) {
		this.current_year = current_year;
	}

	public Integer getCurrent_sem() {
		return current_sem;
	}

	public void setCurrent_sem(Integer current_sem) {
		this.current_sem = current_sem;
	}
	
}
