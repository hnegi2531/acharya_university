package com.au.dto;

import lombok.Data;


@Data
public class purchaseOrderUpdateDto {

	
	private Integer temporary_purchase_order_id;
	private Integer billApprovedStatus;
	private Integer billApprovedId;
}
