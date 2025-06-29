package com.au.dto;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DirectGrnDTO {

	private Integer itemId;
	private String itemName;
	private Double quantity;
	private Double balanceQuantity;
	private Float value;
	private Double enterQuantity;
	private String itemDescription;
	private String attachment;
	private String uom;
	private Boolean isLibrary;
	private Integer envItemId;
	private String originalTotal;
	private Integer envItemsInStoresId;
	private Integer purchaseOrderId;
	private Integer purchaseItemId;
	private Float discount;
	private Float discountTotal;
	private Float gst;
	private Float gstTotal;
	private Float costTotal;
	private Float totalAmount;
}
