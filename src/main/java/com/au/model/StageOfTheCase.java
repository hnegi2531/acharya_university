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
@Table(name = "stage_of_the_case")
public class StageOfTheCase {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer stage_of_the_case_id;
	@Column(unique = true)
	@NotBlank(message = "Stage Of The Case Name Should Not Be Empty OR Null")
	private String stage_of_the_case_name;
	@Column(unique = true)
	@NotBlank(message = "Stage Of The Case Short Name Should Not Be Empty OR Null")
	private String stage_of_the_case_short_name;
	private Boolean active;
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
	
	@Column(updatable = false)
	private String created_username;
	
	private String modified_username;
	
	public StageOfTheCase() {
		super();
	}

	public Integer getStage_of_the_case_id() {
		return stage_of_the_case_id;
	}

	public void setStage_of_the_case_id(Integer stage_of_the_case_id) {
		this.stage_of_the_case_id = stage_of_the_case_id;
	}

	public String getStage_of_the_case_name() {
		return stage_of_the_case_name;
	}

	public void setStage_of_the_case_name(String stage_of_the_case_name) {
		this.stage_of_the_case_name = stage_of_the_case_name;
	}

	public String getStage_of_the_case_short_name() {
		return stage_of_the_case_short_name;
	}

	public void setStage_of_the_case_short_name(String stage_of_the_case_short_name) {
		this.stage_of_the_case_short_name = stage_of_the_case_short_name;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
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
