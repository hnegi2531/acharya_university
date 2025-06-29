package com.au.model;

import java.util.Date;
import java.util.List;

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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Table(name = "store_indent_request")
public class StoreIndentRequest {



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer store_indent_request_id;
	
	private String indent_ticket;    //indentTicket
	private String stock_description;
	
	
	    private Integer quantity;
	    private String purpose;  //@NotBlank(message = "purpose should not be Empty OR Null")
	 
	private Integer requested_by;
	private String  requested_date;
	private Integer purchase_status;
	private String remarks;
	private Integer approver1_id;
	private String approver1_remarks;
	private String approver1_date;
	private Integer approver2_id;
	private String approver2_remarks;
	private String approver2_date;
	private Integer approver1_status;
	private Integer approver2_status;
	private Integer item_id;
	private String description;
	private String others;
	private Integer approver1_active;
	private Integer approver2_active;
	private Integer school_id;
	private Integer dept_id;
	private Integer tag_id;
	private Integer draft_po_status;
	private String grn_date;
	private Integer expense_head_id;
	private Integer financial_year_id;
	private Integer purchase_request;
	private String issued_status;
	private Integer ac_year_id;

	private Integer env_item_id;
	private Integer item_assignment_id;
	private Integer ledger_id;
	private Integer measure_id;
	private Integer emp_id;
	private Integer vendor_id;
	
	 private Integer issued_quantity;
	 private String attachment_name;
	 	private String attachment_path;
	 	
	 	private Boolean cancel_status;

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
	
	private Date issueDate;
	private Integer issuedBy;
	
	private Integer received_status;
	
	
	public StoreIndentRequest() {
		super();
	}
	
