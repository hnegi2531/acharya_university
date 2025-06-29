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
@Table(name = "administration_email_ids")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdministrationEmailIds {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer administration_email_id;
	
	private Integer school_id;
	private String email;
	private Boolean recruit_purpose;
	private Boolean releave_purpose;
	private Boolean frro;
	private Boolean tenure_expired;
	private Boolean email_with_salary;

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
}
