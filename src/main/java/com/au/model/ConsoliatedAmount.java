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
@Table(name="consoliated_amount")
@Entity
public class ConsoliatedAmount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer consoliatedAmountId;
	
	@Column(updatable = false)
	private Integer created_by;
	
	private Integer modified_by;

	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_Date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_Date;
	
	private Integer month;
	
	private Integer year;
	
	private Float amount;
	
	private Integer empId;
	
	private String fromDate;
	
	private String toDate;
	
	private String remarks;
	
	private Float consoliatedAmount;

	private Float remainingAmount;

	private String subject;
}
