package com.au.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

public class PaymentVoucherDto {
	
	private List<HashMap<String, Object>> vendor_id;
	private Integer payment_voucher_id;

	private Integer school_id;
	private String date;
	private Integer bank_id;
	private Double paying_now;
	private String cheque_dd_no;
	private String remarks;
	private String pay_to;
	private String expensense_lead;
	private Integer financial_year_id;
	private Integer voucher_head_new_id;
	private Integer fee_head_id;
	private Double credit_total;
	private Double debit_total;
	private Double credit;
	private String debit;
	private Integer nature_id;
	private Integer payment_status;

	private Integer dept_id;
	private Integer inter_institute_id;
	private Integer vendor_active;
	private Integer status;
	private String note;
	private String clearingDate;
	private Integer lockstatus;
	private Integer cancel_voucher;
	private String voucher_remarks;
	private Integer cancelled_by;
	private String cancelled_date;
	private Integer online;
	private Integer advanced_po_status;
	private Integer draft_payment_voucher_id;
	private String attachment_name;
	private String attachment_path;
	
	private Integer journal_voucher_id;
 	private Integer purchase_order_id;
 	private Integer voucher_no;
 	
 	private Integer approver_id;
 	private Integer approved_status;
 	private String approved_date;
 	
 	private String actual_date;
 	
 	private Integer voucher_head_id;

	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;

	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;

	private Boolean active;

	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	

	public PaymentVoucherDto() {
		super();
	}

	public List<HashMap<String, Object>> getVendor_id() {
		return vendor_id;
	}





	public void setVendor_id(List<HashMap<String, Object>> vendor_id) {
		this.vendor_id = vendor_id;
	}





	public Integer getPayment_voucher_id() {
		return payment_voucher_id;
	}

