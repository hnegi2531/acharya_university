package com.au.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseOrderResponseDTO {

	private String vendor;
	private String institute;
	private String poNo;
	private Double amount;
	private String accountPaymentType;
	private Integer purchaseOrderId;
	private Integer approverId;
	private String remarks;
	private String createdUsername;
	private Boolean grnCreationStatus;
	private String tpoAttachmentFilePath;
	private Date created_date;
	private Date approvedDate;
	private String requestType;
	private String approverName;
	private String status;
	private String cancelComments;

	
	public PurchaseOrderResponseDTO(String vendor, String institute, String poNo, Double amount,
			String accountPaymentType, Integer purchaseOrderId, Integer approverId, String remarks) {
		
		this.vendor = vendor;
		this.institute = institute;
		this.poNo = poNo;
		this.amount = amount;
		this.accountPaymentType = accountPaymentType;
		this.purchaseOrderId = purchaseOrderId;
		this.approverId = approverId;
		this.remarks = remarks;
	}

	public PurchaseOrderResponseDTO(String vendor, String poNo) {
		this.vendor = vendor;
		this.poNo = poNo;
	}

	public PurchaseOrderResponseDTO(String vendor, String institute, String poNo, Double amount,
			String accountPaymentType, Integer purchaseOrderId, Integer approverId, String remarks,
			String createdUsername,Boolean grnCreationStatus, String tpoAttachmentFilePath, Date created_date, Date approvedDate,
									String requestType, String approverName) {
		this.vendor = vendor;
		this.institute = institute;
		this.poNo = poNo;
		this.amount = amount;
		this.accountPaymentType = accountPaymentType;
		this.purchaseOrderId = purchaseOrderId;
		this.approverId = approverId;
		this.remarks = remarks;
		this.createdUsername = createdUsername;
		this.grnCreationStatus=grnCreationStatus;
		this.tpoAttachmentFilePath = tpoAttachmentFilePath;
		this.created_date = created_date;
		this.approvedDate = approvedDate;
		this.requestType = requestType;
		this.approverName = approverName;
	}

	public PurchaseOrderResponseDTO(String vendor, String institute, String poNo, Double amount,
			String accountPaymentType, Integer purchaseOrderId, Integer approverId, String remarks,
			String createdUsername) {
		this.vendor = vendor;
		this.institute = institute;
		this.poNo = poNo;
		this.amount = amount;
		this.accountPaymentType = accountPaymentType;
		this.purchaseOrderId = purchaseOrderId;
		this.approverId = approverId;
		this.remarks = remarks;
		this.createdUsername = createdUsername;
	}
	
	
	
	
	
	
}
