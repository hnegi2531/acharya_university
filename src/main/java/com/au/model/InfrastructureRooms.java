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
@Table(name="infrastructure_rooms")
public class InfrastructureRooms {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "room_id")
	private Integer room_id;
	private String roomcode;
	private String manual_room_no;
	private Integer school_id;  //institute_id;
	private Integer block_id;
	private Integer floor_id;
	private Integer no_of_rooms;
	private String description;
	private Boolean show_in_event;
	private Integer strength;
	private Integer facility_type_id;
	private String area;
	private String remarks;
	private Boolean active;
	private Boolean room_status;
	
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
	
	public InfrastructureRooms() {
		super();
	}

	public Integer getRoom_id() {
		return room_id;
	}

	public void setRoom_id(Integer room_id) {
		this.room_id = room_id;
	}

	public String getRoomcode() {
		return roomcode;
	}

	public void setRoomcode(String roomcode) {
		this.roomcode = roomcode;
	}

	public String getManual_room_no() {
		return manual_room_no;
	}

	public void setManual_room_no(String manual_room_no) {
		this.manual_room_no = manual_room_no;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public Integer getBlock_id() {
		return block_id;
	}

	public void setBlock_id(Integer block_id) {
		this.block_id = block_id;
	}

	public Integer getFloor_id() {
		return floor_id;
	}

	public void setFloor_id(Integer floor_id) {
		this.floor_id = floor_id;
	}

	public Integer getNo_of_rooms() {
		return no_of_rooms;
	}

	public void setNo_of_rooms(Integer no_of_rooms) {
		this.no_of_rooms = no_of_rooms;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getShow_in_event() {
		return show_in_event;
	}

	public void setShow_in_event(Boolean show_in_event) {
		this.show_in_event = show_in_event;
	}

	public Integer getStrength() {
		return strength;
	}

	public void setStrength(Integer strength) {
		this.strength = strength;
	}

	public Integer getFacility_type_id() {
		return facility_type_id;
	}

	public void setFacility_type_id(Integer facility_type_id) {
		this.facility_type_id = facility_type_id;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public Boolean getRoom_status() {
		return room_status;
	}

	public void setRoom_status(Boolean room_status) {
		this.room_status = room_status;
	}
	

}
