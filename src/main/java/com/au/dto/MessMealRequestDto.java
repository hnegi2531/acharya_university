package com.au.dto;

import java.util.Date;
import java.util.List;

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
public class MessMealRequestDto {
	
	private Integer refreshment_id;
	private List<String> date;
	private Integer meal_id;
	private Integer count;
	private String time;
	private String remarks;

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
	private Integer created_by;
	private Integer modified_by;
	private Boolean active;
	
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
	private Integer appvoved_count;
	
	private Integer dept_id;
	private Integer school_id;
	private Integer user_id;
	
	private String time_for_frontend;
}
