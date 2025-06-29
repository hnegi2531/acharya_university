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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bank")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bank {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bank_id;
	private String bank_name;
	private Integer created_by;
	private Integer modified_by;
//	private String acc_name;
//    @Column(unique = true)
//	@NotBlank(message = "account number should not be Empty OR Null")
//	private String acc_number;
//	@Column(unique = true)
//	private String ifsc_code;
//	private String swift_code;
//	private String bank_branch_name;
//	private Boolean payment_voucher_status;
	
	@NotBlank(message = "bank short name should not be Empty OR Null")
	private String bank_short_name;
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
	private Boolean internal_status;
	
	private String bank_branch_name;
	private String account_name;
	@Column(updatable = false)
	@NotBlank(message = "account number should not be Empty OR Null")
	private String account_number;
	private Integer school_id;
	private Double opening_balance;
	private Double bank_balance;
	private String swift_code;
	
	private Integer voucher_head_new_id;
	private String ifsc_code;
	private Integer bank_group_id;

	private String balanceUpdatedBy;
	private Date balanceUpdatedOn;
	private Date previousUpdatedOn;


}
