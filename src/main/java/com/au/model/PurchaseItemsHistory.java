package com.au.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;


@Entity
@Table(name="purchase_items_history")
@Data
public class PurchaseItemsHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="purchase_items_history_id")
	private Integer purchaseItemsHistoryId;
	@Column(name="purchase_item_id")
	private Integer purchaseItemId;
	
	private Float rate;
	
	private Float quantity;
	
	@Column(name="balance_quantity")
	private Float balanceQuantity;
	
	private Float discount;
	
	private Float gst;
	
	private Float totalAmount;
	
	@Column(name="po_reference_no")
	private String poReferenceNo;
	
	@Column(name="item_name")
	private String itemName;
	
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	@Column(name="created_date")
	private Date createdDate;
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	@Column(name="modified_date")
	private Date modifiedDate;
	
	@Column(name="created_by",updatable = false)
	private Integer createdBy;
	@Column(name="modified_by")
	private Integer modifiedBy;
	
	@Column(name="created_username",updatable = false)
	private String createdUsername;
	@Column(name="modified_username")
	private String modifiedUsername;
	
	@ManyToOne
	@JoinColumn(name = "purchase_order_id")
	private PurchaseOrder purchaseOrder;
	
	@ManyToOne
	@JoinColumn(name = "env_item_id")
	private EnvItemsInStores envItemsInStoresId; 
	
	private Boolean active;
	
	@Column(name="gst_total")
    private String gstTotal;
    @Column(name="discount_total")
    private String discountTotal;
    @Column(name="cost_total")
    private String costTotal;

}
