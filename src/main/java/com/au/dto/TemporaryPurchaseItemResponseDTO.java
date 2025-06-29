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
public class TemporaryPurchaseItemResponseDTO {
	private Integer temporary_purchase_item_id;
	
	private Float rate;
	
	private Float quantity;
	
	private Float discount;
	
	private Float gst;
	
	private Float totalAmount;
	
	private String itemName;
	
	private Float balanceQuantity;
	
	
	private Date created_date;
	private Date modified_date;
	
	private String measureName;
	
	private Integer envItemsInStoresId;
	private String gstTotal;
	private String discountTotal;
	private String costTotal;
	private String createdUsername;
}
