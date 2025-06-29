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
@Table(name = "dd_details")
public class DdDetails {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Integer dd_id;
		private String  dd_number;
		private  String dd_date;
		private String bank_name;
		private Double dd_amount;
		private Integer deposited_into;//bank id no
		private String remarks;
		private Integer student_id;
		private Integer school_id;
		private Integer fee_receipt;//fee_receipt no
		private Integer financial_year_id;
		private String receipt_type;
		private Boolean cleared_status;
		private String cleared_date;
		private String cleared_remarks;
		private Double receipt_amount;
		
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
		private String created_username;
		private String modified_username;
		
		public DdDetails() {
			super();
			
		}

		public Integer getDd_id() {
			return dd_id;
		}

		public void setDd_id(Integer dd_id) {
			this.dd_id = dd_id;
		}

		public String getDd_number() {
			return dd_number;
		}

		public void setDd_number(String dd_number) {
			this.dd_number = dd_number;
		}

		public String getDd_date() {
			return dd_date;
		}

		public void setDd_date(String dd_date) {
			this.dd_date = dd_date;
		}

		public String getBank_name() {
			return bank_name;
		}

		public void setBank_name(String bank_name) {
			this.bank_name = bank_name;
		}

		public Double getDd_amount() {
			return dd_amount;
		}

		public void setDd_amount(Double dd_amount) {
			this.dd_amount = dd_amount;
		}

		public Integer getDeposited_into() {
			return deposited_into;
		}

		public void setDeposited_into(Integer deposited_into) {
			this.deposited_into = deposited_into;
		}

		public String getRemarks() {
			return remarks;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
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

		public Integer getFee_receipt() {
			return fee_receipt;
		}

		public void setFee_receipt(Integer fee_receipt) {
			this.fee_receipt = fee_receipt;
		}

		public Integer getFinancial_year_id() {
			return financial_year_id;
		}

		public void setFinancial_year_id(Integer financial_year_id) {
			this.financial_year_id = financial_year_id;
		}

		public String getReceipt_type() {
			return receipt_type;
		}

		public void setReceipt_type(String receipt_type) {
			this.receipt_type = receipt_type;
		}

		public Boolean getCleared_status() {
			return cleared_status;
		}

		public void setCleared_status(Boolean cleared_status) {
			this.cleared_status = cleared_status;
		}

		public String getCleared_date() {
			return cleared_date;
		}

		public void setCleared_date(String cleared_date) {
			this.cleared_date = cleared_date;
		}

		public String getCleared_remarks() {
			return cleared_remarks;
		}

		public void setCleared_remarks(String cleared_remarks) {
			this.cleared_remarks = cleared_remarks;
		}

		public Double getReceipt_amount() {
			return receipt_amount;
		}

		public void setReceipt_amount(Double receipt_amount) {
			this.receipt_amount = receipt_amount;
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

		public Boolean isActive() {
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
		
		
		
		
}
