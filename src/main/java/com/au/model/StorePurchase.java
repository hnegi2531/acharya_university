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
@Table(name = "store_purchase")
public class StorePurchase {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer store_purchase_id;
	
	private Integer emp_id;
	private String food_approver;
	private String travel_indent;
	private String bill_approver1;
	private String bill_approver2;
	
	
	@Column(updatable = false)
	private Integer created_by;
	
	private Integer modified_by;

	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;

	private Boolean active;
	
	@Column(updatable = false)
	private String created_username;
	
	private String modified_username;
	
	
	
	public StorePurchase() {
		super();
	}


	public Integer getStore_purchase_id() {
		return store_purchase_id;
	}


	public void setStore_purchase_id(Integer store_purchase_id) {
		this.store_purchase_id = store_purchase_id;
	}


	public Integer getEmp_id() {
		return emp_id;
	}


	public void setEmp_id(Integer emp_id) {
		this.emp_id = emp_id;
	}


	public String getFood_approver() {
		return food_approver;
	}


	public void setFood_approver(String food_approver) {
		this.food_approver = food_approver;
	}


	public String getTravel_indent() {
		return travel_indent;
	}


	public void setTravel_indent(String travel_indent) {
		this.travel_indent = travel_indent;
	}


	public String getBill_approver1() {
		return bill_approver1;
	}


	public void setBill_approver1(String bill_approver1) {
		this.bill_approver1 = bill_approver1;
	}


	public String getBill_approver2() {
		return bill_approver2;
	}


	public void setBill_approver2(String bill_approver2) {
		this.bill_approver2 = bill_approver2;
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

	
}
