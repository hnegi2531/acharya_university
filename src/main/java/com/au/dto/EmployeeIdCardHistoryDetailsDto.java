package com.au.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeIdCardHistoryDetailsDto {
	
	
	private Integer employeeIdCardHistoryId;
	private Integer empId;
	private Integer issuedBy;
	private String issuedByUsername;
	private Date  issuedDate;
	private Boolean active;
	private String remarks;
	private String receiptDate;
	private String receiptNo;
	private String employeeName;
	private String modifiedUsername;
	private Date modifiedDate;
	private String empCode;
	private String endDate;
	private String empImageAttachmentPath;
	private String dateOfJoining;
	private String designationName;
	private String designationShortName;
	private String departmentName;
	private String departmentNameShort;
	private String schoolName;
	private String schoolNameShort;
	private String displayName;
	private String phdStatus;
	private String email;
	private String mobile;
}
