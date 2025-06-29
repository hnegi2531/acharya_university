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
@Table(name = "course_price")
public class CoursePrice {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer course_price_id;
	private Integer course_assignment_id;           // Fk
	private Integer ac_year_id;                    // Fk
	private Integer school_id;                    // Fk
	private Integer program_id;                  // Fk
	private Integer dept_id;                    // FK
	private Integer program_specialization_id; // Fk
	private Integer course_id;                // Fk
	private Integer course_category_id;      // FK
	private String year_sem;
	private String course_mode;
	private Float course_price;
	private Float course_price_usd;
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
	
	public CoursePrice() {
		super();
	}

	public Integer getCourse_price_id() {
		return course_price_id;
	}

	public void setCourse_price_id(Integer course_price_id) {
		this.course_price_id = course_price_id;
	}

	public Integer getCourse_assignment_id() {
		return course_assignment_id;
	}

	public void setCourse_assignment_id(Integer course_assignment_id) {
		this.course_assignment_id = course_assignment_id;
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

	public Integer getProgram_id() {
		return program_id;
	}

	public void setProgram_id(Integer program_id) {
		this.program_id = program_id;
	}

	public Integer getDept_id() {
		return dept_id;
	}

	public void setDept_id(Integer dept_id) {
		this.dept_id = dept_id;
	}

	public Integer getProgram_specialization_id() {
		return program_specialization_id;
	}

	public void setProgram_specialization_id(Integer program_specialization_id) {
		this.program_specialization_id = program_specialization_id;
	}

	public Integer getCourse_id() {
		return course_id;
	}

	public void setCourse_id(Integer course_id) {
		this.course_id = course_id;
	}

	public Integer getCourse_category_id() {
		return course_category_id;
	}

	public void setCourse_category_id(Integer course_category_id) {
		this.course_category_id = course_category_id;
	}

	public String getYear_sem() {
		return year_sem;
	}

	public void setYear_sem(String year_sem) {
		this.year_sem = year_sem;
	}

	public String getCourse_mode() {
		return course_mode;
	}

	public void setCourse_mode(String course_mode) {
		this.course_mode = course_mode;
	}

	public Float getCourse_price() {
		return course_price;
	}

	public void setCourse_price(Float course_price) {
		this.course_price = course_price;
	}

	public Float getCourse_price_usd() {
		return course_price_usd;
	}

	public void setCourse_price_usd(Float course_price_usd) {
		this.course_price_usd = course_price_usd;
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
