package com.au.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.Data;

@Data
public class ReportingStudentsDto {
	
	private Integer student_id;
	private Integer current_year;
	private Integer current_sem;
	private Integer reported_ac_year_id;
	private String remarks;

	private Date reporting_date;
	private Boolean distinct_status;
	private Integer previous_sem;
	private Integer previous_year;
	private Integer eligible_reported_status;
	private Integer year_back_status;
	private Integer section_id;

	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	@Column(updatable = false)
	private int created_by;
	private int modified_by;
	private Boolean active;
	@Column(updatable = false)
	private String created_username;
	private String modified_username;

}
