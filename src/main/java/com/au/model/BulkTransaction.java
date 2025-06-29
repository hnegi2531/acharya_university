package com.au.model;

import java.util.Date;

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
@Table(name = "bulk_pay_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BulkTransaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long razorPayTransactionId;

	private String receiptId;

	private String orderId;

	private Float amount;

	private String status;

	private String name;
	private String email;
	private String mobile;
	private String paidYear;

	private String transactionType;

	private String paymentId;
	private String transactionId;
	private String signature;
	private String code;
	private String description;
	private String source;
	private String step;
	private String reason;
	private String paymentType;
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;

	private Date transactionDate;

	private String remarks;
	private Integer schoolId;
	private Integer voucherHeadId;
	private String settlementId;
	private String settlementUTR;
	private String transferId;
	private String transferType;

}
