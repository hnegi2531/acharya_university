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
@Table(name = "tally_receipt")
public class TallyReceipt {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Integer tally_receipt_id;
		private Integer student_id;
		private String student_name;//first name plus last name concatenation
		private String auid;
		private String usn;
		private String particulars;
		private Double total;
		private String fee_receipt;//fee receipt number
		private String transaction_type;
		private String school_name;
		private String transaction_no;
		private String deposited_bank;
		private String dd_no;
		private String dd_bank_name;
		private String financial_year;
		private Double total_amount;
		private String received_from;
		private String received_type;
		private String received_in;
		private String remarks;
		private String transaction_date;
		private String bank_institute;
		private Integer vendor_id;
		@Column(updatable = false)
		private String created_username;
		private String modified_username;
		@Column(updatable = false)
		@Temporal(TemporalType.TIMESTAMP)	
		@CreationTimestamp
		private Date created_date;

		@Temporal(TemporalType.TIMESTAMP)	
		@UpdateTimestamp
		private Date modified_date;
		@Column(updatable = false)
		private Integer created_by;
		private Integer modified_by;
		private Boolean active;
		private Integer fee_receipt_id;

		
		public TallyReceipt() {
			super();
			
		}


		public Integer getTally_receipt_id() {
			return tally_receipt_id;
		}


		public void setTally_receipt_id(Integer tally_receipt_id) {
			this.tally_receipt_id = tally_receipt_id;
		}


		public Integer getStudent_id() {
			return student_id;
		}


		public void setStudent_id(Integer student_id) {
			this.student_id = student_id;
		}


		public String getStudent_name() {
			return student_name;
		}


		public void setStudent_name(String student_name) {
			this.student_name = student_name;
		}


		public String getAuid() {
			return auid;
		}


		public void setAuid(String auid) {
			this.auid = auid;
		}


		public String getUsn() {
			return usn;
		}


		public void setUsn(String usn) {
			this.usn = usn;
		}


		public String getParticulars() {
			return particulars;
		}


		public void setParticulars(String particulars) {
			this.particulars = particulars;
		}


		public Double getTotal() {
			return total;
		}


		public void setTotal(Double total) {
			this.total = total;
		}



		public String getFee_receipt() {
			return fee_receipt;
		}


		public void setFee_receipt(String fee_receipt) {
			this.fee_receipt = fee_receipt;
		}


		public Integer getFee_receipt_id() {
			return fee_receipt_id;
		}


		public void setFee_receipt_id(Integer fee_receipt_id) {
			this.fee_receipt_id = fee_receipt_id;
		}


		public String getTransaction_type() {
			return transaction_type;
		}


		public void setTransaction_type(String transaction_type) {
			this.transaction_type = transaction_type;
		}


		public String getSchool_name() {
			return school_name;
		}


		public void setSchool_name(String school_name) {
			this.school_name = school_name;
		}


		public String getTransaction_no() {
			return transaction_no;
		}


		public void setTransaction_no(String transaction_no) {
			this.transaction_no = transaction_no;
		}


		public String getDeposited_bank() {
			return deposited_bank;
		}


		public void setDeposited_bank(String deposited_bank) {
			this.deposited_bank = deposited_bank;
		}


		public String getDd_no() {
			return dd_no;
		}


		public void setDd_no(String dd_no) {
			this.dd_no = dd_no;
		}


		public String getDd_bank_name() {
			return dd_bank_name;
		}


		public void setDd_bank_name(String dd_bank_name) {
			this.dd_bank_name = dd_bank_name;
		}


		public String getFinancial_year() {
			return financial_year;
		}


		public void setFinancial_year(String financial_year) {
			this.financial_year = financial_year;
		}


		public Double getTotal_amount() {
			return total_amount;
		}


		public void setTotal_amount(Double total_amount) {
			this.total_amount = total_amount;
		}


		public String getReceived_from() {
			return received_from;
		}


		public void setReceived_from(String received_from) {
			this.received_from = received_from;
		}


		public String getReceived_type() {
			return received_type;
		}


		public void setReceived_type(String received_type) {
			this.received_type = received_type;
		}


		public String getReceived_in() {
			return received_in;
		}


		public void setReceived_in(String received_in) {
			this.received_in = received_in;
		}


		public String getRemarks() {
			return remarks;
		}


		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}


		public String getTransaction_date() {
			return transaction_date;
		}


		public void setTransaction_date(String transaction_date) {
			this.transaction_date = transaction_date;
		}


		public String getBank_institute() {
			return bank_institute;
		}


		public void setBank_institute(String bank_institute) {
			this.bank_institute = bank_institute;
		}


		public Integer getVendor_id() {
			return vendor_id;
		}


		public void setVendor_id(Integer vendor_id) {
			this.vendor_id = vendor_id;
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
		
		
		
		

}
