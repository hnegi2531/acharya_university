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
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Entity
@Table(name = "vendor_history")
public class VendorHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer VendorHistory_id;
	private Integer vendor_attachment_id;
	private Integer vendor_id;
	private String vendor_name;
	private String vendor_email;
	private String vendor_contact_no;
	private String vendor_gst_no;
	private String vendor_bank_account_holder_name;
	private String vendor_bank_name;
	private String bank_branch;
	private String vendor_bank_ifsc_code;
	private Integer state_id;
	private String pin_code;
	private Integer vendor_city_id;
	private String pan_number;
	private String street_name;
	private String area;
	private String account_no;
	private String vendor_type;
	private Integer tds_percentage;
	
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
	private Integer ledger_id;
	private String nature_of_business;
	private Integer credit_period;
	private String vendor_address;
	
	@Column(updatable = false)
	private String created_username;
	private String modified_username;

	private Integer voucher_head_id;
	private Integer voucher_head_new_id;
	private Integer country_id;
	private Date account_verifier_date;
	private Integer verifier_user_id;
	private Boolean account_verification_status;
	
	private String vendor_attachment_path;
	private String vendor_attachment_file_name;
	private String vendor_attachement_type;
}
