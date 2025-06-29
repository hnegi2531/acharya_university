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
@Table(name="book_chapter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookChapter {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer book_chapter_id;
	
	private String book_chapter;
	private String book_title;
	private String authore;
	private String publisher;
	private String published_year;
	private String isbn_number;
	private String doi;
	private String attachment_path;
	private String attachment_name;
	private Integer emp_id;
	
	private String unit;
	
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
	private Double amount;
	private Integer credited_year;
	private Integer credited_month;
}
