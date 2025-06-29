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
@Table(name = "budget")
public class Budget {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer budget_id;
	
	private Integer emp_id;
	private Integer financial_year_id;
	private Integer school_id;
	private Integer dept_id;
	private Integer ledger_id;
	private Integer voucher_head_new_id;
	private Double proposed_amount;
	private Double recommended_amount;
	private Double approved_amount;
	private String remark;
	
	private Boolean lock_status; 
	private String lock_date;
	
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
}
