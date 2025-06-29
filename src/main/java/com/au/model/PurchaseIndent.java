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
@Table(name="purchase_indent")
@Entity
public class PurchaseIndent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer purchaseIndentId;
	
	private Integer envItemId;
	
	private Integer ledgerId;
	
	private String itemDescription;
	
	private Double quantity;
	
	private Double approxRate;
	
	private Double budgetAvaialable;
	
	private Double balanceQuantity;
	
	private String vendorName;
	
	private String vendorContactNo;
	
	private Boolean active;

	private String attachmantPathType;

	private String remark;
	
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	
	private Integer createdBy;
	
	private Double totalValue;
	
	private String status;
	
	private Integer approverId;
	
	private Integer financialYearId;
	private String indentNo;
	private Date approvedDate;
}