	public void setPayment_voucher_id(Integer payment_voucher_id) {
		this.payment_voucher_id = payment_voucher_id;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getBank_id() {
		return bank_id;
	}

	public void setBank_id(Integer bank_id) {
		this.bank_id = bank_id;
	}

	public Double getPaying_now() {
		return paying_now;
	}

	public void setPaying_now(Double paying_now) {
		this.paying_now = paying_now;
	}

	public String getCheque_dd_no() {
		return cheque_dd_no;
	}

	public void setCheque_dd_no(String cheque_dd_no) {
		this.cheque_dd_no = cheque_dd_no;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getPay_to() {
		return pay_to;
	}

	public void setPay_to(String pay_to) {
		this.pay_to = pay_to;
	}

	public String getExpensense_lead() {
		return expensense_lead;
	}

	public void setExpensense_lead(String expensense_lead) {
		this.expensense_lead = expensense_lead;
	}

	public Integer getFinancial_year_id() {
		return financial_year_id;
	}

	public void setFinancial_year_id(Integer financial_year_id) {
		this.financial_year_id = financial_year_id;
	}

	public Integer getVoucher_head_new_id() {
		return voucher_head_new_id;
	}

	public void setVoucher_head_new_id(Integer voucher_head_new_id) {
		this.voucher_head_new_id = voucher_head_new_id;
	}

	public Integer getFee_head_id() {
		return fee_head_id;
	}

	public void setFee_head_id(Integer fee_head_id) {
		this.fee_head_id = fee_head_id;
	}

	public Double getCredit_total() {
		return credit_total;
	}

	public void setCredit_total(Double credit_total) {
		this.credit_total = credit_total;
	}

	public Double getDebit_total() {
		return debit_total;
	}

	public void setDebit_total(Double debit_total) {
		this.debit_total = debit_total;
	}

	public Double getCredit() {
		return credit;
	}

	public void setCredit(Double credit) {
		this.credit = credit;
	}


	public String getDebit() {
		return debit;
	}

	public void setDebit(String debit) {
		this.debit = debit;
	}

	public Integer getNature_id() {
		return nature_id;
	}

	public void setNature_id(Integer nature_id) {
		this.nature_id = nature_id;
	}

	public Integer getPayment_status() {
		return payment_status;
	}

	public void setPayment_status(Integer payment_status) {
		this.payment_status = payment_status;
	}

	public Integer getDept_id() {
		return dept_id;
	}

	public void setDept_id(Integer dept_id) {
		this.dept_id = dept_id;
	}

	public Integer getInter_institute_id() {
		return inter_institute_id;
	}

	public void setInter_institute_id(Integer inter_institute_id) {
		this.inter_institute_id = inter_institute_id;
	}

	public Integer getVendor_active() {
		return vendor_active;
	}

	public void setVendor_active(Integer vendor_active) {
		this.vendor_active = vendor_active;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getClearingDate() {
		return clearingDate;
	}

	public void setClearingDate(String clearingDate) {
		this.clearingDate = clearingDate;
	}

	public Integer getLockstatus() {
		return lockstatus;
	}

	public void setLockstatus(Integer lockstatus) {
		this.lockstatus = lockstatus;
	}

	public Integer getCancel_voucher() {
		return cancel_voucher;
	}

	public void setCancel_voucher(Integer cancel_voucher) {
		this.cancel_voucher = cancel_voucher;
	}

	public String getVoucher_remarks() {
		return voucher_remarks;
	}

	public void setVoucher_remarks(String voucher_remarks) {
		this.voucher_remarks = voucher_remarks;
	}

	public Integer getCancelled_by() {
		return cancelled_by;
	}

	public void setCancelled_by(Integer cancelled_by) {
		this.cancelled_by = cancelled_by;
	}

	public String getCancelled_date() {
		return cancelled_date;
	}

	public void setCancelled_date(String cancelled_date) {
		this.cancelled_date = cancelled_date;
	}

	public Integer getOnline() {
		return online;
	}

	public void setOnline(Integer online) {
		this.online = online;
	}

	public Integer getAdvanced_po_status() {
		return advanced_po_status;
	}

	public void setAdvanced_po_status(Integer advanced_po_status) {
		this.advanced_po_status = advanced_po_status;
	}

	public String getAttachment_name() {
		return attachment_name;
	}

	public void setAttachment_name(String attachment_name) {
		this.attachment_name = attachment_name;
	}

	public String getAttachment_path() {
		return attachment_path;
	}

	public void setAttachment_path(String attachment_path) {
		this.attachment_path = attachment_path;
	}

	

	public Integer getDraft_payment_voucher_id() {
		return draft_payment_voucher_id;
	}

	public void setDraft_payment_voucher_id(Integer draft_payment_voucher_id) {
		this.draft_payment_voucher_id = draft_payment_voucher_id;
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



	public Integer getJournal_voucher_id() {
		return journal_voucher_id;
	}



	public void setJournal_voucher_id(Integer journal_voucher_id) {
		this.journal_voucher_id = journal_voucher_id;
	}



	public Integer getPurchase_order_id() {
		return purchase_order_id;
	}



	public void setPurchase_order_id(Integer purchase_order_id) {
		this.purchase_order_id = purchase_order_id;
	}

	public Integer getVoucher_no() {
		return voucher_no;
	}

	public void setVoucher_no(Integer voucher_no) {
		this.voucher_no = voucher_no;
	}

	public Integer getApprover_id() {
		return approver_id;
	}

	public void setApprover_id(Integer approver_id) {
		this.approver_id = approver_id;
	}

	public Integer getApproved_status() {
		return approved_status;
	}

	public void setApproved_status(Integer approved_status) {
		this.approved_status = approved_status;
	}

	public String getApproved_date() {
		return approved_date;
	}

	public void setApproved_date(String approved_date) {
		this.approved_date = approved_date;
	}

	public String getActual_date() {
		return actual_date;
	}

	public void setActual_date(String actual_date) {
		this.actual_date = actual_date;
	}

	public Integer getVoucher_head_id() {
		return voucher_head_id;
	}

	public void setVoucher_head_id(Integer voucher_head_id) {
		this.voucher_head_id = voucher_head_id;
	}
	
	


}
