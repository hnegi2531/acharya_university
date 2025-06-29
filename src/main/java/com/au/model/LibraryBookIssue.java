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
@Table(name="library_book_issue")
public class LibraryBookIssue {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer libraryBookIssueId;
	
	private Integer libraryAssignmentId;
	
	private String libraryItemName ;
	
	private String uom;
	
	private Double closingStock;
	
	private Double issueQuantity;
	
	private Integer createdBy;
	
	private Integer issuerId;
	
	private String accessionNumber;
	private String bookName;
	
	private String checkOut;
	private String checkIn;
	
	private Date checkOutTime;
	
	private Date checkInTime;
	
	private Double issuerFine;
	
	private String userCode;
	
	
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
}

