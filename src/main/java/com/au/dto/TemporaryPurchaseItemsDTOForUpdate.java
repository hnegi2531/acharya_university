package com.au.dto;




import lombok.Data;

@Data
public class TemporaryPurchaseItemsDTOForUpdate {
	
	private Integer temporary_purchase_item_id;
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
