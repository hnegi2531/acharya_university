package com.au.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "add_on_fee_voucher_head_wise")
public class AddOnFeeVoucherHeadWise {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer addOnFeeVoucherHeadWiseId;

	@Column(name = "student_id")
	private Integer studentId;

	@Column(name = "voucher_head_new_id")
	private Integer voucherHeadNewId;

	@Column(name = "voucher_head_total_amt")
	private Double voucherHeadTotalAmt;

	@Column(name = "total_paying_now")
	private Double totalPayingNow;

	@Column(name = "paying_now")
	private Double payingNow;

	@Column(name = "add_on_receipt_id")
	private Integer addOnReceiptId;

	@Column(name = "paid_year")
	private Integer paidYear;

	@Column(name = "active")
	private Boolean active;

	@Column(name = "fc_year_id")
	private Integer fcYearId;

	@Column(name = "school_id")
	private Integer schoolId;

	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdDate;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modifiedDate;

	@Column(name = "created_by", updatable = false)
	private Integer createdBy;

	@Column(name = "modified_by")
	private Integer modifiedBy;

	@Column(name = "created_username", updatable = false)
	private String createdUsername;

	@Column(name = "modified_username")
	private String modifiedUsername;

	public AddOnFeeVoucherHeadWise() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getAddOnFeeVoucherHeadWiseId() {
		return addOnFeeVoucherHeadWiseId;
	}

	public void setAddOnFeeVoucherHeadWiseId(Integer addOnFeeVoucherHeadWiseId) {
		this.addOnFeeVoucherHeadWiseId = addOnFeeVoucherHeadWiseId;
	}

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public Integer getVoucherHeadNewId() {
		return voucherHeadNewId;
	}

	public void setVoucherHeadNewId(Integer voucherHeadNewId) {
		this.voucherHeadNewId = voucherHeadNewId;
	}

	public Double getVoucherHeadTotalAmt() {
		return voucherHeadTotalAmt;
	}

	public void setVoucherHeadTotalAmt(Double voucherHeadTotalAmt) {
		this.voucherHeadTotalAmt = voucherHeadTotalAmt;
	}

	public Double getTotalPayingNow() {
		return totalPayingNow;
	}

	public void setTotalPayingNow(Double totalPayingNow) {
		this.totalPayingNow = totalPayingNow;
	}

	public Double getPayingNow() {
		return payingNow;
	}

	public void setPayingNow(Double payingNow) {
		this.payingNow = payingNow;
	}

	public Integer getAddOnReceiptId() {
		return addOnReceiptId;
	}

	public void setAddOnReceiptId(Integer addOnReceiptId) {
		this.addOnReceiptId = addOnReceiptId;
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

	public Integer getFcYearId() {
		return fcYearId;
	}

	public void setFcYearId(Integer fcYearId) {
		this.fcYearId = fcYearId;
	}

	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
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
