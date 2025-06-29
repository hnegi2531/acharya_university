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
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "bank_import_transaction")
public class BankImportTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bank_import_transaction_id;
	
	private String transaction_date;
	private String transaction_no;
	private String cheque_dd_no;
	private Integer deposited_bank_id;
	private String receipt_no;
	private Integer fc_year_id;
	private Integer student_id;
    private Double amount;
    private Integer start_row;
    private Integer end_row;
    private Integer school_id;
    private String dollor;
    private Double dollor_rate;
    private String transaction_remarks;
    private Double paid;
    private Double balance;
    private Double bank_usd_amt;
    private Double bank_inr_amt;
    private String settlement_id;
    private String settlement_utr;
    private String auid;
    private String order_id;
    private String total_usd;
    private String exachange_rate;
    
    @Column(updatable = false)
	private Integer created_by;
	
	private Integer modified_by;

	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_Date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_Date;
	
	private Boolean active;
	
	@Column(updatable = false)
	private String created_username;
	
	private String modified_username;
	
	@Transient
	private MultipartFile file;

	private String receiptStatus;

	private Integer voucher_head_new_id;

	private String transactionType;
	
	
	public BankImportTransaction(String transaction_date, String transaction_no, String cheque_dd_no,
			String transaction_remarks) {
		super();
		this.transaction_date = transaction_date;
		this.transaction_no = transaction_no;
		this.cheque_dd_no = cheque_dd_no;
		this.transaction_remarks = transaction_remarks;
	}


	public BankImportTransaction() {
	   super();
		
	}


	public Integer getBank_import_transaction_id() {
		return bank_import_transaction_id;
	}


	public void setBank_import_transaction_id(Integer bank_import_transaction_id) {
		this.bank_import_transaction_id = bank_import_transaction_id;
	}


	public String getTransaction_date() {
		return transaction_date;
	}


	public void setTransaction_date(String transaction_date) {
		this.transaction_date = transaction_date;
	}


	public String getTransaction_no() {
		return transaction_no;
	}


	public void setTransaction_no(String transaction_no) {
		this.transaction_no = transaction_no;
	}


	public String getCheque_dd_no() {
		return cheque_dd_no;
	}


	public void setCheque_dd_no(String cheque_dd_no) {
		this.cheque_dd_no = cheque_dd_no;
	}


	public Integer getDeposited_bank_id() {
		return deposited_bank_id;
	}


	public void setDeposited_bank_id(Integer deposited_bank_id) {
		this.deposited_bank_id = deposited_bank_id;
	}


	public String getReceipt_no() {
		return receipt_no;
	}


	public void setReceipt_no(String receipt_no) {
		this.receipt_no = receipt_no;
	}


	public Integer getFc_year_id() {
		return fc_year_id;
	}


	public void setFc_year_id(Integer fc_year_id) {
		this.fc_year_id = fc_year_id;
	}


	public Integer getStudent_id() {
		return student_id;
	}


	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}


	public Double getAmount() {
		return amount;
	}


	public void setAmount(Double amount) {
		this.amount = amount;
	}


	public Integer getStart_row() {
		return start_row;
	}


	public void setStart_row(Integer start_row) {
		this.start_row = start_row;
	}


	public Integer getEnd_row() {
		return end_row;
	}


	public void setEnd_row(Integer end_row) {
		this.end_row = end_row;
	}


	public Integer getSchool_id() {
		return school_id;
	}


	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}


	public String getDollor() {
		return dollor;
	}


	public void setDollor(String dollor) {
		this.dollor = dollor;
	}


	public Double getDollor_rate() {
		return dollor_rate;
	}


	public void setDollor_rate(Double dollor_rate) {
		this.dollor_rate = dollor_rate;
	}


	public String getTransaction_remarks() {
		return transaction_remarks;
	}


	public void setTransaction_remarks(String transaction_remarks) {
		this.transaction_remarks = transaction_remarks;
	}


	public Double getPaid() {
		return paid;
	}


	public void setPaid(Double paid) {
		this.paid = paid;
	}


	public Double getBalance() {
		return balance;
	}


	public void setBalance(Double balance) {
		this.balance = balance;
	}


	public Double getBank_usd_amt() {
		return bank_usd_amt;
	}


	public void setBank_usd_amt(Double bank_usd_amt) {
		this.bank_usd_amt = bank_usd_amt;
	}


	public Double getBank_inr_amt() {
		return bank_inr_amt;
	}


	public void setBank_inr_amt(Double bank_inr_amt) {
		this.bank_inr_amt = bank_inr_amt;
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


	public Date getCreated_Date() {
		return created_Date;
	}


	public void setCreated_Date(Date created_Date) {
		this.created_Date = created_Date;
	}


	public Date getModified_Date() {
		return modified_Date;
	}


	public void setModified_Date(Date modified_Date) {
		this.modified_Date = modified_Date;
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


	public MultipartFile getFile() {
		return file;
	}


	public void setFile(MultipartFile file) {
		this.file = file;
	}


	public String readLine() {
		
		return readLine();
	}


	public Integer getVoucher_head_new_id() {
		return voucher_head_new_id;
	}


	public void setVoucher_head_new_id(Integer voucher_head_new_id) {
		this.voucher_head_new_id = voucher_head_new_id;
	}


	public String getTotal_usd() {
		return total_usd;
	}


	public void setTotal_usd(String total_usd) {
		this.total_usd = total_usd;
	}


	public String getExachange_rate() {
		return exachange_rate;
	}


	public void setExachange_rate(String exachange_rate) {
		this.exachange_rate = exachange_rate;
	}


	public String getSettlement_id() {
		return settlement_id;
	}


	public void setSettlement_id(String settlement_id) {
		this.settlement_id = settlement_id;
	}


	public String getSettlement_utr() {
		return settlement_utr;
	}


	public void setSettlement_utr(String settlement_utr) {
		this.settlement_utr = settlement_utr;
	}


	public String getAuid() {
		return auid;
	}


	public void setAuid(String auid) {
		this.auid = auid;
	}


	public String getOrder_id() {
		return order_id;
	}


	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getReceiptStatus() {
		return receiptStatus;
	}

	public void setReceiptStatus(String receiptStatus) {
		this.receiptStatus = receiptStatus;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
}
