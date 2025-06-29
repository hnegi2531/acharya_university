package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDetailForIncrementCreationDTO {

	private Integer empId;

	private String previousDesignation;

	private Integer previousDesignationId;
	private String previousDepartment;
	private Integer previousDepartmentId;
	private String previousSalaryStructure;
	private Integer previousSalaryStructureId;
	private Float previousBasic;
	private Float previousSplPay;
	private Float previousGrosspay;
	private Float previousCtc;
	private Float previousTa;
	private String empCode;
	private String experience;
	private String date_of_joining;
}

