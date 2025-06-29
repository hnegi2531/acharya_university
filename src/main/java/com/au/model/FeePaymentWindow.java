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
@Table(name = "fee_payment_window")
public class FeePaymentWindow {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer fee_payment_window_id;

	private Integer school_id;
	private String voucher_head_new_id;
	private String user_id;
	private Double amount;
	private Boolean fixed;
	private Boolean external_status;
	private String remarks;
	private String attachment_path;
	private String attachment_file;
	private String window_type;
	
	private String from_date;
	private String to_date;

	private String program_id;
	private Boolean Status;
	
	private String voucher_head;
	private String program;
	private String userName;

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
	
	private String transfer_type;
	

}
