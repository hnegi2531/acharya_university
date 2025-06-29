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

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LibraryInventory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer library_inventory_id;
	private String category;
	private String accessionNumber;
	private String barcode;
	private String author;
	private String title;
	private String subtitle;
	private String edition;
	private String year;
	private String pages;
	private String publisher;
	private String place;
	private String sourceOfSupply;
	private String billNo;
	private Date billdate;
	private Integer foriegnCost;
	private Integer indianCost;
	private Integer discount;
	private Double netAmt;
	private String remarks;
	private String uid;
	private Integer itemId;
	private Integer createdBy;

	private String createdUserName;
	private Boolean isIssued;

	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdDate;

}
