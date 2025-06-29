package com.au.dto;

import java.util.Date;
import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.Data;

@Data
public class ClassCommencementDetailsDto {
	
	private Integer class_commencement_details_id;
	private Integer ac_year_id;
	private Integer school_id;
	private HashMap<Integer, Integer> program_specialization_id;
	private Integer program_assignment_id;
	private Integer year_sem;
	private Integer current_year;
	private Integer commencement_id;
	private String remarks;
	
	@Temporal(TemporalType.DATE)
	private Date to_date;
	@Temporal(TemporalType.DATE)
	private Date from_date;
	private String fromDate_for_fronted_use;
	private String toDate_for_fronted_use;
	private Boolean active;
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
	
	@Column(name = "created_username", updatable = false)
	private String created_username;
	private String modified_username;
	
	public ClassCommencementDetailsDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
