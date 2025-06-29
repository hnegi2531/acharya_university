package com.au.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "holiday_calender")
public class HolidayCalender {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer holidayCalendarId;
//	private Integer holidayTypeId;
	//@JsonFormat(pattern = "dd-MM-yyyy")
	//@JsonFormat(pattern = "yyyy-MM-dd")	
	//@Column(unique = true)
//	@JsonFormat(pattern = "dd-MM-yyyy")
	
//	@Temporal(TemporalType.DATE)
	private Date fromDate;
	private Integer daysCount;

	//@Column(unique = true)
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
	private String dept_id;
	private String day;
	
	@Lob
	private String holiday_description;
	
}
