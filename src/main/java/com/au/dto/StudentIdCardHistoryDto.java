package com.au.dto;


import lombok.Data;

@Data
public class StudentIdCardHistoryDto {
	
	private Integer studenIdCardHistoryId;
	private Integer studentId;
	private String remarks;
	private String receiptNo;
	private String receiptDate;
	private String validTill;
	private Integer currentYear;

}
