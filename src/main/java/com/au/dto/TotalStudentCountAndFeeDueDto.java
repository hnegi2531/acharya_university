package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TotalStudentCountAndFeeDueDto {

	
	private Integer programId;
	private String programName;
	private Long studentCount;
	private Double totalDues;
	
	private Integer categoryId;
	private String categoryName;
	private String categoryShortName;
	
	
	
	
	public TotalStudentCountAndFeeDueDto(Long studentCount, Double totalDues, Integer categoryId, String categoryName,String categoryShortName) {
		this.studentCount = studentCount;
		this.totalDues = totalDues;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.categoryShortName=categoryShortName;
	}




	public TotalStudentCountAndFeeDueDto(Integer programId, String programName, Long studentCount, Double totalDues) {
		this.programId = programId;
		this.programName = programName;
		this.studentCount = studentCount;
		this.totalDues = totalDues;
	}
	
	
}
