package com.au.model;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "uniform_receipt")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UniformReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uniformReceiptId;

    @Column(name = "fc_year_id")
    private Long fcYearId;

    @Column(name = "student_id")
    private Long studentId;

    private Double amount;

    @Column(name = "issued_status")
    private Boolean issuedStatus;

    @Column(name = "cancel_status")
    private Boolean cancelStatus;

    @Column(name = "order_id")
    private String orderId;

    private String type;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "other_fee_template_id")
    private Integer otherFeeTemplateId;
    
    @Column(name = "uniform_receipt_no")
    private Integer uniformReceiptNo;

    @Column
    private String paydetails;

    @Column(name = "transaction_date")
    private String transactionDate;
    
    
    private Integer quantity;
	private Double cgst_input;
	private Double cgst_output;
	private Double sgst_input;
	private Double sgst_output;	
	private Double gst;
	private Integer env_item_id;
	private Integer year;
	private Integer sem;
	private Integer schoolId;
	private Integer bankImportTransactionId;
    
    
    
    @Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdDate;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modifiedDate;

	@Column(updatable = false)
	private Integer createdBy;
	private Integer modifiedBy;
	@Column(updatable = false)
	private String createdUsername;
	private String modifiedUsername;
    private Boolean active;
    
    private Integer paidYear;
    private Double total_amount;
    
    
}

