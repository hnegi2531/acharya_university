package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TemporaryPurchaseItemsDTO {

	private Float rate;
	
	private Float quantity;
	
	private Float discount;
	
	private Float gst;
	
	private Float totalAmount;
	
	private String itemName;
	
	private Integer envItemsInStoresId;
	
	
    private String gstTotal;
    private String discountTotal;
    private String costTotal;
	
}
