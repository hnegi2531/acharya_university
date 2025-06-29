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
@Table(name="bulk_fee_receipt")
public class BulkFeeReceipt {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bulk_fee_receipt_id;
	private Double amount_in_som;
	private Integer student_id;
	private Integer voucher_head_new_id;
	private String transaction_type;
	private Integer school_id;
	private Integer bulk_fee_receipt;
	private String received_in;
	private Integer financial_year_id;
	private String from_name;
	private Double amount;
	private String remarks;
	private Integer vendor_id;
	private Integer fee_receipt_id;
	
	
	@Column(name = "created_date", updatable = false) 
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;

	@Column(name = "created_by", updatable = false)
	private Integer created_by;
	private Integer modified_by;
	private Boolean active;
	@Column(name = "created_username", updatable = false)
	private String created_username;
	private String modified_username;
	private Integer bank_id;
	private Integer bankImportTransactionId;
	private String mobile;
	private String orderId;
	private String transferType;
	
	public BulkFeeReceipt() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getBulk_fee_receipt_id() {
		return bulk_fee_receipt_id;
	}

	public void setBulk_fee_receipt_id(Integer bulk_fee_receipt_id) {
		this.bulk_fee_receipt_id = bulk_fee_receipt_id;
	}

	public Double getAmount_in_som() {
		return amount_in_som;
	}

	public void setAmount_in_som(Double amount_in_som) {
		this.amount_in_som = amount_in_som;
	}

	public Integer getStudent_id() {
		return student_id;
	}

	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}

	public Integer getVoucher_head_new_id() {
		return voucher_head_new_id;
	}

	public void setVoucher_head_new_id(Integer voucher_head_new_id) {
		this.voucher_head_new_id = voucher_head_new_id;
	}

	

	public String getTransaction_type() {
		return transaction_type;
	}

	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public Integer getBulk_fee_receipt() {
		return bulk_fee_receipt;
	}

	public void setBulk_fee_receipt(Integer bulk_fee_receipt) {
		this.bulk_fee_receipt = bulk_fee_receipt;
	}

	public String getReceived_in() {
		return received_in;
	}

	public void setReceived_in(String received_in) {
		this.received_in = received_in;
	}

	public Integer getFinancial_year_id() {
		return financial_year_id;
	}

	public void setFinancial_year_id(Integer financial_year_id) {
		this.financial_year_id = financial_year_id;
	}

	public String getFrom_name() {
		return from_name;
	}

	public void setFrom_name(String from_name) {
		this.from_name = from_name;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getVendor_id() {
		return vendor_id;
	}

	public void setVendor_id(Integer vendor_id) {
		this.vendor_id = vendor_id;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public Date getModified_date() {
		return modified_date;
	}

	public void setModified_date(Date modified_date) {
		this.modified_date = modified_date;
	}

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Integer getModified_by() {
		return modified_by;
	}

	public void setModified_by(Integer modified_by) {
		this.modified_by = modified_by;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getCreated_username() {
		return created_username;
	}

	public void setCreated_username(String created_username) {
		this.created_username = created_username;
	}

	public String getModified_username() {
		return modified_username;
	}

	public void setModified_username(String modified_username) {
		this.modified_username = modified_username;
	}

	public Integer getFee_receipt_id() {
		return fee_receipt_id;
	}

	public void setFee_receipt_id(Integer fee_receipt_id) {
		this.fee_receipt_id = fee_receipt_id;
	}

	public Integer getBank_id() {
		return bank_id;
	}

	public void setBank_id(Integer bank_id) {
		this.bank_id = bank_id;
	}

	public Integer getBankImportTransactionId() {
		return bankImportTransactionId;
	}

	public void setBankImportTransactionId(Integer bankImportTransactionId) {
		this.bankImportTransactionId = bankImportTransactionId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTransferType() {
		return transferType;
	}

	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}
}
