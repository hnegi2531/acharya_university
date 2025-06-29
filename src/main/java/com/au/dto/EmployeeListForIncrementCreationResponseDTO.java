package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeListForIncrementCreationResponseDTO {

	private Integer empId;
	private String employeeName;
	private String designation;
	private String department;
	private String email;
	private String dateofJoining;
	private String empCode;
	private String experience;
	private Float ctc;
	private Float grosspay_ctc;
	private String schoolName;
	private String schoolShortName;
	private Integer permanent_status;
	private String empTypeShortName;
	private String dept_name_short;
	
	public EmployeeListForIncrementCreationResponseDTO(Integer empId, String employeeName, String designation,
			String department, String email, String dateofJoining, String empCode,Float ctc,Float grosspay_ctc,String schoolName,
			String schoolShortName,Integer permanent_status,String empTypeShortName,String dept_name_short) {
	
		this.empId = empId;
		this.employeeName = employeeName;
		this.designation = designation;
		this.department = department;
		this.email = email;
		this.dateofJoining = dateofJoining;
		this.empCode = empCode;
		this.ctc = ctc;
		this.grosspay_ctc = grosspay_ctc;
		this.schoolName = schoolName;
		this.schoolShortName = schoolShortName;
		this.permanent_status = permanent_status;
		this.empTypeShortName = empTypeShortName;
		this.dept_name_short = dept_name_short;
	}
	
	

}

