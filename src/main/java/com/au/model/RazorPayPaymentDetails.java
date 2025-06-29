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
@Table(name = "razor_pay_payment_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RazorPayPaymentDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long razorPaypaymentDetailsId;
	
	private Integer voucherHeadId;
	
	private String paymentType;
	
	private String receiptType;
	
	private Integer year;
	
	private Integer sem;
	private Integer acYearId;
	private Long razorPayTransactionId;
	
	private Double amount;
	
	@Temporal(TemporalType.TIMESTAMP)	
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)	
	@UpdateTimestamp
	private Date modified_date;
	
	private Integer paidYear;
}
