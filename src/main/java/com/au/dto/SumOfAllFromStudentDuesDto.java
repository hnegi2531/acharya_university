package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SumOfAllFromStudentDuesDto {

	
	private Integer categoryId;
	
	private String categoryTypeName;
	
	private String categoryShortName;
	
	private Double  sumOfDue;
	
	private Double  sumOfFixed;
	
	private Double  sumOfGrant;

	private Double  sumOfPaid;
	
	private Long studentCount;
}
