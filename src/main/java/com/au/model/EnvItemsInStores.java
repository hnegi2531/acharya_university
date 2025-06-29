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
@Table(name="env_items_in_stores")
public class EnvItemsInStores {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Integer env_item_id;
		private Integer item_id;
		private Integer ledger_id;
		private Integer total_available_in_stores;
		private Integer total_issued;
		private String  item_description;
		private Integer stock_type_id;   //it is stock_type_id from table of stores_stock
		private Integer measure_id;
		private String  make;
		private String item_serial_no;
		private Double opening_balance;
		
		
		
		private String item_assigment_name;
		private Double scrap=0d;
		private String scrap_uom;
		private String scrap_remarks;
		@Column(updatable = false)
		private String created_username;
		private String modified_username;

		@Column(name = "created_date",updatable = false)
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
		private boolean active;
		
		private String title_of_book;
		private String author;
		private String edition;
		private Integer yr_of_Publish;
		private String publisher_details;
		private Integer available_books;
		private String reference_code;
		private String isbn_code;
		
		public EnvItemsInStores() {
			super();
			
		}

		public Integer getEnv_item_id() {
			return env_item_id;
		}

		public void setEnv_item_id(Integer env_item_id) {
			this.env_item_id = env_item_id;
		}

		public Integer getItem_id() {
			return item_id;
		}

		public void setItem_id(Integer item_id) {
			this.item_id = item_id;
		}

		public Integer getLedger_id() {
			return ledger_id;
		}

		public void setLedger_id(Integer ledger_id) {
			this.ledger_id = ledger_id;
		}

		public Integer getTotal_available_in_stores() {
			return total_available_in_stores;
		}

		public void setTotal_available_in_stores(Integer total_available_in_stores) {
			this.total_available_in_stores = total_available_in_stores;
		}

		public Integer getTotal_issued() {
			return total_issued;
		}

		public void setTotal_issued(Integer total_issued) {
			this.total_issued = total_issued;
		}

		public String getItem_description() {
			return item_description;
		}

		public void setItem_description(String item_description) {
			this.item_description = item_description;
		}

		public Integer getStock_type_id() {
			return stock_type_id;
		}

		public void setStock_type_id(Integer stock_type_id) {
			this.stock_type_id = stock_type_id;
		}

		public Integer getMeasure_id() {
			return measure_id;
		}

		public void setMeasure_id(Integer measure_id) {
			this.measure_id = measure_id;
		}
		
		public String getMake() {
			return make;
		}

		public void setMake(String make) {
			this.make = make;
		}

		public String getItem_serial_no() {
			return item_serial_no;
		}

		public void setItem_serial_no(String item_serial_no) {
			this.item_serial_no = item_serial_no;
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

		public Double getOpening_balance() {
			return opening_balance;
		}

		public void setOpening_balance(Double opening_balance) {
			this.opening_balance = opening_balance;
		}

		public String getItem_assigment_name() {
			return item_assigment_name;
		}

		public void setItem_assigment_name(String item_assigment_name) {
			this.item_assigment_name = item_assigment_name;
		}

		public Double getScrap() {
			return scrap;
		}

		public void setScrap(Double scrap) {
			this.scrap = scrap;
		}

		public String getScrap_uom() {
			return scrap_uom;
		}

		public void setScrap_uom(String scrap_uom) {
			this.scrap_uom = scrap_uom;
		}

		public String getScrap_remarks() {
			return scrap_remarks;
		}

		public void setScrap_remarks(String scrap_remarks) {
			this.scrap_remarks = scrap_remarks;
		}

		public String getTitle_of_book() {
			return title_of_book;
		}

		public void setTitle_of_book(String title_of_book) {
			this.title_of_book = title_of_book;
		}

		public String getAuthor() {
			return author;
		}

		public void setAuthor(String author) {
			this.author = author;
		}

		public String getEdition() {
			return edition;
		}

		public void setEdition(String edition) {
			this.edition = edition;
		}

		public Integer getYr_of_Publish() {
			return yr_of_Publish;
		}

		public void setYr_of_Publish(Integer yr_of_Publish) {
			this.yr_of_Publish = yr_of_Publish;
		}

		public String getPublisher_details() {
			return publisher_details;
		}

		public void setPublisher_details(String publisher_details) {
			this.publisher_details = publisher_details;
		}

		public Integer getAvailable_books() {
			return available_books;
		}

		public void setAvailable_books(Integer available_books) {
			this.available_books = available_books;
		}

		public String getReference_code() {
			return reference_code;
		}

		public void setReference_code(String reference_code) {
			this.reference_code = reference_code;
		}

		public String getIsbn_code() {
			return isbn_code;
		}

		public void setIsbn_code(String isbn_code) {
			this.isbn_code = isbn_code;
		}
		
		
		
}
