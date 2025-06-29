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
@Table(name = "cma_fee_receipt")
public class CmaFeeReceipt {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cma_fee_receipt_id;
	
	private Integer cma_receipt_id;
	private String transacation_type;
	private Double amount;
	private Integer student_id;
	private Integer school_id;
	private Integer financial_year_id;
	private String paid_year;
	private Integer bank_import_transaction_id;
	private String remarks;
	private String waiver_status;
	private String receipt_type;
	private Double total_amount;
	private String orderId;
	
	
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;

	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;

	private Boolean active;
	
	@Column(updatable = false)
	private String created_username;
	private String modified_username;

	private String name;
	private String email;
	private Integer voucherHeadId;
	private Integer transactionId;
}
