package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDetailsForLeavePattern {

	private Integer empId;
	private Integer empTypeId;
	private Integer jobTypeId;
	private Integer schoolId;
	private String dateOfJoining;
	private Integer permanentStatus;
	private Boolean marriageStatus; 
	private String martialStatus;
	private Character gender;
	private Boolean maternityStatus;
	private Boolean paternityStatus; 
}
 
