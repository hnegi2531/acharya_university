package com.au.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
@Table(name="master_salary")
@Entity
public class MasterSalary {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer emp_pay_history_id;
	
	private Integer emp_id;
	
	private double pay_days=0;
	private double working_days=0;
	
	private Integer inv_pay;
	
	private Float basic;
	
	private Float hra;
	
	private Float da;
	
	private Float cca;
	private Float ta;
	private Float mr;
	private Float fr;
	private Float other_allow;
	private Float spl_1;
	private Float gross_pay;
	private Float pf;
	private Float pt;
	private Float esi;
	private Float tds;
	private Float advance1;
	private Float advance2;
	private Float total_earning;
	private Float total_deduction;
	private Float net_pay;
	private Float pf_account_no;
	private Float pf_earnings;
	private Float contribution_epf;
	private Float epf_difference;
	private Float pension_fund;
	private Float esi_earnings;
	private Float esi_contribution_employee;
	private Integer year;
	private Integer month;
	private String created_by;
	private String modified_by;
	private Date fromdate;
	private Date todate;
	private Integer dept_id;
	private Integer school_id;
	private String bank;
	private String transport;
	private Integer salary_approve_status;
	private Date salaray_block_date;
	private Integer new_join_status;
	private String bank_account_no;
	private Double er;
	private Float tax;
	private Float pTax;
	private Float advance;
	private Float lic;
	@Lob
	private String remarks;
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date created_date;
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified_date;

	private Float pf_management;

}
