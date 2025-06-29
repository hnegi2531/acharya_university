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


import lombok.Data;

@Entity
@Table(name = "student_offer_acceptance")
@Data
public class StudentOfferAcceptance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer student_offer_acceptance_id;
	private Integer candidate_id;
	private String accepted_date;
	private String ip_address;
	private String offer_voucher_code;
	
	@Column(name = "created_Date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_Date;
	private Boolean active;
}
