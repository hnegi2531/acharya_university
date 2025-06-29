package com.au.dto;



import lombok.Data;

@Data
public class EmployeeIdCardHistoryDto {
	
	
	private Integer empId;
	private Boolean active;
	private String remarks;
	private String receiptDate;
	private String receiptNo;

}
