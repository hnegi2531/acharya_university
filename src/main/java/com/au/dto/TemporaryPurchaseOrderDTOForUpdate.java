package com.au.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Lob;

import lombok.Data;

@Data
public class TemporaryPurchaseOrderDTOForUpdate {
	
	
	private Integer vendorId;
    
    private String institute;
	
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
	
	private String destination;
	
	private String otherReference;
	private Integer instituteId;
	
	@Lob
	private String annexureText;
	
	private List<TemporaryPurchaseItemsDTOForUpdate> temporaryPurchaseItems=new ArrayList<>();

}
