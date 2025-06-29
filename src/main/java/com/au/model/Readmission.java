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
@Table(name="readmission")
public class Readmission {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer readmissionId;
	private String oldAuid;
	private Integer oldStudentId;
	private Integer voucherHeadNewId;
	private Integer feeTemplateId;
	private Integer acYearId;
	private Integer semOrYear;
	private Double totalAmount;
	private String type;
	private Double balance;
	private String remarks;
	
	private Boolean active;
	
	
	@Column(updatable = false)
	private Integer createdBy;
	private Integer modifiedBy;

	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modifiedDate;

	@Column(updatable = false)
	private String createdUsername;
	private String modifiedUsername;
	private String newAuid;
	private Integer newStudentId;
	

}
