package com.au.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

public class BankImportTransactionDto {

	private String transaction_no;
	private String cheque_dd_no;
	private Double amount;
	private String transaction_date;


	private Integer school_id;
	private Integer student_id;
	private Integer deposited_bank_id;
	
	
    private Integer start_row;
    private Integer end_row;
	
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
	
		private Integer voucher_head_new_id;



	private String transaction_type;
	
	public BankImportTransactionDto(String transaction_no,  String cheque_dd_no,Double amount,
			String transaction_date) {
		super();
		this.transaction_no = transaction_no;
		this.cheque_dd_no = cheque_dd_no;
		this.amount = amount;
		this.transaction_date = transaction_date;

	}
	public BankImportTransactionDto() {
		// TODO Auto-generated constructor stub
	}

	

	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
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

	public Integer getStudent_id() {
		return student_id;
	}
	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}
	public Integer getSchool_id() {
		return school_id;
	}
	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
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
	
	
	
	public Integer getVoucher_head_new_id() {
		return voucher_head_new_id;
	}
	public void setVoucher_head_new_id(Integer voucher_head_new_id) {
		this.voucher_head_new_id = voucher_head_new_id;
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
	
	public Integer getDeposited_bank_id() {
		return deposited_bank_id;
	}
	public void setDeposited_bank_id(Integer deposited_bank_id) {
		this.deposited_bank_id = deposited_bank_id;
	}

	public String getTransaction_type() {
		return transaction_type;
	}

	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}
	
	@Override
	public String toString() {
		return "BankImportTransactionDto [transaction_no=" + transaction_no + ", cheque_dd_no=" + cheque_dd_no
				+ ", amount=" + amount + ", transaction_date=" + transaction_date + ", school_id=" + school_id
				+ ", student_id=" + student_id + ", start_row=" + start_row + ", end_row=" + end_row + ", created_by="
				+ created_by + ", modified_by=" + modified_by + ", created_Date=" + created_Date + ", modified_Date="
				+ modified_Date + ", active=" + active + ", created_username=" + created_username
				+ ", modified_username=" + modified_username + ",transaction_type=" + transaction_type + ", file=" + file + "]";
	}



}
