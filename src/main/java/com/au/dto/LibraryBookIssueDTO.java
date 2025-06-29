package com.au.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LibraryBookIssueDTO {

    private Integer libraryId;
	
	private String libraryItemName ;
	

	private Integer libraryAssignmentId;
	
	private String uom;
	
	private Double closingStock;
	
	private Double issueQuantity;
	
	private Integer createdBy;
	
	private Integer issuerId;
	
	private String accessionNumber;
	
	private String bookName;
	
	private String issuedBy;
	
	private Double finePerDay;
	
	private String dueDate;
	
	private Date checkOutTime;
	
	private Date checkInTime;
	
	private Double issuerFine;
	
	private String isssuerName;
	
	private String userCode;

	public LibraryBookIssueDTO(Integer libraryAssignmentId, String libraryItemName, String uom, Double closingStock,
			Double issueQuantity, Integer issuerId, String accessionNumber, String bookName, 
			String issuedBy,Double finePerDay,String dueDate,Date checkOutTime,Date checkInTime,String userCode) {
		this.libraryAssignmentId = libraryAssignmentId;
		this.libraryItemName = libraryItemName;
		this.uom = uom;
		this.closingStock = closingStock;
		this.issueQuantity = issueQuantity;
		this.issuerId = issuerId;
		this.accessionNumber = accessionNumber;
		this.bookName = bookName;
		this.issuedBy = issuedBy;
		this.finePerDay=finePerDay;
		this.dueDate=dueDate;
		this.checkOutTime=checkOutTime;
		this.checkInTime=checkInTime;
	}
	
	public LibraryBookIssueDTO(Integer libraryAssignmentId, String libraryItemName, String uom, Double closingStock,
			Double issueQuantity, Integer issuerId, String accessionNumber, String bookName, 
			String issuedBy,Double finePerDay,String dueDate,Date checkOutTime,Date checkInTime,Double issuerFine,String isssuerName,String userCode) {
		this.libraryAssignmentId = libraryAssignmentId;
		this.libraryItemName = libraryItemName;
		this.uom = uom;
		this.closingStock = closingStock;
		this.issueQuantity = issueQuantity;
		this.issuerId = issuerId;
		this.accessionNumber = accessionNumber;
		this.bookName = bookName;
		this.issuedBy = issuedBy;
		this.finePerDay=finePerDay;
		this.dueDate=dueDate;
		this.checkOutTime=checkOutTime;
		this.checkInTime=checkInTime;
		this.issuerFine=issuerFine;
		this.isssuerName=isssuerName;
	}
	
	
	
}
