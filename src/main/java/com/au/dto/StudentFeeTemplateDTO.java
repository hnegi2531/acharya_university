package com.au.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentFeeTemplateDTO {

	private String templateName;
	private String acYear;
	private String school;
	private String program;
	private String programSpecilization;
	private String feeAdmissionCategory;
	private Integer programId;
	private Integer programSpecilizationId;
	private Integer acYearId;
	private Integer schoolId;
	private Integer templateId;
	
	private List<FeeTemplateDTO> feeTemplateList;

	private OtherFeeTemplateForStudentDTO addOnProgramFeeList;
	

	private OtherFeeTemplateForStudentDTO UniformFeeList;


	public StudentFeeTemplateDTO(String templateName, String acYear, String school, String program,
			String programSpecilization, String feeAdmissionCategory, Integer programId, Integer programSpecilizationId,
			Integer acYearId, Integer schoolId,Integer templateId) {

		this.templateName = templateName;
		this.acYear = acYear;
		this.school = school;
		this.program = program;
		this.programSpecilization = programSpecilization;
		this.feeAdmissionCategory = feeAdmissionCategory;
		this.programId = programId;
		this.programSpecilizationId = programSpecilizationId;
		this.acYearId = acYearId;
		this.schoolId = schoolId;
		this.templateId=templateId;
	}
	
	
	
	
	
}
