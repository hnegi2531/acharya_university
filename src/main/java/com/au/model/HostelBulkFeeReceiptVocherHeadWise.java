package com.au.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.NumberFormat;

import lombok.Data;

/**
 * Author: Rupesh
 * Date: 18-01-2025
 * Description: Hostel Fee Receipt Breakup voucher head wise
 */
@Data
@Entity
public class HostelBulkFeeReceiptVocherHeadWise {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer hostelBulkFeeReciptVocherHeadWiseId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="voucherHeadNewId")
	private VoucherHeadNew voucherHead;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="feeReceiptId")
	private FeeReceipt feeReceipt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "studentId")
	private Student_Details student;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="acYearId")
	private Academic_year acYear;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="hostelBedAssignmentId")
	private HostelBedAssignment hostelBedAssign;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="financialYearId")
	private FinancialYear financialYear;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="schoolId")
	private Schools school;
	
	@NumberFormat(pattern = "#,##0.00")
    @Column(scale = 2)
	private Float totalAmount;
	
	@NumberFormat(pattern = "#,##0.00")
    @Column(scale = 2)
	private Float payingAmount;
	
	@NumberFormat(pattern = "#,##0.00")
    @Column(scale = 2)
	private Float balanceAmount;
	
	@Column(name = "created_date", updatable = false)
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
	
	
	
	private float roundToTwoDecimalPlaces(float value) {
        return Math.round(value * 100.0f) / 100.0f;  
    }

    
    @PrePersist
    @PreUpdate
    public void ensureRounding() {
        this.totalAmount = roundToTwoDecimalPlaces(this.totalAmount);
        this.payingAmount = roundToTwoDecimalPlaces(this.payingAmount);
        this.balanceAmount = roundToTwoDecimalPlaces(this.balanceAmount);
    }

}
