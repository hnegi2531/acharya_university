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
@Table(name = "temporary_razor_pay_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TemporaryRazorPayTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long razorPayTransactionId;
	
	private String receiptId;
	
	private String orderId;
	
	private Float amount;
	
	private String status;
	
	private Integer studentId;
	
	private Integer currentSem;
	
	private Integer currentYear;
	
	private Integer acYearId;
	
	private String paidYear;
	
	private String transactionType;
	
	private String paymentId;
	private String signature;
	private String code;
	private String description;
	private String source;
	private String step;
	private String reason;
	
	@Temporal(TemporalType.TIMESTAMP)	
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)	
	@UpdateTimestamp
	private Date modified_date;
	
	private Date transactionDate;
	
	private String remarks;
	
	private Integer transactionId;
}
