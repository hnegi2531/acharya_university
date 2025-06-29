package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsoliatedPayHistoryDTO {

	private Integer consoliatedPayHistoryId;

	private Integer empId;

	private Float remainingAmount;

	private Integer month;

	private Integer year;

	private Float totalAmount;

	private Float payingAmount;

	private Float tds;

	private Float netPay;

	private String employeeType;

	private String jobType;

	private String institute;

	private String department;

	private String designation;

	private String fromDate;

	private String toDate;

	private Integer createdBy;

	private Float splPay;

	private Float transportAmount;
	
	private String accountNo;
	
	private String bank;
	
	private String ifsc;
	
	private String pan;
	
	private String employeeName;
	
//	private Double paydays;  Removed from Database 13-03-2025
   
	private String empCode;

	private Integer consoliatedAmountId;

	public ConsoliatedPayHistoryDTO(Integer consoliatedPayHistoryId, Integer empId, Float remainingAmount,
			Integer month, Integer year, Float totalAmount, Float payingAmount, Float tds, Float netPay,
			String employeeType, String jobType, String institute, String department, String designation,
			String fromDate, String toDate,Float splPay, Float transportAmount,String accountNo,
			String bank,String ifsc,String pan,String employeeName,String empCode) {

		this.consoliatedPayHistoryId = consoliatedPayHistoryId;
		this.empId = empId;
		this.remainingAmount = remainingAmount;
		this.month = month;
		this.year = year;
		this.totalAmount = totalAmount;
		this.payingAmount = payingAmount;
		this.tds = tds;
		this.netPay = netPay;
		this.employeeType = employeeType;
		this.jobType = jobType;
		this.institute = institute;
		this.department = department;
		this.designation = designation;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.splPay=splPay;
		this.transportAmount=transportAmount;
		this.accountNo=accountNo;
		this.bank=bank;
		this.ifsc=ifsc;
		this.pan=pan;
		this.employeeName=employeeName;
		this.empCode=empCode;
	}

}
