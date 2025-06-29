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
@Table(name="stock_number_configuration")
public class StockNumberConfiguration {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long stockNumberConfigId;
	

	@Column(name="current_year")
	private Integer currentYear;
	
	@Column(name="counter")
	private Integer counter;
}
