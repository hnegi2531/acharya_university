package com.au.dto;
import lombok.Data;

@Data
public class PurchaseOrderDto {

	private Integer purchase_order_id;
	private Integer draft_payment_voucher_id;
	private Integer payment_voucher_id;
}
