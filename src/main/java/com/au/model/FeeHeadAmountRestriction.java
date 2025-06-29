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
import javax.validation.constraints.Digits;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Table(name = "fee_head_amount_restriction")
@Data
public class FeeHeadAmountRestriction {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="fee_head_amount_restriction_id")
	private Integer feeHeadAmountRestrictionId;
	
	private Integer schoolId;
	@Column(name="from_date")
	private Date fromDate;
	@Column(name="to_date")
	private Date toDate;
	@Digits(fraction = 4, integer = 20)
	private Float amount;

	private Integer voucherHeadId;
	private Boolean fixed;
	@Column(name="external_status")
	private String externalStatus;
	private String remarks;
	@Column(name="attachment_path")
	private String attachmentPath;
	@Column(name="attachment_file_name")
	private String attachmentFileName;
	@Column(name="attachment_type")
	private String attachmentType;
	private String userId;
	@Column(name = "created_by",updatable = false)
	private Integer createdBy;
	@Column(name = "modified_by")
	private Integer modifiedBy;
	@Column(name = "created_date",updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdDate;
	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modifiedDate;
	private Boolean active;
	@Column(name = "created_username",updatable = false)
	private String createdUsername;
	@Column(name = "modified_username")
	private String modifiedUsername;
}
