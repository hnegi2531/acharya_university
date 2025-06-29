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
@Table(name = "uniform_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UniformTransaction {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer uniformTransactionId;
	    
	    private Integer studentId;
	    private String orderId;
		
		private Float amount;
		
		private String status;
			
		private Integer currentSem;
		
		private Integer currentYear;
		
		private Integer acYearId;
		
		private String paymentId;
		private String transactionId;
		private String signature;
		private String code;
		private String description;
		private String source;
		private String step;
		private String reason;
		
		private String receiptId;
		
		@Temporal(TemporalType.TIMESTAMP)	
		@CreationTimestamp
		private Date created_date;

		@Temporal(TemporalType.TIMESTAMP)	
		@UpdateTimestamp
		private Date modified_date;
		
		private Date transactionDate;
		
		private String remarks;

		private String transferId;


}
