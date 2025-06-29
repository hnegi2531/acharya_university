package com.au.dto;

import java.util.Date;
import java.util.List;

import com.au.model.ExternalMarksAssignment;

import lombok.Data;

@Data
public class ExternalMarksAssignmentDto {


	private Integer marks_id;
	private Integer external_mark_id;
	private List<Integer> student_id;
	private Integer section_id;
	private Double marks_scored;
	private Double percentage;
	
	private Boolean active;
	
	private Integer course_assignment_id;
	private Integer ac_year_id;
	private Double external_max_mark;
	private Double external_min_mark;
	private String exam_date;
	private Integer program_specialization_id;
	private Integer school_id;
	private Double marks_obtained_external;
}
