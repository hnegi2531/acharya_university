package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockIssueDTO {


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
	
	private Double issueQuantity; 
	
	private String stockNo;
}
