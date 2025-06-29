package com.au.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

public class HolidayCalenderRequest {

	
	//private Integer holidayTypeId;


	private Date fromDate;
	private Integer daysCount;

	@Column(unique = true)
	private String holidayName;
	private String  jobTypeId;
	private List<Integer> schoolId;
	//private String holidayTypeShort;

	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdDate;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modifiedDate;

	private Integer createdBy;
	private Integer modifiedBy;
	private String createdUsername;
	private String modifiedUsername;
	private Boolean active;
	private Integer leave_id;
	private String leave_type_short;
	private List<Integer> dept_id;
	private String day;
	@Lob
	private String holiday_description;
	
	public HolidayCalenderRequest() {
		super();
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

	public List<Integer> getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(List<Integer> schoolId) {
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

	public String getJobTypeId() {
		return jobTypeId;
	}

	public void setJobTypeId(String jobTypeId) {
		this.jobTypeId = jobTypeId;
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

	public List<Integer> getDept_id() {
		return dept_id;
	}

	public void setDept_id(List<Integer> dept_id) {
		this.dept_id = dept_id;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getHoliday_description() {
		return holiday_description;
	}

	public void setHoliday_description(String holiday_description) {
		this.holiday_description = holiday_description;
	}

	
}
