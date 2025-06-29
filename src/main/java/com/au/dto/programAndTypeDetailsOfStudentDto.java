package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class programAndTypeDetailsOfStudentDto {
	
	private Integer programAssignmentId;
	private Integer programId;
	private Integer ProgramTypeId;
	private Integer programSpecializationId;
	private String programName;
	private String programShortName;
	private String programType;
	private Integer numberOfYears;
	private Integer numberOfSemester;
	private String  programSpecializationName;
	private String programSpecializationShortName;
	
}
