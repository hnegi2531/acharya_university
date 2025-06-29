package com.au.dto;




import javax.persistence.Column;

import lombok.Data;

@Data
public class PurchaseItemsHistoryDto {
	
	private Integer purchaseItemId;
	
	private Float rate;
	
	private Float quantity;
	
	private Float balanceQuantity;
	
	private Float discount;
	
	private Float gst;
	
	private Float totalAmount;
	
	private String poReferenceNo;
	
	private String itemName;
	
	private Integer purchaseOrderId;
	
	private Integer envItemId; 
    private String gstTotal;
    private String discountTotal;
    private String costTotal;

}
