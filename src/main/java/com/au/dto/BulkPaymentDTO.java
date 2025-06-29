package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BulkPaymentDTO {

	private Integer studentId;
	private Integer schoolId;
	private Integer acYearId;
	private Integer currentYear;
	private Integer currentSem;
	private String name;
	private String email;
	private String mobile;
	private String feehead;
	private Integer voucherHeadId;
	private Float amount;
	private String remarks;
	private Integer feePaymentWindowId;
	

}
