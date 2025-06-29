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
import javax.validation.Valid;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "holiday_calender_history")
public class HolidayCalenderHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer hch_id;
	private Integer holidayCalendarId;
	//private Integer holidayTypeId;
	// @JsonFormat(pattern = "dd-MM-yyyy")
	// @JsonFormat(pattern = "yyyy-MM-dd")
	// @Column(unique = true)
	private Date fromDate;
	private Integer daysCount;

	// @Column(unique = true)
	private String holidayName;
	private String jobTypeId;
	private Integer schoolId;
	//private String holidayTypeShort;

	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdDate;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modifiedDate;

	@Column(updatable = false)
	private Integer createdBy;
	private Integer modifiedBy;

	@Column(updatable = false)
	private String createdUsername;
	private String modifiedUsername;
	private Boolean active;
	private Integer leave_id;
	private String leave_type_short;
	private String leave_type;
//	private Integer dept_id;

	public HolidayCalenderHistory() {
		super();
	}

	public Integer getHch_id() {
		return hch_id;
	}

	public void setHch_id(Integer hch_id) {
		this.hch_id = hch_id;
	}

	public Integer getHolidayCalendarId() {
		return holidayCalendarId;
	}

	public void setHolidayCalendarId(Integer holidayCalendarId) {
		this.holidayCalendarId = holidayCalendarId;
	}

//	public Integer getHolidayTypeId() {
//		return holidayTypeId;
//	}
//
//	public void setHolidayTypeId(Integer holidayTypeId) {
//		this.holidayTypeId = holidayTypeId;
//	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Integer getDaysCount() {
		return daysCount;
	}

	public void setDaysCount(Integer daysCount) {
		this.daysCount = daysCount;
	}

	public String getHolidayName() {
		return holidayName;
	}

	public void setHolidayName(String holidayName) {
		this.holidayName = holidayName;
	}

	public String getJobTypeId() {
		return jobTypeId;
	}

	public void setJobTypeId(String jobTypeId) {
		this.jobTypeId = jobTypeId;
	}

	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}

//	public String getHolidayTypeShort() {
//		return holidayTypeShort;
//	}
//
//	public void setHolidayTypeShort(String holidayTypeShort) {
//		this.holidayTypeShort = holidayTypeShort;
//	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getCreatedUsername() {
		return createdUsername;
	}

	public void setCreatedUsername(String createdUsername) {
		this.createdUsername = createdUsername;
	}

	public String getModifiedUsername() {
		return modifiedUsername;
	}

	public void setModifiedUsername(String modifiedUsername) {
		this.modifiedUsername = modifiedUsername;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Integer getLeave_id() {
		return leave_id;
	}

	public void setLeave_id(Integer leave_id) {
		this.leave_id = leave_id;
	}

	public String getLeave_type_short() {
		return leave_type_short;
	}

	public void setLeave_type_short(String leave_type_short) {
		this.leave_type_short = leave_type_short;
	}

	public String getLeave_type() {
		return leave_type;
	}

	public void setLeave_type(String leave_type) {
		this.leave_type = leave_type;
	}

//	public Integer getDept_id() {
//		return dept_id;
//	}
//
//	public void setDept_id(Integer dept_id) {
//		this.dept_id = dept_id;
//	}

}
