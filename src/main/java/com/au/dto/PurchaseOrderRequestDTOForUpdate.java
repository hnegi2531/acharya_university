package com.au.dto;

import java.util.List;

import lombok.Data;

@Data
public class PurchaseOrderRequestDTOForUpdate {
	
	private String vendor;

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

	private List<PurchaseItemsForUpdate> purchaseItems;

}
