package com.au.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="grn_number_configuration")
public class GrnNumberConfiguration {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long grnNumberConfigId;
	

	@Column(name="current_year")
	private Integer currentYear;
	
	@Column(name="counter")
	private Integer counter;
	 
}
