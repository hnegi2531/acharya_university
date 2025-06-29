package com.au.dto;

import java.util.List;

import com.au.model.AcerpAmount;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeFeedbackReport {
    private Integer empId;
	private String facultyName;
	private String empCode;
	private String designationName;
	private String departmentName;
	private String dateOfJoining;
	private String employeeImage;
	
	private List<EmployeeFeedbackReportList> feedbackreport;

	
	
	public EmployeeFeedbackReport(Integer empId,String facultyName, String empCode, String designationName, String departmentName,
			String dateOfJoining,String employeeImage) {
		this.empId=empId;
		this.facultyName = facultyName;
		this.empCode = empCode;
		this.designationName = designationName;
		this.departmentName = departmentName;
		this.dateOfJoining = dateOfJoining;
		this.employeeImage=employeeImage;
	}
	
	
}
	