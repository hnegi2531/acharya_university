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
@Table(name = "cancelled_fee_receipts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CancelledFeeReceipts {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cancelled_fee_receipt_id;
	private Integer bulk_fee_receipt_id;
	private Integer bulk_fee_receipt;
	private Integer fee_receipt_id;
	private Double amount_in_som;
	private Integer student_id;
	private String fee_receipt;
	private String transaction_type;
	private Integer school_id;
	private String received_in;
	private Integer financial_year_id;
	private String from_name;
	private Double amount;
	private String remarks;
	private String fee_template;
	private String cheque_dd_no;
	private String bank_name;
//	private Integer cancel_by;
	
	
	
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
	private Boolean active;
	@Column(name = "created_username", updatable = false)
	private String created_username;
	private String modified_username;

}
