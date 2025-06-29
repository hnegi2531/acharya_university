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
@Table(name ="student_payment_history")
public class StudentPaymentHistory {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Integer student_fee_payment_history_id;
		private Integer fee_template_id;
		private Integer student_id;
		private Integer voucher_head_new_id;
		private Double to_pay;
		private Double total_amount;
		private Double paid_amount;
		private Double balance_amount;
		private Integer paid_year;
		private String transcation_type;
		private String type;
		private String created_username;
		private String modified_username;
		@Column(updatable = false)
		@Temporal(TemporalType.TIMESTAMP)	
		@CreationTimestamp
		private Date created_date;

		@Temporal(TemporalType.TIMESTAMP)	
		@UpdateTimestamp
		private Date modified_date;
		private Integer created_by;
		private Integer modified_by;
		private Boolean active;
		private String remarks;
		private String fee_receipt;
		private Integer school_id;
		private Integer insert_column;
		private String dollar_value;
		private Integer financial_year_id;
		private Boolean year_back_status;
		
		private Integer fee_receipt_id;
		
		private Integer bankImportTransactionId;
		private Double inr_value;
		
		public StudentPaymentHistory() {
			super();
		
		}

		public Integer getStudent_fee_payment_history_id() {
			return student_fee_payment_history_id;
		}

		public void setStudent_fee_payment_history_id(Integer student_fee_payment_history_id) {
			this.student_fee_payment_history_id = student_fee_payment_history_id;
		}

		public Integer getFee_template_id() {
			return fee_template_id;
		}

		public void setFee_template_id(Integer fee_template_id) {
			this.fee_template_id = fee_template_id;
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

		public Double getTo_pay() {
			return to_pay;
		}

		public void setTo_pay(Double to_pay) {
			this.to_pay = to_pay;
		}

		public Double getTotal_amount() {
			return total_amount;
		}

		public void setTotal_amount(Double total_amount) {
			this.total_amount = total_amount;
		}

		public Double getPaid_amount() {
			return paid_amount;
		}

		public void setPaid_amount(Double paid_amount) {
			this.paid_amount = paid_amount;
		}

		public Double getBalance_amount() {
			return balance_amount;
		}

		public void setBalance_amount(Double balance_amount) {
			this.balance_amount = balance_amount;
		}

		public Integer getPaid_year() {
			return paid_year;
		}

		public void setPaid_year(Integer paid_year) {
			this.paid_year = paid_year;
		}

		public String getTranscation_type() {
			return transcation_type;
		}

		public void setTranscation_type(String transcation_type) {
			this.transcation_type = transcation_type;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
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

		public String getRemarks() {
			return remarks;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}

		public String getFee_receipt() {
			return fee_receipt;
		}

		public void setFee_receipt(String fee_receipt1) {
			this.fee_receipt = fee_receipt1;
		}

		public Integer getSchool_id() {
			return school_id;
		}

		public void setSchool_id(Integer school_id) {
			this.school_id = school_id;
		}

		public Integer getInsert_column() {
			return insert_column;
		}

		public void setInsert_column(Integer insert_column) {
			this.insert_column = insert_column;
		}

		public String getDollar_value() {
			return dollar_value;
		}

		public void setDollar_value(String dollar_value) {
			this.dollar_value = dollar_value;
		}

		public Integer getFinancial_year_id() {
			return financial_year_id;
		}

		public void setFinancial_year_id(Integer financial_year_id) {
			this.financial_year_id = financial_year_id;
		}

		public Boolean getYear_back_status() {
			return year_back_status;
		}

		public void setYear_back_status(Boolean year_back_status) {
			this.year_back_status = year_back_status;
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

		public Integer getBankImportTransactionId() {
			return bankImportTransactionId;
		}

		public void setBankImportTransactionId(Integer bankImportTransactionId) {
			this.bankImportTransactionId = bankImportTransactionId;
		}

		public Double getInr_value() {
			return inr_value;
		}

		public void setInr_value(Double inr_value) {
			this.inr_value = inr_value;
		}
		
		
		
}
