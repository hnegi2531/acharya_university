package com.au.dto;

import lombok.Data;

@Data
public class HostelFeeReceiptVoucherHeadWiseDto {
	
	private Integer acYearId;
	private Integer hostelBedAssignmentId;
	private Integer schoolId;
	private Integer studentId;
	private Integer voucherHeadNewId;
	private Float totalAmount;
	private Float payingAmount;
	private Float balanceAmount;
	private String receivedFrom;
	private String cashier;
	private String remarks;
}
