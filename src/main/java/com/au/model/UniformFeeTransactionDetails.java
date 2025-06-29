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
@Table(name="uniform_fee_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UniformFeeTransactionDetails {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer uniformDetailsId;
	  
	    private String itemName;
		private Float amount;
		private Integer quantity;
		private Double cgst_input;
		private Double cgst_output;
		private Double sgst_input;
		private Double sgst_output;	
		private Double gst;
		private Integer env_item_id;
		private String paymentType;
		
		private Integer uniformTransactionId;
		
		@Temporal(TemporalType.TIMESTAMP)
		@CreationTimestamp
		private Date createdDate;

		@Column(name = "modified_date")
		@Temporal(TemporalType.TIMESTAMP)
		@UpdateTimestamp
		private Date modifiedDate;
		
		private Integer paidYear;
		private Integer studentId;

}
