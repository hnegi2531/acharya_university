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
@Table(name = "meal_bill")
public class MealBill {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer meal_bill_id;
	
	private String bill_number;
	private Integer refreshment_id;
	private String lock_status;
	private String lock_date;
	private Integer lock_by;
	private Integer approved_by;
	private String approved_date;
	private Integer financial_year_id;
	
	
	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;

	@Column(name = "created_username", updatable = false)
	private String created_username;
	private String modified_username;

	@Column(name = "created_by", updatable = false)
	private Integer created_by;
	private Integer modified_by;

	private Boolean active;
	
	private Integer dept_id;
	private Integer school_id;
	private Boolean approve_status;
	private Integer voucher_head_new_id;
	private String month_year;
	private String total_amount;
	private String lock_ipAddress;
	private String approved_ipAddress;
}
