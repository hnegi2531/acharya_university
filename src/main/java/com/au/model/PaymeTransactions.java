package com.au.model;

import java.time.LocalDateTime;
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
@Table(name ="payme_transaction")
public class PaymeTransactions {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer transaction_id;
	private String paycom_transaction_id;
	private Long paycom_time;
	private Date paycom_time_datetime;
	private Long create_time;
	private Long perform_time;
	private Long cancel_time;
	private Integer state;
	private Integer reason;
	private String receivers;
	private String order_id;
	private Integer amount;
	private Integer candidate_id;
	private LocalDateTime normal_performed_time;
	
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)	
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_Date;
	
	private String payment_for;
	
	/*
	 * For fee head amount restriction payment
	 */
	private String restriction_amount_payer;
	private String payer_email;
	private String mobile;
	private String auid_or_other_info;
	private Integer fee_head_amount_restriction_id;
	
}
