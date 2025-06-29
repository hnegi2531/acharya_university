package com.au.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "purchase_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer purchase_order_id;
	
	@Column(name = "vendor",columnDefinition = "varchar(255)")
	private String vendor;
	
	@Column(name = "institute",columnDefinition = "varchar(255)")
	private String institute;

	@Column(name = "store_name",columnDefinition = "varchar(255)")
	private String storeName;
	
	@Column(name = "vendor_id")
	private Integer vendorId;
	
	@Column(name = "institute_id")
	private Integer instituteId;
	
	
	@Lob
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "quotation_no",columnDefinition = "varchar(255)")
	private String quotationNo;
	
	@Column(name = "term_of_payments",columnDefinition = "varchar(255)")	
	private String termsOfPayment;
	
	@Column(name = "no_of_days",columnDefinition = "varchar(255)")	
	private String noOfDays;
	
	@Column(name = "account_payment_type",columnDefinition = "varchar(255)")	
	private String accountPaymentType;
	
	@Column(name = "request_type",columnDefinition = "varchar(255)")	
	private String requestType;
	
	
	@Lob
	@Column(name = "terms_and_conditions")	
	private String termsAndConditions;
	
	@Lob
	@Column(name = "annexure")
	private String annexure;
	
	@Column(name = "emp_id")
	private Integer empId;
	
	@Column(name = "purchase_approver",columnDefinition = "varchar(255)")	
    private String purchaseApprover;
	

	@Column(name = "purchase_approver_id")
	private Integer purchaseApproverId;
	
	private boolean active=true;
	
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	
	@Column(name = "approver_id")	
	private Integer approverId;
	

	@Column(name = "approver_status")
	private Integer approvedStatus;
	

	@Column(name = "approver_date")
	private Date approvedDate;
	
	private Integer month;
	
	private Integer year;
	
	@Column(name = "quotation_date")
	private Date quotationDate;
	
	@Column(name = "cancel_By_id")	
	private Integer cancelById;
	
	@Column(name = "po_reference_no")	
	private String poReferenceNo;
	
	@Column(name = "po_total_amount")
	private Double poTotalAmount;
	
	@Column(name = "temporary_purchase_order_id")
	private Integer temporaryPurchaseOrderId;
	
	@OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonManagedReference
	private List<PurchaseItems> purchaseItems= new ArrayList<>();
	
	private String purchaseOrderFilePath;
	
	@Column(name="created_by",updatable = false)
	private Integer createdBy;
	@Column(name="modified_by")
	private Integer modifiedBy;
	
	@Column(name="created_username",updatable = false)
	private String createdUsername;
	@Column(name="modified_username")
	private String modifiedUsername;
	
	private String destination;
	@Column(name = "other_reference")	
	private String otherReference;
	
	private Boolean grnCreationStatus;
	
	private Integer voucherHeadNewId;

	private String cancelComments;
	
	private Integer draft_payment_voucher_id;
	private Integer payment_voucher_id;

}
