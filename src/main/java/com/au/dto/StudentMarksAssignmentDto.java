package com.au.dto;

import lombok.Data;

@Data
public class StudentMarksAssignmentDto {

	private Integer student_id;
	private Double marks_obtained_external;
	private Double percentage;
}
