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
@Table(name ="time_slots")
public class TimeSlots {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer time_slots_id;
	
	@NotBlank(message = "Start time should not be Empty OR Null")
	private String starting_time;
	
	@NotBlank(message = "End should not be Empty OR Null")
	private String ending_time;
	
	private Date starting_time_for_fornted;
	private Date ending_time_for_fornted;
	
	private Integer school_id;
	
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
	
	
	private Boolean class_time_table;
	private Long duration;

	
	public TimeSlots() {
		super();
	}

	public Integer getTime_slots_id() {
		return time_slots_id;
	}

	public void setTime_slots_id(Integer time_slots_id) {
		this.time_slots_id = time_slots_id;
	}

	public String getStarting_time() {
		return starting_time;
	}

	public void setStarting_time(String starting_time) {
		this.starting_time = starting_time;
	}
	

	public String getEnding_time() {
		return ending_time;
	}

	public void setEnding_time(String ending_time) {
		this.ending_time = ending_time;
	}

	public Date getStarting_time_for_fornted() {
		return starting_time_for_fornted;
	}

	public void setStarting_time_for_fornted(Date starting_time_for_fornted) {
		this.starting_time_for_fornted = starting_time_for_fornted;
	}

	public Date getEnding_time_for_fornted() {
		return ending_time_for_fornted;
	}

	public void setEnding_time_for_fornted(Date ending_time_for_fornted) {
		this.ending_time_for_fornted = ending_time_for_fornted;
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

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public Boolean getClass_time_table() {
		return class_time_table;
	}

	public void setClass_time_table(Boolean class_time_table) {
		this.class_time_table = class_time_table;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}
	
	
	
}
