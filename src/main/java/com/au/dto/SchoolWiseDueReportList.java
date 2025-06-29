package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SchoolWiseDueReportList {

	private Integer schoolId;
	
	private String schoolShortName;
	private String schoolName;
	
	private BigDecimal collegeDue;
	private BigDecimal AddOn;
	private BigDecimal hostelFee;
	
	private BigDecimal total;
	

}
