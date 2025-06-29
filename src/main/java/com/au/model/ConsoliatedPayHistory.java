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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="consoliated_pay_history")
@Entity
public class ConsoliatedPayHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer consoliatedPayHistoryId;
	
	private Integer empId;
	
	private Float remainingAmount;
	
	private Double paydays;
	
	private Integer month;
	
	private Integer year;
	
	private Float totalAmount;
	
	private Float payingAmount;
	
	private Float tds;
	
	private Float netPay;
	
	private Boolean active;
	
	private Float splPay;
	
	private Float transportAmount;
	
	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdDate;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modifiedDate;
	private Integer createdBy;
	private Integer modifiedBy;

	private Integer consoliatedAmountId;
}
