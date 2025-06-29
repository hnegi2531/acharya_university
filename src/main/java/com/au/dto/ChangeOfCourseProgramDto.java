package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeOfCourseProgramDto {
	
	private Integer oldStudentId;
	private Integer newStudentId;
	private Integer acYearId;
	private Integer schoolId;
	private Integer programAssignmentId;
	private Integer programId;
	private Integer programSpecializationId;
	private Integer feeAdmissionCategoryId;
	private Integer feeTempalateId;
	private Integer nationalityId;
	

}
