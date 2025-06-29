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
public class PurchaseIndentDTO {

	private Integer purchaseIndentId;
    private Integer envItemId;
	
	private Integer ledgerId;
	
	private String itemDescription;
	
	private Double quantity;
	
	private Double approxRate;
	
	private Double budgetAvaialable;
	
	private Double balanceQuantity;
	
	private String vendorName;
	
	private String vendorContactNo;

	private Integer createdBy;
	
	private String attachmantPathType;
	
	private String remark;
	
	private String createdUserName;
	
	private Date createdDate;
	
	private Double totalValue;
	
	private String status;
	
	private Integer approverId;
	
	private String approverName;
	
	private String indentNo;
	
	private Date approvedDate;

	public PurchaseIndentDTO(Integer purchaseIndentId,Integer envItemId, Integer ledgerId, String itemDescription, Double quantity,
			Double approxRate, Double budgetAvaialable, Double balanceQuantity, String vendorName,
			String vendorContactNo,  String attachmantPathType, String remark,Date createdDate, Double totalValue,
			String status,String approverName, Integer approverId,String indentNo,Integer createdBy,Date approvedDate) {
		this.purchaseIndentId=purchaseIndentId;
		this.envItemId = envItemId;
		this.ledgerId = ledgerId;
		this.itemDescription = itemDescription;
		this.quantity = quantity;
		this.approxRate = approxRate;
		this.budgetAvaialable = budgetAvaialable;
		this.balanceQuantity = balanceQuantity;
		this.vendorName = vendorName;
		this.vendorContactNo = vendorContactNo;
		this.createdBy = createdBy;
		this.attachmantPathType = attachmantPathType;
		this.remark = remark;
		this.createdDate=createdDate;
		this.totalValue=totalValue;
		this.status=status;
		this.approverName=approverName;
		this.approverId=approverId;
		this.indentNo=indentNo;
		this.approvedDate=approvedDate;
	}

	public PurchaseIndentDTO(Integer purchaseIndentId,Integer envItemId, Integer ledgerId, String itemDescription, Double quantity,
			Double approxRate, Double budgetAvaialable, Double balanceQuantity, String vendorName,
			String vendorContactNo, String attachmantPathType, String remark, String createdUserName,Date createdDate,Double totalValue,
			String status,String approverName, Integer approverId,String indentNo,Integer createdBy,Date approvedDate) {
		this.purchaseIndentId=purchaseIndentId;
		this.envItemId = envItemId;
		this.ledgerId = ledgerId;
		this.itemDescription = itemDescription;
		this.quantity = quantity;
		this.approxRate = approxRate;
		this.budgetAvaialable = budgetAvaialable;
		this.balanceQuantity = balanceQuantity;
		this.vendorName = vendorName;
		this.vendorContactNo = vendorContactNo;
		this.attachmantPathType = attachmantPathType;
		this.remark = remark;
		this.createdUserName = createdUserName;
		this.createdDate=createdDate;
		this.totalValue=totalValue;
		this.status=status;
		this.approverName=approverName;
		this.approverId=approverId;
		this.indentNo=indentNo;
		this.createdBy=createdBy;
		this.approvedDate=approvedDate;
	}
	
	
	
}
