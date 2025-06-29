package com.au.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.au.model.AdvanceMonthlyEmiDeduction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdvancePayScaleDeductionDto {

	private Integer advance_id;
	private Double principal_amount;
	private String loan_started_date;
	private String loan_end_date;
	private Integer tenure;
	private Double emi_amount;
	private Double remaining_balance;
	private String lic_policy_no;
	private String loan_created_date;
	private String loan_completed_date;
	private Integer completed_tenture;
	private Integer current_tenture;
	private String category_name;
	private Integer school_id;
	private Integer deactivate_year;
	private Integer deactivate_month;
	
	private Integer month;
	private Integer year;
	private String remark;
	private String lic_number;
	
	private List<Integer> emp_id;
	
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
	private List<AdvanceMonthlyEmiDeduction> amed;

}

