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
@Table(name = "item_assignment")
public class ItemAssignment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer item_assignment_id;
	
	private Integer ledger_id;
	private Integer item_creation_id;
	private String item_description;
	private String make;
	private Integer measure_id;
	private Double opening_balance;
	
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
	
	
	public ItemAssignment() {
		super();
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


	public Integer getItem_creation_id() {
		return item_creation_id;
	}


	public void setItem_creation_id(Integer item_creation_id) {
		this.item_creation_id = item_creation_id;
	}


	public String getItem_description() {
		return item_description;
	}


	public void setItem_description(String item_description) {
		this.item_description = item_description;
	}


    public String getMake() {
		return make;
	}


	public void setMake(String make) {
		this.make = make;
	}


	public Integer getMeasure_id() {
		return measure_id;
	}


	public void setMeasure_id(Integer measure_id) {
		this.measure_id = measure_id;
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


	public Double getOpening_balance() {
		return opening_balance;
	}


	public void setOpening_balance(Double opening_balance) {
		this.opening_balance = opening_balance;
	}
	
	
	
}

