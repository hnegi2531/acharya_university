package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FineSlabDTO {

	private Integer fineSlabId;
	private Integer week;
	
	private Integer percentage;
	
	private Integer schoolId;
	
	private String schoolName;
	
	private String fromDate;
	private String tillDate;
	
	

//	public FineSlabDTO(Integer fineSlabId, Integer week, Integer percentage, String schoolName) {
//		
//		this.fineSlabId = fineSlabId;
//		this.week = week;
//		this.percentage = percentage;
//		this.schoolName = schoolName;
//	}
//
//
//
//	public FineSlabDTO(Integer fineSlabId, Integer week, Integer percentage, Integer schoolId) {
//	
//		this.fineSlabId = fineSlabId;
//		this.week = week;
//		this.percentage = percentage;
//		this.schoolId = schoolId;
//	}
//	
	
}
