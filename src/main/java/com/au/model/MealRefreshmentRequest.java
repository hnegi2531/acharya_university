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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "meal_refreshment_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MealRefreshmentRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer refreshment_id;
//	@Column(unique = true)
	private Integer meal_id;
	private Integer count;
	private String date;
	private String time;
	private String remarks;

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
	
	@Column(name = "created_by", updatable = false)
	private Integer created_by;
	private Integer modified_by;
	private boolean active;
	
	private Integer approved_by;
	private Integer approved_status;
	private String approver_remarks;
	private String approved_date;
	private String approved_time;
	private Integer cancel_by;
	private String cancel_remarks;
	private String delivery_address;
	private Boolean email_status;
	
	private String end_user_feedback_remarks;
	private String cancel_date;
	private String receive_date;
	private Integer receive_status;
	
	private Integer approved_count;  

	private Integer dept_id;
	private Integer school_id;
	private Integer user_id;
	private String time_for_frontend;
	private Integer voucher_head_new_id;
	private String gross_amount;
	private Double rate_per_count;
}
