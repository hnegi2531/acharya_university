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
@Table(name = "course_category")
public class CourseCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int course_category_id;

	@Column(unique = true)
	@NotBlank(message = "course category name should not be Empty OR Null")
	private String course_category_name;
	
	@Column(unique = true)
	@NotBlank(message = "course category code should not be Empty OR Null")
	private String course_category_code;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
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
	private String  type;

	public CourseCategory() {
		super();
	}

	public int getCourse_category_id() {
		return course_category_id;
	}

	public void setCourse_category_id(int course_category_id) {
		this.course_category_id = course_category_id;
	}

	public String getCourse_category_name() {
		return course_category_name;
	}

	public void setCourse_category_name(String course_category_name) {
		this.course_category_name = course_category_name;
	}

	public String getCourse_category_code() {
		return course_category_code;
	}

	public void setCourse_category_code(String course_category_code) {
		this.course_category_code = course_category_code;
	}

	public Date getModified_date() {
		return modified_date;
	}

	public void setModified_date(Date modified_date) {
		this.modified_date = modified_date;
	}

	public Date getCreated_date() {
		return created_date;
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

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
