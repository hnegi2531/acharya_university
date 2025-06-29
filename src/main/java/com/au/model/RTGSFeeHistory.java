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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rtgs_fee_history")
public class RTGSFeeHistory {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer rtgs_fee_history_id;
	private Integer student_id;
	private Integer school_id;
	private String receipt_no;
	private Integer fc_year_id;
	private Double paid;
	private Double rtgs_net_amount;
	private Integer bank_transaction_history_id;//bank_import_transaction_id
	private Double rtgs_balance_amount;
	private String remarks;
	private String receipt_type;
	private Integer year;
	private Integer sem;
	
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
	private Boolean active;
	
}
