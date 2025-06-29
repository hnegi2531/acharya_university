package com.au.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="custom_tempate_reference_no_configuration")
public class CustomTemplateReferenceNumberConfiguration {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="custom_tempate_reference_no_configuration_id")
	private Integer customTemplateReferenceNumberConfigurationId;
	

	@Column(name="current_year")
	private Integer currentYear;
	
	@Column(name="counter")
	private Integer counter;
	
	
	private String categoryShortName;
}
