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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="other_fee_template")
@Entity
public class OtherFeeTemplate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer otherFeeTemplateId;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date created_date;
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified_date;
	
	private String createdBy;
	
	private String modifiedBy;
	
	private Boolean active;
	
	private Integer schoolId;

	private Integer acYearId;

	private Integer programId;

	private Integer programSpecializationId;
	
	private String feetype;
	
	private String uniformNumber;
	
	private Integer fee_admission_category_id;
	private Integer currency_type_id;
	private Integer fee_template_id;
}
