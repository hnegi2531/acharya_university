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


@Entity
@Table(name="env_bill_details")
@Data
public class EnvBillDetails {

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer env_bill_details_id;
	
	private Integer category_details_id;
	private String date;
	private String remarks;
	private Integer requested_amount;
	private String attachment_path;
	private String attachment_name;
	private Integer journal_voucher_id;
	
	private Integer aprover1_id;
	private String aprover1_date;
	private String aprover1_remarks;
	private String aprover1_status;
	
	private Integer aprover2_id;
	private String aprover2_remarks;
	private String aprover2_status;
	private String aprover2_date;
	
	private Integer payment_voucher_id;
	private Integer cancel_epayment_grn;
	private String epayment_cancel_remark;
	private Integer env_cancelled_by;
	private String env_cancelled_date;

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

}
