package com.au.dto;

import java.util.Date;

import com.au.model.IncrementCreation;

import lombok.Data;

@Data
public class IncrementCreationResponseDTO {

	private Long incrementCreationId;

	private Integer empId;
	private String empCode;

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
	private Float grossDifference;
	private Float ctcDifference;
	private Integer month;
	private Integer year;
	private Integer proposedDesignationId;
	private Integer proposedDepartmentId;
	private Integer proposedSalaryStructureId;

	private Float previousMedicalReimburesment;
	private Float proposedMedicalReimburesment;

	private Date created_date;
	private String remarks;
	private String createdBy;
	private String dateofJoining;
	private String employeeName;
	private String experience;
	private Boolean isChecked;
	private Boolean isApproved;
	private Boolean isRejected;
	private String attachmentPath;
	private String attachmentPathType;
	private Boolean active;
	private Integer school_id;
	private Integer dept_id;
	private String school_name_short;
	
	public IncrementCreationResponseDTO(Long incrementCreationId, Integer empId, String empCode,
			String previousDesignation, String proposedDesignation, Integer previousDesignationId,
			String previousDepartment, String proposedDepartment, Integer previousDepartmentId,
			String previousSalaryStructure, String proposedSalaryStructure, Integer previousSalaryStructureId,
			Float previousBasic, Float proposedBasic, Float previousSplPay, Float proposedSplPay,
			Float previousGrosspay, Float proposedGrosspay, Float previousCtc, Float proposedCtc, Float grossDifference,
			Float ctcDifference, Integer month, Integer year, Integer proposedDesignationId,
			Integer proposedDepartmentId, Integer proposedSalaryStructureId, Float previousMedicalReimburesment,
			Float proposedMedicalReimburesment, Date created_date, String remarks, String createdBy,
			String dateofJoining, String employeeName,Boolean isChecked,Boolean isApproved,Boolean isRejected,String attachmentPath,
			String attachmentPathType,Boolean active,Integer school_id,Integer dept_id,String school_name_short) {
		this.incrementCreationId = incrementCreationId;
		this.empId = empId;
		this.empCode = empCode;
		this.previousDesignation = previousDesignation;
		this.proposedDesignation = proposedDesignation;
		this.previousDesignationId = previousDesignationId;
		this.previousDepartment = previousDepartment;
		this.proposedDepartment = proposedDepartment;
		this.previousDepartmentId = previousDepartmentId;
		this.previousSalaryStructure = previousSalaryStructure;
		this.proposedSalaryStructure = proposedSalaryStructure;
		this.previousSalaryStructureId = previousSalaryStructureId;
		this.previousBasic = previousBasic;
		this.proposedBasic = proposedBasic;
		this.previousSplPay = previousSplPay;
		this.proposedSplPay = proposedSplPay;
		this.previousGrosspay = previousGrosspay;
		this.proposedGrosspay = proposedGrosspay;
		this.previousCtc = previousCtc;
		this.proposedCtc = proposedCtc;
		this.grossDifference = grossDifference;
		this.ctcDifference = ctcDifference;
		this.month = month;
		this.year = year;
		this.proposedDesignationId = proposedDesignationId;
		this.proposedDepartmentId = proposedDepartmentId;
		this.proposedSalaryStructureId = proposedSalaryStructureId;
		this.previousMedicalReimburesment = previousMedicalReimburesment;
		this.proposedMedicalReimburesment = proposedMedicalReimburesment;
		this.created_date = created_date;
		this.remarks = remarks;
		this.createdBy = createdBy;
		this.dateofJoining = dateofJoining;
		this.employeeName = employeeName;
		this.isChecked=isChecked;
		this.isApproved=isApproved;
		this.isRejected=isRejected;
		this.attachmentPath=attachmentPath;
		this.attachmentPathType=attachmentPathType;
		this.active=active;
		this.school_id=school_id;
		this.dept_id=dept_id;
		this.school_name_short=school_name_short;
			
	}
	
