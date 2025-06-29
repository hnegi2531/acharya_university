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

import lombok.Data;

@Data
@Entity
@Table(name = "custom_template")
public class CustomTemplate {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer customTemplateId;

	private Integer userId;

	private String userCode;

	private String usertype;

	private Integer categoryTypeId;

	private Integer categoryDetailId;

	private String categoryShortName;

	@Lob
	private String content;

	private String referenceNo;

	private Integer createdBy;

	private Boolean withLetterHead;

	private String templateType;
	
	private Integer school_id;

	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
}
