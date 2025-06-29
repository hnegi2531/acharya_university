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
@Table(name = "registration_fee_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationFeeTransaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long registrationFeeTransactionId;
	
	private Integer candidateId;
	
	private String mobileNumber;
	
	private Float amount;
	
	private Integer voucherId;
	
	private String receiptId;
	
	private String orderId;
	
	private String paymentId;
	private String transactionId;
	
	private String signature;
	
	private Date transactionDate;
	
	private String status;
	
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
	
	private String receiptStatus;
	private String settlementId;
	private String settlementUTR;
	private Double dollar_value;
	private Integer schoolId;
}
