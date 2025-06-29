package com.au.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Lob;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TemporaryPurchasseOrderResponseDTO {

	private Integer temporary_purchase_order_id;
	
	private String vendor;
	
	private Integer vendorId;
	
	private String institute;
	
	private Integer instituteId;

	private String storeName;
	
	private String remarks;
	
	private String quotationNo;
	
	private String termsOfPayment;
	
	private String noOfDays;
	
	private String accountPaymentType;
	
	private String requestType;
	
	
	private String termsAndConditions;
	
	private String annexure;
	
	private Integer empId;
	
	private String purchaseApprover;
	

	private Integer purchaseApproverId;
	
	private boolean active=true;
	
	private Date created_date;
	private Date modified_date;
	
	private Integer approverId;
	

	private Integer approvedStatus;
	

	private Date approvedDate;
	
	private Integer month;
	
	private Integer year;
	
	private Date quotationDate;
	
	private Integer cancelById;

	private String bankBranch;
	private String bankName;
	private String accountHolderName;
	private String accountNo;
	private String bankIfscNo;
	
	private String vendorAddress;
	
	private String pinCode;
	
	private String cityName;
	
	private String stateName;
	
	private String countryName;
	
	private String vendorTinNo;
	
	private String vendorContactNo;
	
	private String destination;
	
	private String otherReference;
	
	private List<TemporaryPurchaseItemResponseDTO> temporaryPurchaseItems;
	
	private String createdUsername;
	
	private String vendorStreetName;
	private String panNumber;
	private String vendorGstNo;
	private String vendorEmail;
	private String area;
	
	@Lob
	private String annexureText;
}
