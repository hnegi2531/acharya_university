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
@Table(name = "draft_payment_voucher")
public class DraftPaymentVoucher {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer draft_payment_voucher_id;
	
	private Integer payment_voucher_id;
	private String date;
	private Integer bank_id;
	private String cheque_dd_no;
	private String pay_to;
	private Integer purchase_ref_no;
	private Integer nature_type;
	
	private Integer expense_head_id;
	private Integer vendor_active;
	private String  debit;
	private String remarks;
	private Integer voucher_no;
	private Integer financial_year_id;
	private Integer school_id;
	private String reference_number;
	private String invoice_number;
	private Integer payment_mode;
	private Integer voucher_head_new_id;
	private Integer approved_status;
	private Integer verified_status;
	private String verified_date;
	private String approved_date;
	private Integer verifier_id;
	private Integer approver_id;
	private Integer dept_id;
	private Double  debit_total;
	private String po_reference;
	private Integer online;
	private Integer vendor_id;
	private Integer po_bill_id;
	
	private String attachment_name;
 	private String attachment_path;
 	
 	private String actual_date;
 	
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
	
	private Integer voucher_head_id;
	
	private Integer inter_school_id;
	private Integer inter_bank_id;
	private Integer jv_school_id;
	private Integer jv_financial_year_id;
	
	private String type;
	private Integer env_bill_details_id;
	
	public DraftPaymentVoucher() {
		super();
		
	}


	public Integer getDraft_payment_voucher_id() {
		return draft_payment_voucher_id;
	}


	public void setDraft_payment_voucher_id(Integer draft_payment_voucher_id) {
		this.draft_payment_voucher_id = draft_payment_voucher_id;
	}


	public Integer getPayment_voucher_id() {
		return payment_voucher_id;
	}


	public void setPayment_voucher_id(Integer payment_voucher_id) {
		this.payment_voucher_id = payment_voucher_id;
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


	public String getCheque_dd_no() {
		return cheque_dd_no;
	}


	public void setCheque_dd_no(String cheque_dd_no) {
		this.cheque_dd_no = cheque_dd_no;
	}


	public String getPay_to() {
		return pay_to;
	}


	public void setPay_to(String pay_to) {
		this.pay_to = pay_to;
	}


	public Integer getPurchase_ref_no() {
		return purchase_ref_no;
	}


	public void setPurchase_ref_no(Integer purchase_ref_no) {
		this.purchase_ref_no = purchase_ref_no;
	}


	public Integer getNature_type() {
		return nature_type;
	}


	public void setNature_type(Integer nature_type) {
		this.nature_type = nature_type;
	}


	public Integer getExpense_head_id() {
		return expense_head_id;
	}


	public void setExpense_head_id(Integer expense_head_id) {
		this.expense_head_id = expense_head_id;
	}


	public Integer getVendor_active() {
		return vendor_active;
	}


	public void setVendor_active(Integer vendor_active) {
		this.vendor_active = vendor_active;
	}

	public String getDebit() {
		return debit;
	}


	public void setDebit(String debit) {
		this.debit = debit;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public Integer getFinancial_year_id() {
		return financial_year_id;
	}


	public void setFinancial_year_id(Integer financial_year_id) {
		this.financial_year_id = financial_year_id;
	}


	public Integer getSchool_id() {
		return school_id;
	}


	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}


	public Integer getVoucher_no() {
		return voucher_no;
	}


	public void setVoucher_no(Integer voucher_no) {
		this.voucher_no = voucher_no;
	}


	public String getReference_number() {
		return reference_number;
	}


	public void setReference_number(String reference_number) {
		this.reference_number = reference_number;
	}


	public String getInvoice_number() {
		return invoice_number;
	}


	public void setInvoice_number(String invoice_number) {
		this.invoice_number = invoice_number;
	}


	public Integer getPayment_mode() {
		return payment_mode;
	}


	public void setPayment_mode(Integer payment_mode) {
		this.payment_mode = payment_mode;
	}


	public Integer getVoucher_head_new_id() {
		return voucher_head_new_id;
	}


	public void setVoucher_head_new_id(Integer voucher_head_new_id) {
		this.voucher_head_new_id = voucher_head_new_id;
	}


	public Integer getApproved_status() {
		return approved_status;
	}


	public void setApproved_status(Integer approved_status) {
		this.approved_status = approved_status;
	}


	public Integer getVerified_status() {
		return verified_status;
	}


	public void setVerified_status(Integer verified_status) {
		this.verified_status = verified_status;
	}


	public String getVerified_date() {
		return verified_date;
	}


	public void setVerified_date(String verified_date) {
		this.verified_date = verified_date;
	}


	public String getApproved_date() {
		return approved_date;
	}


	public void setApproved_date(String approved_date) {
		this.approved_date = approved_date;
	}


	public Integer getVerifier_id() {
		return verifier_id;
	}


	public void setVerifier_id(Integer verifier_id) {
		this.verifier_id = verifier_id;
	}


	public Integer getApprover_id() {
		return approver_id;
	}


	public void setApprover_id(Integer approver_id) {
		this.approver_id = approver_id;
	}


	public Integer getDept_id() {
		return dept_id;
	}


	public void setDept_id(Integer dept_id) {
		this.dept_id = dept_id;
	}


	public Double getDebit_total() {
		return debit_total;
	}


	public void setDebit_total(Double debit_total) {
		this.debit_total = debit_total;
	}


	public String getPo_reference() {
		return po_reference;
	}


	public void setPo_reference(String po_reference) {
		this.po_reference = po_reference;
	}


	public Integer getOnline() {
		return online;
	}


	public void setOnline(Integer online) {
		this.online = online;
	}


	public Integer getVendor_id() {
		return vendor_id;
	}


	public void setVendor_id(Integer vendor_id) {
		this.vendor_id = vendor_id;
	}


	public Integer getPo_bill_id() {
		return po_bill_id;
	}


	public void setPo_bill_id(Integer po_bill_id) {
		this.po_bill_id = po_bill_id;
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


	public String setAttachment_path(String attachment_path) {
		return this.attachment_path = attachment_path;
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


	public Integer getInter_school_id() {
		return inter_school_id;
	}


	public void setInter_school_id(Integer inter_school_id) {
		this.inter_school_id = inter_school_id;
	}


	public Integer getJv_school_id() {
		return jv_school_id;
	}


	public void setJv_school_id(Integer jv_school_id) {
		this.jv_school_id = jv_school_id;
	}


	public Integer getJv_financial_year_id() {
		return jv_financial_year_id;
	}


	public void setJv_financial_year_id(Integer jv_financial_year_id) {
		this.jv_financial_year_id = jv_financial_year_id;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public Integer getEnv_bill_details_id() {
		return env_bill_details_id;
	}


	public void setEnv_bill_details_id(Integer env_bill_details_id) {
		this.env_bill_details_id = env_bill_details_id;
	}


	public Integer getInter_bank_id() {
		return inter_bank_id;
	}


	public void setInter_bank_id(Integer inter_bank_id) {
		this.inter_bank_id = inter_bank_id;
	}
	
	

}
