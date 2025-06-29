package com.au.dto;


import com.au.model.PurchaseOrder;
import lombok.Data;

@Data
public class PurchaseItemsForUpdate {
	
	
	private Integer purchase_item_id;
	private Float rate;
	
	private Float quantity;
	
	private Float discount;
	
	private Float gst;
	
	private Float totalAmount;
	
	private String itemName;
	
	private Integer envItemsInStoresId;
	
	private PurchaseOrder purchaseOrder;
	private Integer modifiedBy;
	private String modifiedUsername;
    private String gstTotal;
    private String discountTotal;
    private String costTotal;

}
