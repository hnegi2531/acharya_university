package com.au.dto;

import java.util.Date;

import javax.persistence.Lob;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentPermissionDTO {

	private String auid;
	private String studentName;
	private Integer currentYear;
	private Integer currentSem;
	private String permissionType;
	private Float totalDue;
	private String permittedBy;
	private Integer allowSem;
	private Boolean isAllowExamPermit=Boolean.FALSE;
	private Boolean isAllowAttendencePermit=Boolean.FALSE;
	private Boolean isAllowPartFeePermit=Boolean.FALSE;
	private Boolean isAllowFineWaiver=Boolean.FALSE;
	private String tillDate;
	private String attachment;
	@Lob
	private String remarks;
}
