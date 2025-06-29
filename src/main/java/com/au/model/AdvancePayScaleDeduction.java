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
@Table(name = "advance_payscale_deduction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdvancePayScaleDeduction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer advance_id;
	private Double principal_amount;
	private String loan_started_date;
	private String loan_end_date;
	private Integer tenure;
	private Double emi_amount;
//	private Double remaining_balance;
	private Integer emp_id;
	
//	private String lic_policy_no;
	private String loan_created_date;
	private String loan_completed_date;
	private Integer completed_tenture;
	private Integer current_tenture;
	private String category_name;
	private Integer school_id;
	private Integer deactivate_year;
	private Integer deactivate_month;
	private String lic_number;
	
//	private String month_year;
//	private String remark;
	
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)	
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)	
	@UpdateTimestamp
	private Date modified_date;
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;
	private Boolean active;

}
