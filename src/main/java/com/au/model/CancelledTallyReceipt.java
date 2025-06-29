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
@Table(name = "cancelled_tally_receipt")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CancelledTallyReceipt {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cancelled_tally_receipt_id;
	private Integer tally_receipt_id;
	private Integer student_id;
	private String student_name;//first name plus last name concatenation
	private String auid;
	private String usn;
	private String particulars;
	private Double total;
	private String fee_receipt;//fee receipt number
	private String transaction_type;
	private String school_name;
	private String transaction_no;
	private String deposited_bank;
	private String dd_no;
	private String dd_bank_name;
	private String financial_year;
	private Double total_amount;
	private String received_from;
	private String received_type;
	private String received_in;
	private String remarks;
	private String transaction_date;
	private String bank_institute;
	private Integer vendor_id;
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
	
	private Integer fee_receipt_id;
	private Double total_som;
	private Double total_amount_som;
	private String cancel_remarks;
	private Integer cancel_by;
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)	
	@CreationTimestamp
	private Date cancel_date;
	

}
