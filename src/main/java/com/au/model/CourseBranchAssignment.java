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
@Table(name = "course_branch_assignment")
public class CourseBranchAssignment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer	course_branch_assignment_id;

	private Integer course_branch_id;//fk
	private Integer course_id;//fk
	private Integer dept_id;//fk
	private Integer school_id;//fk
	private String auid_format;
	
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)	
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)	
	@UpdateTimestamp
	private Date modified_date;
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;
	private boolean active;
	
	public CourseBranchAssignment() {
		super();
	}

	public CourseBranchAssignment(Integer course_branch_assignment_id, Integer course_branch_id, Integer course_id,
			Integer dept_id, Integer school_id, String auid_format, Date created_date, Date modified_date,
			Integer created_by, Integer modified_by, boolean active) {
		super();
		this.course_branch_assignment_id = course_branch_assignment_id;
		this.course_branch_id = course_branch_id;
		this.course_id = course_id;
		this.dept_id = dept_id;
		this.school_id = school_id;
		this.auid_format = auid_format;
		this.created_date = created_date;
		this.modified_date = modified_date;
		this.created_by = created_by;
		this.modified_by = modified_by;
		this.active = active;
	}

	public Integer getCourse_branch_assignment_id() {
		return course_branch_assignment_id;
	}

	public void setCourse_branch_assignment_id(Integer course_branch_assignment_id) {
		this.course_branch_assignment_id = course_branch_assignment_id;
	}

	public Integer getCourse_branch_id() {
		return course_branch_id;
	}

	public void setCourse_branch_id(Integer course_branch_id) {
		this.course_branch_id = course_branch_id;
	}

	public Integer getCourse_id() {
		return course_id;
	}

	public void setCourse_id(Integer course_id) {
		this.course_id = course_id;
	}

	public Integer getDept_id() {
		return dept_id;
	}

	public void setDept_id(Integer dept_id) {
		this.dept_id = dept_id;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public String getAuid_format() {
		return auid_format;
	}

	public void setAuid_format(String auid_format) {
		this.auid_format = auid_format;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	
}
