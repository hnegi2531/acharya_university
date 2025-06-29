package com.au.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "purhcase_grn")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GRN {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="grn_id")
	private Integer grnId;
	

	@Column(name="invoice_number")
	private String invoiceNumber;
	
	@Column(name="purchase_ref_no")
	private String purchaseRefNo;
	
	private Double quantity;
	
	@Column(name="institute_id")
	private Integer instituteId;
	
	private boolean active;
	
	@Column(name="invoice_date")
	private Date invoiceDate;
	

	@Column(name="vendor_name")
	private String vendorName;

	@Column(name="invoice_description")
	private String invoiceDescription;
	
	@Column(name="vendor_id")
	private Integer vendorId;
	
	@Column(name="attachment_path")
	private String attachmentPath;
	
	private Float total;

	@Column(name="payment_voucher_id")
	private Integer paymentVoucherId;
	
	@Lob
	@Column(name = "remarks")
	private String remarks;
	
	private String aquistion;
	
	@Lob
	@Column(name = "item_description")
	private String itemDescription;
	
	@Column(name="purchase_item_id")
	private Integer purchaseItemId;
	
	@Column(name="enter_qty")
	private Double enterQty;
	
	@Column(name="balance_qty")
	private Double balanceQty;
	
	@Column(name="qunatity")
	private Double qunatity;
	
	@Column(name="item_name")
	private String itemName;
	

	@Column(name="attachment_path_type")
	private String attachmantPathType;
	

	@Column(name="grn_no")
	private String grnNo;
	
	private Integer storeId;
	
	private String storeName;
	
	private Integer createdBy;
	
	private String createdUserName;
	
	private Integer itemId;
	
	private String uom;
	private String uomShortName;
	
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdDate;
	private Integer envItemId;
	@Enumerated(value = EnumType.STRING)
	private GRN_TYPE grnType;
	
	private String originalTotal;
	
	private Integer purchaseOrderId;
	
	private Integer voucherHeadNewId;
	
	private Float discount;
	private Float discountTotal;
	private Float gst;
	private Float gstTotal;
	private Float costTotal;
	private Float totalAmount;
	
	private Integer draft_journal_voucher_id;
	private Integer journal_voucher_id;
	private Integer draft_payment_voucher_id;
	
	public enum GRN_TYPE{
		PO,DIRECT_GRN;
	}




}
