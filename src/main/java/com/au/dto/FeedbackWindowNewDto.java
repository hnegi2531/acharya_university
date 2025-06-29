package com.au.dto;
import java.util.Date;

import lombok.Data;

@Data
public class FeedbackWindowNewDto {
	private Long feedbackWindowId;
	
	private String academicYear;
	private String institute;
	private String course;
	private String courseAndBranch;
	private String branch;
	private Integer year;
	private Integer sem;
	private Integer program_specialization_id;
	private String semester;
	
	private Date fromDate;
	private Date toDate;
	
	private Boolean active;
	
	private Integer instituteId;
}
