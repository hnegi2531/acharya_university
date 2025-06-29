package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListOfGrnProducts {

	private Integer purchaseOrderId;
	private Integer purchaseItemId;
	private String itemName;
	private Double quantity;
	private Double balanceQuantity;
	private Float value;
	private Double enterQuantity;
	private String itemDescription;
	private String descriptionAsperInvoice;
	private Boolean isLibrary;
	private String originalTotal;
	private Integer envItemsInStoresId;
	private Float discount;
	private Float discountTotal;
	private Float gst;
	private Float gstTotal;
	private Float costTotal;
	private Float totalAmount;
	private Integer itemId;
}
