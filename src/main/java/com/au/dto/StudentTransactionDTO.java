package com.au.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentTransactionDTO {

	private Integer studentId;
	private Integer schoolId;
	private String mobile;
	private Integer currentYear;
	
	private Integer currentSem;
		
	private Integer acYearId;
	
	private Float hostelDue;
	
	private Float totalDue;
	
	private Integer allowSem;
	
	private String partFeeDate;
	
	private FeeTemplateDTO  feeCma;
	
	private FeeTemplateDTO  lateFee;
	private FeeTemplateDTO  feeTemplate;


	private FeeTemplateDTO  uniformAndStationary;
	private List<HostelPayDTO>  hostelPay;

}
