package com.au.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BranchDueReport {
	
	private BigDecimal grandTotal;

	private BigDecimal totalSem1;

	private BigDecimal totalSem2;

	private BigDecimal totalSem3;

	private BigDecimal totalSem4;

	private BigDecimal totalSem5;

	private BigDecimal totalSem6;

	private BigDecimal totalSem7;

	private BigDecimal totalSem8;

	private BigDecimal totalSem9;

	private BigDecimal totalSem10;

	private BigDecimal totalSem11;

	private BigDecimal totalSem12;

	private BigDecimal totalHostelDue;
	
	private List<BranchWisedueReport> branchWisedueReports;

}
