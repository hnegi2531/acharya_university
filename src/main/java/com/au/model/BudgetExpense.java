package com.au.model;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;

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


import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "budget_expense")
public class BudgetExpense {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Integer budget_expense_id;
		private Integer school_id;
		@Temporal(TemporalType.DATE)
		@JsonFormat(pattern = "dd-MM-yyyy")
		private Date from_date;
		@Temporal(TemporalType.DATE)
		@JsonFormat(pattern = "dd-MM-yyyy")
		private Date to_date;
		private Integer financial_year_id;
		private String remarks;
		@Column(updatable = false)
		@Temporal(TemporalType.TIMESTAMP)
		@CreationTimestamp
		private Date created_date;

		@Temporal(TemporalType.TIMESTAMP)
		@UpdateTimestamp
		private Date modified_date;
		private Integer created_by;
		private Integer modified_by;
		private String created_username;
		private String modified_username;
		private Boolean active;
		
		public BudgetExpense() {
			super();
			
		}

		public Integer getBudget_expense_id() {
			return budget_expense_id;
		}

		public void setBudget_expense_id(Integer budget_expense_id) {
			this.budget_expense_id = budget_expense_id;
		}

		public Integer getSchool_id() {
			return school_id;
		}

		public void setSchool_id(Integer school_id) {
			this.school_id = school_id;
		}

		public Date getFrom_date() {
			return from_date;
		}

		public void setFrom_date(Date from_date) {
			this.from_date = from_date;
		}

		public Date getTo_date() {
			return to_date;
		}

		public void setTo_date(Date to_date) {
			this.to_date = to_date;
		}

		public Integer getFinancial_year_id() {
			return financial_year_id;
		}

		public void setFinancial_year_id(Integer financial_year_id) {
			this.financial_year_id = financial_year_id;
		}

		public String getRemarks() {
			return remarks;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
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

		public Boolean getActive() {
			return active;
		}

		public void setActive(Boolean active) {
			this.active = active;
		}
		
		
		
}
