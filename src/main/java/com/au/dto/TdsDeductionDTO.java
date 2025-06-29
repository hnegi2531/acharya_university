package com.au.dto;


import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TdsDeductionDTO {

	private Long id;
	private String empCode;
	private String employeeName;

	private Float amount;

	private Integer createdBy;
	private Integer month;
	private Integer year;
	private String createdByName;
	private Date createdDate;

	public TdsDeductionDTO(Long id, String empCode, String employeeName, Float amount, Integer createdBy, Integer month,
			int year, String createdByName, Date createdDate) {
		this.id = id;
		this.empCode = empCode;
		this.employeeName = employeeName;
		this.amount = amount;
		this.createdBy = createdBy;
		this.month = month;
		this.year = year;
		this.createdByName = createdByName;
		this.createdDate = createdDate;
	}
}
