package com.au.model;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "add_on_fee")
public class AddOnFee {


	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer addOnFeeId;

	    private Integer addOnFeeReceiptId;
	    private String transactionType;
	    private Double amount;
	    private Integer studentId;
	    private Integer schoolId;
	    private Integer fcYearId;
	    private Integer paidYear;
	    private Boolean active;
	    private String transactionDate;
	    private String bankTransactionId;
	    private String remarks;
	    private String orderId;

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
	    
	    
	    
		public AddOnFee() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		
		public Integer getAddOnFeeId() {
			return addOnFeeId;
		}
		public void setAddOnFeeId(Integer addOnFeeId) {
			this.addOnFeeId = addOnFeeId;
		}
		public Integer getAddOnFeeReceiptId() {
			return addOnFeeReceiptId;
		}
		public void setAddOnFeeReceiptId(Integer addOnFeeReceiptId) {
			this.addOnFeeReceiptId = addOnFeeReceiptId;
		}
		public String getTransactionType() {
			return transactionType;
		}
		public void setTransactionType(String transactionType) {
			this.transactionType = transactionType;
		}
		public Double getAmount() {
			return amount;
		}
		public void setAmount(Double amount) {
			this.amount = amount;
		}
		public Integer getStudentId() {
			return studentId;
		}
		public void setStudentId(Integer studentId) {
			this.studentId = studentId;
		}
		public Integer getSchoolId() {
			return schoolId;
		}
		public void setSchoolId(Integer schoolId) {
			this.schoolId = schoolId;
		}
		public Integer getFcYearId() {
			return fcYearId;
		}
		public void setFcYearId(Integer fcYearId) {
			this.fcYearId = fcYearId;
		}
		public Integer getPaidYear() {
			return paidYear;
		}
		public void setPaidYear(Integer paidYear) {
			this.paidYear = paidYear;
		}
		public Boolean getActive() {
			return active;
		}
		public void setActive(Boolean active) {
			this.active = active;
		}
		public String getTransactionDate() {
			return transactionDate;
		}
		public void setTransactionDate(String transactionDate) {
			this.transactionDate = transactionDate;
		}
		public String getBankTransactionId() {
			return bankTransactionId;
		}
		public void setBankTransactionId(String bankTransactionId) {
			this.bankTransactionId = bankTransactionId;
		}
		public String getRemarks() {
			return remarks;
		}
		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}
		public String getOrderId() {
			return orderId;
		}
		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}
		public Date getCreatedDate() {
			return createdDate;
		}
		public void setCreatedDate(Date createdDate) {
			this.createdDate = createdDate;
		}
		public Date getModifiedDate() {
			return modifiedDate;
		}
		public void setModifiedDate(Date modifiedDate) {
			this.modifiedDate = modifiedDate;
		}
		public Integer getCreatedBy() {
			return createdBy;
		}
		public void setCreatedBy(Integer createdBy) {
			this.createdBy = createdBy;
		}
		public Integer getModifiedBy() {
			return modifiedBy;
		}
		public void setModifiedBy(Integer modifiedBy) {
			this.modifiedBy = modifiedBy;
		}
		public String getCreatedUsername() {
			return createdUsername;
		}
		public void setCreatedUsername(String createdUsername) {
			this.createdUsername = createdUsername;
		}
		public String getModifiedUsername() {
			return modifiedUsername;
		}
		public void setModifiedUsername(String modifiedUsername) {
			this.modifiedUsername = modifiedUsername;
		}

}
