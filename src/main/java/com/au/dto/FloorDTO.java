package com.au.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

public class FloorDTO {
	
	private List<Integer> total_no_of_floor;
	private Integer basement;
	private Integer floor_id;
	private String floor_name;
	private String floor_no;
	private Integer block_id;
	private Integer institute_id;
	private String floorcode;
	private Boolean active;
	
	@Column(name = "created_date", updatable = false)
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
	
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	
	public FloorDTO() {
		super();
	}

	public List<Integer> getTotal_no_of_floor() {
		return total_no_of_floor;
	}

	public void setTotal_no_of_floor(List<Integer> total_no_of_floor) {
		this.total_no_of_floor = total_no_of_floor;
	}

	public Integer getBasement() {
		return basement;
	}

	public void setBasement(Integer basement) {
		this.basement = basement;
	}

	public Integer getFloor_id() {
		return floor_id;
	}

	public void setFloor_id(Integer floor_id) {
		this.floor_id = floor_id;
	}

	public String getFloor_name() {
		return floor_name;
	}

	public void setFloor_name(String floor_name) {
		this.floor_name = floor_name;
	}

	public String getFloor_no() {
		return floor_no;
	}

	public void setFloor_no(String floor_no) {
		this.floor_no = floor_no;
	}

	public Integer getBlock_id() {
		return block_id;
	}

	public void setBlock_id(Integer block_id) {
		this.block_id = block_id;
	}

	public Integer getInstitute_id() {
		return institute_id;
	}

	public void setInstitute_id(Integer institute_id) {
		this.institute_id = institute_id;
	}

	public String getFloorcode() {
		return floorcode;
	}

	public void setFloorcode(String floorcode) {
		this.floorcode = floorcode;
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
