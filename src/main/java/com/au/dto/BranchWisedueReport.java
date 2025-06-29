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
public class BranchWisedueReport {

	private Integer programId;
	private Integer programSpecializationId;
	private String program;
	private String programSpecialization;
	private BigDecimal sem1;
	private BigDecimal sem2;

	private BigDecimal sem3;
	private BigDecimal sem4;
	private BigDecimal sem5;
	private BigDecimal sem6;
	private BigDecimal sem7;
	private BigDecimal sem8;
	private BigDecimal sem9;

	private BigDecimal sem10;

	private BigDecimal sem11;

	private BigDecimal sem12;
	
	private BigDecimal total;
	
	private Integer numberOfSemester;

	private BigDecimal hostelDue;

}