	public Integer getStore_indent_request_id() {
		return store_indent_request_id;
	}
	public void setStore_indent_request_id(Integer store_indent_request_id) {
		this.store_indent_request_id = store_indent_request_id;
	}
	public String getIndent_ticket() {
		return indent_ticket;
	}
	public void setIndent_ticket(String indent_ticket) {
		this.indent_ticket = indent_ticket;
	}
	public String getStock_description() {
		return stock_description;
	}
	public void setStock_description(String stock_description) {
		this.stock_description = stock_description;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public Integer getRequested_by() {
		return requested_by;
	}
	public void setRequested_by(Integer requested_by) {
		this.requested_by = requested_by;
	}
	public String getRequested_date() {
		return requested_date;
	}
	public void setRequested_date(String requested_date) {
		this.requested_date = requested_date;
	}
	public Integer getPurchase_status() {
		return purchase_status;
	}
	public void setPurchase_status(Integer purchase_status) {
		this.purchase_status = purchase_status;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Integer getApprover1_id() {
		return approver1_id;
	}
	public void setApprover1_id(Integer approver1_id) {
		this.approver1_id = approver1_id;
	}
	public String getApprover1_remarks() {
		return approver1_remarks;
	}
	public void setApprover1_remarks(String approver1_remarks) {
		this.approver1_remarks = approver1_remarks;
	}
	public String getApprover1_date() {
		return approver1_date;
	}
	public void setApprover1_date(String approver1_date) {
		this.approver1_date = approver1_date;
	}
	public Integer getApprover2_id() {
		return approver2_id;
	}
	public void setApprover2_id(Integer approver2_id) {
		this.approver2_id = approver2_id;
	}
	public String getApprover2_remarks() {
		return approver2_remarks;
	}
	public void setApprover2_remarks(String approver2_remarks) {
		this.approver2_remarks = approver2_remarks;
	}
	public String getApprover2_date() {
		return approver2_date;
	}
	public void setApprover2_date(String approver2_date) {
		this.approver2_date = approver2_date;
	}
	public Integer getApprover1_status() {
		return approver1_status;
	}
	public void setApprover1_status(Integer approver1_status) {
		this.approver1_status = approver1_status;
	}
	public Integer getApprover2_status() {
		return approver2_status;
	}
	public void setApprover2_status(Integer approver2_status) {
		this.approver2_status = approver2_status;
	}
	public Integer getItem_id() {
		return item_id;
	}
	public void setItem_id(Integer item_id) {
		this.item_id = item_id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOthers() {
		return others;
	}
	public void setOthers(String others) {
		this.others = others;
	}
	public Integer getApprover1_active() {
		return approver1_active;
	}
	public void setApprover1_active(Integer approver1_active) {
		this.approver1_active = approver1_active;
	}
	public Integer getApprover2_active() {
		return approver2_active;
	}
	public void setApprover2_active(Integer approver2_active) {
		this.approver2_active = approver2_active;
	}
	public Integer getSchool_id() {
		return school_id;
	}
	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}
	public Integer getDept_id() {
		return dept_id;
	}
	public void setDept_id(Integer dept_id) {
		this.dept_id = dept_id;
	}
	public Integer getTag_id() {
		return tag_id;
	}
	public void setTag_id(Integer tag_id) {
		this.tag_id = tag_id;
	}
	public Integer getDraft_po_status() {
		return draft_po_status;
	}
	public void setDraft_po_status(Integer draft_po_status) {
		this.draft_po_status = draft_po_status;
	}
	public String getGrn_date() {
		return grn_date;
	}
	public void setGrn_date(String grn_date) {
		this.grn_date = grn_date;
	}
	public Integer getExpense_head_id() {
		return expense_head_id;
	}
	public void setExpense_head_id(Integer expense_head_id) {
		this.expense_head_id = expense_head_id;
	}
	public Integer getFinancial_year_id() {
		return financial_year_id;
	}
	public void setFinancial_year_id(Integer financial_year_id) {
		this.financial_year_id = financial_year_id;
	}
	public Integer getPurchase_request() {
		return purchase_request;
	}
	public void setPurchase_request(Integer purchase_request) {
		this.purchase_request = purchase_request;
	}
	public String getIssued_status() {
		return issued_status;
	}
	public void setIssued_status(String issued_status) {
		this.issued_status = issued_status;
	}
	public Integer getAc_year_id() {
		return ac_year_id;
	}
	public void setAc_year_id(Integer ac_year_id) {
		this.ac_year_id = ac_year_id;
	}
	public Integer getEnv_item_id() {
		return env_item_id;
	}
	public void setEnv_item_id(Integer env_item_id) {
		this.env_item_id = env_item_id;
	}
	public Integer getItem_assignment_id() {
		return item_assignment_id;
	}
	public void setItem_assignment_id(Integer item_assignment_id) {
		this.item_assignment_id = item_assignment_id;
	}
	public Integer getLedger_id() {
		return ledger_id;
	}
	public void setLedger_id(Integer ledger_id) {
		this.ledger_id = ledger_id;
	}
	public Integer getMeasure_id() {
		return measure_id;
	}
	public void setMeasure_id(Integer measure_id) {
		this.measure_id = measure_id;
	}
	public Integer getEmp_id() {
		return emp_id;
	}
	public void setEmp_id(Integer emp_id) {
		this.emp_id = emp_id;
	}
	public Integer getVendor_id() {
		return vendor_id;
	}
	public void setVendor_id(Integer vendor_id) {
		this.vendor_id = vendor_id;
	}
	public Integer getIssued_quantity() {
		return issued_quantity;
	}
	public void setIssued_quantity(Integer issued_quantity) {
		this.issued_quantity = issued_quantity;
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
		return  attachment_path;
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

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Integer getIssuedBy() {
		return issuedBy;
	}

	public void setIssuedBy(Integer issuedBy) {
		this.issuedBy = issuedBy;
	}

	public Integer getReceived_status() {
		return received_status;
	}

	public void setReceived_status(Integer received_status) {
		this.received_status = received_status;
	}

	public Boolean getCancel_status() {
		return cancel_status;
	}

	public void setCancel_status(Boolean cancel_status) {
		this.cancel_status = cancel_status;
	}


	
	
	
	
}