	public IncrementCreationResponseDTO(Long incrementCreationId, Integer empId, String empCode,
			String previousDesignation, String proposedDesignation, Integer previousDesignationId,
			String previousDepartment, String proposedDepartment, Integer previousDepartmentId,
			String previousSalaryStructure, String proposedSalaryStructure, Integer previousSalaryStructureId,
			Float previousBasic, Float proposedBasic, Float previousSplPay, Float proposedSplPay,
			Float previousGrosspay, Float proposedGrosspay, Float previousCtc, Float proposedCtc, Float grossDifference,
			Float ctcDifference, Integer month, Integer year, Integer proposedDesignationId,
			Integer proposedDepartmentId, Integer proposedSalaryStructureId, Float previousMedicalReimburesment,
			Float proposedMedicalReimburesment, Date created_date, String remarks, String createdBy,
			String dateofJoining, String employeeName,Boolean isChecked) {
		this.incrementCreationId = incrementCreationId;
		this.empId = empId;
		this.empCode = empCode;
		this.previousDesignation = previousDesignation;
		this.proposedDesignation = proposedDesignation;
		this.previousDesignationId = previousDesignationId;
		this.previousDepartment = previousDepartment;
		this.proposedDepartment = proposedDepartment;
		this.previousDepartmentId = previousDepartmentId;
		this.previousSalaryStructure = previousSalaryStructure;
		this.proposedSalaryStructure = proposedSalaryStructure;
		this.previousSalaryStructureId = previousSalaryStructureId;
		this.previousBasic = previousBasic;
		this.proposedBasic = proposedBasic;
		this.previousSplPay = previousSplPay;
		this.proposedSplPay = proposedSplPay;
		this.previousGrosspay = previousGrosspay;
		this.proposedGrosspay = proposedGrosspay;
		this.previousCtc = previousCtc;
		this.proposedCtc = proposedCtc;
		this.grossDifference = grossDifference;
		this.ctcDifference = ctcDifference;
		this.month = month;
		this.year = year;
		this.proposedDesignationId = proposedDesignationId;
		this.proposedDepartmentId = proposedDepartmentId;
		this.proposedSalaryStructureId = proposedSalaryStructureId;
		this.previousMedicalReimburesment = previousMedicalReimburesment;
		this.proposedMedicalReimburesment = proposedMedicalReimburesment;
		this.created_date = created_date;
		this.remarks = remarks;
		this.createdBy = createdBy;
		this.dateofJoining = dateofJoining;
		this.employeeName = employeeName;
		this.isChecked=isChecked;
	}
	
	public IncrementCreationResponseDTO(Long incrementCreationId, Integer empId, String empCode,
			String previousDesignation, String proposedDesignation, Integer previousDesignationId,
			String previousDepartment, String proposedDepartment, Integer previousDepartmentId,
			String previousSalaryStructure, String proposedSalaryStructure, Integer previousSalaryStructureId,
			Float previousBasic, Float proposedBasic, Float previousSplPay, Float proposedSplPay,
			Float previousGrosspay, Float proposedGrosspay, Float previousCtc, Float proposedCtc, Float grossDifference,
			Float ctcDifference, Integer month, Integer year, Integer proposedDesignationId,
			Integer proposedDepartmentId, Integer proposedSalaryStructureId, Float previousMedicalReimburesment,
			Float proposedMedicalReimburesment, Date created_date, String remarks, String createdBy,
			String dateofJoining, String employeeName) {
		this.incrementCreationId = incrementCreationId;
		this.empId = empId;
		this.empCode = empCode;
		this.previousDesignation = previousDesignation;
		this.proposedDesignation = proposedDesignation;
		this.previousDesignationId = previousDesignationId;
		this.previousDepartment = previousDepartment;
		this.proposedDepartment = proposedDepartment;
		this.previousDepartmentId = previousDepartmentId;
		this.previousSalaryStructure = previousSalaryStructure;
		this.proposedSalaryStructure = proposedSalaryStructure;
		this.previousSalaryStructureId = previousSalaryStructureId;
		this.previousBasic = previousBasic;
		this.proposedBasic = proposedBasic;
		this.previousSplPay = previousSplPay;
		this.proposedSplPay = proposedSplPay;
		this.previousGrosspay = previousGrosspay;
		this.proposedGrosspay = proposedGrosspay;
		this.previousCtc = previousCtc;
		this.proposedCtc = proposedCtc;
		this.grossDifference = grossDifference;
		this.ctcDifference = ctcDifference;
		this.month = month;
		this.year = year;
		this.proposedDesignationId = proposedDesignationId;
		this.proposedDepartmentId = proposedDepartmentId;
		this.proposedSalaryStructureId = proposedSalaryStructureId;
		this.previousMedicalReimburesment = previousMedicalReimburesment;
		this.proposedMedicalReimburesment = proposedMedicalReimburesment;
		this.created_date = created_date;
		this.remarks = remarks;
		this.createdBy = createdBy;
		this.dateofJoining = dateofJoining;
		this.employeeName = employeeName;
	}
	
	
}
