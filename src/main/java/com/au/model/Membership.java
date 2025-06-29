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
@Table(name="membership")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Membership {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer membership_id;
	
	private String membership_type;
	private String professional_body;
	private String year;
	private String nature_of_membership;
	private String priority;
	private String member_id;
	private String attachment_path;
	private String attachment_name;
	private Integer emp_id;
	private String citation;

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
