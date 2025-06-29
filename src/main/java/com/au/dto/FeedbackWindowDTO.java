package com.au.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackWindowDTO {

	private Long feedbackWindowId;
	private String academicYear;
	private Integer instituteId;
	private String courseAndBranch;
	private String branch;
	private Integer year;
	private Integer sem;
	private String semester;
	
	private Date fromDate;
	private Date toDate;
	private List<Integer> program_specialization_id;
}
