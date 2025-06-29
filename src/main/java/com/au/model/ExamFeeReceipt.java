package com.au.model;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import org.springframework.format.annotation.NumberFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "exam_fee_receipt")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamFeeReceipt {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer examFeeReceiptId;
	private Integer paidYear;
	@ManyToOne
	@JoinColumn(name = "studentId")
	private Student_Details student;
	@ManyToOne
	@JoinColumn(name = "financialYearId")
	private FinancialYear financialYear; 
	
	@ManyToOne
	@JoinColumn(name = "acYearId")
	private Academic_year acYear;
	
	@ManyToOne
	@JoinColumn(name = "voucherHeadNewId")
	private VoucherHeadNew voucherHeadNew;
	
	@NumberFormat(pattern = "#.###")
	private Double amount;
	
	@ManyToOne
	@JoinColumn(name = "schoolId")
	private Schools school;
	
	@ManyToOne
	@JoinColumn(name = "feeReceiptId")
	private FeeReceipt feeReceipt;
	
	private String remarks;
	private String receivedIn;	
	
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
	private String feeReceiptNo;
	
	

}
