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
@Table(name="tds_deduction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TdsDeduction {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tdsDeductionId ;
	
	private String empCode;
	private String employeeName;
	
	private Float amount;
	
	private Integer createdBy;
	
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)	
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)	
	@UpdateTimestamp
	private Date modified_date;
	
	private Integer month;
	private Integer year;
	
	public TdsDeduction(String empCode, String employeeName, String amount, Integer month, Integer year) {
	this.empCode=empCode;
	this.employeeName=employeeName;
	this.amount=parseFloat(amount);
	this.month=month;
	this.year=year;
	
	}
	
	private Float parseFloat(String value) {
	    try {
	        return (value != null) ? Float.parseFloat(value) : null;
	    } catch (NumberFormatException e) {
	        // Handle the case where the string cannot be parsed to a float
	        return null;
	    }
	}
}
