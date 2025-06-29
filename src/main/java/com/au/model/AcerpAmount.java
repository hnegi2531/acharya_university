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
@Table(name="acerp_amount")
public class AcerpAmount {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer acerpAmountId;
	private String auid;
	private Integer studentId;
	private Float paidYear1;
	private Float paidYear2;
	private Float paidYear3;
	private Float paidYear4;
	private Float paidYear5;
	private Float paidYear6;
	private Float paidYear7;
	private Float paidYear8;
	private Float paidYear9;
	private Float paidYear10;
	private Float paidYear11;
	private Float paidYear12;
	
	private String remarks;
	private Boolean active;
	private String acerpAmountAttachPath;
	private String type; 
	
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

}
