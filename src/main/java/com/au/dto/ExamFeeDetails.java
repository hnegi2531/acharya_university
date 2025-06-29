package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamFeeDetails {
	
	private Integer sem;
	private Double amount;
	private String feeType;
	private Integer voucherHeadNewId;
	private Integer paidYear;

}
