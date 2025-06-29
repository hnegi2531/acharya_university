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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="stock_issue")
public class StockIssue {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long stockId;
	
	private Integer itemId;
	
	private String itemName;
	

	private Integer storeId;
	
	private String storeName;
	
	private String description;
	
	private String uom;
	
	private Double availableQuantity;
	
	private Double stocks;
	
	private String purpose;
	
	private Integer createdBy;
	private String createdByUserName;
	
	private String endUserName;
	private Integer endUserId;
	
	private String stockNo;
	
	private Double issueQuantity;
	
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdDate;
}
