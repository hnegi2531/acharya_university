package com.au.dto;
import lombok.Data;

@Data
public class EligibleStudentFeedbackRequestDTO {

	private Integer academicYearId;
	private String academicYear;
	private Integer instituteId;
	private String institute;
	private Integer courseId;
	private String course;
	private Integer branchId;
	private String branch;
	private Integer year;
	private Integer sem;
	private Integer sectionId;
	private String section;
	private Boolean ischeck;
	private Integer studentId;
	private Integer subjectId;
	private Integer studentAttendenceId;
	private Integer program_specialization_id;
}
