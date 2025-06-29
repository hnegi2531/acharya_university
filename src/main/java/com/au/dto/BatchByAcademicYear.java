package com.au.dto;

public class BatchByAcademicYear {

	private Integer batch_assignment_id;
	private Integer current_year;
	private Integer current_sem;
	private String course_short_name;
	private String remarks;
	private String batch_short_name;
	private String batch_name;

	public BatchByAcademicYear() {

	}

	public BatchByAcademicYear(Integer batch_assignment_id, Integer current_year, Integer current_sem,
			String course_short_name, String remarks, String batch_short_name,
			String batch_name) {
		this.batch_assignment_id = batch_assignment_id;
		this.current_year = current_year;
		this.current_sem = current_sem;
		this.course_short_name = course_short_name;
		this.remarks = remarks;
		this.batch_short_name = batch_short_name;
		this.batch_name = batch_name;
	}

	public Integer getBatch_assignment_id() {
		return batch_assignment_id;
	}

	public void setBatch_assignment_id(Integer batch_assignment_id) {
		this.batch_assignment_id = batch_assignment_id;
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

	public String getCourse_short_name() {
		return course_short_name;
	}

	public void setCourse_short_name(String course_short_name) {
		this.course_short_name = course_short_name;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getBatch_short_name() {
		return batch_short_name;
	}

	public void setBatch_short_name(String batch_short_name) {
		this.batch_short_name = batch_short_name;
	}

	public String getBatch_name() {
		return batch_name;
	}

	public void setBatch_name(String batch_name) {
		this.batch_name = batch_name;
	}
	
}
