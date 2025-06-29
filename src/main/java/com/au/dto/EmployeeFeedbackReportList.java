package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeFeedbackReportList {

	private Integer sectionId;
	private String section;
	
	private String subjectCode;
 
	private String subject;
	private String acYear;
	private Integer currentYear;
	private Integer currentSem;
	private Integer questionId;
	private String question;
	private Long feedbackCount;
	private Long ratings;
	private Float feedbackPercentage;
	private Long studentCount;
	
	public EmployeeFeedbackReportList(Integer sectionId,String section, String subjectCode, String subject, String acYear,
			Integer currentYear, Integer currentSem, Long feedbackCount, Long ratings) {
		this.sectionId=sectionId;
		this.section = section;
		this.subjectCode = subjectCode;
		this.subject = subject;
		this.acYear = acYear;
		this.currentYear = currentYear;
		this.currentSem = currentSem;
		this.feedbackCount = feedbackCount;
		this.ratings = ratings;
		
	}
	
	
	
}
