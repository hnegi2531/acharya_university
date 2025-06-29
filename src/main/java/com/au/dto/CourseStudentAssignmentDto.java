package com.au.dto;


import java.util.List;


public class CourseStudentAssignmentDto {
	
	private Integer course_id;
	private List<Integer> student_id;
	private Boolean active;
	
	private Integer current_year_sem ;
	private Integer course_assignment_id;
	private List<Integer> course_ids;
	private List<Integer> course_assignment_ids;
	private Integer stud_id;
	
	public CourseStudentAssignmentDto() {
		super();
	}

	public Integer getCourse_id() {
		return course_id;
	}

	public void setCourse_id(Integer course_id) {
		this.course_id = course_id;
	}

	public List<Integer> getStudent_id() {
		return student_id;
	}

	public void setStudent_id(List<Integer> student_id) {
		this.student_id = student_id;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Integer getCurrent_year_sem() {
		return current_year_sem;
	}

	public void setCurrent_year_sem(Integer current_year_sem) {
		this.current_year_sem = current_year_sem;
	}

	public Integer getCourse_assignment_id() {
		return course_assignment_id;
	}

	public void setCourse_assignment_id(Integer course_assignment_id) {
		this.course_assignment_id = course_assignment_id;
	}

	public List<Integer> getCourse_ids() {
		return course_ids;
	}

	public void setCourse_ids(List<Integer> course_ids) {
		this.course_ids = course_ids;
	}

	public List<Integer> getCourse_assignment_ids() {
		return course_assignment_ids;
	}

	public void setCourse_assignment_ids(List<Integer> course_assignment_ids) {
		this.course_assignment_ids = course_assignment_ids;
	}

	public Integer getStud_id() {
		return stud_id;
	}

	public void setStud_id(Integer stud_id) {
		this.stud_id = stud_id;
	}

	
	
	
	
}
