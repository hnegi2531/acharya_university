package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDueEventDTO {

	private Integer studentId;

	private String auid;

	private Integer currentSem;

	private Integer currentYear;
	private Integer schoolId;
	private Integer programId;
	private Integer programSpecializationId;
	private Integer feeAdmissionCatgoryId;
	private Integer acYearId;
	private Integer feeTemplateId;
}
