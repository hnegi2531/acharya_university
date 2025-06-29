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
@Table(name = "class_feedback_questions")
public class ClassFeedbackQuestions {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer class_feedback_questions_id;
	private String question;
	private Integer school_id;
	private Boolean active;
	
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
	
	public ClassFeedbackQuestions() {
		super();
	}
	
	public Integer getClass_feedback_questions_id() {
		return class_feedback_questions_id;
	}
	public void setClass_feedback_questions_id(Integer class_feedback_questions_id) {
		this.class_feedback_questions_id = class_feedback_questions_id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public Integer getSchool_id() {
		return school_id;
	}
	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
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
	
	

}
