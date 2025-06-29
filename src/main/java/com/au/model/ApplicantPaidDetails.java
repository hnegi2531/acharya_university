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
@Table(name = "applicant_paid_details")
public class ApplicantPaidDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer applicant_paid_details_id;
	
	private String lead_id;
	private String opportunity_id;
	private String mobile_number;
	private String email;
	private Double amount;
	private String candidate_name;
	private String application_no_npf;
	private String order_id;
	private String razorpay_id;
	private String payment_status;
	
	
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
	
}
