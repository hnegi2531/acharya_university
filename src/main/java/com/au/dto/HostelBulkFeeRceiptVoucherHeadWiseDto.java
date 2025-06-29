package com.au.dto;

import lombok.Data;

@Data
public class HostelBulkFeeRceiptVoucherHeadWiseDto {
	
	private Integer acYearId;
	private Integer hostelBedAssignmentId;
	private Integer schoolId;
	private Integer studentId;
	private Integer voucherHeadNewId;
	private Float totalAmount;
	private Float payingAmount;
	private Float balanceAmount;

}
