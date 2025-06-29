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
@Table(name = "items_creation")
public class ItemsCreation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer item_id;
	@Column(unique = true)
	private String item_names;
	private String item_short_name;
	private String item_type;

	@Column(name = "created_username", updatable = false)
	private String created_username;
	private String modified_username;

	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	@Column(name = "created_by", updatable = false)
	private Integer created_by;
	private Integer modified_by;
	private boolean active;
	private boolean library_book_status;
	private String  item_nature;
	private boolean is_accession=false;
	private Integer ledger_id;
	
	private Integer measure_id;
	
	private Integer voucher_head_new_id;

	public ItemsCreation() {
		super();

	}

	public Integer getItem_id() {
		return item_id;
	}

	public void setItem_id(Integer item_id) {
		this.item_id = item_id;
	}

	public String getItem_names() {
		return item_names;
	}

	public void setItem_names(String item_names) {
		this.item_names = item_names;
	}

	public String getItem_short_name() {
		return item_short_name;
	}

	public void setItem_short_name(String item_short_name) {
		this.item_short_name = item_short_name;
	}

	public String getItem_type() {
		return item_type;
	}

	public void setItem_type(String item_type) {
		this.item_type = item_type;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isLibrary_book_status() {
		return library_book_status;
	}

	public void setLibrary_book_status(boolean library_book_status) {
		this.library_book_status = library_book_status;
	}

	public String getItem_nature() {
		return item_nature;
	}

	public void setItem_nature(String item_nature) {
		this.item_nature = item_nature;
	}

	public boolean isIs_accession() {
		return is_accession;
	}

	public void setIs_accession(boolean is_accession) {
		this.is_accession = is_accession;
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

	public Integer getVoucher_head_new_id() {
		return voucher_head_new_id;
	}

	public void setVoucher_head_new_id(Integer voucher_head_new_id) {
		this.voucher_head_new_id = voucher_head_new_id;
	}
	

}
