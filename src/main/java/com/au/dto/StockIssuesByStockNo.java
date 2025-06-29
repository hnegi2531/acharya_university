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
public class StockIssuesByStockNo {
	
	
	private Integer storeId;
	private String storeName;
	private String description;
	private Double availableQuantity;
	private Double stocks;
	private String purpose;
	private Integer createdBy;
	private String createdByUserName;
	private String endUserName;
	private Integer endUserId;
	private String stockNo;
	private Double issueQuantity;
	private Date createdDate;
	private String uom;
	
	
}
