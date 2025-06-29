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
@Table(name = "click_payment_transaction")
public class ClickPayment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer click_payment_id;
	private String click_trans_id;
	private Integer service_id;
	private String click_paydoc_id;
	private String merchant_trans_id;//Order_id in PaymeGateway
	private Float amount;
	private Integer action;
	private Integer error;
	private String error_note;
	private String sign_time;
	private String sign_string;
	private Integer merchant_prepare_id;
	private Integer candidate_id;
	private Integer merchant_confirm_id;
	
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_Date;

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
