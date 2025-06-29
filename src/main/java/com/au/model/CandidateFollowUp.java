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

import lombok.Data;

@Data
@Entity
@Table(name = "candidate_follow_up")
public class CandidateFollowUp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer candidate_followup_id;
	private Integer candidate_id;
	private String follow_up_remarks;
	private Date follow_up_date;
	
	
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;
	
	@Column(updatable = false)
	private String created_username;
	private String modified_username;


	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	private Boolean active=true;
	private String agent;
	private Boolean pay_status;
}
