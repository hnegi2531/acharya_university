package com.au.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "reference_books")
public class ReferenceBooks {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer book_id;
	private String title_of_book;
	private String author;
	private String edition;
	private Integer yr_of_Publish;
	private String publisher_details;
	private Integer available_books;

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
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	private Boolean active;
	private Integer program_specialization_id; // course_branch_assignment_id
	private String reference_code;
	private String isbn_code;
	private Integer course_assignment_id;
	private Integer school_id;
	
	@Lob
	@Column(columnDefinition="LONGTEXT")
	private String eresource;

	public ReferenceBooks() {
		super();
	}
	
	public String getReference_code() {
		return reference_code;
	}

	public void setReference_code(String reference_code) {
		this.reference_code = reference_code;
	}

	public Integer getBook_id() {
		return book_id;
	}

	public void setBook_id(Integer book_id) {
		this.book_id = book_id;
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

	public Integer getProgram_specialization_id() {
		return program_specialization_id;
	}

	public void setProgram_specialization_id(Integer program_specialization_id) {
		this.program_specialization_id = program_specialization_id;
	}

	public String getIsbn_code() {
		return isbn_code;
	}

	public void setIsbn_code(String isbn_code) {
		this.isbn_code = isbn_code;
	}

	public Integer getCourse_assignment_id() {
		return course_assignment_id;
	}

	public void setCourse_assignment_id(Integer course_assignment_id) {
		this.course_assignment_id = course_assignment_id;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public String getEresource() {
		return eresource;
	}

	public void setEresource(String eresource) {
		this.eresource = eresource;
	}
	
	
	
}
