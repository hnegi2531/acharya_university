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
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "course_type")
public class CourseType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer course_type_id;
	@Column(unique = true)
	@NotBlank(message = "course_type_name should not be Empty OR Null")
	private String course_type_name;
	@Column(unique = true)
	@NotBlank(message = "course_type_code should not be Empty OR Null")
	private String course_type_code;

	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;
	private Boolean active;
	@Column(updatable = false)
	private String created_username;
	private String modified_username;

	public CourseType() {
		super();
	}

	public Integer getCourse_type_id() {
		return course_type_id;
	}

	public void setCourse_type_id(Integer course_type_id) {
		this.course_type_id = course_type_id;
	}

	public String getCourse_type_name() {
		return course_type_name;
	}

	public void setCourse_type_name(String course_type_name) {
		this.course_type_name = course_type_name;
	}

	public String getCourse_type_code() {
		return course_type_code;
	}

	public void setCourse_type_code(String course_type_code) {
		this.course_type_code = course_type_code;
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
