package com.au.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class PurchaseOrderFileRequest {
	
	private MultipartFile file;
	private Integer purchaseOrderId;
	private Integer temporary_purchase_order_id;

}
