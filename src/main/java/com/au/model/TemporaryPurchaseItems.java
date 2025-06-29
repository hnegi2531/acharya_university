package com.au.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "temp_purchase_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TemporaryPurchaseItems {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer temporary_purchase_item_id;
	
	private Float rate;
	
	private Float quantity;
	
	private Float discount;
	
	private Float gst;
	
	private Float totalAmount;
	
	@Column(name="item_name")
	private String itemName;
	
	@Column(name="balance_quantity")
	private Float balanceQuantity;
	
	
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	
	@ManyToOne
	@JoinColumn(name = "temporary_purchase_order_id")
	@JsonBackReference
	private TemporaryPurchaseOrder temporaryPurchaseOrder;
	
	@ManyToOne
	@JoinColumn(name = "env_item_id")
	@JsonBackReference
	private EnvItemsInStores envItemsInStoresId;
	
	@Column(name="gst_total")
	private String gstTotal;
	@Column(name="discount_total")
	private String discountTotal;
	@Column(name="cost_total")
	private String costTotal;
	
	@Column(name="created_by",updatable = false)
	private Integer createdBy;
	@Column(name="modified_by")
	private Integer modifiedBy;
	
	@Column(name="created_username",updatable = false)
	private String createdUsername;
	@Column(name="modified_username")
	private String modifiedUsername;
	private boolean active=true;
}
