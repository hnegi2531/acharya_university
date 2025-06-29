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
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
@Entity
@Table(name = "payment_voucher")
public class PaymentVoucher {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer payment_voucher_id;
	
	private Integer school_id;
	private String date;
	private Integer bank_id;
	private Double paying_now;
	private String cheque_dd_no;
	private String remarks;
	private String pay_to;
	private String expensense_lead;
	private Integer financial_year_id;
	private Integer voucher_head_new_id;
 	private Integer fee_head_id;
 	private Double credit_total;
 	private Double debit_total;
 	private Double credit;
 	private String debit;
 	private Integer nature_id;
 	private Integer payment_status;
 	private Integer vendor_id;
 	private Integer dept_id;
 	private Integer vendor_active;
 	private Integer status;
 	private String note;
    private String clearingDate;
    private Integer lockstatus;
    private Integer cancel_voucher; 
    private String voucher_remarks;
    private Integer cancelled_by;
    private String cancelled_date;
    private Integer online;
 	private Integer advanced_po_status;
 	private String attachment_name;
 	private String attachment_path;
 	
 	private Integer journal_voucher_id;
 	private Integer purchase_order_id;
 	private Integer draft_payment_voucher_id;
 	
 	private Integer approver_id;
 	private Integer approved_status;
 	private String approved_date;
 	
 	private String actual_date;
 	
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
	
	@Transient
	private MultipartFile file;
	private Integer voucher_no;
	
	private Integer voucher_head_id;
	
	private Integer jv_joucher_number;
	private Integer jv_school_id;
	private Integer jv_financial_year_id;
	
	private String created_name;
	private String type;
	private Integer env_bill_details_id;
	
	private Integer inter_institute_id;
	private Integer inter_school_id;
	private Integer inter_bank_id;
	private String cancelled_remarks;
}

