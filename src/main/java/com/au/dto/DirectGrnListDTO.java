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
public class DirectGrnListDTO {

	private Integer grnId;
	private String itemName;
	private Double quantity;
	private Double balanceQuantity;
	private Float value;
	private Double enterQuantity;
	private String itemDescription;
	private String attachment;
	private String invoiceNo;
	private Date invoiceDate;
	private Integer vendorId;
	private String vendorName;
	private Integer instituteId;
	private String aquistion;
	private String remarks;
	private Integer storeId;
	private String storeName;
	private String createdByUserName;
	private String grnNumber;
	private Date createdDate;
	private String uom;
	private String uomShortName;
	private Double poTotalAmount;
	private String poReferenceNo;
	private Date approvedDate;

}
