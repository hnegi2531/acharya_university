package com.au.model;


import java.util.Date;
import java.util.List;

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
@Entity
@Table(name ="fee_receipt")
public class FeeReceipt {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer fee_receipt_id;
	private Integer change_course_id;
	private Integer fee_payment_id;
	private String remarks;
	private Integer bus_fee_receipt_id;
	private Integer student_id;
	private Integer ac_year_id;
	
	@Column(unique = true,updatable =  false)
	private String fee_receipt;
	
	private Integer school_id;
	private Integer financial_year_id;
	private Float paid_amount;
	private String transaction_type;
	private String exam_id;
	private String bulk_id;
	private String receipt_type;
	private String received_in;
	private Double inr_value;
	private String paid_year;
	private Integer cancel_by;//user table id store
	private String cancel_date;
	private String cancel_remarks;
	private Integer print_status;
	private String hostel_bulk_id;
	private String hostel_fee_payment_id;
	private Integer hostel_status;
	private Integer bank_transaction_history_id;//bank_import_transaction_id
	private Integer vendor_id;
	
	@Column(updatable = false)
	private String created_username;
	
	private String modified_username;

	@Column(name = "created_date",updatable = false)
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
	
	private Boolean active = true;;
	
	private Double amount_in_som;
	private Integer bank_id;
	private Integer voucher_head_new_id;
	private String transactionMode;
	
}
