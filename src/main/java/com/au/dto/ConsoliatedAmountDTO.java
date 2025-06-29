package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsoliatedAmountDTO {

	private Integer month;

	private Integer year;

	private Float amount;

	private Integer empId;

	private String fromDate;

	private String toDate;

	private String remarks;

	private String subject;
}
