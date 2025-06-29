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
@Table(name = "internal_timetable_assignment")
public class InternalTimeTableAssignment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer internal_room_id;
	private String student_ids;
	private Integer emp_ids;
	private Integer room_id;
	private Integer internal_time_table_id;
	private Integer internal_id;
	private String remarks;
	private String week_day;
	
	private Integer time_slots_id;
	@Temporal(TemporalType.DATE)
	private Date selected_date;
	

	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;

	@Column(name = "created_by", updatable = false)
	private Integer created_by;
	private Integer modified_by;
	private Boolean active;
	@Column(name = "created_username", updatable = false)
	private String created_username;
	private String modified_username;
	private Boolean attendance_status;

	public InternalTimeTableAssignment() {
		super();
	}

	public Integer getInternal_room_id() {
		return internal_room_id;
	}

	public void setInternal_room_id(Integer internal_room_id) {
		this.internal_room_id = internal_room_id;
	}

	public String getStudent_ids() {
		return student_ids;
	}

	public void setStudent_ids(String student_ids) {
		this.student_ids = student_ids;
	}

	public Integer getEmp_ids() {
		return emp_ids;
	}

	public void setEmp_ids(Integer emp_ids) {
		this.emp_ids = emp_ids;
	}

	public Integer getRoom_id() {
		return room_id;
	}

	public void setRoom_id(Integer room_id) {
		this.room_id = room_id;
	}

	public Integer getInternal_time_table_id() {
		return internal_time_table_id;
	}

	public void setInternal_time_table_id(Integer internal_time_table_id) {
		this.internal_time_table_id = internal_time_table_id;
	}

	public Integer getInternal_id() {
		return internal_id;
	}

	public void setInternal_id(Integer internal_id) {
		this.internal_id = internal_id;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getWeek_day() {
		return week_day;
	}

	public void setWeek_day(String week_day) {
		this.week_day = week_day;
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

	public Integer getTime_slots_id() {
		return time_slots_id;
	}

	public void setTime_slots_id(Integer time_slots_id) {
		this.time_slots_id = time_slots_id;
	}

	public Date getSelected_date() {
		return selected_date;
	}

	public void setSelected_date(Date selected_date) {
		this.selected_date = selected_date;
	}

	public Boolean getAttendance_status() {
		return attendance_status;
	}

	public void setAttendance_status(Boolean attendance_status) {
		this.attendance_status = attendance_status;
	}
	
	
	
}
