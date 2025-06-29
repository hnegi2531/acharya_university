package com.au.dto;

import lombok.Data;

@Data
public class StudentIdCardBucketDto {
	
	private Integer studentId;
	private String remarks;
	private String receiptNo;
	private String receiptDate;
	private String validTill;
	private Integer currentYear;
	private Boolean active;
	private Integer studentIdCardHistoryId;

}
