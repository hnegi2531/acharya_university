package com.au.model;

import java.util.Date;

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

import lombok.Data;

@Data
@Entity
@Table(name = "event_creation")
public class EventCreation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer event_id;
	private String event_name;
	private String event_sub_name;
	private String school_id;
	@Lob
	private String event_description;
	private String event_start_time;
	private String event_end_time;
	private String guest_name;
	
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	
	@Column(updatable = false)
	private Integer created_by;
	
	private Integer modified_by;
	private Boolean active;
	
	@Column(updatable = false)
	private String created_username;
	
	private String modified_username;
	private String is_common;
	
	private String approved_status;
	private String approved_date;
	private Integer approved_by;
	private String remarks;
	private Boolean tt_status;
	private Boolean event_status;
	
	private String summarize;
	private String summarize_status;
}

