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
public class SchoolWiseDueReport {

	private List<SchoolWiseDueReportList> schoolWiseDueReportLists;
	
	private BigDecimal grantDueTotal;
	private BigDecimal grantAddOnTotal;
	private BigDecimal grantHostelFeeTotal;
	private BigDecimal grantTotal;
	
}
