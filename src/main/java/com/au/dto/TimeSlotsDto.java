package com.au.dto;

import java.util.Date;
import java.util.List;

public class TimeSlotsDto {
	
	
	private String starting_time;
	private String ending_time;

	private Date starting_time_for_fornted;
	private Date ending_time_for_fornted;

	private List<Integer> school_id;
	private Integer created_by;

	private Integer modified_by;
	private Boolean active;

	private String created_username;

	private String modified_username;
	
	private Boolean class_time_table;
	
	private Long duration;

	public TimeSlotsDto() {
		super();
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

	public List<Integer> getSchool_id() {
		return school_id;
	}

	public void setSchool_id(List<Integer> school_id) {
		this.school_id = school_id;
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
