package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentWiseDueReport {

	private String studentName;
	
	private String auid;
	
	private Integer currentYear;
	
	private Integer currentSem;
	
	private String templateName;
	
	private Integer schoolId;
	private Integer acYearId;
	private Integer programId;
	private Integer programSpecialiaztionId;
	
	private Float sem1;

	private Float sem2;

	private Float sem3;

	private Float sem4;

	private Float sem5;

	private Float sem6;

	private Float sem7;

	private Float sem8;

	private Float sem9;

	private Float sem10;

	private Float sem11;

	private Float sem12;
	
	private BigDecimal totalDue;
	private Integer numberOfSemester;

	private Float addOn;
	

	private Double hostelFee;


	public StudentWiseDueReport(String studentName, String auid, Integer currentYear, Integer currentSem,
			String templateName,Integer schoolId,Integer acYearId,Integer programId,Integer programSpecialiaztionId, Float sem1, Float sem2, Float sem3, Float sem4, Float sem5, Float sem6,
			Float sem7, Float sem8, Float sem9, Float sem10, Float sem11, Float sem12,BigDecimal totalDue, Float addOn, Integer numberOfSemester) {

		this.studentName = studentName;
		this.auid = auid;
		this.currentYear = currentYear;
		this.currentSem = currentSem;
		this.templateName = templateName;
		this.schoolId=schoolId;
		this.acYearId=acYearId;
		this.programId=programId;
		this.programSpecialiaztionId=programSpecialiaztionId;
		this.sem1 = sem1;
		this.sem2 = sem2;
		this.sem3 = sem3;
		this.sem4 = sem4;
		this.sem5 = sem5;
		this.sem6 = sem6;
		this.sem7 = sem7;
		this.sem8 = sem8;
		this.sem9 = sem9;
		this.sem10 = sem10;
		this.sem11 = sem11;
		this.sem12 = sem12;
		this.totalDue=totalDue;
		this.numberOfSemester=numberOfSemester;
		this.addOn = addOn;
	}
	
	
	

	
}
