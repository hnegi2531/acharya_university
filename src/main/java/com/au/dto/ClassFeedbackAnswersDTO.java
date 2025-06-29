package com.au.dto;

import java.util.Date;
import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

public class ClassFeedbackAnswersDTO {
	
	private Integer class_feedback_answers_id;
	private Integer class_feedback_questions_id;
	private Integer student_id;
	private Integer user_id;
	private Integer course_id;
	private Integer course_assignment_id;
	private String remarks;
	private Boolean active;
	private HashMap<Integer, Integer> ratings;
	@Column(updatable=false)
	private Integer created_by;
	
	private Integer modified_by;
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	
	@Column(updatable=false)
	private String created_username;
	private String modified_username;
	
	private Integer feedback_window_id;
	private Integer window_count;
	
	public ClassFeedbackAnswersDTO() {
		super();
	}

	public Integer getClass_feedback_answers_id() {
		return class_feedback_answers_id;
	}

	public void setClass_feedback_answers_id(Integer class_feedback_answers_id) {
		this.class_feedback_answers_id = class_feedback_answers_id;
	}

	public Integer getClass_feedback_questions_id() {
		return class_feedback_questions_id;
	}

	public void setClass_feedback_questions_id(Integer class_feedback_questions_id) {
		this.class_feedback_questions_id = class_feedback_questions_id;
	}

	public Integer getStudent_id() {
		return student_id;
	}

	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}

	public Integer getCourse_id() {
		return course_id;
	}

	public void setCourse_id(Integer course_id) {
		this.course_id = course_id;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public HashMap<Integer, Integer> getRatings() {
		return ratings;
	}

	public void setRatings(HashMap<Integer, Integer> ratings) {
		this.ratings = ratings;
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

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Integer getCourse_assignment_id() {
		return course_assignment_id;
	}

	public void setCourse_assignment_id(Integer course_assignment_id) {
		this.course_assignment_id = course_assignment_id;
	}

	public Integer getFeedback_window_id() {
		return feedback_window_id;
	}

	public void setFeedback_window_id(Integer feedback_window_id) {
		this.feedback_window_id = feedback_window_id;
	}

	public Integer getWindow_count() {
		return window_count;
	}

	public void setWindow_count(Integer window_count) {
		this.window_count = window_count;
	}
	

}
