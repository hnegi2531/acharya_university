package com.au.dto;

import java.util.Date;

import javax.persistence.Column;
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
public class CancelAdmissionsDto {

	private Integer cancel_id;
	private String auid;
	private String student_name;
	private String remarks;
	private String attachment_path;
	private String attachment_name;
	@Column(name = "created_by", updatable = false)
	private Integer created_by;
	
	private Integer modified_by;
	
	@Column(name = "created_username", updatable = false)
	private String created_username;
	
	private String modified_username;

	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	private Boolean active;
	private Integer dept_id;
	private Integer school_id;
	private String hostel_remarks;
	private Integer approved_by;
	private String approved_date;
}
