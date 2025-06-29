package com.au.model;

import java.time.LocalDateTime;
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
@Table(name = "time_table")
public class TimeTable {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer time_table_id;
	private Integer ac_year_id;
	private Integer school_id;
	private Integer program_id;  // course_assignment_id
	private Integer program_specialization_id;// course_branch_assignment_id
	private Integer section_assignment_id;
	private Integer batch_assignment_id;
	private Integer subject_assignment_id;
	private Integer interval_type_id;
	private Integer emp_id;
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;

	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	private Boolean active;
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	
	@Column(length = 20)
	private String class_interval;
	@Column(length = 20)
	private String intervals;
	
	@Column(length = 20)
	private String week_day;

	private Integer block_id;
	private Integer floor_id;
	private Integer room_id;
	private Integer current_year;
	private Integer current_sem;
	private Integer year;
//	private Integer period_no;
	private String column_of_date_time_for_fronted_use;
	private String fromDate_for_fronted_use;
	private String toDate_for_fronted_use;
	@Temporal(TemporalType.DATE)
	private Date from_date;
	@Temporal(TemporalType.DATE)
	private Date to_date;
	private Integer time_slots_id;  //from time slots table
	@Temporal(TemporalType.DATE)
	private Date selected_date;
	private String subject_name_short;
	private Boolean is_online;
	private Boolean test_status;
	
	private Integer program_assignment_id;
	
    /**
     * 1 = for attendance done
     * 2 = partial attendance by lms
     * null = attendance is not taken
     */
    private Integer attendance_status;

	public TimeTable() {
		super();
	}

	public Integer getTime_table_id() {
		return time_table_id;
	}

	public void setTime_table_id(Integer time_table_id) {
		this.time_table_id = time_table_id;
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

	public Integer getProgram_specialization_id() {
		return program_specialization_id;
	}

	public void setProgram_specialization_id(Integer program_specialization_id) {
		this.program_specialization_id = program_specialization_id;
	}

	public String getSubject_name_short() {
		return subject_name_short;
	}

	public void setSubject_name_short(String subject_name_short) {
		this.subject_name_short = subject_name_short;
	}

	public Integer getSection_assignment_id() {
		return section_assignment_id;
	}

	public void setSection_assignment_id(Integer section_assignment_id) {
		this.section_assignment_id = section_assignment_id;
	}

	public Integer getBatch_assignment_id() {
		return batch_assignment_id;
	}

	public void setBatch_assignment_id(Integer batch_assignment_id) {
		this.batch_assignment_id = batch_assignment_id;
	}

	public Integer getSubject_assignment_id() {
		return subject_assignment_id;
	}

	public void setSubject_assignment_id(Integer subject_assignment_id) {
		this.subject_assignment_id = subject_assignment_id;
	}

	public Integer getInterval_type_id() {
		return interval_type_id;
	}

	public void setInterval_type_id(Integer interval_type_id) {
		this.interval_type_id = interval_type_id;
	}

	public Integer getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(Integer emp_id) {
		this.emp_id = emp_id;
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

	public String getClass_interval() {
		return class_interval;
	}

	public void setClass_interval(String class_interval) {
		this.class_interval = class_interval;
	}

	public String getIntervals() {
		return intervals;
	}

	public void setIntervals(String intervals) {
		this.intervals = intervals;
	}

	public String getWeek_day() {
		return week_day;
	}

	public void setWeek_day(String week_day) {
		this.week_day = week_day;
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

	public Integer getRoom_id() {
		return room_id;
	}

	public void setRoom_id(Integer room_id) {
		this.room_id = room_id;
	}

	public Integer getCurrent_year() {
		return current_year;
	}

	public void setCurrent_year(Integer current_year) {
		this.current_year = current_year;
	}

	public Integer getCurrent_sem() {
		return current_sem;
	}

	public void setCurrent_sem(Integer current_sem) {
		this.current_sem = current_sem;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getColumn_of_date_time_for_fronted_use() {
		return column_of_date_time_for_fronted_use;
	}

	public void setColumn_of_date_time_for_fronted_use(String column_of_date_time_for_fronted_use) {
		this.column_of_date_time_for_fronted_use = column_of_date_time_for_fronted_use;
	}

	public Date getFrom_date() {
		return from_date;
	}

	public void setFrom_date(Date from_date) {
		this.from_date = from_date;
	}

	public Date getTo_date() {
		return to_date;
	}

	public void setTo_date(Date to_date) {
		this.to_date = to_date;
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

	public Boolean getIs_online() {
		return is_online;
	}

	public void setIs_online(Boolean is_online) {
		this.is_online = is_online;
	}

	public String getFromDate_for_fronted_use() {
		return fromDate_for_fronted_use;
	}

	public void setFromDate_for_fronted_use(String fromDate_for_fronted_use) {
		this.fromDate_for_fronted_use = fromDate_for_fronted_use;
	}

	public String getToDate_for_fronted_use() {
		return toDate_for_fronted_use;
	}

	public void setToDate_for_fronted_use(String toDate_for_fronted_use) {
		this.toDate_for_fronted_use = toDate_for_fronted_use;
	}

	public Boolean getTest_status() {
		return test_status;
	}

	public void setTest_status(Boolean test_status) {
		this.test_status = test_status;
	}

	public Integer getProgram_assignment_id() {
		return program_assignment_id;
	}

	public void setProgram_assignment_id(Integer program_assignment_id) {
		this.program_assignment_id = program_assignment_id;
	}

	public Integer getAttendance_status() {
		return attendance_status;
	}

	public void setAttendance_status(Integer attendance_status) {
		this.attendance_status = attendance_status;
	}
	
	
	
}
