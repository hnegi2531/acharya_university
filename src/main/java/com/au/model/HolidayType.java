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
@Table(name = "holiday_type")
public class HolidayType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer holidayTypeId;
	@Column(unique = true)
	@NotBlank(message = "Holiday Type should not be Empty OR Null")
	private String holidayType;

	@Column(unique = true)
	@NotBlank(message = "Holiday Type Short name should not be Empty OR Null")
	private String holidayTypeShort;
	private Boolean holiday;
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

	public HolidayType() {
		super();
	}

	public Integer getHolidayTypeId() {
		return holidayTypeId;
	}

	public void setHolidayTypeId(Integer holidayTypeId) {
		this.holidayTypeId = holidayTypeId;
	}

	public String getHolidayType() {
		return holidayType;
	}

	public void setHolidayType(String holidayType) {
		this.holidayType = holidayType;
	}

	public String getHolidayTypeShort() {
		return holidayTypeShort;
	}

	public void setHolidayTypeShort(String holidayTypeShort) {
		this.holidayTypeShort = holidayTypeShort;
	}

	public Boolean getHoliday() {
		return holiday;
	}

	public void setHoliday(Boolean holiday) {
		this.holiday = holiday;
	}

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

	@Override
	public String toString() {
		return "HolidayType [holidayTypeId=" + holidayTypeId + ", holidayType=" + holidayType + ", holidayTypeShort="
				+ holidayTypeShort + ", createdDate=" + createdDate + ", modifiedDate=" + modifiedDate + ", createdBy="
				+ createdBy + ", modifiedBy=" + modifiedBy + ", createdUsername=" + createdUsername
				+ ", modifiedUsername=" + modifiedUsername + ", active=" + active + "]";
	}

}
