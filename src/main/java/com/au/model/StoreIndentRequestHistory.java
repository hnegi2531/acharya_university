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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "store_indent_request_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreIndentRequestHistory {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer store_indent_request_history_id;
	
	private Integer store_indent_request_id;
	
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
	
	private String approver1_remarks;
	private Integer approver1_status;
	private Integer approver2_status;
	private Integer approver1_id;
	private Integer approver2_id;
	private String issued_status;
	private String indent_ticket;
	private Integer requested_by;
	private String  requested_date;
	private Integer financial_year_id;
	private String description;
	private Integer env_item_id;
	private Integer item_id;
	private Integer emp_id;
	private Integer quantity;
	private String purpose; 
	private String remarks;
	private String stock_description;
}
