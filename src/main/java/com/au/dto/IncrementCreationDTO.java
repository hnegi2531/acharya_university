package com.au.dto;

import java.util.Date;

import com.au.model.IncrementCreation;

import lombok.Data;

@Data
public class IncrementCreationDTO {

	private Integer empId;
    private  String empCode;
	private String previousDesignation;

	private String proposedDesignation;
	private Integer previousDesignationId;
	private String previousDepartment;
	private String proposedDepartment;
	private Integer previousDepartmentId;
	private String previousSalaryStructure;
	private String proposedSalaryStructure;
	private Integer previousSalaryStructureId;
	private Float previousBasic;
	private Float proposedBasic;
	private Float previousSplPay;
	private Float proposedSplPay;
	private Float previousGrosspay;
	private Float proposedGrosspay;
	private Float previousCtc;
	private Float proposedCtc;
	private Float previousTa;
	private Float proposedTa;
	private Float grossDifference;
	private Float ctcDifference;
	private Integer month;
	private Integer year;
	private String remarks;
	private Integer createdBy;
	
	private Integer proposedDesignationId;
	private Integer proposedDepartmentId;
	private Integer proposedSalaryStructureId;
	
	private Float previousMedicalReimburesment;
	private Float proposedMedicalReimburesment;
	private Boolean active;
}
