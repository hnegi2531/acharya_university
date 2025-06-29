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
@Table(name = "contra_voucher")
public class ContraVoucher {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer contra_voucher_id;
	
	private Integer school_id;
    private Integer bank_id;
    private String date_of_deposit;
    private String selected_date;
    private Double net_amount;
    private Double deposited_amount;
    private String row_created_date;
    private Double balance;
    private Double closing_cash;
    private Double cash_received;
    private Double cash_payment;
    private Double cash_summary;
    private String remarks;
    private Integer financial_year_id;
    private Integer voucher_no;
    private Integer cancel_voucher;
    private String voucher_remarks;
    private Integer cancelled_by;
    private String cancelled_date;
    private Integer inter_school_id;
    
    private Double total_amount;
    
    @Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;

	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;
	
	private Boolean active;
	
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	
}
