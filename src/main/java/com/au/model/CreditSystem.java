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
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "credit_system")
public class CreditSystem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer credit_system_id;

	@NotBlank
	private String grade;

	@NotNull
	private Integer min_marks;

	@NotNull
	private Integer max_marks;

	@NotNull
	private Integer grade_points;

	@NotBlank
	private String performance;
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

	public CreditSystem() {
		super();
	}

	public Integer getCredit_system_id() {
		return credit_system_id;
	}

	public void setCredit_system_id(Integer credit_system_id) {
		this.credit_system_id = credit_system_id;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public Integer getMin_marks() {
		return min_marks;
	}

	public void setMin_marks(Integer min_marks) {
		this.min_marks = min_marks;
	}

	public Integer getMax_marks() {
		return max_marks;
	}

	public void setMax_marks(Integer max_marks) {
		this.max_marks = max_marks;
	}

	public Integer getGrade_points() {
		return grade_points;
	}

	public void setGrade_points(Integer grade_points) {
		this.grade_points = grade_points;
	}

	public String getPerformance() {
		return performance;
	}

	public void setPerformance(String performance) {
		this.performance = performance;
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
