package com.au.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name="conferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Conferences {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer conferences_id;
	
	private Integer emp_id;
	private String conference_type;
	private String paper_type;
	private String conference_name;
	private String paper_title;
	private String from_date;
	private String to_date;
	private String organiser;
	private String place;
	private String presentation_type;
	private String attachment_paper_path;
	private String attachment_paper_name;
	private String attachment_cert_path;
	private String attachment_cert_name;
	
	
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
